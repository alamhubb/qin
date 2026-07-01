import {
  forEachEmbeddedCode,
  type CodeMapping,
  type LanguagePlugin,
  type VirtualCode,
} from '@volar/language-core'
import type { TypeScriptExtraServiceScript } from '@volar/typescript'
import type { IScriptSnapshot } from 'typescript'
import { URI } from 'vscode-uri'
import type { LanguageServerMetadata } from './LanguageServerMetadata'
import { extensionWithoutDot } from './LanguageServerMetadata'
import { parseGeneratedQinSource, probeGeneratedQinParser } from './QinGeneratedParserProbe'
import { logToFile } from './logutil'

const ScriptKind = {
  Deferred: 0,
  JS: 1,
  TS: 3,
} as const

const QIN_LANGUAGE_ID = 'qin'

interface QinLoweringResult {
  code: string
  mappings: CodeMapping[]
}

interface QinTextMapping {
  sourceOffset: number
  generatedOffset: number
  length: number
  generatedLength?: number
  semantic?: boolean
  structure?: boolean
}

interface QinObjectDeclarationInfo {
  body: unknown
  wrapper: unknown
  exported: boolean
  defaultExport: boolean
}

export function QinLanguagePlugin(metadata: LanguageServerMetadata): LanguagePlugin<URI> {
  const sourceExtension = extensionWithoutDot(metadata.sourceExtension)
  return {
    getLanguageId(uri) {
      if (uri.path.endsWith(`.${sourceExtension}`)) {
        return QIN_LANGUAGE_ID
      }
      return undefined
    },

    createVirtualCode(_uri, languageId, snapshot) {
      if (languageId === QIN_LANGUAGE_ID) {
        return new QinVirtualCode(snapshot)
      }
      return undefined
    },

    typescript: {
      extraFileExtensions: [
        {
          extension: sourceExtension,
          isMixedContent: true,
          scriptKind: ScriptKind.Deferred,
        },
      ],
      getServiceScript(root) {
        const code = root.embeddedCodes.find(item => item.id === 'qin-script' && item.languageId === 'typescript')
        if (!code) {
          return undefined
        }
        return {
          code,
          extension: metadata.serviceExtension,
          scriptKind: ScriptKind.TS,
        }
      },
      getExtraServiceScripts(fileName, root) {
        const scripts: TypeScriptExtraServiceScript[] = []
        for (const code of forEachEmbeddedCode(root)) {
          if (code.id === 'qin-script') {
            continue
          }
          if (code.languageId === 'typescript') {
            scripts.push({
              fileName: fileName + '.' + code.id + metadata.serviceExtension,
              code,
              extension: metadata.serviceExtension,
              scriptKind: ScriptKind.TS,
            })
          } else if (code.languageId === 'js') {
            scripts.push({
              fileName: fileName + '.' + code.id + '.js',
              code,
              extension: '.js',
              scriptKind: ScriptKind.JS,
            })
          }
        }
        return scripts
      },
    },
  }
}

export class QinVirtualCode implements VirtualCode {
  id = 'root'
  languageId = QIN_LANGUAGE_ID
  mappings: CodeMapping[]
  embeddedCodes: VirtualCode[] = []

  constructor(public snapshot: IScriptSnapshot) {
    const sourceCode = snapshot.getText(0, snapshot.getLength())
    const parserProbe = probeGeneratedQinParser(sourceCode)
    if (parserProbe.available && !parserProbe.ok) {
      logToFile('Generated Qin parser did not accept source:', JSON.stringify(parserProbe))
    }
    let lowering: QinLoweringResult = createTransformErrorResult(sourceCode, 'Qin transform did not run')

    try {
      lowering = lowerQinToTypeScriptWithMappings(sourceCode)
    } catch (e) {
      logToFile('Qin transform failed:', e instanceof Error ? e.stack || e.message : String(e))
      lowering = createTransformErrorResult(sourceCode, formatTransformErrorMessage(e))
    }

    this.mappings = lowering.mappings

    this.embeddedCodes = [{
      id: 'qin-script',
      languageId: 'typescript',
      snapshot: {
        getText: (start, end) => lowering.code.substring(start, end),
        getLength: () => lowering.code.length,
        getChangeRange: () => undefined,
      },
      mappings: this.mappings,
      embeddedCodes: [],
    }]
  }
}

