import ts from 'typescript'
import type { DocumentSymbol } from 'vscode-languageserver-protocol'
import { SymbolKind } from 'vscode-languageserver-protocol'
import type { TextDocument } from 'vscode-languageserver-textdocument'
import { parseGeneratedQinSource } from './QinGeneratedParserProbe'

interface QinObjectSymbolInfo {
  name: string
  nameStart: number
  nameEnd: number
  rangeStart: number
  rangeEnd: number
}

export function provideSourceDocumentSymbols(document: TextDocument): DocumentSymbol[] {
  const source = document.getText()
  const qinObjects = collectQinObjectSymbols(source)
  if (qinObjects.length) {
    return qinObjects.map(item => createQinObjectSymbol(document, item))
  }
  return provideTypeScriptSourceDocumentSymbols(document)
}

function provideTypeScriptSourceDocumentSymbols(document: TextDocument): DocumentSymbol[] {
  const sourceFile = ts.createSourceFile(
    document.uri,
    document.getText(),
    ts.ScriptTarget.Latest,
    true,
    ts.ScriptKind.TS,
  )
  return sourceFile.statements
    .map(statement => symbolFromStatement(document, statement))
    .filter((symbol): symbol is DocumentSymbol => Boolean(symbol))
}

function symbolFromStatement(document: TextDocument, statement: ts.Statement): DocumentSymbol | undefined {
  if (ts.isInterfaceDeclaration(statement)) {
    return createSymbol(document, statement.name, statement, SymbolKind.Interface, statement.members.map(member => symbolFromTypeElement(document, member)).filter(Boolean) as DocumentSymbol[])
  }
  if (ts.isTypeAliasDeclaration(statement)) {
    const children = ts.isTypeLiteralNode(statement.type)
      ? statement.type.members.map(member => symbolFromTypeElement(document, member)).filter(Boolean) as DocumentSymbol[]
      : []
    return createSymbol(document, statement.name, statement, SymbolKind.TypeParameter, children)
  }
  if (ts.isClassDeclaration(statement) && statement.name) {
    return createSymbol(document, statement.name, statement, SymbolKind.Class, statement.members.map(member => symbolFromClassElement(document, member)).filter(Boolean) as DocumentSymbol[])
  }
  if (ts.isFunctionDeclaration(statement) && statement.name) {
    return createSymbol(document, statement.name, statement, SymbolKind.Function)
  }
  if (ts.isVariableStatement(statement)) {
    const declarations = statement.declarationList.declarations
    if (declarations.length === 1 && ts.isIdentifier(declarations[0].name)) {
      return createSymbol(document, declarations[0].name, statement, SymbolKind.Variable, symbolChildrenFromInitializer(document, declarations[0].initializer))
    }
  }
  return undefined
}

function symbolFromTypeElement(document: TextDocument, member: ts.TypeElement): DocumentSymbol | undefined {
  const name = getDeclarationName(member)
  if (!name) return undefined
  if (ts.isMethodSignature(member) || ts.isCallSignatureDeclaration(member) || ts.isConstructSignatureDeclaration(member)) {
    return createSymbol(document, name, member, SymbolKind.Method)
  }
  return createSymbol(document, name, member, SymbolKind.Property)
}

function symbolFromClassElement(document: TextDocument, member: ts.ClassElement): DocumentSymbol | undefined {
  const name = getDeclarationName(member)
  if (!name) return undefined
  if (ts.isMethodDeclaration(member) || ts.isConstructorDeclaration(member)) {
    return createSymbol(document, name, member, SymbolKind.Method)
  }
  return createSymbol(document, name, member, SymbolKind.Property)
}

function symbolChildrenFromInitializer(document: TextDocument, initializer: ts.Expression | undefined): DocumentSymbol[] {
  if (!initializer || !ts.isObjectLiteralExpression(initializer)) return []
  return initializer.properties
    .map(property => {
      const name = getDeclarationName(property)
      return name ? createSymbol(document, name, property, SymbolKind.Property) : undefined
    })
    .filter((symbol): symbol is DocumentSymbol => Boolean(symbol))
}

