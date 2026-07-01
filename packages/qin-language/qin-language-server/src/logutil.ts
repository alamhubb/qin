import fs from 'node:fs'
import os from 'node:os'
import path from 'node:path'
import Glog from 'glogjs'

const logPath = path.join(os.tmpdir(), 'qin-language-server.log')
let glogInitialized = false

function initGlog() {
  if (glogInitialized) {
    return
  }
  glogInitialized = true
  Glog.init({
    logRoot: process.env.QIN_LSP_GLOG_ROOT || process.cwd(),
    filePath: import.meta.url,
    level: 'debug',
    prefix: 'qin-lsp',
    console: false,
    writeFile: true,
    timestamp: true,
  })
}

export function logToFile(...args: unknown[]) {
  const line = args.map((item) => {
    if (typeof item === 'string') return item
    try {
      return JSON.stringify(item)
    } catch {
      return String(item)
    }
  }).join(' ')
  try {
    initGlog()
    Glog.info(line)
  } catch {
    // Keep the language server alive even when logger setup is broken.
  }
  fs.appendFileSync(logPath, `[${new Date().toISOString()}] ${line}\n`, 'utf8')
}
