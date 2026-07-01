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

function selectionRangeContains(item: any, line: number, character: number): boolean {
  return rangeContains({ range: item?.range }, line, character)
}

function selectionRangeChainContains(item: any, line: number, character: number): boolean {
  let current = item
  while (current) {
    if (selectionRangeContains(current, line, character)) {
      return true
    }
    current = current.parent
  }
  return false
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

function collectWorkspaceEditTexts(edit: any): string[] {
  const texts: string[] = []
  const changes = edit?.changes
  if (changes && typeof changes === 'object') {
    for (const uriChanges of Object.values(changes)) {
      if (Array.isArray(uriChanges)) {
        for (const change of uriChanges) {
          if (typeof change?.newText === 'string') {
            texts.push(change.newText)
          }
        }
      }
    }
  }
  const documentChanges = edit?.documentChanges
  if (Array.isArray(documentChanges)) {
    for (const documentChange of documentChanges) {
      const edits = documentChange?.edits
      if (Array.isArray(edits)) {
        for (const change of edits) {
          if (typeof change?.newText === 'string') {
            texts.push(change.newText)
          }
        }
      }
    }
  }
  return texts
}

function semanticTokenCovers(result: any, line: number, character: number): boolean {
  const data = result?.data
  if (!Array.isArray(data) || data.length % 5 !== 0) {
    return false
  }
  let currentLine = 0
  let currentCharacter = 0
  for (let index = 0; index < data.length; index += 5) {
    const deltaLine = data[index]
    const deltaStart = data[index + 1]
    const length = data[index + 2]
    currentLine += deltaLine
    currentCharacter = deltaLine === 0 ? currentCharacter + deltaStart : deltaStart
    if (currentLine === line && currentCharacter <= character && character < currentCharacter + length) {
      return true
    }
  }
  return false
}

function requireSemanticTokenAt(result: any, line: number, character: number, label: string) {
  if (!semanticTokenCovers(result, line, character)) {
    throw new Error(`${label} semanticTokens did not cover ${line}:${character}: ${JSON.stringify(result)}`)
  }
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

function languageServerEnvironment(): NodeJS.ProcessEnv {
  return {
    ...process.env,
    NODE_OPTIONS: [process.env.NODE_OPTIONS, '--max-old-space-size=512'].filter(Boolean).join(' '),
  }
}

async function main() {
  const serverPath = resolveServerPath()
  const tsdkPath = resolveTsdkPath()
  const server = spawn('node', [serverPath, '--stdio'], {
    cwd: path.join(__dirname, '..'),
    env: languageServerEnvironment(),
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
        symbol: {},
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
        linkedEditingRange: {},
        documentLink: {},
        documentSymbol: {},
        selectionRange: {},
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
      qin: {
        languageServer: {
          sourceExtension: '.qin',
          serviceExtension: '.ts',
          generatedParserTarget: '@qin/generated-qin-parser-ts',
          parserPackage: 'com.qin:qin-parser',
        },
      },
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
    'function formatLabel(name: string, count: number): string { return name + count }',
    'const formattedLabel = formatLabel("qin", alphaNumber)',
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

  const objectUri = toFileUri(path.join(__dirname, 'object-use.qin'))
  const objectSource = [
    'export object Counter {',
    '  value = 1',
    '}',
    'const currentValue = Counter.value',
    'Coun',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: objectUri,
      languageId: 'qin',
      version: 1,
      text: objectSource,
    },
  }))

  const objectExtendsUri = toFileUri(path.join(__dirname, 'object-extends.qin'))
  const objectExtendsSource = [
    'class BaseCounter {',
    '  baseValue = 1',
    '}',
    'export object Counter extends BaseCounter {',
    '  value = this.baseValue',
    '}',
    'const currentValue = Counter.baseValue',
    'Counter.ba',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: objectExtendsUri,
      languageId: 'qin',
      version: 1,
      text: objectExtendsSource,
    },
  }))

  const forOfUri = toFileUri(path.join(__dirname, 'for-of.qin'))
  const forOfSource = [
    'export function sumItems(items: number[]): number {',
    '  let totalValue = 0',
    '  for (const itemValue of items) {',
    '    totalValue = totalValue + itemValue',
    '  }',
    '  tot',
    '  return totalValue',
    '}',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: forOfUri,
      languageId: 'qin',
      version: 1,
      text: forOfSource,
    },
  }))

  const importProviderUri = toFileUri(path.join(__dirname, 'provider.qin'))
  const importProviderSource = [
    'export object Counter {',
    '  value = 1',
    '  next() {',
    '    return this.value + 1',
    '  }',
    '}',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: importProviderUri,
      languageId: 'qin',
      version: 1,
      text: importProviderSource,
    },
  }))

  const importConsumerUri = toFileUri(path.join(__dirname, 'consumer.qin'))
  const importConsumerSource = [
    "import { Counter } from './provider.qin'",
    'const currentValue = Counter.value',
    'Counter.va',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: importConsumerUri,
      languageId: 'qin',
      version: 1,
      text: importConsumerSource,
    },
  }))

  const sharedJavaImportUri = toFileUri(path.join(__dirname, 'shared', 'shared-policy.qin'))
  const sharedJavaImportSource = [
    "import { ArrayList } from 'java:java.util'",
    'export const sharedValue = ArrayList',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: sharedJavaImportUri,
      languageId: 'qin',
      version: 1,
      text: sharedJavaImportSource,
    },
  }))

  const appJavaImportUri = toFileUri(path.join(__dirname, 'app', 'app-policy.qin'))
  const appJavaImportSource = [
    "import { ArrayList } from 'java:java.util'",
    'export const appValue = ArrayList',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: appJavaImportUri,
      languageId: 'qin',
      version: 1,
      text: appJavaImportSource,
    },
  }))

  const mainJavaImportUri = toFileUri(path.join(__dirname, 'main', 'main-policy.qin'))
  const mainJavaImportSource = [
    "import { ArrayList } from 'java:java.util'",
    'export const mainValue = ArrayList',
    '',
  ].join('\n')
  server.stdin.write(createNotification('textDocument/didOpen', {
    textDocument: {
      uri: mainJavaImportUri,
      languageId: 'qin',
      version: 1,
      text: mainJavaImportSource,
    },
  }))

  await waitFor(
    'Qin publishDiagnostics for valid, for-of, invalid, and import policy documents',
    () => {
      const diagnosticsMessages = messages.filter(message => message.method === 'textDocument/publishDiagnostics')
      const sharedDiagnostics = diagnosticsMessages
        .filter(message => sameUri(message.params?.uri, sharedJavaImportUri))
        .at(-1)?.params?.diagnostics ?? []
      const appDiagnostics = diagnosticsMessages
        .filter(message => sameUri(message.params?.uri, appJavaImportUri))
        .at(-1)?.params?.diagnostics ?? []
      return diagnosticsMessages.some(message => sameUri(message.params?.uri, validUri))
        && diagnosticsMessages.some(message => sameUri(message.params?.uri, forOfUri))
        && diagnosticsMessages.some(message => sameUri(message.params?.uri, invalidUri))
        && sharedDiagnostics.some((item: any) => item.source === 'qin-import-policy')
        && appDiagnostics.some((item: any) => item.source === 'qin-import-policy')
        && diagnosticsMessages.some(message => sameUri(message.params?.uri, mainJavaImportUri))
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
  const forOfDiagnostics = diagnosticsMessages
    .filter(message => sameUri(message.params?.uri, forOfUri))
    .at(-1)?.params?.diagnostics ?? []
  const sharedJavaImportDiagnostics = diagnosticsMessages
    .filter(message => sameUri(message.params?.uri, sharedJavaImportUri))
    .at(-1)?.params?.diagnostics ?? []
  const appJavaImportDiagnostics = diagnosticsMessages
    .filter(message => sameUri(message.params?.uri, appJavaImportUri))
    .at(-1)?.params?.diagnostics ?? []
  const mainJavaImportDiagnostics = diagnosticsMessages
    .filter(message => sameUri(message.params?.uri, mainJavaImportUri))
    .at(-1)?.params?.diagnostics ?? []

  if (validDiagnostics.length) {
    throw new Error(`Valid Qin source produced diagnostics: ${JSON.stringify(validDiagnostics)}`)
  }

  if (forOfDiagnostics.length) {
    throw new Error(`Qin for...of source produced diagnostics: ${JSON.stringify(forOfDiagnostics)}`)
  }

  if (!invalidDiagnostics.length) {
    throw new Error(`Invalid Qin source did not produce parser diagnostics. Messages=${JSON.stringify(messages)}`)
  }

  if (!sharedJavaImportDiagnostics.some((item: any) => item.source === 'qin-import-policy' && String(item.message).includes('shared code cannot import java modules'))) {
    throw new Error(`Shared Qin java: import did not produce import-policy diagnostic: ${JSON.stringify(sharedJavaImportDiagnostics)}`)
  }

  if (!appJavaImportDiagnostics.some((item: any) => item.source === 'qin-import-policy' && String(item.message).includes('app code cannot import java modules'))) {
    throw new Error(`App Qin java: import did not produce import-policy diagnostic: ${JSON.stringify(appJavaImportDiagnostics)}`)
  }

  if (mainJavaImportDiagnostics.some((item: any) => item.source === 'qin-import-policy')) {
    throw new Error(`Main Qin java: import must not produce app/shared import-policy diagnostic: ${JSON.stringify(mainJavaImportDiagnostics)}`)
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
    position: { line: 4, character: 2 },
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

  const signatureHelpRequest = createRequest('textDocument/signatureHelp', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 3, character: 42 },
    context: {
      triggerKind: 1,
      isRetrigger: false,
    },
  })
  server.stdin.write(signatureHelpRequest.packet)
  const signatureHelpResponse = await waitForResponse(
    signatureHelpRequest.id,
    messages,
    `Qin TS-subset signatureHelp response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const signatures = signatureHelpResponse.result?.signatures ?? []
  if (!signatures.some((item: any) => JSON.stringify(item).includes('formatLabel'))) {
    throw new Error(`Qin signatureHelp did not include formatLabel signature: ${JSON.stringify(signatureHelpResponse.result)}`)
  }

  const objectCompletionRequest = createRequest('textDocument/completion', {
    textDocument: { uri: objectUri },
    position: { line: 4, character: 4 },
    context: { triggerKind: 1 },
  })
  server.stdin.write(objectCompletionRequest.packet)
  const objectCompletionResponse = await waitForResponse(
    objectCompletionRequest.id,
    messages,
    `Qin object completion response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const objectCompletionItems = Array.isArray(objectCompletionResponse.result)
    ? objectCompletionResponse.result
    : objectCompletionResponse.result?.items ?? []
  const objectCompletionLabels = objectCompletionItems.map((item: any) => item.label)
  if (!objectCompletionLabels.includes('Counter')) {
    throw new Error(`Qin object completion did not include generated singleton symbol: ${JSON.stringify(objectCompletionLabels.slice(0, 30))}`)
  }

  const objectExtendsCompletionRequest = createRequest('textDocument/completion', {
    textDocument: { uri: objectExtendsUri },
    position: { line: 7, character: 10 },
    context: { triggerKind: 1 },
  })
  server.stdin.write(objectExtendsCompletionRequest.packet)
  const objectExtendsCompletionResponse = await waitForResponse(
    objectExtendsCompletionRequest.id,
    messages,
    `Qin object extends completion response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const objectExtendsCompletionItems = Array.isArray(objectExtendsCompletionResponse.result)
    ? objectExtendsCompletionResponse.result
    : objectExtendsCompletionResponse.result?.items ?? []
  const objectExtendsCompletionLabels = objectExtendsCompletionItems.map((item: any) => item.label)
  if (!objectExtendsCompletionLabels.includes('baseValue')) {
    throw new Error(`Qin object extends completion did not include inherited class field: ${JSON.stringify(objectExtendsCompletionLabels.slice(0, 30))}`)
  }

  const forOfCompletionRequest = createRequest('textDocument/completion', {
    textDocument: { uri: forOfUri },
    position: { line: 5, character: 5 },
    context: { triggerKind: 1 },
  })
  server.stdin.write(forOfCompletionRequest.packet)
  const forOfCompletionResponse = await waitForResponse(
    forOfCompletionRequest.id,
    messages,
    `Qin for...of completion response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const forOfCompletionItems = Array.isArray(forOfCompletionResponse.result)
    ? forOfCompletionResponse.result
    : forOfCompletionResponse.result?.items ?? []
  const forOfCompletionLabels = forOfCompletionItems.map((item: any) => item.label)
  if (!forOfCompletionLabels.includes('totalValue')) {
    throw new Error(`Qin for...of completion did not include loop-scope symbol: ${JSON.stringify(forOfCompletionLabels.slice(0, 30))}`)
  }

  const importCompletionRequest = createRequest('textDocument/completion', {
    textDocument: { uri: importConsumerUri },
    position: { line: 2, character: 10 },
    context: { triggerKind: 1 },
  })
  server.stdin.write(importCompletionRequest.packet)
  const importCompletionResponse = await waitForResponse(
    importCompletionRequest.id,
    messages,
    `Qin cross-file import completion response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const importCompletionItems = Array.isArray(importCompletionResponse.result)
    ? importCompletionResponse.result
    : importCompletionResponse.result?.items ?? []
  const importCompletionLabels = importCompletionItems.map((item: any) => item.label)
  if (!importCompletionLabels.includes('value')) {
    throw new Error(`Qin cross-file import completion did not include imported object field: ${JSON.stringify(importCompletionLabels.slice(0, 30))}`)
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

  const forOfDefinitionRequest = createRequest('textDocument/definition', {
    textDocument: { uri: forOfUri },
    position: { line: 3, character: 32 },
  })
  server.stdin.write(forOfDefinitionRequest.packet)
  const forOfDefinitionResponse = await waitForResponse(
    forOfDefinitionRequest.id,
    messages,
    `Qin for...of definition response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const forOfDefinitionLocations = Array.isArray(forOfDefinitionResponse.result)
    ? forOfDefinitionResponse.result
    : forOfDefinitionResponse.result ? [forOfDefinitionResponse.result] : []
  if (!forOfDefinitionLocations.some(item => sameUri(locationUri(item), forOfUri) && rangeContains(item, 2, 13))) {
    throw new Error(`Qin for...of definition did not resolve itemValue declaration: ${JSON.stringify(forOfDefinitionResponse.result)}`)
  }

  const objectDefinitionRequest = createRequest('textDocument/definition', {
    textDocument: { uri: objectUri },
    position: { line: 3, character: 23 },
  })
  server.stdin.write(objectDefinitionRequest.packet)
  const objectDefinitionResponse = await waitForResponse(
    objectDefinitionRequest.id,
    messages,
    `Qin object definition response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const objectDefinitionLocations = Array.isArray(objectDefinitionResponse.result)
    ? objectDefinitionResponse.result
    : objectDefinitionResponse.result ? [objectDefinitionResponse.result] : []
  if (!objectDefinitionLocations.some(item => sameUri(locationUri(item), objectUri))) {
    throw new Error(`Qin object definition did not resolve Counter through generated object lowering: ${JSON.stringify(objectDefinitionResponse.result)}`)
  }

  const objectExtendsDefinitionRequest = createRequest('textDocument/definition', {
    textDocument: { uri: objectExtendsUri },
    position: { line: 6, character: 31 },
  })
  server.stdin.write(objectExtendsDefinitionRequest.packet)
  const objectExtendsDefinitionResponse = await waitForResponse(
    objectExtendsDefinitionRequest.id,
    messages,
    `Qin object extends definition response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const objectExtendsDefinitionLocations = Array.isArray(objectExtendsDefinitionResponse.result)
    ? objectExtendsDefinitionResponse.result
    : objectExtendsDefinitionResponse.result ? [objectExtendsDefinitionResponse.result] : []
  if (!objectExtendsDefinitionLocations.some(item => sameUri(locationUri(item), objectExtendsUri) && rangeContains(item, 1, 2))) {
    throw new Error(`Qin object extends definition did not resolve inherited class field: ${JSON.stringify(objectExtendsDefinitionResponse.result)}`)
  }

  const importSymbolDefinitionRequest = createRequest('textDocument/definition', {
    textDocument: { uri: importConsumerUri },
    position: { line: 0, character: 10 },
  })
  server.stdin.write(importSymbolDefinitionRequest.packet)
  const importSymbolDefinitionResponse = await waitForResponse(
    importSymbolDefinitionRequest.id,
    messages,
    `Qin cross-file import symbol definition response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const importSymbolDefinitionLocations = Array.isArray(importSymbolDefinitionResponse.result)
    ? importSymbolDefinitionResponse.result
    : importSymbolDefinitionResponse.result ? [importSymbolDefinitionResponse.result] : []
  if (!importSymbolDefinitionLocations.some(item => sameUri(locationUri(item), importProviderUri) && rangeContains(item, 0, 14))) {
    throw new Error(`Qin cross-file import symbol definition did not resolve provider object: ${JSON.stringify(importSymbolDefinitionResponse.result)}`)
  }

  const importMemberDefinitionRequest = createRequest('textDocument/definition', {
    textDocument: { uri: importConsumerUri },
    position: { line: 1, character: 29 },
  })
  server.stdin.write(importMemberDefinitionRequest.packet)
  const importMemberDefinitionResponse = await waitForResponse(
    importMemberDefinitionRequest.id,
    messages,
    `Qin cross-file import member definition response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const importMemberDefinitionLocations = Array.isArray(importMemberDefinitionResponse.result)
    ? importMemberDefinitionResponse.result
    : importMemberDefinitionResponse.result ? [importMemberDefinitionResponse.result] : []
  if (!importMemberDefinitionLocations.some(item => sameUri(locationUri(item), importProviderUri) && rangeContains(item, 1, 2))) {
    throw new Error(`Qin cross-file import member definition did not resolve provider field: ${JSON.stringify(importMemberDefinitionResponse.result)}`)
  }

  const objectReferencesRequest = createRequest('textDocument/references', {
    textDocument: { uri: objectUri },
    position: { line: 3, character: 23 },
    context: { includeDeclaration: true },
  })
  server.stdin.write(objectReferencesRequest.packet)
  const objectReferencesResponse = await waitForResponse(
    objectReferencesRequest.id,
    messages,
    `Qin object references response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const objectReferences = Array.isArray(objectReferencesResponse.result) ? objectReferencesResponse.result : []
  if (
    !objectReferences.some(item => sameUri(locationUri(item), objectUri) && rangeStartsAt(item, 3, 21))
    || !objectReferences.some(item => sameUri(locationUri(item), objectUri) && rangeStartsAt(item, 0, 14))
  ) {
    throw new Error(`Qin object references did not include generated object usage mappings: ${JSON.stringify(objectReferencesResponse.result)}`)
  }

  const forOfReferencesRequest = createRequest('textDocument/references', {
    textDocument: { uri: forOfUri },
    position: { line: 3, character: 32 },
    context: { includeDeclaration: true },
  })
  server.stdin.write(forOfReferencesRequest.packet)
  const forOfReferencesResponse = await waitForResponse(
    forOfReferencesRequest.id,
    messages,
    `Qin for...of references response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const forOfReferences = Array.isArray(forOfReferencesResponse.result) ? forOfReferencesResponse.result : []
  if (
    !forOfReferences.some(item => sameUri(locationUri(item), forOfUri) && rangeStartsAt(item, 2, 13))
    || !forOfReferences.some(item => sameUri(locationUri(item), forOfUri) && rangeStartsAt(item, 3, 30))
  ) {
    throw new Error(`Qin for...of references did not include itemValue declaration and usage: ${JSON.stringify(forOfReferencesResponse.result)}`)
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

  const documentHighlightRequest = createRequest('textDocument/documentHighlight', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 0, character: 8 },
  })
  server.stdin.write(documentHighlightRequest.packet)
  const documentHighlightResponse = await waitForResponse(
    documentHighlightRequest.id,
    messages,
    `Qin TS-subset documentHighlight response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const documentHighlights = Array.isArray(documentHighlightResponse.result) ? documentHighlightResponse.result : []
  if (
    !documentHighlights.some(item => rangeStartsAt(item, 0, 6))
    || !documentHighlights.some(item => rangeStartsAt(item, 1, 18))
  ) {
    throw new Error(`Qin documentHighlight did not include alphaNumber declaration and usage: ${JSON.stringify(documentHighlightResponse.result)}`)
  }

  const renameRequest = createRequest('textDocument/rename', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 0, character: 8 },
    newName: 'renamedNumber',
  })
  server.stdin.write(renameRequest.packet)
  const renameResponse = await waitForResponse(
    renameRequest.id,
    messages,
    `Qin TS-subset rename response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const renameTexts = collectWorkspaceEditTexts(renameResponse.result)
  if (renameTexts.filter(text => text === 'renamedNumber').length < 2) {
    throw new Error(`Qin rename did not return declaration and usage edits: ${JSON.stringify(renameResponse.result)}`)
  }

  const prepareRenameRequest = createRequest('textDocument/prepareRename', {
    textDocument: { uri: tsSubsetUri },
    position: { line: 0, character: 8 },
  })
  server.stdin.write(prepareRenameRequest.packet)
  const prepareRenameResponse = await waitForResponse(
    prepareRenameRequest.id,
    messages,
    `Qin TS-subset prepareRename response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const prepareRenameRange = prepareRenameResponse.result?.range ?? prepareRenameResponse.result
  if (!rangeStartsAt({ range: prepareRenameRange }, 0, 6)) {
    throw new Error(`Qin prepareRename did not return alphaNumber declaration range: ${JSON.stringify(prepareRenameResponse.result)}`)
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

  const objectDocumentSymbolRequest = createRequest('textDocument/documentSymbol', {
    textDocument: { uri: objectUri },
  })
  server.stdin.write(objectDocumentSymbolRequest.packet)
  const objectDocumentSymbolResponse = await waitForResponse(
    objectDocumentSymbolRequest.id,
    messages,
    `Qin object documentSymbol response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const objectSymbolNames = collectSymbolNames(Array.isArray(objectDocumentSymbolResponse.result) ? objectDocumentSymbolResponse.result : [])
  if (!objectSymbolNames.includes('Counter') || objectSymbolNames.includes('__QinObject_Counter')) {
    throw new Error(`Qin object documentSymbol did not expose source object symbols: ${JSON.stringify(objectDocumentSymbolResponse.result)}`)
  }

  const foldingRangeRequest = createRequest('textDocument/foldingRange', {
    textDocument: { uri: objectUri },
  })
  server.stdin.write(foldingRangeRequest.packet)
  const foldingRangeResponse = await waitForResponse(
    foldingRangeRequest.id,
    messages,
    `Qin object foldingRange response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const foldingRanges = Array.isArray(foldingRangeResponse.result) ? foldingRangeResponse.result : []
  if (!foldingRanges.some((item: any) => item.startLine === 0 && item.endLine >= 2)) {
    throw new Error(`Qin foldingRange did not include object block range: ${JSON.stringify(foldingRangeResponse.result)}`)
  }

  const selectionRangeRequest = createRequest('textDocument/selectionRange', {
    textDocument: { uri: objectUri },
    positions: [{ line: 0, character: 16 }],
  })
  server.stdin.write(selectionRangeRequest.packet)
  const selectionRangeResponse = await waitForResponse(
    selectionRangeRequest.id,
    messages,
    `Qin object selectionRange response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const selectionRanges = Array.isArray(selectionRangeResponse.result) ? selectionRangeResponse.result : []
  const objectSelectionRange = selectionRanges[0]
  if (!selectionRangeChainContains(objectSelectionRange, 0, 14)
    || !selectionRangeChainContains(objectSelectionRange, 0, 7)
    || !selectionRangeChainContains(objectSelectionRange, 3, 0)) {
    throw new Error(`Qin selectionRange did not include object name, declaration, and source ranges: ${JSON.stringify(selectionRangeResponse.result)}`)
  }

  const linkedEditingRequest = createRequest('textDocument/linkedEditingRange', {
    textDocument: { uri: objectUri },
    position: { line: 0, character: 16 },
  })
  server.stdin.write(linkedEditingRequest.packet)
  const linkedEditingResponse = await waitForResponse(
    linkedEditingRequest.id,
    messages,
    `Qin object linkedEditingRange response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const linkedRanges = Array.isArray(linkedEditingResponse.result?.ranges) ? linkedEditingResponse.result.ranges : []
  if (!linkedRanges.some((range: any) => rangeStartsAt({ range }, 0, 14))
    || !linkedRanges.some((range: any) => rangeStartsAt({ range }, 3, 21))) {
    throw new Error(`Qin linkedEditingRange did not include object declaration and usage ranges: ${JSON.stringify(linkedEditingResponse.result)}`)
  }

  const documentLinkRequest = createRequest('textDocument/documentLink', {
    textDocument: { uri: importConsumerUri },
  })
  server.stdin.write(documentLinkRequest.packet)
  const documentLinkResponse = await waitForResponse(
    documentLinkRequest.id,
    messages,
    `Qin import documentLink response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const documentLinks = Array.isArray(documentLinkResponse.result) ? documentLinkResponse.result : []
  if (!documentLinks.some((item: any) => sameUri(item.target, importProviderUri) && rangeStartsAt(item, 0, 25))) {
    throw new Error(`Qin documentLink did not include local import target: ${JSON.stringify(documentLinkResponse.result)}`)
  }

  const workspaceSymbolRequest = createRequest('workspace/symbol', {
    query: 'Counter',
  })
  server.stdin.write(workspaceSymbolRequest.packet)
  const workspaceSymbolResponse = await waitForResponse(
    workspaceSymbolRequest.id,
    messages,
    `Qin workspaceSymbol response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const workspaceSymbols = Array.isArray(workspaceSymbolResponse.result) ? workspaceSymbolResponse.result : []
  if (!workspaceSymbols.some((item: any) => item.name === 'Counter' && sameUri(item.location?.uri, objectUri) && rangeContains(item.location, 0, 14))) {
    throw new Error(`Qin workspaceSymbol did not include source Counter object: ${JSON.stringify(workspaceSymbolResponse.result)}`)
  }

  const forOfDocumentSymbolRequest = createRequest('textDocument/documentSymbol', {
    textDocument: { uri: forOfUri },
  })
  server.stdin.write(forOfDocumentSymbolRequest.packet)
  const forOfDocumentSymbolResponse = await waitForResponse(
    forOfDocumentSymbolRequest.id,
    messages,
    `Qin for...of documentSymbol response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const forOfSymbolNames = collectSymbolNames(Array.isArray(forOfDocumentSymbolResponse.result) ? forOfDocumentSymbolResponse.result : [])
  if (!forOfSymbolNames.includes('sumItems')) {
    throw new Error(`Qin for...of documentSymbol did not include function symbol: ${JSON.stringify(forOfDocumentSymbolResponse.result)}`)
  }

  const objectSemanticTokensRequest = createRequest('textDocument/semanticTokens/full', {
    textDocument: { uri: objectUri },
  })
  server.stdin.write(objectSemanticTokensRequest.packet)
  const objectSemanticTokensResponse = await waitForResponse(
    objectSemanticTokensRequest.id,
    messages,
    `Qin object semanticTokens response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const objectSemanticTokenData = objectSemanticTokensResponse.result?.data ?? []
  if (!Array.isArray(objectSemanticTokenData) || objectSemanticTokenData.length === 0) {
    throw new Error(`Qin object semanticTokens did not return token data: ${JSON.stringify(objectSemanticTokensResponse.result)}`)
  }
  requireSemanticTokenAt(objectSemanticTokensResponse.result, 0, 14, 'Qin object declaration Counter')
  requireSemanticTokenAt(objectSemanticTokensResponse.result, 3, 21, 'Qin object usage Counter')

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

  const forOfSemanticTokensRequest = createRequest('textDocument/semanticTokens/full', {
    textDocument: { uri: forOfUri },
  })
  server.stdin.write(forOfSemanticTokensRequest.packet)
  const forOfSemanticTokensResponse = await waitForResponse(
    forOfSemanticTokensRequest.id,
    messages,
    `Qin for...of semanticTokens response. exitCode=${exitCode} stderr=${stderr} messages=${JSON.stringify(messages)}`,
  )
  const forOfSemanticTokenData = forOfSemanticTokensResponse.result?.data ?? []
  if (!Array.isArray(forOfSemanticTokenData) || forOfSemanticTokenData.length === 0) {
    throw new Error(`Qin for...of semanticTokens did not return token data: ${JSON.stringify(forOfSemanticTokensResponse.result)}`)
  }
  requireSemanticTokenAt(forOfSemanticTokensResponse.result, 2, 13, 'Qin for...of declaration itemValue')
  requireSemanticTokenAt(forOfSemanticTokensResponse.result, 3, 30, 'Qin for...of usage itemValue')

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
