import type { LanguageServicePlugin } from '@volar/language-service'
import path from 'node:path'
import ts from 'typescript'
import {
  type CodeAction,
  CodeActionKind,
  type CodeActionContext,
  DiagnosticSeverity,
  type DocumentLink,
  type DocumentSymbol,
  type FoldingRange,
  type LinkedEditingRanges,
  type Position,
  type Range,
  type SelectionRange,
  type WorkspaceSymbol,
} from 'vscode-languageserver-protocol'
import type { TextDocument } from 'vscode-languageserver-textdocument'
import { URI } from 'vscode-uri'
import { parseGeneratedQinSource, probeGeneratedQinParser, type QinGeneratedParserProbeResult } from './QinGeneratedParserProbe'
import { provideSourceDocumentSymbols } from './SourceDocumentSymbols'

export const QinLanguageServicePlugin: LanguageServicePlugin = {
  name: 'qin-generated-parser-diagnostics',
  capabilities: {
    diagnosticProvider: {
      interFileDependencies: false,
      workspaceDiagnostics: false,
    },
    codeActionProvider: {
      codeActionKinds: [CodeActionKind.QuickFix],
    },
    documentLinkProvider: {},
    documentSymbolProvider: true,
    foldingRangeProvider: true,
    linkedEditingRangeProvider: true,
    selectionRangeProvider: true,
    workspaceSymbolProvider: {},
  },
  create(context) {
    const workspaceSymbols = new Map<string, WorkspaceSymbol[]>()
    return {
      provideDocumentSymbols(document: TextDocument) {
        if (!isQinDocument(document)) {
          return
        }
        const symbols = provideSourceDocumentSymbols(document)
        workspaceSymbols.set(document.uri, createWorkspaceSymbolsFromDocumentSymbols(document, symbols))
        return symbols
      },
      provideDocumentLinks(document: TextDocument) {
        if (!isQinDocument(document)) {
          return []
        }
        const sourceUri = readSourceDocumentUri(context, document.uri)
        return provideSourceDocumentLinks(document, sourceUri)
      },
      provideCodeActions(document: TextDocument, range: Range, codeActionContext: CodeActionContext) {
        const sourceUri = readSourceDocumentUri(context, document.uri)
        if (!isQinDocument(document) && sourceUri === document.uri) {
          return []
        }
        return provideImportPolicyCodeActions(document, sourceUri, range, codeActionContext)
      },
      provideFoldingRanges(document: TextDocument) {
        if (!isQinDocument(document)) {
          return []
        }
        return provideSourceFoldingRanges(document)
      },
      provideLinkedEditingRanges(document: TextDocument, position: Position) {
        if (!isQinDocument(document)) {
          return
        }
        return provideSourceLinkedEditingRanges(document, position)
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
        updateWorkspaceSymbolsFromDocument(document, workspaceSymbols)
        const result = probeGeneratedQinParser(document.getText())
        const sourceUri = readSourceDocumentUri(context, document.uri)
        return [
          ...createQinParserDiagnostics(result),
          ...createQinImportPolicyDiagnostics(document, sourceUri),
        ]
      },
      provideWorkspaceSymbols(query: string) {
        const matched: WorkspaceSymbol[] = []
        for (const symbols of workspaceSymbols.values()) {
          for (const symbol of symbols) {
            if (matchesWorkspaceSymbolQuery(symbol.name, query)) {
              matched.push(symbol)
            }
          }
        }
        return matched
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

function updateWorkspaceSymbolsFromDocument(document: TextDocument, workspaceSymbols: Map<string, WorkspaceSymbol[]>): void {
  const symbols = provideSourceDocumentSymbols(document)
  workspaceSymbols.set(document.uri, createWorkspaceSymbolsFromDocumentSymbols(document, symbols))
}

function createWorkspaceSymbolsFromDocumentSymbols(document: TextDocument, symbols: DocumentSymbol[]): WorkspaceSymbol[] {
  const workspaceSymbols: WorkspaceSymbol[] = []
  collectWorkspaceSymbols(document, symbols, workspaceSymbols)
  return workspaceSymbols
}

function collectWorkspaceSymbols(document: TextDocument, symbols: DocumentSymbol[], workspaceSymbols: WorkspaceSymbol[]): void {
  for (const symbol of symbols) {
    workspaceSymbols.push({
      name: symbol.name,
      kind: symbol.kind,
      location: {
        uri: document.uri,
        range: symbol.selectionRange,
      },
    })
    if (symbol.children?.length) {
      collectWorkspaceSymbols(document, symbol.children, workspaceSymbols)
    }
  }
}

function matchesWorkspaceSymbolQuery(name: string, query: string): boolean {
  const normalizedName = name.toLowerCase()
  const normalizedQuery = query.trim().toLowerCase()
  if (!normalizedQuery) {
    return true
  }
  let nameIndex = 0
  for (const char of normalizedQuery) {
    nameIndex = normalizedName.indexOf(char, nameIndex)
    if (nameIndex < 0) {
      return false
    }
    nameIndex++
  }
  return true
}

function readSourceDocumentUri(context: Parameters<LanguageServicePlugin['create']>[0], documentUri: string): string {
  const uri = URI.parse(documentUri)
  return context.decodeEmbeddedDocumentUri?.(uri)?.[0].toString() ?? documentUri
}

function provideSourceDocumentLinks(document: TextDocument, sourceUri: string): DocumentLink[] {
  const source = document.getText()
  return collectModuleSpecifierLinks(source)
    .filter(item => isLocalModuleSpecifier(item.text))
    .map(item => ({
      range: createRange(document, item.start, item.end),
      target: resolveLocalModuleTarget(sourceUri, item.text),
    }))
}

interface SourceModuleSpecifierInfo {
  text: string
  start: number
  end: number
}

function collectModuleSpecifierLinks(source: string): SourceModuleSpecifierInfo[] {
  const sourceFile = ts.createSourceFile('source.qin', source, ts.ScriptTarget.Latest, true, ts.ScriptKind.TS)
  const links: SourceModuleSpecifierInfo[] = []
  const visit = (node: ts.Node): void => {
    const moduleSpecifier = readModuleSpecifier(node)
    if (moduleSpecifier) {
      links.push({
        text: moduleSpecifier.text,
        start: moduleSpecifier.getStart(sourceFile) + 1,
        end: moduleSpecifier.getEnd() - 1,
      })
    }
    ts.forEachChild(node, visit)
  }
  ts.forEachChild(sourceFile, visit)
  return links
}

function readModuleSpecifier(node: ts.Node): ts.StringLiteralLike | undefined {
  if ((ts.isImportDeclaration(node) || ts.isExportDeclaration(node)) && node.moduleSpecifier && isStringLiteralLike(node.moduleSpecifier)) {
    return node.moduleSpecifier
  }
  return undefined
}

function isStringLiteralLike(node: ts.Node): node is ts.StringLiteralLike {
  return node.kind === ts.SyntaxKind.StringLiteral || node.kind === ts.SyntaxKind.NoSubstitutionTemplateLiteral
}

function isLocalModuleSpecifier(specifier: string): boolean {
  return specifier.startsWith('./') || specifier.startsWith('../')
}

function createQinImportPolicyDiagnostics(document: TextDocument, sourceUri: string) {
  const zone = readQinSourceZone(sourceUri)
  if (zone !== 'app' && zone !== 'shared') {
    return []
  }
  return collectModuleSpecifierLinks(document.getText())
    .filter(item => isJavaModuleSpecifier(item.text))
    .map(item => ({
      range: createRange(document, item.start, item.end),
      severity: DiagnosticSeverity.Error,
      source: 'qin-import-policy',
      message: `${zone === 'app' ? 'QIN1001 app code' : 'QIN1002 shared code'} cannot import java modules: ${item.text}`,
    }))
}

function provideImportPolicyCodeActions(document: TextDocument, sourceUri: string, range: Range, context: CodeActionContext): CodeAction[] {
  const zone = readQinSourceZone(sourceUri)
  if (zone !== 'app' && zone !== 'shared') {
    return []
  }
  const actions: CodeAction[] = []
  const diagnostics = context.diagnostics.filter(diagnostic => diagnostic.source === 'qin-import-policy')
  for (const specifier of collectModuleSpecifierLinks(document.getText()).filter(item => isJavaModuleSpecifier(item.text))) {
    const specifierRange = createRange(document, specifier.start, specifier.end)
    if (!rangesOverlap(range, specifierRange)) {
      continue
    }
    const matchingDiagnostic = diagnostics.find(diagnostic => rangesOverlap(diagnostic.range, specifierRange))
    actions.push({
      title: 'Remove forbidden java import',
      kind: CodeActionKind.QuickFix,
      diagnostics: matchingDiagnostic ? [matchingDiagnostic] : [],
      edit: {
        changes: {
          [document.uri]: [{
            range: expandRangeToWholeLine(document, specifierRange),
            newText: '',
          }],
        },
      },
    })
  }
  return actions
}

function rangesOverlap(left: Range, right: Range): boolean {
  return comparePositions(left.start, right.end) <= 0 && comparePositions(right.start, left.end) <= 0
}

function comparePositions(left: Position, right: Position): number {
  if (left.line !== right.line) {
    return left.line - right.line
  }
  return left.character - right.character
}

function expandRangeToWholeLine(document: TextDocument, range: Range): Range {
  const lineStart = document.offsetAt({ line: range.start.line, character: 0 })
  const nextLineStart = range.start.line + 1 < document.lineCount
    ? document.offsetAt({ line: range.start.line + 1, character: 0 })
    : document.getText().length
  return createRange(document, lineStart, nextLineStart)
}

type QinSourceZone = 'app' | 'main' | 'shared'

function readQinSourceZone(sourceUri: string): QinSourceZone | undefined {
  const segments = URI.parse(sourceUri).path.split('/').filter(Boolean)
  for (let index = segments.length - 1; index >= 0; index--) {
    const segment = segments[index]
    if (segment === 'app' || segment === 'main' || segment === 'shared') {
      return segment
    }
  }
  return undefined
}

function isJavaModuleSpecifier(specifier: string): boolean {
  return specifier.startsWith('java:')
}

function resolveLocalModuleTarget(documentUri: string, specifier: string): string {
  const documentPath = URI.parse(documentUri).fsPath
  const targetPath = path.resolve(path.dirname(documentPath), specifier)
  return toFileUriFromPath(targetPath)
}

function toFileUriFromPath(filePath: string): string {
  const normalized = path.resolve(filePath).replace(/\\/g, '/')
  if (/^[A-Za-z]:\//.test(normalized)) {
    return `file:///${normalized}`
  }
  return URI.file(normalized).toString()
}

function provideSourceLinkedEditingRanges(document: TextDocument, position: Position): LinkedEditingRanges | undefined {
  const source = document.getText()
  const parsed = parseGeneratedQinSource(source)
  if (!parsed.ok || !parsed.cst) {
    return
  }
  const objectRanges = collectQinObjectSelectionRanges(source, parsed.cst)
  const identifier = identifierAtOffset(source, document.offsetAt(position))
  if (!identifier || !objectRanges.some(item => source.slice(item.nameStart, item.nameEnd) === identifier.text)) {
    return
  }
  const ranges = collectIdentifierRanges(source, identifier.text)
    .map(item => createRange(document, item.start, item.end))
  return ranges.length > 1
    ? {
      ranges,
      wordPattern: '[A-Za-z_$][A-Za-z0-9_$]*',
    }
    : undefined
}

interface SourceIdentifierRange {
  text: string
  start: number
  end: number
}

function identifierAtOffset(source: string, offset: number): SourceIdentifierRange | undefined {
  for (const item of collectAllIdentifierRanges(source)) {
    if (offset >= item.start && offset <= item.end) {
      return item
    }
  }
  return undefined
}

function collectIdentifierRanges(source: string, name: string): SourceIdentifierRange[] {
  return collectAllIdentifierRanges(source).filter(item => item.text === name)
}

function collectAllIdentifierRanges(source: string): SourceIdentifierRange[] {
  const identifiers: SourceIdentifierRange[] = []
  const scanner = ts.createScanner(ts.ScriptTarget.Latest, false, ts.LanguageVariant.Standard, source)
  let token = scanner.scan()
  while (token !== ts.SyntaxKind.EndOfFileToken) {
    if (token === ts.SyntaxKind.Identifier) {
      identifiers.push({
        text: scanner.getTokenText(),
        start: scanner.getTokenPos(),
        end: scanner.getTextPos(),
      })
    }
    token = scanner.scan()
  }
  return identifiers
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
