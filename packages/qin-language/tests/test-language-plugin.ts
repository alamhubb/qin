import { probeGeneratedQinParser } from '../qin-language-server/src/QinGeneratedParserProbe'
import { lowerQinToTypeScript } from '../qin-language-server/src/QinLanguagePlugin'

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

console.log('Qin language plugin smoke passed')
