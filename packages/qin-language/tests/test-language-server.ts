import { spawn } from 'node:child_process'
import fs from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

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

function createRequest(method: string, params: any): { id: number, packet: string } {
  const id = ++messageId
  const body = JSON.stringify({
    jsonrpc: '2.0',
    id,
    method,
    params,
  })
  return {
    id,
    packet: `Content-Length: ${Buffer.byteLength(body)}\r\n\r\n${body}`,
  }
}

function createNotification(method: string, params: any): string {
  const body = JSON.stringify({
    jsonrpc: '2.0',
    method,
    params,
  })
  return `Content-Length: ${Buffer.byteLength(body)}\r\n\r\n${body}`
}

function createResponse(id: number, result: any): string {
  const body = JSON.stringify({
    jsonrpc: '2.0',
    id,
    result,
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
  const normalized = path.resolve(filePath).replace(/\\/g, '/')
  if (/^[A-Za-z]:\//.test(normalized)) {
    return `file:///${normalized}`
  }
  return `file://${normalized.startsWith('/') ? '' : '/'}${normalized}`
}

function sameUri(left: string | undefined, right: string): boolean {
  if (left === undefined) {
    return false
  }
  if (left.toLowerCase() === right.toLowerCase()) {
    return true
  }
  const leftName = decodeURIComponent(left).replace(/\\/g, '/').split('/').at(-1)
  const rightName = decodeURIComponent(right).replace(/\\/g, '/').split('/').at(-1)
  return leftName !== undefined && leftName.toLowerCase() === rightName?.toLowerCase()
}

function rangeStartsAt(item: any, line: number, character?: number): boolean {
  const start = item?.range?.start ?? item?.targetSelectionRange?.start
  if (!start || start.line !== line) {
    return false
  }
  return character === undefined || start.character === character
}

function rangeContains(item: any, line: number, character: number): boolean {
  const range = item?.range ?? item?.targetSelectionRange
  const start = range?.start
  const end = range?.end
  if (!start || !end || line < start.line || line > end.line) {
    return false
  }
  if (line === start.line && character < start.character) {
    return false
  }
  if (line === end.line && character > end.character) {
    return false
  }
  return true
}

function locationUri(item: any): string | undefined {
  return item?.uri ?? item?.targetUri
}

function collectSymbolNames(symbols: any[]): string[] {
  const names: string[] = []
  for (const symbol of symbols) {
    if (typeof symbol.name === 'string') {
      names.push(symbol.name)
    }
    if (Array.isArray(symbol.children)) {
      names.push(...collectSymbolNames(symbol.children))
    }
  }
  return names
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

async function waitForResponse(id: number, messages: LspMessage[], description: string): Promise<LspMessage> {
  await waitFor(description, () => messages.some(message => message.id === id && !message.method))
  const message = messages.find(item => item.id === id && !item.method)
  if (!message) {
    throw new Error(`Missing response after wait: ${description}`)
  }
  if (message.error) {
    throw new Error(`${description} returned error: ${JSON.stringify(message.error)}`)
  }
  return message
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
    for (const message of parsed.messages) {
      if (typeof message.id === 'number' && message.method === 'workspace/configuration') {
        const items = message.params?.items ?? []
        server.stdin.write(createResponse(message.id, items.map((item: any) => configurationForSection(item.section))))
      } else if (typeof message.id === 'number' && message.method === 'client/registerCapability') {
        server.stdin.write(createResponse(message.id, null))
      }
    }
  })

  server.stderr.on('data', chunk => {
    stderr += chunk.toString()
  })

  server.on('exit', code => {
    exitCode = code
  })

  const initializeRequest = createRequest('initialize', {
    processId: process.pid,
    capabilities: {
      workspace: {
        configuration: true,
      },
      textDocument: {
        completion: {
          completionItem: {
            snippetSupport: true,
            insertReplaceSupport: true,
          },
        },
        hover: {},
        definition: {},
        references: {},
        documentSymbol: {},
        semanticTokens: {
          requests: {
            full: true,
          },
        },
        publishDiagnostics: {},
      },
    },
    rootUri: toFileUri(path.join(__dirname, '..')),
    initializationOptions: {
      typescript: { tsdk: tsdkPath },
    },
  })
  server.stdin.write(initializeRequest.packet)

  await waitFor('initialize response', () => messages.some(message => message.id === initializeRequest.id) || exitCode !== null)
  const initResponse = messages.find(message => message.id === initializeRequest.id)
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

  const tsSubsetUri = toFileUri(path.join(__dirname, 'ts-subset.qin'))
  const tsSubsetSource = [
    'const alphaNumber = 41',
    'const alphaText = alphaNumber.toString()',
    'al',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: tsSubsetUri,
      languageId: 'qin',
      version: 1,
      text: tsSubsetSource,
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

  const hoverRequest = createRequest('textDocument/hover', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 0, character: 8 },
  })
  server.stdin.write(hoverRequest.packet)
  const hoverResponse = await waitForResponse(
    hoverRequest.id,
    messages,
    `Qin TS-subset hover response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  if (!JSON.stringify(hoverResponse.result ?? '').includes('alphaNumber')) {
    throw new Error(`Qin hover did not return TS service content: ${JSON.stringify(hoverResponse.result)} stderr=${stderr} messages=${JSON.stringify(messages)}`)
  }

  const completionRequest = createRequest('textDocument/completion', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 2, character: 2 },
    context: { triggerKind: 1 },
  })
  server.stdin.write(completionRequest.packet)
  const completionResponse = await waitForResponse(
    completionRequest.id,
    messages,
    `Qin TS-subset completion response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const completionItems = Array.isArray(completionResponse.result)
    ? completionResponse.result
    : completionResponse.result?.items ?? []
  const completionLabels = completionItems.map((item: any) => item.label)
  if (!completionLabels.includes('alphaNumber') || !completionLabels.includes('alphaText')) {
    throw new Error(`Qin completion did not include TS service symbols: ${JSON.stringify(completionLabels.slice(0, 30))}`)
  }

  const definitionRequest = createRequest('textDocument/definition', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 1, character: 20 },
  })
  server.stdin.write(definitionRequest.packet)
  const definitionResponse = await waitForResponse(
    definitionRequest.id,
    messages,
    `Qin TS-subset definition response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const definitionLocations = Array.isArray(definitionResponse.result)
    ? definitionResponse.result
    : definitionResponse.result ? [definitionResponse.result] : []
  if (!definitionLocations.some(item => sameUri(locationUri(item), tsSubsetUri) && rangeContains(item, 0, 6))) {
    throw new Error(`Qin definition did not resolve alphaNumber declaration: ${JSON.stringify(definitionResponse.result)}`)
  }

  const referencesRequest = createRequest('textDocument/references', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 0, character: 8 },
    context: { includeDeclaration: true },
  })
  server.stdin.write(referencesRequest.packet)
  const referencesResponse = await waitForResponse(
    referencesRequest.id,
    messages,
    `Qin TS-subset references response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const references = Array.isArray(referencesResponse.result) ? referencesResponse.result : []
  if (
    !references.some(item => sameUri(locationUri(item), tsSubsetUri) && rangeStartsAt(item, 0, 6))
    || !references.some(item => sameUri(locationUri(item), tsSubsetUri) && rangeStartsAt(item, 1, 18))
  ) {
    throw new Error(`Qin references did not include alphaNumber declaration and usage: ${JSON.stringify(referencesResponse.result)}`)
  }

  const documentSymbolRequest = createRequest('textDocument/documentSymbol', {
    textDocument: { uri: tsSubsetUri },
  })
  server.stdin.write(documentSymbolRequest.packet)
  const documentSymbolResponse = await waitForResponse(
    documentSymbolRequest.id,
    messages,
    `Qin TS-subset documentSymbol response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const symbolNames = collectSymbolNames(Array.isArray(documentSymbolResponse.result) ? documentSymbolResponse.result : [])
  if (!symbolNames.includes('alphaNumber') || !symbolNames.includes('alphaText')) {
    throw new Error(`Qin documentSymbol did not include TS-subset symbols: ${JSON.stringify(documentSymbolResponse.result)}`)
  }

  const semanticTokensRequest = createRequest('textDocument/semanticTokens/full', {
    textDocument: { uri: tsSubsetUri },
  })
  server.stdin.write(semanticTokensRequest.packet)
  const semanticTokensResponse = await waitForResponse(
    semanticTokensRequest.id,
    messages,
    `Qin TS-subset semanticTokens response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const semanticTokenData = semanticTokensResponse.result?.data ?? []
  if (!Array.isArray(semanticTokenData) || semanticTokenData.length === 0) {
    throw new Error(`Qin semanticTokens did not return token data: ${JSON.stringify(semanticTokensResponse.result)}`)
  }

  server.stdin.write(createRequest('shutdown', null).packet)
  await sleep(200)
  server.stdin.write(createNotification('exit', null))
  await sleep(200)

  console.log('Qin language server LSP smoke passed')
}

function configurationForSection(section: string | undefined): any {
  if (section === 'typescript') {
    return {
      suggest: {
        autoImports: false,
        includeCompletionsForImportStatements: false,
      },
      preferences: {
        includePackageJsonAutoImports: 'off',
      },
    }
  }
  if (section === 'javascript') {
    return {
      suggest: {
        autoImports: false,
        includeCompletionsForImportStatements: false,
      },
      preferences: {
        includePackageJsonAutoImports: 'off',
      },
    }
  }
  return {}
}

main().catch(error => {
  console.error(error instanceof Error ? error.stack || error.message : String(error))
  process.exit(1)
})
