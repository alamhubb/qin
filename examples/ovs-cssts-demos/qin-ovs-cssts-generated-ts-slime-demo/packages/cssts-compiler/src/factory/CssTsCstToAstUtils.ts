import { SlimeCstToAst, registerSlimeCstToAstUtil } from "@qin/generated-qin-parser-ts/SlimeCstToAstBridge"
import { SubhutiCst } from "subhuti"
import { com_slime_ast_nodes_misc_Program as GeneratedSlimeProgram } from "@qin/generated-qin-parser-ts/com/slime/ast/nodes/misc/Program.ts"
import { com_slime_parser_cstToAst_SlimeAstCreateUtils as GeneratedSlimeAstCreateUtils } from "@qin/generated-qin-parser-ts/com/slime/parser/cstToAst/SlimeAstCreateUtils.ts"
import { __QinJavaUtilHashMap, __QinJavaUtilIdentityHashMap, __QinJavaUtilUnmodifiableMap } from "@qin/java-sdk-js"
import CssTsParser from "../parser/CssTsParser.js"
import { normalizeGeneratedAstList, readGeneratedEnumName, readGeneratedField } from "../parser/generated-runtime-adapter.js"
import {
  type SlimeExpression,
  type SlimeStatement,
  type SlimeModuleDeclaration,
  type SlimeProgram,
  type SlimeIdentifier,
  type SlimeImportDeclaration,
  type SlimeImportDefaultSpecifier,
  type SlimeImportSpecifier,
  type SlimeImportSpecifierItem,
  type SlimeLiteral,
  type SlimeModuleSpecifier,
  type SlimeVariableDeclarator,
  type SlimeCallExpression,
  type SlimeMemberExpression,
} from "slime-ast"
import { com_subhuti_struct_SubhutiSourceLocation as SubhutiSourceLocation } from "@qin/generated-qin-parser-ts/SubhutiSourceLocation"
import { CSSTS_CONFIG, isBuiltinAtom } from "../utils/cssClassName.js"

export interface CssStyleInfo {
  name: string
  isAtomic: boolean
  dependencies: string[]
  cssClassName: string
  loc?: any
}

export interface GroupUtilInfo {
  varName: string
  className: string
  pseudos: string[]
  atomNames: string[]
}

/**
 * CssTsCstToAst - CSS-in-TS CST 到 AST 转换器
 *
 * 核心职责：
 * 1. 将 CssTsParser 解析出的 CST 转换为标准 ESTree AST
 * 2. 处理 css { } 表达式语法，转换为 cssts.merge() 调用
 * 3. 收集使用的原子类名（usedAtoms），供 vite 插件生成 CSS
 * 4. 处理伪类变量（如 btn$$hover），注入运行时参数
 * 5. 作用域分析：区分局部变量和原子类名，支持自动解构
 */
// 版本号，用于确认使用的是最新版本
const CSSTS_COMPILER_VERSION = '2.1.1-commaToken-fix'

export class CssTsCstToAst extends SlimeCstToAst {
  private cssStyles: Map<string, CssStyleInfo> = new Map()
  private usedAtoms: Set<string> = new Set()
  private currentVarName: string | null = null
  protected _hasCsstsSyntax = false
  private scopeStack: Set<string>[] = []
  private static _loggedVersion = false

  constructor() {
    super()
    // 版本日志（只打印一次）
    if (!CssTsCstToAst._loggedVersion) {
      console.log(`[cssts-compiler] v${CSSTS_COMPILER_VERSION} - 100% mapping coverage`)
      CssTsCstToAst._loggedVersion = true
    }
    // 注册当前实例到 cssts 全局
    CssTsCstToAst.registerInstance(this)
  }

  private static registerInstance(instance: CssTsCstToAst): void {
    registerSlimeCstToAstUtil(instance)
  }

  private get currentScope(): Set<string> {
    if (this.scopeStack.length === 0) {
      this.scopeStack.push(new Set())
    }
    return this.scopeStack[this.scopeStack.length - 1]
  }

  private isInScope(name: string): boolean {
    for (let i = this.scopeStack.length - 1; i >= 0; i--) {
      if (this.scopeStack[i].has(name)) return true
    }
    return false
  }

