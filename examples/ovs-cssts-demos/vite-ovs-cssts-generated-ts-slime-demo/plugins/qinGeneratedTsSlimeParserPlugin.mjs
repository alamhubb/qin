import { pathToFileURL } from 'node:url'
import fs from 'node:fs'
import path from 'node:path'

const VIRTUAL_STATUS_ID = 'virtual:qin-generated-slime-status'
const RESOLVED_VIRTUAL_STATUS_ID = '\0' + VIRTUAL_STATUS_ID

export default function qinGeneratedTsSlimeParserPlugin(options = {}) {
  let root = process.cwd()
  let tsRoot = ''
  let tsEntryPath = ''
  let runtimePath = ''
  let runtimePromise = null
  let runtimeLoadSeq = 0
  const parsed = new Map()

  async function ensureRuntimeBundle() {
    const stats = collectGeneratedTsStats(tsRoot)
    const runtimeExists = fs.existsSync(runtimePath)
    const runtimeMtime = runtimeExists ? fs.statSync(runtimePath).mtimeMs : 0
    if (runtimeExists && runtimeMtime >= stats.latestMtimeMs) {
      return stats
    }

    fs.mkdirSync(path.dirname(runtimePath), { recursive: true })
    const esbuild = await import('esbuild')
    await esbuild.build({
      entryPoints: [tsEntryPath],
      bundle: true,
      platform: 'node',
      format: 'esm',
      target: 'es2022',
      outfile: runtimePath,
      absWorkingDir: tsRoot,
      logLevel: 'silent'
    })
    return stats
  }

  async function loadRuntime() {
    if (runtimePromise) return runtimePromise
    runtimePromise = (async () => {
      const stats = await ensureRuntimeBundle()
      const runtimeStats = fs.statSync(runtimePath)
      const importUrl = pathToFileURL(runtimePath).href + `?mtime=${runtimeStats.mtimeMs}&load=${++runtimeLoadSeq}`
      const module = await import(importUrl)
      const SlimeParser = module.SlimeParser || module.com_slime_parser_SlimeParser
      if (typeof SlimeParser !== 'function') {
        throw new Error(`Generated TS SlimeParser export is missing from ${tsEntryPath}`)
      }
      return {
        SlimeParser,
        mode: 'ts-esm-single-file-source',
        entryPath: tsEntryPath,
        rootPath: tsRoot,
        runtimePath,
        size: fs.statSync(tsEntryPath).size,
        runtimeSize: runtimeStats.size,
        fileCount: stats.fileCount
      }
    })().catch((error) => {
      runtimePromise = null
      throw error
    })
    return runtimePromise
  }

  function collectGeneratedTsStats(dir) {
    let fileCount = 0
    let latestMtimeMs = 0
    const stack = [dir]
    while (stack.length > 0) {
      const current = stack.pop()
      for (const entry of fs.readdirSync(current, { withFileTypes: true })) {
        const full = path.join(current, entry.name)
        if (entry.isDirectory()) {
          if (entry.name !== 'node_modules') stack.push(full)
          continue
        }
        if (!entry.isFile() || !entry.name.endsWith('.ts')) continue
        fileCount += 1
        latestMtimeMs = Math.max(latestMtimeMs, fs.statSync(full).mtimeMs)
      }
    }
    return { fileCount, latestMtimeMs }
  }

  async function parseWithGeneratedSlime(code, id) {
    const { SlimeParser } = await loadRuntime()
    const codeForParser = String(code)
      .split(/\r?\n/)
      .map((line) => (/^\s*import\b/.test(line) && !/[;]\s*$/.test(line) ? `${line};` : line))
      .join('\n')
    try {
      const parser = new SlimeParser(codeForParser)
      parser.ModuleBody()
      const unparsedTokens = parser.getUnparsedTokens()
      const ok = !parser.isParserFail() && unparsedTokens.size() === 0
      if (ok) {
        parsed.set(path.relative(root, id), { ok: true, parsedTokens: parser.getParsedTokens().size() })
        return { ok: true }
      }
      const preview = codeForParser.slice(0, 240).replace(/\r?\n/g, '\\n')
      const firstUnparsed = unparsedTokens.isEmpty?.() ? null : unparsedTokens.get(0)
      const error = [
        parser.getErrorInfo(),
        `isParserFail=${parser.isParserFail()}`,
        `parsedTokens=${parser.getParsedTokens().size()}`,
        `unparsedTokens=${unparsedTokens.size()}`,
        firstUnparsed == null ? null : `firstUnparsed=${firstUnparsed.value?.() ?? firstUnparsed.value?.call?.(firstUnparsed)} (${firstUnparsed.tokenName?.()})`,
        `preview: ${preview}`
      ].filter(Boolean).join('\n')
      parsed.set(path.relative(root, id), { ok: false, error })
      return { ok: false, error }
    } catch (error) {
      const preview = codeForParser.slice(0, 240).replace(/\r?\n/g, '\\n')
      const message = `${error?.stack || error?.message || String(error)}\npreview: ${preview}`
      parsed.set(path.relative(root, id), { ok: false, error: message })
      return { ok: false, error: message }
    }
  }

  return {
    name: 'vite-plugin-qin-generated-ts-slime-parser',
    enforce: 'post',

    configResolved(config) {
      root = config.root
      if (options.packageName) {
        tsRoot = resolvePackageRoot(root, options.packageName)
        tsEntryPath = path.resolve(tsRoot, 'index.ts')
      } else {
        tsRoot = path.resolve(root, options.tsRoot || '../qin/.qin/generated/slime-parser/ts-esm')
        tsEntryPath = path.resolve(tsRoot, 'com/slime/parser/SlimeParser.ts')
      }
      runtimePath = path.resolve(root, '.qin-runtime/slime-parser-ts-runtime.mjs')
      if (!fs.existsSync(tsEntryPath)) {
        throw new Error(`Qin generated TS SlimeParser entry not found: ${tsEntryPath}`)
      }
      this.info(`[qin-generated-ts-slime] using TS package entry ${tsEntryPath}`)
    },

    resolveId(id) {
      if (id === VIRTUAL_STATUS_ID) return RESOLVED_VIRTUAL_STATUS_ID
      return null
    },

    async load(id) {
      if (id !== RESOLVED_VIRTUAL_STATUS_ID) return null
      const runtime = await loadRuntime()
      return [
        `export const bundlePath = ${JSON.stringify(runtime.runtimePath)}`,
        `export const runtimeBundlePath = ${JSON.stringify(runtime.runtimePath)}`,
        `export const runtimeMode = ${JSON.stringify(runtime.mode)}`,
        `export const runtimeEntryPath = ${JSON.stringify(runtime.entryPath)}`,
        `export const runtimeRootPath = ${JSON.stringify(runtime.rootPath)}`,
        `export const runtimeFileCount = ${runtime.fileCount}`,
        `export const bundleSize = ${runtime.size}`,
        `export const runtimeBundleSize = ${runtime.runtimeSize}`,
        `export const parserClass = "com.slime.parser.SlimeParser"`,
        `export const parsedModules = ${JSON.stringify(Object.fromEntries(parsed))}`
      ].join('\n')
    },

    async transform(code, id) {
      if (id.includes('node_modules') || id.includes('?')) return null
      if (!/\.(ovs|cssts)$/.test(id)) return null
      const result = await parseWithGeneratedSlime(code, id)
      if (!result.ok) {
        this.warn(`[qin-generated-ts-slime] parser validation failed for ${id}\n${result.error}`)
      }
      return null
    }
  }
}

function resolvePackageRoot(projectRoot, packageName) {
  const packageJson = path.resolve(projectRoot, 'node_modules', ...packageName.split('/'), 'package.json')
  if (!fs.existsSync(packageJson)) {
    throw new Error(`Generated TS package is not installed: ${packageName}. Expected ${packageJson}`)
  }
  return path.dirname(packageJson)
}
