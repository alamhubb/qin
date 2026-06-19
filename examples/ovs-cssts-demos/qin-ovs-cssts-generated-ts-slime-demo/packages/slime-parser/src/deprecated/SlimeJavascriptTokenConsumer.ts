/**
 * ES2025 Token Consumer - Token 消费封装
 *
 * 职责：
 * 1. 为每个 ES2025 token 提供类型安全的消费方法
 * 2. 提供语义化的 API（方法名即文档）
 * 3. 支持 IDE 自动补全和编译时检查
 *
 * 设计模式：
 * - 继承 SubhutiTokenConsumer（基于接口依赖）
 * - 为每个 TokenNames 提供对应的消费方法
 * - 方法名与 token 名一致，易于理解
 *
 * @version 1.0.0
 */

import {
    SlimeBinaryOperatorTokenTypes,
    SlimeContextualKeywordTokenTypes,
    SlimeTokenType, SlimeReservedWordTokenTypes, SlimeUnaryOperatorTokenTypes
} from "slime-token"
import {SubhutiTokenConsumer} from "subhuti"
import {RegexpMode, TemplateTailMode} from "./SlimeJavascriptTokens.ts"
import SlimeJavascriptParser from "./SlimeJavascriptParser.ts";


export default class SlimeJavascriptTokenConsumer<T extends SlimeJavascriptParser = SlimeJavascriptParser> extends SubhutiTokenConsumer<T> {

    // ============================================
    // 软关键字消费辅助方法
    // ============================================

    /**
     * 消费一个 IdentifierName 并检查其值是否匹配
     *
     * 用于软关键字（如 get, set, of, target, meta, from）
     * 按照 ES2025 规范，这些在词法层是 IdentifierName，
     * 在语法层通过值检查来识别
     *
     * @param value 期望的标识符值
     * @returns CST 节点或 undefined
     */
    protected consumeIdentifierValue(value: string) {
        const token = this.parser.curToken
        if (token?.tokenName === SlimeTokenType.IdentifierName && token.tokenValue === value) {
            this.consume(SlimeTokenType.IdentifierName)
            return
        }
        // 匹配失败才标记解析失败
        this.parser.setParseFail()
        return undefined
    }

    // ============================================
    // 关键字 (Keywords)
    // ============================================

    Await() {
        this.consume(SlimeReservedWordTokenTypes.Await)
    }

    Break() {
        this.consume(SlimeReservedWordTokenTypes.Break)
    }

    Case() {
        this.consume(SlimeReservedWordTokenTypes.Case)
    }

    Catch() {
        this.consume(SlimeReservedWordTokenTypes.Catch)
    }

    Class() {
        this.consume(SlimeReservedWordTokenTypes.Class)
    }

    Const() {
        this.consume(SlimeReservedWordTokenTypes.Const)
    }

    Continue() {
        this.consume(SlimeReservedWordTokenTypes.Continue)
    }

    Debugger() {
        this.consume(SlimeReservedWordTokenTypes.Debugger)
    }

    Default() {
        this.consume(SlimeReservedWordTokenTypes.Default)
    }

    Do() {
        this.consume(SlimeReservedWordTokenTypes.Do)
    }

    Else() {
        this.consume(SlimeReservedWordTokenTypes.Else)
    }

    Enum() {
        this.consume(SlimeReservedWordTokenTypes.Enum)
    }

    Export() {
        this.consume(SlimeReservedWordTokenTypes.Export)
    }

    Extends() {
        this.consume(SlimeReservedWordTokenTypes.Extends)
    }

    False() {
        this.consume(SlimeReservedWordTokenTypes.False)
    }

    Finally() {
        this.consume(SlimeReservedWordTokenTypes.Finally)
    }

    For() {
        this.consume(SlimeReservedWordTokenTypes.For)
    }

    Function() {
        this.consume(SlimeReservedWordTokenTypes.Function)
    }