  protected pushScope(): void {
    this.scopeStack.push(new Set())
  }

  protected popScope(): void {
    if (this.scopeStack.length > 0) this.scopeStack.pop()
  }

  protected addToScope(name: string): void {
    this.currentScope.add(name)
  }

  /**
   * 判断标识符是否是原子类名
   *
   * 判断逻辑（作用域 + 白名单）：
   * 1. 如果在作用域中 → 不是原子类（是变量）
   * 2. 如果在内置原子类白名单中 → 是原子类
   * 3. 否则 → 不是原子类（保持原样）
   */
  private isAtomName(name: string): boolean {
    if (this.isInScope(name)) return false
    return isBuiltinAtom(name)
  }

  get hasCsstsSyntax(): boolean {
    return this._hasCsstsSyntax
  }

  protected resetState(): void {
    this._hasCsstsSyntax = false
    this.cssStyles.clear()
    this.usedAtoms.clear()
    this.currentVarName = null
    this.scopeStack = [new Set()]
  }

  toProgram(cst: SubhutiCst): GeneratedSlimeProgram {
    this.resetState()
    return super.toProgram(cst) as GeneratedSlimeProgram
  }

  getCssStyles(): Map<string, CssStyleInfo> { return this.cssStyles }
  getUsedAtoms(): Set<string> { return this.usedAtoms }
  clearUsedAtoms() { this.usedAtoms.clear() }

  private sourceLocationOf(loc?: SubhutiSourceLocation | null, type?: string, value?: string): SubhutiSourceLocation | null {
    if (!loc) return null
    const start = loc.start()
    const end = loc.end()
    if (!start || !end) return null
    const resolvedType = type ?? loc.getType()
    if (value !== undefined) {
      return SubhutiSourceLocation.ofWithValue(resolvedType ?? null, value, start, end)
    }
    if (resolvedType !== null && resolvedType !== undefined) {
      return SubhutiSourceLocation.of(resolvedType, start, end)
    }
    return SubhutiSourceLocation.of(start, end)
  }

  private generatorLoc(loc?: SubhutiSourceLocation | null, value?: string, type?: string): any {
    if (!loc) return undefined
    const subhutiLoc: SubhutiSourceLocation | null = this.sourceLocationOf(loc, type, value)
    if (!subhutiLoc) return undefined
    const start = subhutiLoc.start()
    const end = subhutiLoc.end()
    return {
      type: subhutiLoc.getType(),
      value: subhutiLoc.getValue(),
      newLine: subhutiLoc.getNewLine(),
      index: start.index(),
      length: Math.max(0, end.index() - start.index()),
      start: { line: start.line(), column: start.column(), index: start.index() },
      end: { line: end.line(), column: end.column(), index: end.index() },
      filename: subhutiLoc.getFilename(),
      identifierName: subhutiLoc.getIdentifierName()
    }
  }

  private isSlimeNodeMapLike(node: any): node is __QinJavaUtilHashMap | __QinJavaUtilIdentityHashMap | __QinJavaUtilUnmodifiableMap {
    return node instanceof __QinJavaUtilHashMap
      || node instanceof __QinJavaUtilIdentityHashMap
      || node instanceof __QinJavaUtilUnmodifiableMap
  }

  private slimeNodeTypeName(node: any): string | undefined {
    if (node === null || node === undefined) return undefined
    if (this.isSlimeNodeMapLike(node)) {
      return this.readSlimeMapNodeTypeValue(node as __QinJavaUtilHashMap | __QinJavaUtilIdentityHashMap | __QinJavaUtilUnmodifiableMap)
    }
    return this.readSlimeNodeTypeValue(node)
  }

  private readSlimeNodeTypeValue(node: any): string | undefined {
    if (node === null || node === undefined) return undefined
    return this.normalizeSlimeNodeTypeValue(readGeneratedField(node, 'type'))
  }

  private readSlimeMapNodeTypeValue(node: __QinJavaUtilHashMap | __QinJavaUtilIdentityHashMap | __QinJavaUtilUnmodifiableMap | null | undefined): string | undefined {
    if (node === null || node === undefined) return undefined
    return this.normalizeSlimeNodeTypeValue(node.get('type'))
  }