export function lowerQinToTypeScript(source: string): string {
  return lowerQinToTypeScriptWithMappings(source).code
}

export function lowerQinToTypeScriptWithMappings(source: string): QinLoweringResult {
  const sourceCode = stripBom(source ?? '')
  const parsed = parseGeneratedQinSource(sourceCode)
  if (!parsed.available) {
    return createTransformErrorResult(sourceCode, 'Generated Qin parser package is not available')
  }
  if (!parsed.ok || !parsed.cst) {
    return createTransformErrorResult(sourceCode, parsed.error ?? 'Generated Qin parser rejected source')
  }
  return lowerProgramCstToTypeScript(sourceCode, parsed.cst)
}

function stripBom(source: string): string {
  return source.charCodeAt(0) === 0xFEFF ? source.substring(1) : source
}

function lowerProgramCstToTypeScript(source: string, cst: unknown): QinLoweringResult {
  const objectDeclarations = findQinObjectDeclarations(cst)
  if (objectDeclarations.length === 0) {
    return {
      code: source,
      mappings: createCodeMappings([{
        sourceOffset: 0,
        generatedOffset: 0,
        length: source.length,
        generatedLength: source.length,
      }]),
    }
  }

  let cursor = 0
  let generatedCursor = 0
  let generated = ''
  const mappings: QinTextMapping[] = []
  const appendGenerated = (text: string): number => {
    const start = generatedCursor
    generated += text
    generatedCursor = generated.length
    return start
  }
  const appendMappedSource = (sourceStart: number, sourceEnd: number): void => {
    if (sourceEnd <= sourceStart) {
      return
    }
    const generatedStart = appendGenerated(source.slice(sourceStart, sourceEnd))
    mappings.push({
      sourceOffset: sourceStart,
      generatedOffset: generatedStart,
      length: sourceEnd - sourceStart,
      generatedLength: sourceEnd - sourceStart,
    })
  }
  const appendMappedGenerated = (
    text: string,
    sourceStart: number,
    sourceEnd: number,
    generatedOffsetInText = 0,
    generatedLength = sourceEnd - sourceStart,
  ): void => {
    const generatedStart = appendGenerated(text)
    if (sourceEnd > sourceStart) {
      mappings.push({
        sourceOffset: sourceStart,
        generatedOffset: generatedStart + generatedOffsetInText,
        length: sourceEnd - sourceStart,
        generatedLength,
      })
    }
  }
  for (const declaration of objectDeclarations) {
    const node = declaration.body
    const objectKeyword = findToken(node, item => readCstName(item) === 'IdentifierName' && readCstValue(item) === 'object')
    const bindingName = findBindingIdentifierName(node)
    const openBrace = findToken(node, item => readCstName(item) === 'LBrace')
    const closeBrace = findLastToken(node, item => readCstName(item) === 'RBrace')
    if (!objectKeyword || !bindingName || !openBrace || !closeBrace) {
      return createTransformErrorResult(source, 'Generated Qin CST is missing object declaration tokens')
    }

    const objectStart = readTokenStart(objectKeyword)
    const objectEnd = readTokenEnd(objectKeyword)
    const nameStart = readTokenStart(bindingName)
    const nameEnd = readTokenEnd(bindingName)
    const bodyStart = readTokenStart(openBrace)
    const bodyEnd = readTokenEnd(closeBrace)
    if (!isValidRange(objectStart, objectEnd, source.length)
      || !isValidRange(nameStart, nameEnd, source.length)
      || !isValidRange(bodyStart, bodyEnd, source.length)
      || objectStart < cursor) {
      return createTransformErrorResult(source, 'Generated Qin CST has invalid object declaration ranges')
    }

    const objectName = source.slice(nameStart, nameEnd)
    const internalName = `__QinObject_${objectName}`
    const decoratorsEnd = declaration.exported
      ? findExportTokenStart(declaration.wrapper, objectStart)
      : objectStart
    appendMappedSource(cursor, decoratorsEnd)
    const classStart = generatedCursor
    appendMappedGenerated(`class ${internalName}`, nameStart, nameEnd, 'class '.length, internalName.length)
    appendMappedSource(nameEnd, bodyStart)
    appendMappedSource(bodyStart, bodyEnd)
    appendGenerated('\n')
    mappings.push({
      sourceOffset: nameStart,
      generatedOffset: classStart,
      length: nameEnd - nameStart,
      generatedLength: generatedCursor - classStart,
      semantic: false,
      structure: true,
    })
    appendMappedGenerated(`const ${objectName} = new ${internalName}()\n`, nameStart, nameEnd, 'const '.length)
    if (declaration.defaultExport) {
      appendMappedGenerated(`export default ${objectName}\n`, nameStart, nameEnd, 'export default '.length)
    } else if (declaration.exported) {
      appendMappedGenerated(`export { ${objectName} }\n`, nameStart, nameEnd, 'export { '.length)
    }
    cursor = bodyEnd
  }
  appendMappedSource(cursor, source.length)
  return {
    code: generated,
    mappings: createCodeMappings(mappings),
  }
}

