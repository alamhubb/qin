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
}

export function probeGeneratedQinParser(source: string): QinGeneratedParserProbeResult {
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
    return { available: true, ok: cstName === 'Program', cstName }
  } catch (error) {
    const message = error instanceof Error ? error.stack || error.message : String(error)
    logToFile('Generated Qin parser probe failed:', message)
    return { available: true, ok: false, error: message }
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
