import CssTsTokenConsumer, { cssTsTokens } from "./CssTsTokenConsumer.js"
import { Subhuti, SubhutiRule } from 'subhuti'
import {
  QinParser,
  ExpressionParams as GeneratedExpressionParams,
  type ExpressionParams
} from "@qin/generated-qin-parser-ts"
import { normalizeGeneratedTokens } from "./generated-runtime-adapter.ts"

export class CssTsParserOptions<T extends CssTsTokenConsumer = CssTsTokenConsumer> {
  tokenConsumer: CssTsTokenConsumer
  tokenDefinitions: any

  constructor(tokenConsumer: CssTsTokenConsumer = new CssTsTokenConsumer(), tokenDefinitions: any = cssTsTokens) {
    this.tokenConsumer = tokenConsumer
    this.tokenDefinitions = tokenDefinitions
  }
}

/**
 * CssTsParser - CSS-in-TS 鏍峰紡瑙ｆ瀽鍣?
 * 
 * 鏀寔鐨勮娉曪細
 * 
 * css 琛ㄨ揪寮?- 鍦ㄤ换浣曡〃杈惧紡浣嶇疆浣跨敤锛?
 *   const buttonBase = css { colorRed, fontBold }
 *   const styles = { primary: css { bgPrimary } }
 *   div(class = css { primaryButton, marginTop }) {}
 * 
 * 娉ㄦ剰锛氫笉鏀寔 css 澹版槑璇硶锛堝 `css colorRed`锛夛紝
 * 鍥犱负澹版槑璇硶闇€瑕侀噸鍐?Statement/Declaration 瑙勫垯锛?
 * 浼氬鑷翠笌鏍囧噯 JS 璇硶鍐茬獊锛堝 async function锛夈€?
 * 鎺ㄨ崘浣跨敤琛ㄨ揪寮忚娉曪紝鏇寸伒娲讳笖涓嶄細鐮村潖 JS 鍏煎鎬с€?
 */
@Subhuti
export default class CssTsParser<T extends CssTsTokenConsumer = CssTsTokenConsumer> extends QinParser<T> {
  tokenConsumer: CssTsTokenConsumer

  constructor(sourceCode: string = '', options: CssTsParserOptions<T> | null = null) {
    super(sourceCode)
    const effectiveOptions: CssTsParserOptions<T> = options == null
      ? new CssTsParserOptions<T>()
      : options
    const consumer: CssTsTokenConsumer = effectiveOptions.tokenConsumer
    consumer.setParser(this)
    ;(this as any).__qin_field_tokenConsumer = consumer
    this.tokenConsumer = consumer
  }

  get parsedTokens(): any[] {
    const tokens = typeof (this as any).getParsedTokens === 'function'
      ? (this as any).getParsedTokens()
      : (this as any).__qin_field_parsedTokens
    return normalizeGeneratedTokens(tokens)
  }

  expressionParamsWith(params: ExpressionParams | null = null): ExpressionParams {
    const effectiveParams: ExpressionParams = params == null
      ? new GeneratedExpressionParams() as ExpressionParams
      : params
    return new GeneratedExpressionParams(effectiveParams.in(), effectiveParams.yield(), effectiveParams.await()) as ExpressionParams
  }

  /**
   * CssExpression - css 琛ㄨ揪寮?
   * 
   * 璇硶锛?
   *   css { element1, element2, ... }
   *   css atomName
   */
  @SubhutiRule
  CssExpression(params: ExpressionParams = {} as any) {
    const expressionParams: ExpressionParams = this.expressionParamsWith(params)
    this.consumeIdentifierValue('css')
    if (this.tokenNameAt(1) === 'LBrace') {
      this.CssStyleObject(expressionParams)
    } else {
      this.tokenConsumer.IdentifierName()
    }
    return this.getCurCst()
  }

  /**
   * CssStyleObject - css 鏍峰紡瀵硅薄
   * 
   * 璇硶锛歿 element1, element2, ... }
   */
  @SubhutiRule
  CssStyleObject(params: ExpressionParams = {} as any) {
    this.tokenConsumer.LBrace()
    if (this.tokenNameAt(1) !== 'RBrace') {
      this.CssAtomList()
    }
    this.tokenConsumer.RBrace()
    return this.getCurCst()
  }

  @SubhutiRule
  CssAtomList() {
    this.tokenConsumer.IdentifierName()
    while (this.tokenNameAt(1) === 'Comma') {
      this.tokenConsumer.Comma()
      this.tokenConsumer.IdentifierName()
    }
    return this.getCurCst()
  }

  /**
   * 閲嶅啓 PrimaryExpression锛屾坊鍔?CssExpression 鏀寔
   * 
   * 娉ㄦ剰锛欳ssExpression 浠?'css' 杞叧閿瓧寮€澶达紝
   * 蹇呴』鏀惧湪 IdentifierReference 涔嬪墠锛屽惁鍒?'css' 浼氳褰撲綔鏅€氭爣璇嗙
   */
  @SubhutiRule
  PrimaryExpression(params: ExpressionParams = {} as any) {
    const expressionParams: ExpressionParams = this.expressionParamsWith(params)
    if (this.matchIdentifierValue('css')) {
      return this.CssExpression(expressionParams)
    }
    return super.__qin_subhuti_raw_StandardPrimaryExpression(expressionParams)
  }
}