function findQinObjectDeclarations(cst: unknown): QinObjectDeclarationInfo[] {
  const found: QinObjectDeclarationInfo[] = []
  walkCstWithParents(cst, [], (node, parents) => {
    if (readCstName(node) === 'QinObjectDeclarationBody') {
      const exportDeclaration = findNearestParent(parents, 'ExportDeclaration')
      found.push({
        body: node,
        wrapper: exportDeclaration ?? node,
        exported: !!exportDeclaration,
        defaultExport: exportDeclaration ? hasDirectToken(exportDeclaration, 'Default', 'default') : false,
      })
    }
  })
  return found
}

function findBindingIdentifierName(cst: unknown): unknown | undefined {
  let found: unknown | undefined
  walkCst(cst, node => {
    if (found) {
      return
    }
    const name = readCstName(node)
    if (name === 'BindingIdentifier') {
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

function hasDirectToken(cst: unknown, tokenName: string, value: string): boolean {
  return readCstChildren(cst).some(child => readCstName(child) === tokenName || readCstValue(child) === value)
}

function findExportTokenStart(cst: unknown, defaultStart: number): number {
  const token = findToken(cst, item => readCstName(item) === 'Export' || readCstValue(item) === 'export')
  const start = token ? readTokenStart(token) : -1
  return start >= 0 ? start : defaultStart
}

function formatTransformErrorMessage(error: unknown): string {
  return error instanceof Error ? error.message : String(error)
}

function createTransformErrorCode(message: string): string {
  return `throw new Error(${JSON.stringify(`Qin transform failed: ${message}`)});\n`
}

function createTransformErrorResult(source: string, message: string): QinLoweringResult {
  const code = createTransformErrorCode(message)
  return {
    code,
    mappings: createCodeMappings([{
      sourceOffset: 0,
      generatedOffset: 0,
      length: source.length,
      generatedLength: code.length,
    }]),
  }
}

function createCodeMappings(mappings: QinTextMapping[]): CodeMapping[] {
  return mappings.map(item => ({
    sourceOffsets: [item.sourceOffset],
    generatedOffsets: [item.generatedOffset],
    lengths: [item.length],
    generatedLengths: [item.generatedLength ?? item.length],
    data: {
      completion: true,
      format: true,
      navigation: true,
      semantic: item.semantic ?? true,
      structure: item.structure ?? true,
      verification: true,
    },
  }))
}
