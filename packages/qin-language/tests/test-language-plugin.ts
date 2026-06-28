import { probeGeneratedQinParser } from '../qin-language-server/src/QinGeneratedParserProbe'
import { createQinParserDiagnostics, QinLanguageServicePlugin } from '../qin-language-server/src/QinLanguageServicePlugin'
import { lowerQinToTypeScript } from '../qin-language-server/src/QinLanguagePlugin'
import { TextDocument } from 'vscode-languageserver-textdocument'

const source = `
export object Counter {
  value = 1
}
`

const generated = lowerQinToTypeScript(source)

if (generated.includes('__QinObject_Counter')) {
  if (!generated.includes('class __QinObject_Counter') || !generated.includes('const Counter = new __QinObject_Counter()')) {
    throw new Error(`Qin object lowering must expose object class and singleton symbols, got: ${generated}`)
  }
} else {
  throw new Error(`Qin object source must lower through generated Qin CST into TypeScript symbols, got: ${generated}`)
}

const tsSubset = 'const alphaNumber = 41\nconst alphaText = alphaNumber.toString()\n'
if (lowerQinToTypeScript(tsSubset) !== tsSubset) {
  throw new Error('Qin TS-subset source must remain unchanged when generated CST has no Qin-only syntax')
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

const invalidSource = invalidDocument.getText()
const invalidGenerated = lowerQinToTypeScript(invalidSource)
if (invalidGenerated === invalidSource) {
  throw new Error('Invalid Qin source must not fall back to identity virtual TypeScript')
}
if (!invalidGenerated.includes('Qin transform failed')) {
  throw new Error(`Invalid Qin source must produce explicit transform failure code, got: ${invalidGenerated}`)
}

const unavailableDiagnostics = createQinParserDiagnostics({
  available: false,
  ok: false,
})
if (!unavailableDiagnostics.length) {
  throw new Error('Missing generated Qin parser package must produce a visible diagnostic')
}
if (unavailableDiagnostics[0].source !== 'qin-parser'
  || !unavailableDiagnostics[0].message.includes('Generated Qin parser package is not available')) {
  throw new Error(`Missing generated Qin parser diagnostic is not explicit: ${JSON.stringify(unavailableDiagnostics)}`)
}

console.log('Qin language plugin smoke passed')
