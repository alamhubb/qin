/**
 * CSSTS 配置类型定义（手动维护）
 *
 * 配置层次：
 * - CsstsConfig: 核心业务配置，控制"生成什么原子类"
 * - CsstsCompilerConfig: 编译器配置，继承业务配置 + 添加构建配置
 */

import type {
  CssProgressiveRange,
  CssCustomPropertyValue,
  CssNumberUnitName,
  CssNumberUnitConfig,
  CssNumberCategoryName,
  CssNumberCategoryConfig,
  CssKeywordName,
  CssColorTypeName,
  CssColorTypeConfig,
  CssColorName,
  CssPropertyName,
  CssPropertyConfig,
  CssPseudoClassName,
  CssPseudoElementName,
  CssPseudoClassConfig,
  CssPseudoElementConfig,
  GroupConfig
} from './cssPropertyConfig';

// ==================== 核心业务配置 ====================

/**
 * CSSTS 核心业务配置
 *
 * 控制"生成什么原子类"：属性、颜色、数值范围、伪类等
 */
export interface CsstsConfig {
  // ==================== 属性配置 ====================

  /**
   * 支持的 CSS 属性列表（属性名数组）
   * 用于指定生成哪些属性的原子类
   * @example ['width', 'height', 'margin', 'padding']
   */
  properties?: CssPropertyName[];

  /**
   * 排除的 CSS 属性列表（属性名数组）
   * 从 properties 中排除指定的属性
   * @example ['display', 'position']
   */
  excludeProperties?: CssPropertyName[];

  /**
   * 特定属性的详细配置
   * 用于覆盖特定属性的数值范围、步长、预设值等
   * @example [{ zIndex: { unitless: { max: 9999, presets: [-1, 999] } } }]
   */
  propertiesConfig?: CssPropertyConfig[];

  // ==================== 数值配置 ====================

  /**
   * 支持的数值类别列表（类别名数组）
   * @example ['pixel', 'fontRelative', 'percentage']
   */
  numberCategories?: CssNumberCategoryName[];

  /**
   * 排除的数值类别列表（类别名数组）
   * @example ['physical', 'frequency', 'resolution']
   */
  excludeNumberCategories?: CssNumberCategoryName[];

  /**
   * 数值类别的详细配置
   * 用于配置特定类别的数值范围、步长、预设值等
   * @example [{ pixel: { min: 0, max: 1000, step: 1 } }]
   */
  numberCategoriesConfig?: CssNumberCategoryConfig[];

  /**
   * 支持的数值单位列表（单位名数组）
   * @example ['px', 'em', 'rem', 'vw', 'vh']
   */
  numberUnits?: CssNumberUnitName[];

  /**
   * 排除的数值单位列表（单位名数组）
   * @example ['cm', 'mm', 'in', 'pt', 'pc']
   */
  excludeUnits?: CssNumberUnitName[];

  /**
   * 数值单位的详细配置
   * 用于配置特定单位的数值范围、步长、预设值等
   * @example [{ px: { min: 0, max: 1000, step: 1 } }]
   */
  numberUnitsConfig?: CssNumberUnitConfig[];

  /** 渐进步长范围 */
  progressiveRanges?: CssProgressiveRange[];

  // ==================== 关键字配置 ====================

  /** 关键字 */
  keywords?: CssKeywordName[];

  /** 排除的关键字 */
  excludeKeywords?: CssKeywordName[];

  // ==================== 颜色配置 ====================

  /**
   * 支持的颜色类型列表（类型名数组）
   * @example ['namedColor', 'systemColor']
   */
  colorTypes?: CssColorTypeName[];

  /**
   * 排除的颜色类型列表（类型名数组）
   * @example ['deprecatedSystemColor', 'nonStandardColor']
   */
  excludeColorTypes?: CssColorTypeName[];

  /**
   * 颜色类型的详细配置
   * 用于配置特定颜色类型包含哪些颜色
   * @example [{ namedColor: ['red', 'blue', 'green'] }]
   */
  colorTypesConfig?: CssColorTypeConfig[];

  /** 颜色 */
  colors?: CssColorName[];

  /** 排除的颜色 */
  excludeColors?: CssColorName[];

  // ==================== 伪类/伪元素配置 ====================

  /** 伪类 */
  pseudoClasses?: CssPseudoClassName[];

  /** 排除的伪类 */
  excludePseudoClasses?: CssPseudoClassName[];

  /** 伪元素 */
  pseudoElements?: CssPseudoElementName[];

  /** 排除的伪元素 */
  excludePseudoElements?: CssPseudoElementName[];

  /** 伪类样式配置 */
  pseudoClassConfig?: CssPseudoClassConfig;

  /** 伪元素样式配置 */
  pseudoElementsConfig?: CssPseudoElementConfig;

  // ==================== 组合配置 ====================

  /**
   * 组合原子类配置
   * 类名生成规则：prefix + name + [自动生成] + suffix
   * 支持三种类型：
   * 1. numberProperties: 数值组合
   * 2. keywordProperties: 固定关键字组合
   * 3. keywordIterations: 遍历关键字生成多个原子类
   * @example
   * groups: [
   *   { name: 'marginX', numberProperties: ['marginLeft', 'marginRight'] },
   *   { name: 'flexRowCenter', keywordProperties: { display: 'flex', flexDirection: 'row', justifyContent: 'center' } },
   *   { prefix: 'flex', keywordIterations: { flexDirection: ['row', 'column'] } }
   * ]
   */
  groups?: GroupConfig[];

  /** 自定义属性 */
  customProperties?: Record<string, CssCustomPropertyValue>;

  // ==================== 输出配置 ====================

  /**
   * CSS 类名前缀
   * @default 'cssts_'
   * @example 'my-' => .my-display-flex
   */
  classPrefix?: string;

  /**
   * 类组合配置
   * 将多个原子类（包括伪类）组合成一个新类
   * @example { click: ['hover', 'active', 'cursorPointer'] }
   */
  classGroup?: Record<string, string[]>;
}

// ==================== 编译器配置 ====================

/**
 * CSSTS 编译器配置
 *
 * 继承核心业务配置，添加构建相关配置
 */
export interface CsstsCompilerConfig extends CsstsConfig {
  // ==================== 类型定义生成配置 ====================

  /**
   * 是否生成类型声明文件
   * @default true
   */
  dts?: boolean;

  /**
   * 类型声明文件输出目录
   * @default 'node_modules/@types/cssts-ts'
   */
  dtsOutputDir?: string;

  /**
   * 是否将类型声明文件拆分为多个文件
   * @default false
   */
  dtsSplitFiles?: boolean;

  // ==================== 调试配置 ====================

  /**
   * 是否开启调试模式（打印详细日志）
   * @default false
   */
  debug?: boolean;

  // ==================== 运行时状态配置 ====================

  /**
   * 使用过的原子类集合
   *
   * 用于收集转换过程中使用的原子类名称。
   * - 如果传入，则使用传入的 Set（如 Vite 的 globalStyles）
   * - 如果不传，则使用 RuntimeStore 的内部 Set
   *
   * @example
   * const myStyles = new Set<string>()
   * cssTsPlugin({ usedStyles: myStyles })
   */
  usedStyles?: Set<string>;
}

// ==================== 辅助类型 ====================

export type CsstsConfigRequired = Required<CsstsConfig>;
export type CsstsCompilerConfigRequired = Required<CsstsCompilerConfig>;