  private normalizeSlimeNodeTypeValue(typeValue: any): string | undefined {
    if (typeValue === null || typeValue === undefined) return undefined
    if (typeof typeValue === 'string') return typeValue
    if (typeof typeValue !== 'object' && typeof typeValue !== 'function') return undefined
    const generatedEnumName = readGeneratedEnumName(typeValue)
    if (generatedEnumName !== undefined) return generatedEnumName
    const typeText = String(typeValue)
    if (typeText.length === 0 || typeText === '[object Object]') return undefined
    return typeText
  }

  toFileAst(cst: SubhutiCst): SlimeProgram {
    const program = this.toProgram(cst)
    return this.normalizeGeneratedProgram(program)
  }

  private normalizeGeneratedProgram(value: GeneratedSlimeProgram): SlimeProgram {
    this.resetState()
    const body = normalizeGeneratedAstList(value.body()) as Array<SlimeStatement | SlimeModuleDeclaration>
    const transformedBody = this.processCsstsPostTransform(body)
    return {
      type: 'Program',
      sourceType: value.sourceType(),
      body: transformedBody
    } as SlimeProgram
  }

  protected processCsstsPostTransform(body: Array<SlimeStatement | SlimeModuleDeclaration>): Array<SlimeStatement | SlimeModuleDeclaration> {
    if (this.usedAtoms.size > 0) {
      return this.ensureCsstsImports(body)
    }
    return body
  }


  /**
   * 确保 CSSTS 相关的导入语句存在
   *
   * 当使用了 css {} 语法时，需要添加以下三个导入：
   * 1. import 'virtual:cssts.css'        - 虚拟 CSS 模块，包含生成的原子类样式
   * 2. import {cssts} from 'cssts-ts'    - CSSTS 运行时，提供 merge 等方法
   * 3. import {csstsAtom} from 'virtual:csstsAtom' - 原子类名映射对象
   *
   * 为什么分开判断每个导入：
   * - 虽然使用 css {} 语法时通常需要全部三个导入
   * - 但用户可能手动添加了其中某些导入（如从 cssts-ts 导入其他内容）
   * - 分开检查可以避免重复导入，同时确保不遗漏任何必需的导入
   * - 这种细粒度的检查也便于未来扩展（如某些场景只需要部分导入）
   */
  protected ensureCsstsImports(body: Array<SlimeStatement | SlimeModuleDeclaration>): Array<SlimeStatement | SlimeModuleDeclaration> {
    let hasCsstsImport = false
    let hasCsstsAtomImport = false
    let hasCsstsCssImport = false

    // 遍历现有导入，检查是否已存在 CSSTS 相关导入
    for (const stmt of body) {
      if (this.slimeNodeTypeName(stmt) === 'IMPORT_DECLARATION') {
        const importDecl: SlimeImportDeclaration = stmt as SlimeImportDeclaration
        const sourceLiteral: SlimeLiteral = readGeneratedField(importDecl, 'source') as SlimeLiteral
        const source: string = readGeneratedField(sourceLiteral, 'value') as string

        // 只检查 cssts-ts 导入（运行时）
        if (source === 'cssts-ts') {
          const specifiers: Array<SlimeImportSpecifierItem> = readGeneratedField(importDecl, 'specifiers') as Array<SlimeImportSpecifierItem>
          for (const spec of specifiers) {
            // 注意：spec 可能是 { specifier: {...}, commaToken: ... } 结构
            const actualSpec: SlimeModuleSpecifier = spec.specifier

            if (this.slimeNodeTypeName(actualSpec) === 'IMPORT_SPECIFIER') {
              const namedSpec: SlimeImportSpecifier = actualSpec as SlimeImportSpecifier
              const importedIdentifier: SlimeIdentifier = readGeneratedField(namedSpec, 'imported') as SlimeIdentifier
              const localIdentifier: SlimeIdentifier = readGeneratedField(namedSpec, 'local') as SlimeIdentifier
              const importedName: string = readGeneratedField(importedIdentifier, 'name') as string
              const localName: string = readGeneratedField(localIdentifier, 'name') as string
              if (importedName === 'cssts' || localName === 'cssts') {
                hasCsstsImport = true
              }
            } else if (this.slimeNodeTypeName(actualSpec) === 'IMPORT_DEFAULT_SPECIFIER') {
              const defaultSpec: SlimeImportDefaultSpecifier = actualSpec as SlimeImportDefaultSpecifier
              const localIdentifier: SlimeIdentifier = readGeneratedField(defaultSpec, 'local') as SlimeIdentifier
              const localName: string = readGeneratedField(localIdentifier, 'name') as string
              if (localName === 'cssts') hasCsstsImport = true
            }
          }
        }

        // 检查虚拟模块导入
        if (source === 'virtual:csstsAtom') hasCsstsAtomImport = true
        if (source === 'virtual:cssts.css') hasCsstsCssImport = true
      }
    }

    // 按需添加缺失的导入
    const newImports: SlimeModuleDeclaration[] = []
    if (!hasCsstsCssImport) newImports.push(this.createCsstsCssImport())
    if (!hasCsstsImport) newImports.push(this.createCsstsImport())
    if (!hasCsstsAtomImport) newImports.push(this.createCsstsAtomImport())

    // 将新导入插入到现有导入语句之后
    if (newImports.length > 0) {
      let insertIndex = 0
      for (let i = 0; i < body.length; i++) {
        if (this.slimeNodeTypeName(body[i]) === 'IMPORT_DECLARATION') insertIndex = i + 1
        else break
      }
      return [...body.slice(0, insertIndex), ...newImports, ...body.slice(insertIndex)]
    }
    return body
  }

