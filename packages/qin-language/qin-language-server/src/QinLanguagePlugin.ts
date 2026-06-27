import {
  forEachEmbeddedCode,
  type CodeMapping,
  type LanguagePlugin,
  type VirtualCode,
} from '@volar/language-core'
import type { TypeScriptExtraServiceScript } from '@volar/typescript'
import type { IScriptSnapshot } from 'typescript'
import { URI } from 'vscode-uri'
import { probeGeneratedQinParser } from './QinGeneratedParserProbe'
import { logToFile } from './logutil'

const ScriptKind = {
  Deferred: 0,
  JS: 1,
  TS: 3,
} as const

const QIN_LANGUAGE_ID = 'qin'

export const QinLanguagePlugin: LanguagePlugin<URI> = {
  getLanguageId(uri) {
    if (uri.path.endsWith('.qin')) {
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
        extension: 'qin',
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
        extension: '.ts',
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
            fileName: fileName + '.' + code.id + '.ts',
            code,
            extension: '.ts',
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
    let generatedCode = sourceCode

    try {
      generatedCode = lowerQinToTypeScript(sourceCode)
    } catch (e) {
      logToFile('Qin transform failed:', e instanceof Error ? e.stack || e.message : String(e))
    }

    this.mappings = [{
      sourceOffsets: [0],
      generatedOffsets: [0],
      lengths: [sourceCode.length],
      generatedLengths: [generatedCode.length],
      data: {
        completion: true,
        format: true,
        navigation: true,
        semantic: true,
        structure: true,
        verification: true,
      },
    }]

    this.embeddedCodes = [{
      id: 'qin-script',
      languageId: 'typescript',
      snapshot: {
        getText: (start, end) => generatedCode.substring(start, end),
        getLength: () => generatedCode.length,
        getChangeRange: () => undefined,
      },
      mappings: this.mappings,
      embeddedCodes: [],
    }]
  }
}

export function lowerQinToTypeScript(source: string): string {
  return stripBom(source ?? '')
}

function stripBom(source: string): string {
  return source.charCodeAt(0) === 0xFEFF ? source.substring(1) : source
}
