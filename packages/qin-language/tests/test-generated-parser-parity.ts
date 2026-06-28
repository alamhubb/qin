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
    name: 'invalid qin object initializer',
    source: 'export object Broken { value = }\n',
    ok: false,
  },
]

for (const testCase of cases) {
  const javaOk = runJavaParserProbe(testCase)
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

function runJavaParserProbe(testCase: ParserCase): boolean {
  const tempDir = fs.mkdtempSync(path.join(os.tmpdir(), 'qin-parser-parity-'))
  const sourceFile = path.join(tempDir, `${sanitizeFileName(testCase.name)}.qin`)
  try {
    fs.writeFileSync(sourceFile, testCase.source, 'utf8')
    const qinArgs = [
      'run',
      'com.qin.parser.QinParserRuleProbeMain',
      'Program',
      '--file',
      sourceFile,
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
      throw new Error(`Java parser probe process failed for ${testCase.name}:\n${output}`)
    }
    if (output.includes('success=true')) {
      return true
    }
    if (output.includes('success=false')) {
      return false
    }
    throw new Error(`Java parser probe did not report success state for ${testCase.name}:\n${output}`)
  } finally {
    fs.rmSync(tempDir, { recursive: true, force: true })
  }
}

function sanitizeFileName(value: string): string {
  return value.replace(/[^a-z0-9_-]+/gi, '-').replace(/^-|-$/g, '') || 'case'
}
