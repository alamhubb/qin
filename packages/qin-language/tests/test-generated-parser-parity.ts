import { spawnSync } from 'node:child_process'
import fs from 'node:fs'
import { fileURLToPath } from 'node:url'
import os from 'node:os'
import path from 'node:path'
import { probeGeneratedQinParser } from '../qin-language-server/src/QinGeneratedParserProbe'

interface ParserCase {
  name: string
  source: string
  ok: boolean
}

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)
const languageRoot = path.resolve(__dirname, '..')
const workspaceRoot = path.resolve(languageRoot, '..', '..', '..')
const qinParserRoot = path.join(workspaceRoot, 'qin', 'packages', 'qin-parser')
const qinCommand = path.join(workspaceRoot, 'qin', process.platform === 'win32' ? 'qin.bat' : 'qin')

const cases: ParserCase[] = [
  {
    name: 'plain export',
    source: 'export const answer = 41\n',
    ok: true,
  },
  {
    name: 'qin object',
    source: [
      'export object Counter {',
      '  value = 1',
      '  next() { return this.value + 1 }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'qin object method body control flow',
    source: [
      'export object Labeler {',
      '  label(name: string, flag: boolean): string {',
      '    const prefix = "hello "',
      '    if (flag) {',
      '      return prefix + name',
      '    }',
      '    return "bye " + name',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'qin object nested method body control flow',
    source: [
      'export object NestedLabeler {',
      '  label(name: string, premium: boolean, active: boolean): string {',
      '    const base = "hello "',
      '    if (active) {',
      '      if (premium) {',
      '        const label = "vip "',
      '        return label + name',
      '      }',
      '      const standard = "std "',
      '      return standard + name',
      '    }',
      '    return base + name',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'qin object method body exception flow',
    source: [
      'export object ResilientLabeler {',
      '  label(flag: boolean): string {',
      '    try {',
      '      if (flag) {',
      '        throw new Error("boom")',
      '      }',
      '      return "ok"',
      '    } catch (error) {',
      '      return "caught"',
      '    }',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'decorated qin object',
    source: [
      "import { GetMapping } from './http.qin'",
      '@RestController',
      'export object UserController {',
      "  @GetMapping('/users')",
      '  list() { return [] }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'default export qin object',
    source: [
      'export default object App {',
      '  value = 1',
      '  next() { return this.value + 1 }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'qin object extends local class',
    source: [
      'class BaseCounter {',
      '  baseValue = 1',
      '}',
      '',
      'export object Counter extends BaseCounter {',
      '  value = this.baseValue',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'object keyword in type alias',
    source: [
      'type Box = object;',
      'export const ok = true',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'java import class extends',
    source: [
      'import { ArrayList } from "java:java.util"',
      '',
      'class MyList extends ArrayList {',
      '  label(): string {',
      '    return "ok"',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'local class extends',
    source: [
      'class BaseService {',
      '  label(): string {',
      '    return "base"',
      '  }',
      '}',
      '',
      'class ChildService extends BaseService {',
      '  childLabel(): string {',
      '    return "child"',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'interface and type exports',
    source: [
      'export interface User {',
      '  id: string',
      '  active?: boolean',
      '}',
      'export type UserList = User[]',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'generic function',
    source: [
      'export function identity<T>(value: T): T {',
      '  return value',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'generic interface and object type alias',
    source: [
      'export interface Box<T> {',
      '  value: T',
      '}',
      'export type Pair<T, U> = { left: T, right: U }',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'namespace export',
    source: [
      'export namespace Api {',
      '  export const version = "1"',
      '  export function label(name: string) { return name + version }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'enum export',
    source: [
      'export enum Status {',
      '  Ready = "ready",',
      '  Done = "done"',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'import export variants',
    source: [
      'import defaultThing, { named as alias, other } from "./dep.qin"',
      'import * as everything from "./all.qin"',
      'export { alias, other as renamed }',
      'export * from "./more.qin"',
      'export { default as DefaultThing } from "./dep.qin"',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'type import export variants',
    source: [
      'import type { User } from "./types.qin"',
      'export type { User }',
      'export interface Result<T> { value: T }',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'class method expressions',
    source: [
      'class HelloService {',
      '  greet(name: string): string {',
      '    return "hello " + name',
      '  }',
      '',
      '  choose(flag: boolean): string {',
      '    return flag ? "yes" : "no"',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'class fields and constructor',
    source: [
      'class UserService {',
      '  name: string = "qin"',
      '  count = 0',
      '',
      '  constructor(name: string) {',
      '    this.name = name',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'decorated class and method',
    source: [
      '@Controller',
      'export class UserController {',
      '  @Get("/users")',
      '  list(): string[] {',
      '    return []',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'control flow in function body',
    source: [
      'export function summarize(items: number[]) {',
      '  let total = 0',
      '  for (const item of items) {',
      '    if (item > 0) {',
      '      total = total + item',
      '    }',
      '  }',
      '  return total',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'while loop break continue control flow',
    source: [
      'export function findPositive(values: number[]): number {',
      '  let index = 0',
      '  let result = 0',
      '  while (index < values.length) {',
      '    const value = values[index]',
      '    index = index + 1',
      '    if (value < 0) {',
      '      continue',
      '    }',
      '    result = value',
      '    break',
      '  }',
      '  return result',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'switch statement control flow',
    source: [
      'export function statusLabel(status: string): string {',
      '  switch (status) {',
      '    case "ready":',
      '      return "Ready"',
      '    case "done":',
      '      return "Done"',
      '    default:',
      '      return "Unknown"',
      '  }',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'destructuring declarations',
    source: [
      'const config = { name: "qin", values: [1, 2, 3] }',
      'const { name, values: [first] } = config',
      'export const label = name + first',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'async await function',
    source: [
      'export async function load(fetcher) {',
      '  const result = await fetcher()',
      '  return result.ok ? result.value : null',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'optional chaining expression',
    source: [
      'export function readName(user) {',
      '  return user?.profile?.name',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'nullish coalescing expression',
    source: [
      'export function label(user) {',
      '  return user.name ?? "anonymous"',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'template literal expression',
    source: [
      'export function greet(name: string) {',
      '  return `hello ${name}`',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'import meta url expression',
    source: [
      'export const moduleUrl = import.meta.url',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'dynamic import expression',
    source: [
      'export async function loadModule() {',
      '  const loaded = await import("./dep.qin")',
      '  return loaded',
      '}',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'top level function and const exports',
    source: [
      'export function health() {',
      '  return true',
      '}',
      'export const enabled = true',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'top level object and array literals',
    source: [
      'export const config = { name: "qin", values: [1, 2, 3] }',
      'export const enabled = true',
      '',
    ].join('\n'),
    ok: true,
  },
  {
    name: 'invalid qin object initializer',
    source: 'export object Broken { value = }\n',
    ok: false,
  },
  {
    name: 'invalid unclosed import',
    source: 'import { User from "./types.qin"\n',
    ok: false,
  },
  {
    name: 'invalid unclosed decorator',
    source: [
      '@Controller(',
      'export object Broken {',
      '  value = 1',
      '}',
      '',
    ].join('\n'),
    ok: false,
  },
  {
    name: 'invalid unclosed class',
    source: [
      'export class Broken {',
      '  value = 1',
      '',
    ].join('\n'),
    ok: false,
  },
]

const javaResults = runJavaParserBatchProbe(cases)

for (const testCase of cases) {
  const javaOk = javaResults.get(testCase.name)
  if (javaOk === undefined) {
    throw new Error(`Java parser batch probe did not report ${testCase.name}`)
  }
  const tsProbe = probeGeneratedQinParser(testCase.source)
  if (!tsProbe.available) {
    throw new Error('Generated Qin parser package is required for parser parity smoke')
  }
  const tsOk = tsProbe.ok

  if (javaOk !== tsOk || javaOk !== testCase.ok) {
    throw new Error([
      `Qin parser parity failed for ${testCase.name}`,
      `expected=${testCase.ok}`,
      `java=${javaOk}`,
      `typescript=${tsOk}`,
      `typescriptProbe=${JSON.stringify(tsProbe)}`,
    ].join('\n'))
  }
}

console.log('Qin generated parser parity smoke passed')

function runJavaParserBatchProbe(parserCases: ParserCase[]): Map<string, boolean> {
  const tempDir = fs.mkdtempSync(path.join(os.tmpdir(), 'qin-parser-parity-'))
  try {
    const sourceFiles = parserCases.map((testCase, index) => {
      const sourceFile = path.join(tempDir, `${String(index).padStart(2, '0')}-${sanitizeFileName(testCase.name)}.qin`)
      fs.writeFileSync(sourceFile, testCase.source, 'utf8')
      return sourceFile
    })
    const qinArgs = [
      'run',
      'com.qin.parser.QinParserBatchProbeMain',
      'Program',
      ...sourceFiles,
    ]
    const command = process.platform === 'win32' ? 'cmd.exe' : qinCommand
    const args = process.platform === 'win32' ? ['/c', qinCommand, ...qinArgs] : qinArgs
    const result = spawnSync(command, args, {
      cwd: qinParserRoot,
      encoding: 'utf8',
      stdio: ['ignore', 'pipe', 'pipe'],
    })
    const output = `${result.stdout ?? ''}\n${result.stderr ?? ''}\n${result.error?.stack ?? result.error?.message ?? ''}`
    if (result.status !== 0) {
      throw new Error(`Java parser batch probe process failed:\n${output}`)
    }

    const results = new Map<string, boolean>()
    for (const line of output.split(/\r?\n/)) {
      if (!line.startsWith('case=')) {
        continue
      }
      const fields = parseProbeFields(line)
      const caseIndex = Number(fields.get('case'))
      const success = fields.get('success')
      if (!Number.isInteger(caseIndex) || caseIndex < 0 || caseIndex >= parserCases.length) {
        throw new Error(`Java parser batch probe reported invalid case index:\n${line}`)
      }
      if (success !== 'true' && success !== 'false') {
        throw new Error(`Java parser batch probe reported invalid success field:\n${line}`)
      }
      results.set(parserCases[caseIndex].name, success === 'true')
    }

    if (results.size !== parserCases.length) {
      throw new Error(`Java parser batch probe reported ${results.size} of ${parserCases.length} cases:\n${output}`)
    }
    return results
  } finally {
    fs.rmSync(tempDir, { recursive: true, force: true })
  }
}

function parseProbeFields(line: string): Map<string, string> {
  const fields = new Map<string, string>()
  for (const part of line.split('\t')) {
    const separator = part.indexOf('=')
    if (separator > 0) {
      fields.set(part.slice(0, separator), part.slice(separator + 1))
    }
  }
  return fields
}

function sanitizeFileName(value: string): string {
  return value.replace(/[^a-z0-9_-]+/gi, '-').replace(/^-|-$/g, '') || 'case'
}