  private createCsstsImport(): SlimeModuleDeclaration {
    return {
      type: 'ImportDeclaration',
      specifiers: [{
        type: 'ImportSpecifier',
        imported: this.createStaticIdentifier('cssts'),
        local: this.createStaticIdentifier('cssts')
      }],
      source: this.createStaticStringLiteral('cssts-ts')
    } as any
  }

  private createCsstsAtomImport(): SlimeModuleDeclaration {
    return {
      type: 'ImportDeclaration',
      specifiers: [{
        type: 'ImportSpecifier',
        imported: this.createStaticIdentifier('csstsAtom'),
        local: this.createStaticIdentifier('csstsAtom')
      }],
      source: this.createStaticStringLiteral('virtual:csstsAtom')
    } as any
  }

  /** 创建 import 'virtual:cssts.css' 导入（副作用导入，无 specifiers） */
  private createCsstsCssImport(): SlimeModuleDeclaration {
    return {
      type: 'ImportDeclaration',
      specifiers: [],
      source: this.createStaticStringLiteral('virtual:cssts.css')
    } as any
  }

  /** 收集导入的标识符到作用域 */
  private createStaticIdentifier(name: string): SlimeIdentifier {
    return {
      type: 'Identifier',
      name
    } as SlimeIdentifier
  }

  private createStaticStringLiteral(value: string): SlimeLiteral {
    return {
      type: 'Literal',
      value,
      raw: value
    } as SlimeLiteral
  }

  private cstChildren(cst: SubhutiCst | undefined | null): SubhutiCst[] {
    if (cst === undefined || cst === null) return []
    const children = cst.getChildren()
    if (children === undefined || children === null) return []
    return children as SubhutiCst[]
  }

  createImportDeclarationAst(cst: SubhutiCst): any {
    const result: SlimeImportDeclaration = super.createImportDeclarationAst(cst) as SlimeImportDeclaration
    const specifiers: Array<SlimeImportSpecifierItem> = readGeneratedField(result, 'specifiers') as Array<SlimeImportSpecifierItem>
    for (const spec of specifiers) {
      const actualSpec: SlimeModuleSpecifier = spec.specifier
      if (this.slimeNodeTypeName(actualSpec) === 'IMPORT_SPECIFIER') {
        const namedSpec: SlimeImportSpecifier = actualSpec as SlimeImportSpecifier
        const localIdentifier: SlimeIdentifier = readGeneratedField(namedSpec, 'local') as SlimeIdentifier
        const localName: string = readGeneratedField(localIdentifier, 'name') as string
        if (localName.length > 0) this.addToScope(localName)
      } else if (
        this.slimeNodeTypeName(actualSpec) === 'IMPORT_DEFAULT_SPECIFIER' ||
        this.slimeNodeTypeName(actualSpec) === 'IMPORT_NAMESPACE_SPECIFIER'
      ) {
        const defaultSpec: SlimeImportDefaultSpecifier = actualSpec as SlimeImportDefaultSpecifier
        const localIdentifier: SlimeIdentifier = readGeneratedField(defaultSpec, 'local') as SlimeIdentifier
        const localName: string = readGeneratedField(localIdentifier, 'name') as string
        if (localName.length > 0) this.addToScope(localName)
      }
    }
    return result
  }

