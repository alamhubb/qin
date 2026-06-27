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

console.log('Qin language plugin smoke passed')
