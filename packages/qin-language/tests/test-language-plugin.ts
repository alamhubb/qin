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

const tsSubsetEditingSource = 'console.\ndocument.\n'
const tsSubsetStrictEditingGenerated = lowerQinToTypeScript(tsSubsetEditingSource)
if (!tsSubsetStrictEditingGenerated.includes('Qin transform failed')) {
  throw new Error(`Strict Qin lowering must reject incomplete TS-subset member access, got: ${tsSubsetStrictEditingGenerated}`)
}
const tsSubsetEditorProbe = probeGeneratedQinParser(tsSubsetEditingSource, { mode: 'editor' })
if (tsSubsetEditorProbe.cstName !== 'Program') {
  throw new Error(`Editor Qin parser recovery must accept incomplete TS-subset member access, got: ${JSON.stringify(tsSubsetEditorProbe)}`)
}
const tsSubsetEditingGenerated = lowerQinToTypeScript(tsSubsetEditingSource, { mode: 'editor' })
if (!tsSubsetEditingGenerated.includes('console.__qin_member_completion__')
  || !tsSubsetEditingGenerated.includes('document.__qin_member_completion__')
  || tsSubsetEditingGenerated.includes('Qin transform failed')) {
  throw new Error(`Editor Qin lowering must normalize recovered member access for TypeScript service code, got: ${tsSubsetEditingGenerated}`)
}

const qinObjectEditingSource = `
export object Counter {
  value = 1
  show() {
    console.
  }
}
`
const qinObjectStrictEditingGenerated = lowerQinToTypeScript(qinObjectEditingSource)
if (!qinObjectStrictEditingGenerated.includes('Qin transform failed')) {
  throw new Error(`Strict Qin lowering must reject incomplete object source, got: ${qinObjectStrictEditingGenerated}`)
}
const qinObjectEditorProbe = probeGeneratedQinParser(qinObjectEditingSource, { mode: 'editor' })
if (qinObjectEditorProbe.cstName !== 'Program') {
  throw new Error(`Editor Qin parser recovery must accept incomplete object member access, got: ${JSON.stringify(qinObjectEditorProbe)}`)
}
const qinObjectEditorEditingGenerated = lowerQinToTypeScript(qinObjectEditingSource, { mode: 'editor' })
if (!qinObjectEditorEditingGenerated.includes('class __QinObject_Counter')
  || !qinObjectEditorEditingGenerated.includes('console.')
  || qinObjectEditorEditingGenerated.includes('Qin transform failed')) {
  throw new Error(`Editor Qin lowering must preserve incomplete object member access service code, got: ${qinObjectEditorEditingGenerated}`)
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

const editorCompletionDocument = TextDocument.create('file:///completion.qin', 'qin', 1, tsSubsetEditingSource)
const editorCompletionDiagnostics = await diagnosticsProvider(editorCompletionDocument, {} as never)
if (editorCompletionDiagnostics?.some(item => item.source === 'qin-parser')) {
  throw new Error(`Dangling member access used for editor completion must not produce parser diagnostics: ${JSON.stringify(editorCompletionDiagnostics)}`)
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

const invalidEditorProbe = probeGeneratedQinParser(invalidDocument.getText(), { mode: 'editor' })
if (invalidEditorProbe.cstName !== 'Program' || invalidEditorProbe.ok || !invalidEditorProbe.diagnostics?.length) {
  throw new Error(`Editor parser must preserve CST and expose recovery diagnostics for invalid Qin, got ${JSON.stringify(invalidEditorProbe)}`)
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