    If() {
        this.consume(SlimeReservedWordTokenTypes.If)
    }

    Import() {
        this.consume(SlimeReservedWordTokenTypes.Import)
    }

    New() {
        this.consume(SlimeReservedWordTokenTypes.New)
    }

    /**
     * NullLiteral
     * 规范 A.1: NullLiteral :: null
     */
    NullLiteral() {
        this.consume(SlimeReservedWordTokenTypes.NullLiteral)
    }

    Return() {
        this.consume(SlimeReservedWordTokenTypes.Return)
    }

    Super() {
        this.consume(SlimeReservedWordTokenTypes.Super)
    }

    Switch() {
        this.consume(SlimeReservedWordTokenTypes.Switch)
    }

    This() {
        this.consume(SlimeReservedWordTokenTypes.This)
    }

    Throw() {
        this.consume(SlimeReservedWordTokenTypes.Throw)
    }

    True() {
        this.consume(SlimeReservedWordTokenTypes.True)
    }

    Try() {
        this.consume(SlimeReservedWordTokenTypes.Try)
    }


    Var() {
        this.consume(SlimeReservedWordTokenTypes.Var)
    }

    While() {
        this.consume(SlimeReservedWordTokenTypes.While)
    }

    With() {
        this.consume(SlimeReservedWordTokenTypes.With)
    }

    Yield() {
        this.consume(SlimeReservedWordTokenTypes.Yield)
    }

