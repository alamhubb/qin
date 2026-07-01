import type { LanguageServicePlugin } from '@volar/language-service'
import { DiagnosticSeverity, type FoldingRange, type Position, type Range, type SelectionRange } from 'vscode-languageserver-protocol'
import type { TextDocument } from 'vscode-languageserver-textdocument'
import { parseGeneratedQinSource, probeGeneratedQinParser, type QinGeneratedParserProbeResult } from './QinGeneratedParserProbe'
import { provideSourceDocumentSymbols } from './SourceDocumentSymbols'

export const QinLanguageServicePlugin: LanguageServicePlugin = {
  name: 'qin-generated-parser-diagnostics',
  capabilities: {
    diagnosticProvider: {
      interFileDependencies: false,
      workspaceDiagnostics: false,
    },
    documentSymbolProvider: true,
    foldingRangeProvider: true,
    selectionRangeProvider: true,
  },
  create() {
    return {
      provideDocumentSymbols(document: TextDocument) {
        if (!isQinDocument(document)) {
          return
        }
        return provideSourceDocumentSymbols(document)
      },
      provideFoldingRanges(document: TextDocument) {
        if (!isQinDocument(document)) {
          return []
        }
        return provideSourceFoldingRanges(document)
      },
      provideSelectionRanges(document: TextDocument, positions: Position[]) {
        if (!isQinDocument(document)) {
          return positions.map(position => createPointSelectionRange(position))
        }
        return provideSourceSelectionRanges(document, positions)
      },
      provideDiagnostics(document: TextDocument) {
        if (!isQinDocument(document)) {
          return []
        }
        const result = probeGeneratedQinParser(document.getText())
        return createQinParserDiagnostics(result)
      },
    }
  },
}

function isQinDocument(document: TextDocument): boolean {
  return document.languageId === 'qin'
}

export function createQinParserDiagnostics(result: QinGeneratedParserProbeResult) {
  if (result.ok) {
    return []
  }
  const diagnostics = result.available
    ? result.diagnostics ?? [{
      message: result.error ?? 'Qin parser error',
      line: 0,
      column: 0,
    }]
    : [{
      message: 'Generated Qin parser package is not available',
      line: 0,
      column: 0,
    }]
  return diagnostics.map(diagnostic => {
    const position = {
      line: diagnostic.line,
      character: diagnostic.column,
    }
    return {
      range: {
        start: position,
        end: {
          line: position.line,
          character: position.character + 1,
        },
      },
      severity: DiagnosticSeverity.Error,
      source: 'qin-parser',
      message: diagnostic.message,
    }
  })
}

function provideSourceFoldingRanges(document: TextDocument): FoldingRange[] {
  const source = document.getText()
  const parsed = parseGeneratedQinSource(source)
  if (!parsed.ok || !parsed.cst) {
    return []
  }
  const ranges: FoldingRange[] = []
  walkCst(parsed.cst, node => {
    if (readCstName(node) !== 'QinObjectDeclarationBody') {
      return
    }
    const openBrace = findToken(node, item => readCstName(item) === 'LBrace')
    const closeBrace = findLastToken(node, item => readCstName(item) === 'RBrace')
    if (!openBrace || !closeBrace) {
      return
    }
    const startOffset = readTokenStart(openBrace)
    const endOffset = readTokenEnd(closeBrace)
    if (startOffset < 0 || endOffset <= startOffset || endOffset > source.length) {
      return
    }
    const start = document.positionAt(startOffset)
    const end = document.positionAt(endOffset)
    if (end.line <= start.line) {
      return
    }
    ranges.push({
      startLine: start.line,
      startCharacter: start.character,
      endLine: end.line,
      endCharacter: end.character,
    })
  })
  return ranges
}

function provideSourceSelectionRanges(document: TextDocument, positions: Position[]): SelectionRange[] {
  const source = document.getText()
  const parsed = parseGeneratedQinSource(source)
  if (!parsed.ok || !parsed.cst) {
    return positions.map(position => createPointSelectionRange(position))
  }
  const objectRanges = collectQinObjectSelectionRanges(source, parsed.cst)
  const sourceRange = createRange(document, 0, source.length)
  return positions.map(position => {
    const offset = document.offsetAt(position)
    const objectRange = objectRanges.find(item => offset >= item.nameStart && offset <= item.nameEnd)
    if (!objectRange) {
      return {
        range: createZeroWidthRange(position),
        parent: { range: sourceRange },
      }
    }
    return {
      range: createRange(document, objectRange.nameStart, objectRange.nameEnd),
      parent: {
        range: createRange(document, objectRange.declarationStart, objectRange.declarationEnd),
        parent: { range: sourceRange },
      },
    }
  })
}

interface QinObjectSelectionInfo {
  nameStart: number
  nameEnd: number
  declarationStart: number
  declarationEnd: number
}