  /** 收集变量声明到作用域，处理伪类变量 */
  createLexicalBindingAst(cst: SubhutiCst): any {
    const firstChild = cst.getChildren()?.[0]
    let varName: string | null = null
    if (firstChild?.getName() === 'BindingIdentifier') {
      const idChild = firstChild.getChildren()?.[0]
      const resolvedName = this.extractCstValue(idChild)
      if (resolvedName.length > 0) varName = resolvedName
    }

    // 收集变量名到作用域
    if (varName !== null && varName.length > 0) this.addToScope(varName)

    // 伪类变量处理
    if (varName !== null && varName.includes(CSSTS_CONFIG.PSEUDO_SEPARATOR)) {
      this.currentVarName = varName
    }

    const result: SlimeVariableDeclarator = super.createLexicalBindingAst(cst) as SlimeVariableDeclarator

    if (this.currentVarName !== null && this.currentVarName.includes(CSSTS_CONFIG.PSEUDO_SEPARATOR)) {
      this.usedAtoms.add(this.currentVarName)
      const initExpression: SlimeExpression = readGeneratedField(result, 'init') as SlimeExpression
      if (initExpression !== null && initExpression !== undefined && this.slimeNodeTypeName(initExpression) === 'CALL_EXPRESSION') {
        const callExpr: SlimeCallExpression = initExpression as SlimeCallExpression
        const calleeExpression: SlimeExpression = readGeneratedField(callExpr, 'callee') as SlimeExpression
        if (calleeExpression !== null && calleeExpression !== undefined && this.slimeNodeTypeName(calleeExpression) === 'MEMBER_EXPRESSION') {
          const memberExpr: SlimeMemberExpression = calleeExpression as SlimeMemberExpression
          const objectIdentifier: SlimeIdentifier = readGeneratedField(memberExpr, 'object') as SlimeIdentifier
          const propertyIdentifier: SlimeIdentifier = readGeneratedField(memberExpr, 'property') as SlimeIdentifier
          const objectName: string = readGeneratedField(objectIdentifier, 'name') as string
          const propertyName: string = readGeneratedField(propertyIdentifier, 'name') as string
          if (objectName === 'cssts' && propertyName === 'merge') {
            const groupUtilRef = this.createCsstsAtomMember(this.currentVarName)
            const callArgs = normalizeGeneratedAstList(readGeneratedField(callExpr, 'arguments')) as Array<SlimeExpression>
            const rebuiltCall = GeneratedSlimeAstCreateUtils.createCallExpression(
              calleeExpression,
              [groupUtilRef, ...callArgs],
              readGeneratedField(callExpr, 'optional'),
              readGeneratedField(callExpr, 'location')
            ) as SlimeCallExpression
            this.currentVarName = null
            return GeneratedSlimeAstCreateUtils.createVariableDeclarator(
              readGeneratedField(result, 'id'),
              rebuiltCall,
              readGeneratedField(result, 'typeAnnotation'),
              readGeneratedField(result, 'location')
            ) as SlimeVariableDeclarator
          }
        }
      }
      this.currentVarName = null
    }
    return result
  }



  createPrimaryExpressionAst(cst: SubhutiCst): any {
    const cssExpression = this.findCssExpressionCst(cst)
    if (cssExpression !== undefined) return this.createCssExpressionAst(cssExpression)
    if (cst.getName() === "CssExpression") {
      return this.createCssExpressionAst(cst)
    }
    const first = cst.getChildren()?.[0]
    if (first !== undefined && first !== null && first.getName() === "CssExpression") {
      return this.createCssExpressionAst(first)
    }
    // 直接调用基类逻辑，不再进行拦截复制
    return super.createPrimaryExpressionAst(cst)
  }

