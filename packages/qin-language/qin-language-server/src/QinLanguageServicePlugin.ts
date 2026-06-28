import type { LanguageServicePlugin } from '@volar/language-service'
import { DiagnosticSeverity } from 'vscode-languageserver-protocol'
import type { TextDocument } from 'vscode-languageserver-textdocument'
import { probeGeneratedQinParser, type QinGeneratedParserProbeResult } from './QinGeneratedParserProbe'

export const QinLanguageServicePlugin: LanguageServicePlugin = {
  name: 'qin-generated-parser-diagnostics',
  capabilities: {
    diagnosticProvider: {
      interFileDependencies: false,
      workspaceDiagnostics: false,
    },
  },
  create() {
    return {
      provideDiagnostics(document: TextDocument) {
        if (document.languageId !== 'qin' && !document.uri.endsWith('.qin')) {
          return []
        }
        const result = probeGeneratedQinParser(document.getText())
        return createQinParserDiagnostics(result)
      },
    }
  },
}

export function createQinParserDiagnostics(result: QinGeneratedParserProbeResult) {
  if (result.ok) {
    return []
  }
  const diagnostics = result.available
    ? result.diagnostics ?? [{
      message: result.error ?? 'Qin parser error',
      line: 0,
      column: 0,
    }]
    : [{
      message: 'Generated Qin parser package is not available',
      line: 0,
      column: 0,
    }]
  return diagnostics.map(diagnostic => {
    const position = {
      line: diagnostic.line,
      character: diagnostic.column,
    }
    return {
      range: {
        start: position,
        end: {
          line: position.line,
          character: position.character + 1,
        },
      },
      severity: DiagnosticSeverity.Error,
      source: 'qin-parser',
      message: diagnostic.message,
    }
  })
}
