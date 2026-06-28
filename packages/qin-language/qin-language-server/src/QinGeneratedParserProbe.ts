import { createRequire } from 'node:module'
import { logToFile } from './logutil'

const GENERATED_QIN_PARSER_PACKAGE = '@qin/generated-qin-parser-ts'

type QinParserConstructor = new (source: string) => {
  parse?: () => unknown
  Program?: (...args: unknown[]) => unknown
}

let parserConstructor: QinParserConstructor | null | undefined

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
}

export function probeGeneratedQinParser(source: string): QinGeneratedParserProbeResult {
  const result = parseGeneratedQinSource(source)
  return {
    available: result.available,
    ok: result.ok,
    cstName: result.cstName,
    error: result.error,
    diagnostics: result.diagnostics,
  }
}

export function parseGeneratedQinSource(source: string): QinGeneratedParserParseResult {
  const Parser = loadGeneratedQinParser()
  if (!Parser) {
    return { available: false, ok: false }
  }

  try {
    const parser = new Parser(stripBom(source ?? ''))
    const cst = typeof parser.parse === 'function'
      ? parser.parse()
      : typeof parser.Program === 'function'
        ? parser.Program()
        : null
    const cstName = readCstName(cst)
    return { available: true, ok: cstName === 'Program', cstName, cst }
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