  createExpressionAst(cst: SubhutiCst): any {
    const cssExpression = this.findCssExpressionCst(cst)
    if (cssExpression !== undefined) return this.createCssExpressionAst(cssExpression)
    return super.createExpressionAst(cst)
  }

  createExpressionAstUncached(cst: SubhutiCst): any {
    const cssExpression = this.findCssExpressionCst(cst)
    if (cssExpression !== undefined) return this.createCssExpressionAst(cssExpression)
    if (cst.getName() === "CssExpression") {
      return this.createCssExpressionAst(cst)
    }
    const first = cst.getChildren()?.[0]
    if (first !== undefined && first !== null && first.getName() === "CssExpression") {
      return this.createCssExpressionAst(first)
    }
    return super.createExpressionAstUncached(cst)
  }

  private findCssExpressionCst(cst: SubhutiCst | undefined): SubhutiCst | undefined {
    if (cst === undefined || cst === null) return undefined
    if (cst.getName() === 'CssExpression') return cst
    for (const child of this.cstChildren(cst)) {
      const found = this.findCssExpressionCst(child)
      if (found !== undefined) return found
    }
    return undefined
  }

  createCssExpressionAst(cst: SubhutiCst): any {
    this._hasCsstsSyntax = true
    const children = this.cstChildren(cst)
    let styleObjectCst: SubhutiCst | undefined = undefined
    for (const child of children) {
      if (child.getName() === 'CssStyleObject') {
        styleObjectCst = child
        break
      }
    }

    // 提取 css 关键字的位置
    let cssTokenCst: SubhutiCst | undefined = undefined
    for (const child of children) {
      if (child.getName() === 'Css' || child.getValue() === 'css') {
        cssTokenCst = child
        break
      }
    }
    const cssTokenLoc = cssTokenCst === undefined ? null : cssTokenCst.getLoc()

    if (styleObjectCst !== undefined) {
      // 提取 { 和 } 的位置
      let lBraceCst: SubhutiCst | undefined = undefined
      let rBraceCst: SubhutiCst | undefined = undefined
      for (const child of this.cstChildren(styleObjectCst)) {
        if (lBraceCst === undefined && (child.getName() === 'LBrace' || child.getValue() === '{')) lBraceCst = child
        if (rBraceCst === undefined && (child.getName() === 'RBrace' || child.getValue() === '}')) rBraceCst = child
      }
      const lBraceLoc = lBraceCst === undefined ? null : lBraceCst.getLoc()
      const rBraceLoc = rBraceCst === undefined ? null : rBraceCst.getLoc()

      const args = this.extractCssPropertyExpressions(styleObjectCst)
      const callExpr = this.createCsstsClsCallWithArgs(args, cst.getLoc(), cssTokenLoc, lBraceLoc, rBraceLoc)
      return callExpr
    }

    let identifierCount = 0
    let atomCst: SubhutiCst = cst
    for (const child of children) {
      if (child.getName() === 'IdentifierName') {
        if (identifierCount === 1) atomCst = child
        identifierCount++
      }
    }
    if (identifierCount >= 2) {
      const atomName = this.extractCstValue(atomCst)
      this.usedAtoms.add(atomName)
      return GeneratedSlimeAstCreateUtils.createStringLiteral(atomName, atomCst.getLoc(), atomName)
    }
    return GeneratedSlimeAstCreateUtils.createStringLiteral('', cst.getLoc(), '')
  }

