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

const objectExtendsSource = `
class BaseCounter {
  baseValue = 1
}

export object Counter extends BaseCounter {
  value = this.baseValue
}
`
const generatedObjectExtends = lowerQinToTypeScript(objectExtendsSource)
if (!generatedObjectExtends.includes('class __QinObject_Counter extends BaseCounter')) {
  throw new Error(`Qin object lowering must preserve ClassTail heritage, got: ${generatedObjectExtends}`)
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

const sharedBareImportDocument = TextDocument.create('file:///workspace/shared/shared-bare.qin', 'qin', 1, "import lodash from 'lodash'\n")
const sharedBareImportDiagnostics = await diagnosticsProvider(sharedBareImportDocument, {} as never)
if (!sharedBareImportDiagnostics?.some(item => item.source === 'qin-import-policy'
  && item.message.includes('QIN1003 shared code cannot import bare/non-local modules'))) {
  throw new Error(`Shared Qin bare import must produce QIN1003 diagnostic: ${JSON.stringify(sharedBareImportDiagnostics)}`)
}

const sharedLocalImportDocument = TextDocument.create('file:///workspace/shared/shared-local.qin', 'qin', 1, "import { Contract } from './contract.qin'\n")
const sharedLocalImportDiagnostics = await diagnosticsProvider(sharedLocalImportDocument, {} as never)
if (sharedLocalImportDiagnostics?.some(item => item.source === 'qin-import-policy')) {
  throw new Error(`Shared Qin local import must not produce import-policy diagnostic: ${JSON.stringify(sharedLocalImportDiagnostics)}`)
}

const appBareImportDocument = TextDocument.create('file:///workspace/app/app-bare.qin', 'qin', 1, "import lodash from 'lodash'\n")
const appBareImportDiagnostics = await diagnosticsProvider(appBareImportDocument, {} as never)
if (appBareImportDiagnostics?.some(item => item.source === 'qin-import-policy')) {
  throw new Error(`App Qin bare import must not produce shared import-policy diagnostic: ${JSON.stringify(appBareImportDiagnostics)}`)
}

const mainBareImportDocument = TextDocument.create('file:///workspace/main/main-bare.qin', 'qin', 1, "import lodash from 'lodash'\n")
const mainBareImportDiagnostics = await diagnosticsProvider(mainBareImportDocument, {} as never)
if (mainBareImportDiagnostics?.some(item => item.source === 'qin-import-policy')) {
  throw new Error(`Main Qin bare import must not produce shared import-policy diagnostic: ${JSON.stringify(mainBareImportDiagnostics)}`)
}

console.log('Qin language plugin smoke passed')
