import { spawn } from 'node:child_process'
import fs from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'
import { URI } from 'vscode-uri'

const __dirname = path.dirname(fileURLToPath(import.meta.url))

interface LspMessage {
  jsonrpc: '2.0'
  id?: number
  method?: string
  params?: any
  result?: any
  error?: any
}

let messageId = 0

function createRequest(method: string, params: any): string {
  const body = JSON.stringify({
    jsonrpc: '2.0',
    id: ++messageId,
    method,
    params,
  })
  return `Content-Length: ${Buffer.byteLength(body)}\r\n\r\n${body}`
}

function createNotification(method: string, params: any): string {
  const body = JSON.stringify({
    jsonrpc: '2.0',
    method,
    params,
  })
  return `Content-Length: ${Buffer.byteLength(body)}\r\n\r\n${body}`
}

function extractMessages(raw: string): { messages: LspMessage[], rest: string } {
  const messages: LspMessage[] = []
  let rest = raw

  while (true) {
    const headerEnd = rest.indexOf('\r\n\r\n')
    if (headerEnd < 0) break

    const header = rest.slice(0, headerEnd)
    const lengthMatch = header.match(/Content-Length:\s*(\d+)/i)
    if (!lengthMatch) {
      throw new Error(`Malformed LSP header: ${header}`)
    }

    const bodyLength = Number(lengthMatch[1])
    const bodyStart = headerEnd + 4
    const packetEnd = bodyStart + bodyLength
    if (rest.length < packetEnd) break

    messages.push(JSON.parse(rest.slice(bodyStart, packetEnd)))
    rest = rest.slice(packetEnd)
  }

  return { messages, rest }
}

function resolveServerPath(): string {
  const serverPath = path.join(__dirname, '..', 'dist', 'language-server.cjs')
  if (!fs.existsSync(serverPath)) {
    throw new Error(`Qin language server bundle not found: ${serverPath}`)
  }
  return serverPath
}

function resolveTsdkPath(): string {
  const candidates = [
    path.join(__dirname, '..', 'node_modules', 'typescript', 'lib'),
    path.join(__dirname, '..', '..', '..', 'node_modules', 'typescript', 'lib'),
    path.join(process.cwd(), 'node_modules', 'typescript', 'lib'),
  ]

  for (const candidate of candidates) {
    if (
      fs.existsSync(path.join(candidate, 'typescript.js')) ||
      fs.existsSync(path.join(candidate, 'tsserverlibrary.js'))
    ) {
      return candidate
    }
  }

  throw new Error(`TypeScript SDK not found. Checked: ${candidates.join(', ')}`)
}

function sleep(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms))
}

function toFileUri(filePath: string): string {
  return URI.file(filePath).toString()
}

function sameUri(left: string | undefined, right: string): boolean {
  return left !== undefined && left.toLowerCase() === right.toLowerCase()
}

async function waitFor(
  description: string,
  predicate: () => boolean,
  timeoutMs = 10000,
): Promise<void> {
  const startedAt = Date.now()
  while (Date.now() - startedAt < timeoutMs) {
    if (predicate()) {
      return
    }
    await sleep(50)
  }
  throw new Error(`Timed out waiting for ${description}`)
}

async function main() {
  const serverPath = resolveServerPath()
  const tsdkPath = resolveTsdkPath()
  const server = spawn('node', [serverPath, '--stdio'], {
    cwd: path.join(__dirname, '..'),
    stdio: ['pipe', 'pipe', 'pipe'],
  })

  if (!server.stdin || !server.stdout || !server.stderr) {
    throw new Error('Failed to start Qin language server process')
  }

  let stdoutBuffer = ''
  const messages: LspMessage[] = []
  let stderr = ''
  let exitCode: number | null = null

  server.stdout.on('data', chunk => {
    stdoutBuffer += chunk.toString()
    const parsed = extractMessages(stdoutBuffer)
    stdoutBuffer = parsed.rest
    messages.push(...parsed.messages)
  })

  server.stderr.on('data', chunk => {
    stderr += chunk.toString()
  })

  server.on('exit', code => {
    exitCode = code
  })

  server.stdin.write(createRequest('initialize', {
    processId: process.pid,
    capabilities: {
      textDocument: {
        completion: {},
        hover: {},
        publishDiagnostics: {},
      },
    },
    rootUri: toFileUri(path.join(__dirname, '..')),
    initializationOptions: {
      typescript: { tsdk: tsdkPath },
    },
  }))

  await waitFor('initialize response', () => messages.some(message => message.id === 1) || exitCode !== null)
  const initResponse = messages.find(message => message.id === 1)
  if (!initResponse?.result?.capabilities) {
    throw new Error(`Qin language server initialize failed. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`)
  }

  server.stdin.write(createNotification('initialized', {}))

  const validUri = toFileUri(path.join(__dirname, 'valid.qin'))
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: validUri,
      languageId: 'qin',
      version: 1,
      text: 'export object Counter { value = 1 }',
    },
  }))

  const invalidUri = toFileUri(path.join(__dirname, 'invalid.qin'))
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: invalidUri,
      languageId: 'qin',
      version: 1,
      text: 'export object Counter { value = }',
    },
  }))

  await waitFor(
    'Qin publishDiagnostics for valid and invalid documents',
    () => {
      const diagnosticsMessages = messages.filter(message => message.method === 'textDocument/publishDiagnostics')
      return diagnosticsMessages.some(message => sameUri(message.params?.uri, validUri))
        && diagnosticsMessages.some(message => sameUri(message.params?.uri, invalidUri))
    },
    15000,
  ).catch(error => {
    throw new Error(`${error instanceof Error ? error.message : String(error)}. stderr=${stderr} messages=${JSON.stringify(messages)}`)
  })

  const diagnosticsMessages = messages.filter(message => message.method === 'textDocument/publishDiagnostics')
  const validDiagnostics = diagnosticsMessages
    .filter(message => sameUri(message.params?.uri, validUri))
    .at(-1)?.params?.diagnostics ?? []
  const invalidDiagnostics = diagnosticsMessages
    .filter(message => sameUri(message.params?.uri, invalidUri))
    .at(-1)?.params?.diagnostics ?? []

  if (validDiagnostics.length) {
    throw new Error(`Valid Qin source produced diagnostics: ${JSON.stringify(validDiagnostics)}`)
  }

  if (!invalidDiagnostics.length) {
    throw new Error(`Invalid Qin source did not produce parser diagnostics. Messages=${JSON.stringify(messages)}`)
  }

  if (invalidDiagnostics[0].source !== 'qin-parser') {
    throw new Error(`Expected qin-parser diagnostic source, got ${JSON.stringify(invalidDiagnostics[0])}`)
  }

  server.stdin.write(createRequest('shutdown', null))
  await sleep(200)
  server.stdin.write(createNotification('exit', null))
  await sleep(200)

  console.log('Qin language server LSP smoke passed')
}

main().catch(error => {
  console.error(error instanceof Error ? error.stack || error.message : String(error))
  process.exit(1)
})
