import { SlimeParser } from "java:com.slime.parser"

class QinOvsParser extends SlimeParser {
  parserKind(): string {
    return "ovs"
  }
}

class QinCssTsParser extends SlimeParser {
  parserKind(): string {
    return "cssts"
  }
}

const result = {
  route: "qin-jvm-direct-java-slimeparser",
  parserSuperclass: "com.slime.parser.SlimeParser",
  ovsParser: "QinOvsParser extends Java SlimeParser",
  csstsParser: "QinCssTsParser extends Java SlimeParser",
  classfile: "TS parser classes compiled to .class",
  javaToJsParser: false
}
