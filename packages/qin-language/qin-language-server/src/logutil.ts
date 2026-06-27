import fs from 'node:fs'
import os from 'node:os'
import path from 'node:path'

const logPath = path.join(os.tmpdir(), 'qin-language-server.log')

export function logToFile(...args: unknown[]) {
  const line = args.map((item) => {
    if (typeof item === 'string') return item
    try {
      return JSON.stringify(item)
    } catch {
      return String(item)
    }
  }).join(' ')
  fs.appendFileSync(logPath, `[${new Date().toISOString()}] ${line}\n`, 'utf8')
}
