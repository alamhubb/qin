import type { LanguageServicePlugin } from '@volar/language-service'
import { DiagnosticSeverity } from 'vscode-languageserver-protocol'
import type { TextDocument } from 'vscode-languageserver-textdocument'
import { probeGeneratedQinParser } from './QinGeneratedParserProbe'

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
        if (!result.available || result.ok) {
          return []
        }
        return (result.diagnostics ?? [{
          message: result.error ?? 'Qin parser error',
          line: 0,
          column: 0,
        }]).map(diagnostic => {
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
      },
    }
  },
}
