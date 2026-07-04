import fs from 'node:fs'
import path from 'node:path'
import ts from 'typescript'
import { URI } from 'vscode-uri'

export interface QinSymbolModel {
  modules: QinModuleSymbol[]
}

export interface QinModuleSymbol {
  specifier: string
  classes: QinClassSymbol[]
}

export interface QinClassSymbol {
  name: string
  fields: QinFieldSymbol[]
  methods: QinMethodSymbol[]
}

export interface QinMethodSymbol {
  name: string
  staticMethod: boolean
  parameters: QinParameterSymbol[]
  returnType: string
}

export interface QinFieldSymbol {
  name: string
  staticField: boolean
  readonlyField: boolean
  type: string
}

export interface QinParameterSymbol {
  name: string
  type: string
}

export function buildJavaSourceSymbolDts(sourceUri: URI, qinSource: string): string {
  const importedJavaModules = collectImportedJavaModules(qinSource)
  if (importedJavaModules.size === 0) {
    return ''
  }
  const projectRoot = findQinProjectRoot(sourceUri.fsPath)
  if (!projectRoot) {
    return ''
  }
  const symbolModel = buildJavaSourceSymbolModel(projectRoot, importedJavaModules)
  return emitSymbolModelDts(symbolModel)
}

function collectImportedJavaModules(source: string): Set<string> {
  const sourceFile = ts.createSourceFile('source.qin', source, ts.ScriptTarget.Latest, true, ts.ScriptKind.TS)
  const modules = new Set<string>()
  const visit = (node: ts.Node): void => {
    if (ts.isImportDeclaration(node) && node.moduleSpecifier && ts.isStringLiteralLike(node.moduleSpecifier)) {
      const specifier = node.moduleSpecifier.text
      if (specifier.startsWith('java:')) {
        modules.add(specifier)
      }
    }
    ts.forEachChild(node, visit)
  }
  ts.forEachChild(sourceFile, visit)
  return modules
}

function findQinProjectRoot(sourceFilePath: string): string | undefined {
  let current = path.dirname(sourceFilePath)
  while (current && current !== path.dirname(current)) {
    if (fs.existsSync(path.join(current, 'qin.config.js'))) {
      return current
    }
    current = path.dirname(current)
  }
  return undefined
}

function buildJavaSourceSymbolModel(projectRoot: string, importedJavaModules: Set<string>): QinSymbolModel {
  const sourceRoot = path.join(projectRoot, 'src', 'main')
  if (!fs.existsSync(sourceRoot)) {
    return { modules: [] }
  }
  const modules = new Map<string, QinModuleSymbol>()
  for (const filePath of listJavaFiles(sourceRoot)) {
    const source = fs.readFileSync(filePath, 'utf8')
    const packageName = readJavaPackageName(source)
    const specifier = `java:${packageName}`
    if (!importedJavaModules.has(specifier)) {
      continue
    }
    const classes = parseJavaClassSymbols(source)
    if (classes.length === 0) {
      continue
    }
    const module = modules.get(specifier) ?? { specifier, classes: [] }
    module.classes.push(...classes)
    modules.set(specifier, module)
  }
  return { modules: [...modules.values()] }
}

function listJavaFiles(root: string): string[] {
  const files: string[] = []
  const visit = (directory: string): void => {
    for (const entry of fs.readdirSync(directory, { withFileTypes: true })) {
      const entryPath = path.join(directory, entry.name)
      if (entry.isDirectory()) {
        visit(entryPath)
      } else if (entry.isFile() && entry.name.endsWith('.java')) {
        files.push(entryPath)
      }
    }
  }
  visit(root)
  return files
}

