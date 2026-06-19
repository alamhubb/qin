import OvsParser from "../../ovsjs/ovs/ovs-compiler/src/parser/OvsParser.ts"
import { CssTsParser } from "../../cssts/cssts/cssts-compiler/src/parser/index.ts"

const ovsSource = `
import { ref } from 'vue'
let count = ref(0)
div {
  p { count.value }
}
`

const csstsSource = `
const buttonStyle = css {
  paddingX12px,
  backgroundColorBlue
}
`

function parseOvsSample() {
  const parser = new OvsParser(ovsSource)
  parser.Program()
  return {
    parserFail: parser.parserFail,
    parsedTokens: parser.parsedTokens.length
  }
}

function parseCssTsSample() {
  const parser = new CssTsParser(csstsSource)
  parser.Program()
  return {
    parserFail: parser.parserFail,
    parsedTokens: parser.parsedTokens.length
  }
}

const ovsParse = parseOvsSample()
const csstsParse = parseCssTsSample()

const result = {
  route: "qin-jvm-handwritten-ts-slimeparser",
  parserSuperclass: "slime-parser handwritten TypeScript",
  ovsParser: "OvsParser from ovs-compiler",
  csstsParser: "CssTsParser from cssts-compiler",
  classfile: "handwritten TS parser packages compiled/managed by Qin",
  javaToJsParser: false,
  ovsParse,
  csstsParse
}