function getDeclarationName(node: ts.Node): ts.Node | undefined {
  const named = node as ts.NamedDeclaration
  if (named.name) return named.name
  if (ts.isConstructorDeclaration(node)) return node
  return undefined
}

function createSymbol(document: TextDocument, nameNode: ts.Node, rangeNode: ts.Node, kind: SymbolKind, children: DocumentSymbol[] = []): DocumentSymbol {
  return {
    name: symbolName(nameNode),
    kind,
    range: {
      start: document.positionAt(rangeNode.getStart()),
      end: document.positionAt(rangeNode.getEnd()),
    },
    selectionRange: {
      start: document.positionAt(nameNode.getStart()),
      end: document.positionAt(nameNode.getEnd()),
    },
    children,
  }
}

function symbolName(node: ts.Node): string {
  if (ts.isIdentifier(node) || ts.isStringLiteral(node) || ts.isNumericLiteral(node)) return node.text
  if (ts.isConstructorDeclaration(node)) return 'constructor'
  return node.getText()
}

function collectQinObjectSymbols(source: string): QinObjectSymbolInfo[] {
  const parsed = parseGeneratedQinSource(source)
  if (!parsed.ok || !parsed.cst) {
    return []
  }
  const symbols: QinObjectSymbolInfo[] = []
  walkCstWithParents(parsed.cst, [], (node, parents) => {
    if (readCstName(node) !== 'QinObjectDeclarationBody') {
      return
    }
    const nameToken = findBindingIdentifierName(node)
    const objectToken = findToken(node, item => readCstName(item) === 'IdentifierName' && readCstValue(item) === 'object')
    const closeBrace = findLastToken(node, item => readCstName(item) === 'RBrace')
    if (!nameToken || !objectToken || !closeBrace) {
      return
    }
    const exportDeclaration = findNearestParent(parents, 'ExportDeclaration')
    const exportToken = exportDeclaration
      ? findToken(exportDeclaration, item => readCstName(item) === 'Export' || readCstValue(item) === 'export')
      : undefined
    const rangeStart = readTokenStart(exportToken ?? objectToken)
    const rangeEnd = readTokenEnd(closeBrace)
    const nameStart = readTokenStart(nameToken)
    const nameEnd = readTokenEnd(nameToken)
    if (!isValidRange(rangeStart, rangeEnd, source.length) || !isValidRange(nameStart, nameEnd, source.length)) {
      return
    }
    symbols.push({
      name: source.slice(nameStart, nameEnd),
      nameStart,
      nameEnd,
      rangeStart,
      rangeEnd,
    })
  })
  return symbols
}

function createQinObjectSymbol(document: TextDocument, info: QinObjectSymbolInfo): DocumentSymbol {
  return {
    name: info.name,
    kind: SymbolKind.Class,
    range: {
      start: document.positionAt(info.rangeStart),
      end: document.positionAt(info.rangeEnd),
    },
    selectionRange: {
      start: document.positionAt(info.nameStart),
      end: document.positionAt(info.nameEnd),
    },
    children: [],
  }
}

function findBindingIdentifierName(cst: unknown): unknown | undefined {
  let found: unknown | undefined
  walkCst(cst, node => {
    if (found) {
      return
    }
    const name = readCstName(node)
    if (name === 'BindingIdentifier' || name === 'QinObjectName') {
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

function walkCstWithParents(cst: unknown, parents: unknown[], visit: (node: unknown, parents: unknown[]) => void): void {
  if (!cst || typeof cst !== 'object') {
    return
  }
  visit(cst, parents)
  const nextParents = [...parents, cst]
  for (const child of readCstChildren(cst)) {
    walkCstWithParents(child, nextParents, visit)
  }
}

function findNearestParent(parents: unknown[], name: string): unknown | undefined {
  for (let i = parents.length - 1; i >= 0; i--) {
    if (readCstName(parents[i]) === name) {
      return parents[i]
    }
  }
  return undefined
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