    /**
     * 消费 'let' 软关键字
     * 用于 let 声明
     * 注意：let 在非严格模式下可作为标识符，因此作为软关键字处理
     */
    Let() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Let)
    }

    Void() {
        this.consume(SlimeUnaryOperatorTokenTypes.Void)
    }

    Typeof() {
        this.consume(SlimeUnaryOperatorTokenTypes.Typeof)
    }

    In() {
        this.consume(SlimeBinaryOperatorTokenTypes.In)
    }

    Instanceof() {
        this.consume(SlimeBinaryOperatorTokenTypes.Instanceof)
    }

    Delete() {
        this.consume(SlimeUnaryOperatorTokenTypes.Delete)
    }

    // ============================================
    // 软关键字 (Soft Keywords / Contextual Keywords)
    // 按照 ES2025 规范，这些在词法层是 IdentifierName
    // 在语法层通过值检查来识别
    // ============================================

    /**
     * 消费 'async' 软关键字
     * 用于 async 函数、async 箭头函数、async 方法
     * 注意：async 可作为标识符使用，如 `let async = 1`
     */
    Async() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Async)
    }

    /**
     * 消费 'static' 软关键字
     * 用于类的静态成员
     * 注意：非严格模式下可作为标识符
     */
    Static() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Static)
    }

    /**
     * 消费 'as' 软关键字
     * 用于 import/export 的重命名
     */
    As() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.As)
    }

    /**
     * 消费 'get' 软关键字
     * 用于 getter 方法定义
     */
    Get() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Get)
    }

    /**
     * 消费 'set' 软关键字
     * 用于 setter 方法定义
     */
    Set() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Set)
    }

    /**
     * 消费 'of' 软关键字
     * 用于 for-of 语句
     */
    Of() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Of)
    }

    /**
     * 消费 'target' 软关键字
     * 用于 new.target
     */
    Target() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Target)
    }

    /**
     * 消费 'meta' 软关键字
     * 用于 import.meta
     */
    Meta() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.Meta)
    }

    /**
     * 消费 'from' 软关键字
     * 用于 import/export 语句
     */
    From() {
        this.consumeIdentifierValue(SlimeContextualKeywordTokenTypes.From)
    }

    // ============================================
    // 字面量 (Literals)
    // ============================================

    /**
     * NumericLiteral
     * 规范中 NumericLiteral 包含所有数字变体：
     * - DecimalLiteral (如 123, 1.5, .5, 1e10)
     * - DecimalBigIntegerLiteral (如 123n)
     * - NonDecimalIntegerLiteral (如 0xFF, 0b11, 0o77)
     * - NonDecimalIntegerLiteral BigIntLiteralSuffix (如 0xFFn, 0b11n, 0o77n)
     * - LegacyOctalIntegerLiteral (如 077, Annex B)
     */
    NumericLiteral() {
        this.consume(SlimeTokenType.NumericLiteral)
    }

    StringLiteral() {
        this.consume(SlimeTokenType.StringLiteral)
    }

    // ============================================
    // 模板字符串和正则表达式 Token 消费方法
    // ============================================

    /**
     * 消费 TemplateHead: `...${
     */
    TemplateHead() {
        return this.consume('TemplateHead')
    }

    /**
     * 消费 TemplateMiddle: }...${
     * 需要传递 TemplateTailMode
     */
    TemplateMiddle() {
        return this.consume('TemplateMiddle', TemplateTailMode)
    }

    /**
     * 消费 TemplateTail: }...`
     * 需要传递 TemplateTailMode
     */
    TemplateTail() {
        return this.consume('TemplateTail', TemplateTailMode)
    }

    /**
     * 消费 NoSubstitutionTemplate: `...`
     */
    NoSubstitutionTemplate() {
        return this.consume('NoSubstitutionTemplate')
    }

    /**
     * 消费 RegularExpressionLiteral: /.../
     * 需要在正则模式下消费
     */
    RegularExpressionLiteral() {
        return this.consume('RegularExpressionLiteral', RegexpMode)
    }

    // ============================================
    // 注释 (Comments)
    // ============================================

    /**
     * Hashbang 注释 (#!...)
     * 只能出现在文件开头，由 Parser 的 Program 规则显式调用
     */
    HashbangComment() {
        this.consume(SlimeTokenType.HashbangComment)
    }

    // ============================================
    // 标识符 (Identifiers)
    // ============================================

    /**
     * IdentifierName
     * 规范: IdentifierName :: IdentifierStart | IdentifierName IdentifierPart
     */
    IdentifierName() {
        this.consume(SlimeTokenType.IdentifierName)
    }

    /**
     * PrivateIdentifier
     * 规范: PrivateIdentifier :: # IdentifierName
     */
    PrivateIdentifier() {
        this.consume(SlimeTokenType.PrivateIdentifier)
    }

    // ============================================
    // 运算符 - 4字符 (4-character Operators)
    // ============================================

    UnsignedRightShiftAssign() {
        this.consume(SlimeTokenType.UnsignedRightShiftAssign)
    }

    // ============================================
    // 运算符 - 3字符 (3-character Operators)
    // ============================================

    Ellipsis() {
        this.consume(SlimeTokenType.Ellipsis)
    }

    UnsignedRightShift() {
        this.consume(SlimeTokenType.UnsignedRightShift)
    }

    StrictEqual() {
        this.consume(SlimeTokenType.StrictEqual)
    }

    StrictNotEqual() {
        this.consume(SlimeTokenType.StrictNotEqual)
    }

    LeftShiftAssign() {
        this.consume(SlimeTokenType.LeftShiftAssign)
    }

    RightShiftAssign() {
        this.consume(SlimeTokenType.RightShiftAssign)
    }

    ExponentiationAssign() {
        this.consume(SlimeTokenType.ExponentiationAssign)
    }

    LogicalAndAssign() {
        this.consume(SlimeTokenType.LogicalAndAssign)
    }

    LogicalOrAssign() {
        this.consume(SlimeTokenType.LogicalOrAssign)
    }

    NullishCoalescingAssign() {
        this.consume(SlimeTokenType.NullishCoalescingAssign)
    }

    // ============================================
    // 运算符 - 2字符 (2-character Operators)
    // ============================================

    Arrow() {
        this.consume(SlimeTokenType.Arrow)
    }

    PlusAssign() {
        this.consume(SlimeTokenType.PlusAssign)
    }

    MinusAssign() {
        this.consume(SlimeTokenType.MinusAssign)
    }

    MultiplyAssign() {
        this.consume(SlimeTokenType.MultiplyAssign)
    }

    DivideAssign() {
        this.consume(SlimeTokenType.DivideAssign)
    }

    ModuloAssign() {
        this.consume(SlimeTokenType.ModuloAssign)
    }

    LeftShift() {
        this.consume(SlimeTokenType.LeftShift)
    }

    RightShift() {
        this.consume(SlimeTokenType.RightShift)
    }

    LessEqual() {
        this.consume(SlimeTokenType.LessEqual)
    }

    GreaterEqual() {
        this.consume(SlimeTokenType.GreaterEqual)
    }

    Equal() {
        this.consume(SlimeTokenType.Equal)
    }

    NotEqual() {
        this.consume(SlimeTokenType.NotEqual)
    }

    LogicalAnd() {
        this.consume(SlimeTokenType.LogicalAnd)
    }

    LogicalOr() {
        this.consume(SlimeTokenType.LogicalOr)
    }

    NullishCoalescing() {
        this.consume(SlimeTokenType.NullishCoalescing)
    }

    Increment() {
        this.consume(SlimeTokenType.Increment)
    }

    Decrement() {
        this.consume(SlimeTokenType.Decrement)
    }

    Exponentiation() {
        this.consume(SlimeTokenType.Exponentiation)
    }

    BitwiseAndAssign() {
        this.consume(SlimeTokenType.BitwiseAndAssign)
    }

    BitwiseOrAssign() {
        this.consume(SlimeTokenType.BitwiseOrAssign)
    }

    BitwiseXorAssign() {
        this.consume(SlimeTokenType.BitwiseXorAssign)
    }

    OptionalChaining() {
        this.consume(SlimeTokenType.OptionalChaining)
    }

    // ============================================
    // 运算符 - 1字符 (1-character Operators)
    // ============================================

    LBrace() {
        this.consume(SlimeTokenType.LBrace)
    }

    RBrace() {
        this.consume(SlimeTokenType.RBrace)
    }

    LParen() {
        this.consume(SlimeTokenType.LParen)
    }

    RParen() {
        this.consume(SlimeTokenType.RParen)
    }

    LBracket() {
        this.consume(SlimeTokenType.LBracket)
    }

    RBracket() {
        this.consume(SlimeTokenType.RBracket)
    }

    Dot() {
        this.consume(SlimeTokenType.Dot)
    }

    Semicolon() {
        this.consume(SlimeTokenType.Semicolon)
    }

    Comma() {
        this.consume(SlimeTokenType.Comma)
    }

    Less() {
        this.consume(SlimeTokenType.Less)
    }

    Greater() {
        this.consume(SlimeTokenType.Greater)
    }

    Plus() {
        this.consume(SlimeTokenType.Plus)
    }

    Minus() {
        this.consume(SlimeTokenType.Minus)
    }

    Asterisk() {
        this.consume(SlimeTokenType.Asterisk)
    }

    Slash() {
        this.consume(SlimeTokenType.Slash)
    }

    Modulo() {
        this.consume(SlimeTokenType.Modulo)
    }

    BitwiseAnd() {
        this.consume(SlimeTokenType.BitwiseAnd)
    }

    BitwiseOr() {
        this.consume(SlimeTokenType.BitwiseOr)
    }

    BitwiseXor() {
        this.consume(SlimeTokenType.BitwiseXor)
    }

    BitwiseNot() {
        this.consume(SlimeTokenType.BitwiseNot)
    }

    LogicalNot() {
        this.consume(SlimeTokenType.LogicalNot)
    }

    Question() {
        this.consume(SlimeTokenType.Question)
    }

    Colon() {
        this.consume(SlimeTokenType.Colon)
    }

    Assign() {
        this.consume(SlimeTokenType.Assign)
    }
}