function collectQinObjectSelectionRanges(source: string, cst: unknown): QinObjectSelectionInfo[] {
  const ranges: QinObjectSelectionInfo[] = []
  walkCst(cst, node => {
    if (readCstName(node) !== 'QinObjectDeclarationBody') {
      return
    }
    const objectKeyword = findToken(node, item => readCstName(item) === 'IdentifierName' && readCstValue(item) === 'object')
    const nameToken = findBindingIdentifierName(node)
    const closeBrace = findLastToken(node, item => readCstName(item) === 'RBrace')
    if (!objectKeyword || !nameToken || !closeBrace) {
      return
    }
    const declarationStart = readTokenStart(objectKeyword)
    const declarationEnd = readTokenEnd(closeBrace)
    const nameStart = readTokenStart(nameToken)
    const nameEnd = readTokenEnd(nameToken)
    if (!isValidRange(declarationStart, declarationEnd, source.length) || !isValidRange(nameStart, nameEnd, source.length)) {
      return
    }
    ranges.push({
      nameStart,
      nameEnd,
      declarationStart,
      declarationEnd,
    })
  })
  return ranges
}

function createRange(document: TextDocument, startOffset: number, endOffset: number): Range {
  return {
    start: document.positionAt(startOffset),
    end: document.positionAt(endOffset),
  }
}

function createPointSelectionRange(position: Position): SelectionRange {
  return { range: createZeroWidthRange(position) }
}

function createZeroWidthRange(position: Position): Range {
  return {
    start: position,
    end: position,
  }
}

function findBindingIdentifierName(cst: unknown): unknown | undefined {
  let found: unknown | undefined
  walkCst(cst, node => {
    if (found) {
      return
    }
    if (readCstName(node) === 'BindingIdentifier') {
      const token = findToken(node, item => readCstName(item) === 'IdentifierName' && readCstValue(item) !== undefined)
      if (token) {
        found = token
      }
    }
  })
  return found
}

function findToken(cst: unknown, predicate: (node: unknown) => boolean): unknown | undefined {
  let found: unknown | undefined
  walkCst(cst, node => {
    if (!found && predicate(node)) {
      found = node
    }
  })
  return found
}

function findLastToken(cst: unknown, predicate: (node: unknown) => boolean): unknown | undefined {
  let found: unknown | undefined
  walkCst(cst, node => {
    if (predicate(node)) {
      found = node
    }
  })
  return found
}

function walkCst(cst: unknown, visit: (node: unknown) => void): void {
  if (!cst || typeof cst !== 'object') {
    return
  }
  visit(cst)
  for (const child of readCstChildren(cst)) {
    walkCst(child, visit)
  }
}

function readCstChildren(cst: unknown): unknown[] {
  const children = callMethod(cst, 'getChildren') ?? (cst as { __qin_field_children?: unknown }).__qin_field_children
  if (!children) {
    return []
  }
  if (Array.isArray(children)) {
    return children
  }
  if (typeof (children as { [Symbol.iterator]?: unknown })[Symbol.iterator] === 'function') {
    return Array.from(children as Iterable<unknown>)
  }
  return []
}

function readCstName(cst: unknown): string | undefined {
  const value = callMethod(cst, 'getName') ?? (cst as { __qin_field_name?: unknown }).__qin_field_name
  return value == null ? undefined : String(value)
}

function readCstValue(cst: unknown): string | undefined {
  const value = callMethod(cst, 'getValue') ?? (cst as { __qin_field_value?: unknown }).__qin_field_value
  return value == null ? undefined : String(value)
}

function readTokenStart(cst: unknown): number {
  return readTokenPosition(cst, 'getStart', '__qin_field_start')
}

function readTokenEnd(cst: unknown): number {
  return readTokenPosition(cst, 'getEnd', '__qin_field_end')
}

function readTokenPosition(cst: unknown, methodName: string, fieldName: string): number {
  const loc = callMethod(cst, 'getLoc') ?? callMethod(cst, 'getLocation') ?? (cst as { __qin_field_loc?: unknown }).__qin_field_loc
  const position = callMethod(loc, methodName) ?? (loc as Record<string, unknown> | undefined)?.[fieldName]
  const index = callMethod(position, 'getIndex') ?? callMethod(position, 'index') ?? (position as { __qin_field_index?: unknown } | undefined)?.__qin_field_index
  const value = Number(index)
  return Number.isFinite(value) ? value : -1
}

function callMethod(value: unknown, methodName: string): unknown {
  if (!value || typeof value !== 'object') {
    return undefined
  }
  const method = (value as Record<string, unknown>)[methodName]
  return typeof method === 'function' ? method.call(value) : undefined
}

function isValidRange(start: number, end: number, sourceLength: number): boolean {
  return start >= 0 && end >= start && end <= sourceLength
}
