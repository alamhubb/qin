import { createRequire } from 'node:module'
import { logToFile } from './logutil'

const GENERATED_QIN_PARSER_PACKAGE = '@qin/generated-qin-parser-ts'

type QinParserConstructor = new (source: string) => {
  enableErrorRecovery?: () => unknown
  getRecoveryDiagnostics?: () => unknown
  parse?: () => unknown
  Program?: (...args: unknown[]) => unknown
}

let parserConstructor: QinParserConstructor | null | undefined

export type QinGeneratedParserMode = 'strict' | 'editor'

export interface QinGeneratedParserOptions {
  mode?: QinGeneratedParserMode
}

export interface QinGeneratedParserProbeResult {
  available: boolean
  ok: boolean
  cstName?: string
  error?: string
  diagnostics?: QinGeneratedParserDiagnostic[]
}

export interface QinGeneratedParserParseResult extends QinGeneratedParserProbeResult {
  cst?: unknown
}

export interface QinGeneratedParserDiagnostic {
  message: string
  line: number
  column: number
  offset?: number
  length?: number
}

export function probeGeneratedQinParser(
  source: string,
  options: QinGeneratedParserOptions = {},
): QinGeneratedParserProbeResult {
  const result = parseGeneratedQinSource(source, options)
  return {
    available: result.available,
    ok: result.ok,
    cstName: result.cstName,
    error: result.error,
    diagnostics: result.diagnostics,
  }
}

export function parseGeneratedQinSource(
  source: string,
  options: QinGeneratedParserOptions = {},
): QinGeneratedParserParseResult {
  const Parser = loadGeneratedQinParser()
  if (!Parser) {
    return { available: false, ok: false }
  }

  try {
    const parser = new Parser(stripBom(source ?? ''))
    if (options.mode === 'editor') {
      enableEditorErrorRecovery(parser)
    }
    const cst = typeof parser.parse === 'function'
      ? parser.parse()
      : typeof parser.Program === 'function'
        ? parser.Program()
        : null
    const cstName = readCstName(cst)
    const parserFail = readParserFail(parser)
    const recoveryDiagnostics = readRecoveryDiagnostics(parser)
    const diagnostics = parserFail && recoveryDiagnostics.length === 0
      ? createParserFailDiagnostics(parser, source, options.mode)
      : recoveryDiagnostics
    return {
      available: true,
      ok: cstName === 'Program' && !parserFail && (options.mode !== 'editor' || diagnostics.length === 0),
      cstName,
      cst,
      diagnostics,
    }
  } catch (error) {
    const message = error instanceof Error ? error.stack || error.message : String(error)
    logToFile('Generated Qin parser probe failed:', message)
    return {
      available: true,
      ok: false,
      error: message,
      diagnostics: [createGeneratedParserDiagnostic(error)],
    }
  }
}

function readParserFail(parser: unknown): boolean {
  if (!parser || typeof parser !== 'object') {
    return false
  }
  const isParserFail = (parser as { isParserFail?: () => unknown }).isParserFail
  if (typeof isParserFail !== 'function') {
    return false
  }
  return isParserFail.call(parser) === true
}

function createParserFailDiagnostics(
  parser: unknown,
  source: string,
  mode: 'strict' | 'editor' | undefined,
): QinGeneratedParserDiagnostic[] {
  const token = callObjectMethod(parser, 'curToken')
  const line = Math.max(0, callNumberMethod(token, 'rowNum', 1) - 1)
  const column = Math.max(0, callNumberMethod(token, 'columnStartNum', 1) - 1)
  const offset = callNumberMethod(token, 'index', -1)
  const tokenValue = callStringMethod(token, 'value') ?? callStringMethod(token, 'tokenName')
  if (mode === 'editor' && isEditorIncompleteMemberAccess(source, offset)) {
    return []
  }
  return [{
    message: 'Qin parser rejected source',
    line,
    column,
    offset: offset >= 0 ? offset : undefined,
    length: tokenValue ? tokenValue.length : undefined,
  }]
}