  /**
   * 创建 cssts.merge(...) 调用
   *
   * @param args 参数列表
   * @param loc 整体位置
   * @param cssTokenLoc css keyword location for the generated cssts.merge mapping
   * @param lBraceLoc `{` location for the generated `(` mapping
   * @param rBraceLoc `}` location for the generated `)` mapping
   */
  protected createCsstsClsCallWithArgs(
    args: any[],
    loc?: any,
    cssTokenLoc?: SubhutiSourceLocation | null,
    lBraceLoc?: SubhutiSourceLocation | null,
    rBraceLoc?: SubhutiSourceLocation | null
  ): any {
    let callLoc = loc as SubhutiSourceLocation | null
    if (callLoc === undefined || callLoc === null) callLoc = cssTokenLoc ?? null
    if (callLoc === null) callLoc = lBraceLoc ?? null
    if (callLoc === null) callLoc = rBraceLoc ?? null
    let idLoc = cssTokenLoc ?? null
    if (idLoc === null) idLoc = callLoc
    const csstsId = GeneratedSlimeAstCreateUtils.createIdentifier('cssts', idLoc)
    const mergeId = GeneratedSlimeAstCreateUtils.createIdentifier('merge', idLoc)
    const callee = GeneratedSlimeAstCreateUtils.createMemberExpression(csstsId, mergeId, false, false, idLoc)
    return GeneratedSlimeAstCreateUtils.createCallExpression(callee, args, false, callLoc)
  }


  private extractCssPropertyExpressions(styleObjectCst: SubhutiCst | undefined): any[] {
    if (styleObjectCst === undefined || styleObjectCst === null) return []
    let elementListCst: SubhutiCst | undefined = undefined
    let atomListCst: SubhutiCst | undefined = undefined
    for (const child of this.cstChildren(styleObjectCst)) {
      if (elementListCst === undefined && child.getName() === 'ElementList') elementListCst = child
      if (atomListCst === undefined && child.getName() === 'CssAtomList') atomListCst = child
    }
    if (elementListCst === undefined) {
      return this.processCssAtomList(atomListCst)
    }
    const elements = this.processElementList(elementListCst)
    const transformed: any[] = []
    for (const expr of elements) {
      transformed.push(this.transformCssPropertyExpression(expr))
    }
    return transformed
  }

  private extractCstValue(cst: SubhutiCst | undefined): string {
    if (cst === undefined || cst === null) return ''
    const value = cst.getValue()
    if (value !== undefined && value !== null) return String(value)
    let result = ''
    for (const child of this.cstChildren(cst)) {
      result = result + this.extractCstValue(child)
    }
    return result
  }

  private processCssAtomList(cst: SubhutiCst | undefined): any[] {
    if (cst === undefined || cst === null) return []
    const expressions: any[] = []
    for (const child of this.cstChildren(cst)) {
      if (child.getName() === 'Comma' || child.getValue() === ',') {
        continue
      }
      if (child.getName() === 'IdentifierName') {
        const name = this.extractCstValue(child)
        if (name.length > 0 && this.isAtomName(name)) {
          this.usedAtoms.add(name)
          expressions.push(this.createCsstsAtomMember(name, child.getLoc()))
        } else {
          expressions.push(GeneratedSlimeAstCreateUtils.createIdentifier(name, child.getLoc()))
        }
      }
    }
    return expressions
  }

  /**
   * 处理 ElementList，提取表达式
   */
  private processElementList(cst: SubhutiCst): any[] {
    const children = this.cstChildren(cst)
    const expressions: any[] = []

    for (let i = 0; i < children.length; i++) {
      const child = children[i]
      const childName = child.getName()
      const childValue = child.getValue()

      // 跳过逗号分隔符
      if (childName === 'Comma' || childValue === ',') {
        continue
      }

      // 跳过 Elision
      if (childName === 'Elision') continue

      // 处理表达式
      if (childName === 'AssignmentExpression') {
        expressions.push(this.createAssignmentExpressionAst(child))
      } else if (childName === 'SpreadElement') {
        expressions.push(this.createSpreadElementAst(child) as any)
      }
    }
    return expressions
  }

  createSpreadElementAst(cst: SubhutiCst): any {
    let assignExprCst: SubhutiCst | undefined = undefined
    for (const child of this.cstChildren(cst)) {
      if (child.getName() === 'AssignmentExpression') {
        assignExprCst = child
        break
      }
    }
    if (assignExprCst === undefined) throw new Error('SpreadElement: missing AssignmentExpression')
    const argument = this.createAssignmentExpressionAst(assignExprCst)
    return { type: 'SpreadElement', argument, loc: cst.getLoc() }
  }

