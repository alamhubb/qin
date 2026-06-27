import { probeGeneratedQinParser } from '../qin-language-server/src/QinGeneratedParserProbe'
import { QinLanguageServicePlugin } from '../qin-language-server/src/QinLanguageServicePlugin'
import { lowerQinToTypeScript } from '../qin-language-server/src/QinLanguagePlugin'
import { TextDocument } from 'vscode-languageserver-textdocument'

const source = `
export object Counter {
  value = 1
}
`

const generated = lowerQinToTypeScript(source)

if (generated !== source) {
  throw new Error('Qin language plugin must not rewrite source with a fallback transform')
}

if (generated.includes('__QinObject_Counter')) {
  throw new Error('Qin object syntax must be handled by the generated Qin parser, not TS string lowering')
}

const parserProbe = probeGeneratedQinParser(source)

if (!parserProbe.available) {
  throw new Error('Generated Qin parser package is required for qin-language tests')
}

if (!parserProbe.ok || parserProbe.cstName !== 'Program') {
  throw new Error(`Generated Qin parser must parse Qin object source, got ${JSON.stringify(parserProbe)}`)
}

const diagnosticsProvider = QinLanguageServicePlugin.create({} as never).provideDiagnostics
if (!diagnosticsProvider) {
  throw new Error('Qin language service plugin must provide parser diagnostics')
}

const validDocument = TextDocument.create('file:///valid.qin', 'qin', 1, source)
const validDiagnostics = await diagnosticsProvider(validDocument, {} as never)
if (validDiagnostics?.length) {
  throw new Error(`Valid Qin source must not produce parser diagnostics: ${JSON.stringify(validDiagnostics)}`)
}

const invalidDocument = TextDocument.create('file:///invalid.qin', 'qin', 1, 'export object Counter { value = }')
const invalidDiagnostics = await diagnosticsProvider(invalidDocument, {} as never)
if (!invalidDiagnostics?.length) {
  throw new Error('Invalid Qin source must produce parser diagnostics')
}
if (invalidDiagnostics[0].source !== 'qin-parser') {
  throw new Error(`Qin diagnostics must come from qin-parser, got ${JSON.stringify(invalidDiagnostics[0])}`)
}

console.log('Qin language plugin smoke passed')