function isEditorIncompleteMemberAccess(source: string, offset: number): boolean {
  const cursor = offset >= 0 ? Math.min(offset, source.length) : source.length
  let previous = cursor - 1
  while (previous >= 0 && /\s/.test(source.charAt(previous))) {
    previous--
  }
  if (previous < 0 || source.charAt(previous) !== '.') {
    return false
  }
  if (cursor >= source.length) {
    return true
  }
  const current = source.charAt(cursor)
  return /[A-Za-z_$}]/.test(current)
}

function readRecoveryDiagnostics(parser: unknown): QinGeneratedParserDiagnostic[] {
  if (!parser || typeof parser !== 'object') {
    return []
  }
  const getRecoveryDiagnostics = (parser as { getRecoveryDiagnostics?: () => unknown }).getRecoveryDiagnostics
  if (typeof getRecoveryDiagnostics !== 'function') {
    return []
  }
  return toArray(getRecoveryDiagnostics.call(parser)).map(readRecoveryDiagnostic).filter(item => item != null)
}

function readRecoveryDiagnostic(value: unknown): QinGeneratedParserDiagnostic | undefined {
  if (!value || typeof value !== 'object') {
    return
  }
  const message = callStringMethod(value, 'message')
    ?? readObjectStringField(value, '__qin_field_message')
    ?? 'Qin parser recovered from invalid syntax'
  const line = Math.max(0, callNumberMethod(value, 'line', 1) - 1)
  const column = Math.max(0, callNumberMethod(value, 'column', 1) - 1)
  const offset = callNumberMethod(value, 'offset', -1)
  const tokenValue = callStringMethod(value, 'tokenValue') ?? readObjectStringField(value, '__qin_field_tokenValue')
  return {
    message,
    line,
    column,
    offset: offset >= 0 ? offset : undefined,
    length: tokenValue ? tokenValue.length : undefined,
  }
}

function enableEditorErrorRecovery(parser: unknown): void {
  if (!parser || typeof parser !== 'object') {
    return
  }
  const enableErrorRecovery = (parser as { enableErrorRecovery?: () => unknown }).enableErrorRecovery
  if (typeof enableErrorRecovery === 'function') {
    enableErrorRecovery.call(parser)
  }
}

export function createGeneratedParserDiagnostic(error: unknown): QinGeneratedParserDiagnostic {
  const details = readErrorDetails(error)
  const position = readDetailsPosition(details)
  const line = Math.max(0, readNumber(position, 'line', '__qin_field_line', 1) - 1)
  const column = Math.max(0, readNumber(position, 'column', '__qin_field_column', 1) - 1)
  const offset = readNumber(position, 'offset', '__qin_field_offset', -1)
  return {
    message: createParserDiagnosticMessage(error, details),
    line,
    column,
    offset: offset >= 0 ? offset : undefined,
  }
}

function loadGeneratedQinParser(): QinParserConstructor | null {
  if (parserConstructor !== undefined) {
    return parserConstructor
  }

  try {
    const require = createRequire(import.meta.url)
    const parserModule = require(GENERATED_QIN_PARSER_PACKAGE)
    parserConstructor = parserModule.default ?? parserModule.QinParser ?? parserModule.com_qin_parser_QinParser ?? null
  } catch (error) {
    const message = error instanceof Error ? error.message : String(error)
    logToFile(`Generated Qin parser package not available: ${GENERATED_QIN_PARSER_PACKAGE}`, message)
    parserConstructor = null
  }
  return parserConstructor
}

function readCstName(cst: unknown): string | undefined {
  if (cst && typeof cst === 'object' && 'getName' in cst) {
    const getName = (cst as { getName?: () => unknown }).getName
    if (typeof getName === 'function') {
      const value = getName.call(cst)
      return value == null ? undefined : String(value)
    }
  }
  return undefined
}

function stripBom(source: string): string {
  return source.charCodeAt(0) === 0xFEFF ? source.substring(1) : source
}

function toArray(value: unknown): unknown[] {
  if (!value) {
    return []
  }
  if (Array.isArray(value)) {
    return value
  }
  if (typeof (value as { [Symbol.iterator]?: unknown })[Symbol.iterator] === 'function') {
    return Array.from(value as Iterable<unknown>)
  }
  return []
}