  /**
   * 转换 css { } 内部的表达式
   *
   * 规则：
   * - 标识符 + 是全局样式类 → csstsAtom.xxx
   * - 其他 → 保持原样
   */
  private transformCssPropertyExpression(expr: SlimeExpression): SlimeExpression {
    if (expr === undefined || expr === null) return expr

    // 标识符：判断是否是全局样式类
    if (this.slimeNodeTypeName(expr) === 'IDENTIFIER') {
      const identifierExpr = expr as SlimeIdentifier
      const name: string = readGeneratedField(identifierExpr, 'name') as string
      if (name.length > 0 && this.isAtomName(name)) {
        // 是全局样式类：转换为 csstsAtom.xxx
        // 保留原始标识符的 loc，用于 source map 映射
        this.usedAtoms.add(name)
        const result = this.createCsstsAtomMember(name, null)
        return result
      }
      // 不是样式类（变量引用）：保持原样
      return expr
    }

    // 逻辑表达式：递归处理两侧

    // 三元表达式：递归处理三个部分

    // 函数调用：递归处理参数
    // 其他：保持原样（字符串、展开等）
    return expr
  }

  /**
   * 创建 csstsAtom.xxx 成员表达式
   * @param propName 属性名（原子类名）
   * @param propLoc 原始标识符的位置信息，用于 source map 映射
   */
  protected createCsstsAtomMember(propName: string, propLoc?: SubhutiSourceLocation | null): any {
    const idLoc = propLoc ?? null
    const csstsAtomId = GeneratedSlimeAstCreateUtils.createIdentifier('csstsAtom', idLoc)
    const propId = GeneratedSlimeAstCreateUtils.createIdentifier(propName, idLoc)
    return GeneratedSlimeAstCreateUtils.createMemberExpression(csstsAtomId, propId, false, false, idLoc)
  }

  createAssignmentExpressionAst(cst: SubhutiCst): any {
    const ast = super.createAssignmentExpressionAst(cst)

    // 如果右侧是 css 语法，转换为带合并的 merge
    if (false) {
      return this.transformToCssMerge(ast)
    }

    return ast
  }

  /**
   * 转换为带合并的 merge 调用
   *
   * 输入：leftExpr = cssts.merge(a, b, c)
   * 输出：leftExpr = cssts.merge(leftExpr, a, b, c)
   *
   * 支持任意左侧表达式：
   * - style = css { } → style = merge(style, ...)
   * - obj.style = css { } → obj.style = merge(obj.style, ...)
   */
  private transformToCssMerge(ast: any): SlimeExpression {
    const leftExpr = ast as SlimeExpression
    const rightArgs: SlimeExpression[] = []

    // 复用 createCsstsClsCallWithArgs，将左侧表达式作为第一个参数
    const mergeCall = leftExpr

    return {
      type: 'AssignmentExpression',
      operator: '=',
      left: leftExpr,
      right: mergeCall,
      loc: undefined
    } as any
  }


}

// ==================== 全局注册机制 ====================
// Use an explicit facade so imports keep calling the currently registered instance.

/**
 * 注册 CssTsCstToAst 实例到全局
 *
 * 子类构造函数会自动调用此方法，所以会注册最终的子类实例
 * 父层（slime-parser）的注册已通过 super() 中的父类构造函数自动完成
 */
let _cssTsCstToAstUtils: CssTsCstToAst | null = null

function getCssTsCstToAstUtils(): CssTsCstToAst {
  if (_cssTsCstToAstUtils === null) {
    registerCssTsCstToAst(new CssTsCstToAst())
  }
  return _cssTsCstToAstUtils
}

export function registerCssTsCstToAst(instance: CssTsCstToAst): void {
  _cssTsCstToAstUtils = instance
  registerSlimeCstToAstUtil(instance)
}

export const CssTsCstToAstUtils = {
  toFileAst(cst: SubhutiCst): SlimeProgram {
    return getCssTsCstToAstUtils().toFileAst(cst)
  },

  getUsedAtoms(): Set<string> {
    return getCssTsCstToAstUtils().getUsedAtoms()
  },

  clearUsedAtoms(): void {
    getCssTsCstToAstUtils().clearUsedAtoms()
  },

  getCssStyles(): Map<string, CssStyleInfo> {
    return getCssTsCstToAstUtils().getCssStyles()
  },

  get hasCsstsSyntax(): boolean {
    return getCssTsCstToAstUtils().hasCsstsSyntax
  },
}
