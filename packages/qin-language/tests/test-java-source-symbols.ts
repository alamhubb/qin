import fs from 'node:fs'
import os from 'node:os'
import path from 'node:path'
import { URI } from 'vscode-uri'
import { buildJavaSourceSymbolDts } from '../qin-language-server/src/QinJavaSourceSymbols'

const projectRoot = fs.mkdtempSync(path.join(os.tmpdir(), 'qin-java-source-symbols-'))
const mainRoot = path.join(projectRoot, 'src', 'main')
const packageRoot = path.join(mainRoot, 'demo')
fs.mkdirSync(packageRoot, { recursive: true })
fs.writeFileSync(path.join(projectRoot, 'qin.config.js'), [
  'export default {',
  '  name: "qin-java-source-symbols-test",',
  '  type: "app",',
  '  entry: "src/main/App.qin"',
  '}',
  '',
].join('\n'))
fs.writeFileSync(path.join(packageRoot, 'Greeter.java'), [
  'package demo;',
  '',
  'public class Greeter {',
  '  public final static String DEFAULT_NAME = "Qin";',
  '',
  '  public synchronized static String greet(String name) {',
  '    return "Hello " + name;',
  '  }',
  '}',
  '',
].join('\n'))
fs.writeFileSync(path.join(packageRoot, 'Counter.java'), [
  'package demo;',
  '',
  'public class Counter {',
  '  public static int COUNT = 1;',
  '',
  '  public static int count() {',
  '    return COUNT;',
  '  }',
  '}',
  '',
].join('\n'))

const qinSourcePath = path.join(mainRoot, 'App.qin')
const qinSource = [
  'import { Greeter, Counter } from "java:demo"',
  'const name = Greeter.DE',
  'const total = Counter.CO',
  '',
].join('\n')
const dts = buildJavaSourceSymbolDts(URI.file(qinSourcePath), qinSource)

const expected = [
  'export class Greeter',
  'static readonly DEFAULT_NAME: string;',
  'static greet(name: string): string;',
  'export class Counter',
  'static COUNT: number;',
  'static count(): number;',
]
for (const text of expected) {
  if (!dts.includes(text)) {
    throw new Error(`Java source symbol d.ts missing ${text}: ${dts}`)
  }
}

const greeterBlock = classBlock(dts, 'Greeter')
if (greeterBlock.includes('COUNT') || greeterBlock.includes('count(): number')) {
  throw new Error(`Java source symbol model leaked Counter members into Greeter: ${dts}`)
}

const counterBlock = classBlock(dts, 'Counter')
if (counterBlock.includes('DEFAULT_NAME') || counterBlock.includes('greet(name: string)')) {
  throw new Error(`Java source symbol model leaked Greeter members into Counter: ${dts}`)
}

function classBlock(source: string, className: string): string {
  const marker = `export class ${className}`
  const start = source.indexOf(marker)
  if (start < 0) {
    throw new Error(`Java source symbol d.ts missing ${marker}: ${source}`)
  }
  const next = source.indexOf('  export class ', start + marker.length)
  return next < 0 ? source.slice(start) : source.slice(start, next)
}

console.log('Qin Java source symbols smoke passed')