function readErrorDetails(error: unknown): unknown {
  if (!error || typeof error !== 'object') {
    return undefined
  }
  const getDetails = (error as { getDetails?: () => unknown }).getDetails
  if (typeof getDetails === 'function') {
    return getDetails.call(error)
  }
  return (error as { __qin_field_details?: unknown }).__qin_field_details
}

function readDetailsPosition(details: unknown): unknown {
  if (!details || typeof details !== 'object') {
    return undefined
  }
  const position = (details as { position?: () => unknown }).position
  if (typeof position === 'function') {
    return position.call(details)
  }
  return (details as { __qin_field_position?: unknown }).__qin_field_position
}

function readNumber(value: unknown, methodName: string, fieldName: string, defaultValue: number): number {
  if (!value || typeof value !== 'object') {
    return defaultValue
  }
  const method = (value as Record<string, unknown>)[methodName]
  if (typeof method === 'function') {
    const result = Number(method.call(value))
    return Number.isFinite(result) ? result : defaultValue
  }
  const field = Number((value as Record<string, unknown>)[fieldName])
  return Number.isFinite(field) ? field : defaultValue
}

function createParserDiagnosticMessage(error: unknown, details: unknown): string {
  const detailMessage = createDetailsMessage(details)
  if (detailMessage) {
    return detailMessage
  }
  if (error instanceof Error) {
    return error.message || error.stack || String(error)
  }
  return String(error)
}

function createDetailsMessage(details: unknown): string | undefined {
  if (!details || typeof details !== 'object') {
    return undefined
  }
  const errorType = readDetailString(details, 'errorType', '__qin_field_errorType')
  const expected = readDetailString(details, 'expected', '__qin_field_expected')
  const found = readFoundToken(details)
  const hint = readDetailString(details, 'hint', '__qin_field_hint')
  const parts = [
    errorType ? `Qin parser ${errorType}` : 'Qin parser error',
    expected ? `expected ${expected}` : '',
    found ? `found ${found}` : '',
    hint ? hint : '',
  ].filter(Boolean)
  return parts.length ? parts.join(', ') : undefined
}

function readDetailString(details: unknown, methodName: string, fieldName: string): string | undefined {
  if (!details || typeof details !== 'object') {
    return undefined
  }
  const method = (details as Record<string, unknown>)[methodName]
  const value = typeof method === 'function'
    ? method.call(details)
    : (details as Record<string, unknown>)[fieldName]
  return value == null ? undefined : String(value)
}

function readFoundToken(details: unknown): string | undefined {
  if (!details || typeof details !== 'object') {
    return undefined
  }
  const foundMethod = (details as { found?: () => unknown }).found
  const found = typeof foundMethod === 'function'
    ? foundMethod.call(details)
    : (details as { __qin_field_found?: unknown }).__qin_field_found
  if (!found || typeof found !== 'object') {
    return undefined
  }
  const tokenName = callStringMethod(found, 'tokenName')
  const value = callStringMethod(found, 'value')
  return value ? `${tokenName ?? 'token'} '${value}'` : tokenName
}

function callObjectMethod(value: unknown, methodName: string): unknown {
  if (!value || typeof value !== 'object') {
    return undefined
  }
  const method = (value as Record<string, unknown>)[methodName]
  return typeof method === 'function' ? method.call(value) : undefined
}

function callStringMethod(value: unknown, methodName: string): string | undefined {
  if (!value || typeof value !== 'object') {
    return undefined
  }
  const method = (value as Record<string, unknown>)[methodName]
  if (typeof method !== 'function') {
    return undefined
  }
  const result = method.call(value)
  return result == null ? undefined : String(result)
}

function callNumberMethod(value: unknown, methodName: string, defaultValue: number): number {
  if (!value || typeof value !== 'object') {
    return defaultValue
  }
  const method = (value as Record<string, unknown>)[methodName]
  if (typeof method !== 'function') {
    return defaultValue
  }
  const result = Number(method.call(value))
  return Number.isFinite(result) ? result : defaultValue
}

function readObjectStringField(value: unknown, fieldName: string): string | undefined {
  if (!value || typeof value !== 'object') {
    return undefined
  }
  const result = (value as Record<string, unknown>)[fieldName]
  return result == null ? undefined : String(result)
}