function readJavaPackageName(source: string): string {
  const match = source.match(/^\s*package\s+([A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*)\s*;/m)
  return match?.[1] ?? ''
}

function parseJavaClassSymbols(source: string): QinClassSymbol[] {
  const classes: QinClassSymbol[] = []
  const classPattern = /\bpublic\s+(?:(?:final|abstract|sealed|non-sealed)\s+)*(?:class|record)\s+([A-Za-z_$][\w$]*)[^{};]*\{/g
  for (const match of source.matchAll(classPattern)) {
    const openBrace = source.indexOf('{', match.index)
    const closeBrace = findMatchingBrace(source, openBrace)
    const body = closeBrace >= 0 ? source.slice(openBrace + 1, closeBrace) : source.slice(openBrace + 1)
    classes.push({
      name: match[1],
      fields: parseJavaFieldSymbols(body),
      methods: parseJavaMethodSymbols(body),
    })
  }
  return classes
}

function parseJavaMethodSymbols(source: string): QinMethodSymbol[] {
  const methods: QinMethodSymbol[] = []
  const methodPattern = /\bpublic\s+((?:(?:static|final|synchronized|native|strictfp)\s+)*)((?:[A-Za-z_$][\w$]*(?:\s*<[^;{}()]+>)?(?:\s*\[\])?|\w+(?:\.\w+)*(?:\s*<[^;{}()]+>)?(?:\s*\[\])?))\s+([A-Za-z_$][\w$]*)\s*\(([^)]*)\)\s*(?:throws\s+[^{;]+)?[{;]/g
  for (const match of source.matchAll(methodPattern)) {
    const modifiers = match[1].trim().split(/\s+/).filter(Boolean)
    methods.push({
      name: match[3],
      staticMethod: modifiers.includes('static'),
      parameters: parseJavaParameters(match[4]),
      returnType: mapJavaTypeToTypeScript(match[2]),
    })
  }
  return methods
}

function parseJavaFieldSymbols(source: string): QinFieldSymbol[] {
  const fields: QinFieldSymbol[] = []
  const fieldPattern = /\bpublic\s+((?:(?:static|final|transient|volatile)\s+)*)((?:[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)?)(?:\s*<[^;{}()]+>)?(?:\s*\[\])?)\s+([A-Za-z_$][\w$]*)\s*(?:=[^;]*)?;/g
  for (const match of source.matchAll(fieldPattern)) {
    const modifiers = match[1].trim().split(/\s+/).filter(Boolean)
    fields.push({
      name: match[3],
      staticField: modifiers.includes('static'),
      readonlyField: modifiers.includes('final'),
      type: mapJavaTypeToTypeScript(match[2]),
    })
  }
  return fields
}

function findMatchingBrace(source: string, openBrace: number): number {
  if (openBrace < 0) {
    return -1
  }
  let depth = 0
  let quote: string | undefined
  let escaping = false
  for (let index = openBrace; index < source.length; index++) {
    const char = source[index]
    const next = source[index + 1]
    if (quote) {
      if (escaping) {
        escaping = false
      } else if (char === '\\') {
        escaping = true
      } else if (char === quote) {
        quote = undefined
      }
      continue
    }
    if (char === '/' && next === '/') {
      while (index < source.length && source[index] !== '\n' && source[index] !== '\r') {
        index++
      }
      continue
    }
    if (char === '/' && next === '*') {
      index += 2
      while (index + 1 < source.length && !(source[index] === '*' && source[index + 1] === '/')) {
        index++
      }
      index++
      continue
    }
    if (char === '"' || char === "'" || char === '`') {
      quote = char
      continue
    }
    if (char === '{') {
      depth++
    } else if (char === '}') {
      depth--
      if (depth === 0) {
        return index
      }
    }
  }
  return -1
}

function parseJavaParameters(source: string): QinParameterSymbol[] {
  const text = source.trim()
  if (!text) {
    return []
  }
  return splitTopLevelCommas(text).map((parameter, index) => {
    const cleaned = parameter
      .replace(/@\w+(?:\([^)]*\))?\s*/g, '')
      .replace(/\bfinal\s+/g, '')
      .trim()
    const parts = cleaned.split(/\s+/)
    if (parts.length < 2) {
      return { name: `arg${index}`, type: 'unknown' }
    }
    const name = parts[parts.length - 1].replace(/^\.\.\./, '')
    const type = cleaned.slice(0, cleaned.length - parts[parts.length - 1].length).trim().replace(/\.\.\.$/, '[]')
    return {
      name: isIdentifier(name) ? name : `arg${index}`,
      type: mapJavaTypeToTypeScript(type),
    }
  })
}

function splitTopLevelCommas(source: string): string[] {
  const parts: string[] = []
  let depth = 0
  let start = 0
  for (let i = 0; i < source.length; i++) {
    const char = source[i]
    if (char === '<') depth++
    if (char === '>') depth = Math.max(0, depth - 1)
    if (char === ',' && depth === 0) {
      parts.push(source.slice(start, i).trim())
      start = i + 1
    }
  }
  parts.push(source.slice(start).trim())
  return parts.filter(Boolean)
}

function mapJavaTypeToTypeScript(typeName: string): string {
  const normalized = typeName.replace(/\s+/g, '')
  if (!normalized) {
    return 'unknown'
  }
  if (normalized.endsWith('[]')) {
    return `${mapJavaTypeToTypeScript(normalized.slice(0, -2))}[]`
  }
  if (normalized === 'void') return 'void'
  if (normalized === 'boolean' || normalized === 'Boolean' || normalized === 'java.lang.Boolean') return 'boolean'
  if (['byte', 'short', 'int', 'long', 'float', 'double', 'char',
    'Byte', 'Short', 'Integer', 'Long', 'Float', 'Double', 'Character',
    'java.lang.Byte', 'java.lang.Short', 'java.lang.Integer', 'java.lang.Long',
    'java.lang.Float', 'java.lang.Double', 'java.lang.Character'].includes(normalized)) {
    return 'number'
  }
  if (normalized === 'String' || normalized === 'java.lang.String') {
    return 'string'
  }
  const listType = readGenericTypeArgument(normalized, ['List', 'java.util.List', 'ArrayList', 'java.util.ArrayList'])
  if (listType) {
    return `${mapJavaTypeToTypeScript(listType)}[]`
  }
  return 'unknown'
}

function readGenericTypeArgument(typeName: string, owners: string[]): string | undefined {
  for (const owner of owners) {
    const prefix = `${owner}<`
    if (typeName.startsWith(prefix) && typeName.endsWith('>')) {
      return typeName.slice(prefix.length, -1)
    }
  }
  return undefined
}

function emitSymbolModelDts(model: QinSymbolModel): string {
  if (model.modules.length === 0) {
    return ''
  }
  const lines: string[] = []
  for (const module of model.modules) {
    lines.push(`declare module ${JSON.stringify(module.specifier)} {`)
    for (const classSymbol of module.classes) {
      if (!isIdentifier(classSymbol.name)) {
        continue
      }
      lines.push(`  export class ${classSymbol.name} {`)
      for (const field of classSymbol.fields) {
        if (!isIdentifier(field.name)) {
          continue
        }
        lines.push(`    ${field.staticField ? 'static ' : ''}${field.readonlyField ? 'readonly ' : ''}${field.name}: ${field.type};`)
      }
      for (const method of classSymbol.methods) {
        if (!isIdentifier(method.name)) {
          continue
        }
        const parameters = method.parameters
          .map(parameter => `${isIdentifier(parameter.name) ? parameter.name : 'arg'}: ${parameter.type}`)
          .join(', ')
        lines.push(`    ${method.staticMethod ? 'static ' : ''}${method.name}(${parameters}): ${method.returnType};`)
      }
      lines.push('  }')
    }
    lines.push('}')
  }
  return `${lines.join('\n')}\n`
}

function isIdentifier(value: string): boolean {
  return /^[A-Za-z_$][\w$]*$/.test(value)
}
