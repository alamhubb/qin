import { createRegToken } from 'subhuti'
import { SlimeTokensObj } from "slime-parser"
import { SlimeTokenType } from "slime-token"
import { SlimeTokenConsumer } from "slime-parser"

export const cssTsTokenName = {
  ...SlimeTokenType,
}

/**
 * CssTs 软关键字（上下文关键字）
 */
export const CssTsContextualKeywordTypes = {
  Css: 'css',
} as const

export const cssTsTokens = [
  ...Object.values(SlimeTokensObj),
]

export default class CssTsTokenConsumer extends SlimeTokenConsumer {
  /**
   * 消费 'css' 软关键字
   */
  Css() {
    this.consumeIdentifierValue(CssTsContextualKeywordTypes.Css)
  }
}
