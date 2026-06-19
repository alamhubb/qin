// Parser
export { CssTsParser, CssTsTokenConsumer, cssTsTokens, CssTsContextualKeywordTypes } from './parser'

// 统一初始化
export { CsstsInit, type RuntimeAtomData } from './init/CsstsInit'

// 运行时存储
export { RuntimeStore } from './store/RuntimeStore'

// AST Transformer
export { CssTsCstToAstUtils, CssTsCstToAst } from './factory'

// Transform（核心转换功能）
export {
  type ParsedStyleInfo,
  type TransformContext,
  type TransformResult,
  type TransformResultWithMapping,
  parseStyleName,
  hasPseudos,
  transformCssTs,
  generateStylesCss,
  generateCsstsAtomModule,
} from './transform'


/** CSS 属性值映射类型 */
export type CssPropertyValueMap = Record<string, string | undefined>

/**伪类工具配置类型 */
export type PseudoUtilsConfig = Record<string, CssPropertyValueMap>

// CSS class name utilities
export {
  CSSTS_CONFIG,
  camelToKebab,
  parseTsAtomName,
  getCssProperty,
  getCssValue,
  getCssClassName,
  generateAtomCssRule,
} from './utils/cssClassName.ts'

// ==================== 配置 ====================
export { csstsDefaultConfig, csstsDefaultConfig as defaultConfig } from './config/CsstsDefaultConfig.ts'
export { ConfigLookup } from './config/ConfigLookup.ts'

// ==================== 配置工具函数 ====================
export {
  shouldInclude,
  getEffectiveList,
  normalizeUnitsConfig,
  normalizeUnitCategoriesConfig,
} from './utils/config-utils.ts'

// ==================== DTS 生成器 ====================
export {
  generateAtoms,
  generateDts,
  generateStats,
  generateAtomsByProperty,
  generatePropertyDts,
  generateIndexDts,
  generatePseudoAtoms,
  generatePseudoDts,
  generateClassGroupAtoms,
  generateClassGroupDts,
  type AtomDefinition,
  type AtomsByProperty,
  type PseudoAtomDefinition,
  type ClassGroupAtomDefinition,
} from './dts/atom-generator.ts'

export {
  generateDtsFiles,
  generateModulesDts,
  writeAtomUsedDts,
  type DtsGenerateResult,
} from './dts/dts-writer.ts'


// ==================== 类型导出 ====================
export type {
  CsstsConfig,
  CsstsConfigRequired,
  CsstsCompilerConfig,
  CsstsCompilerConfigRequired,
} from './config/types/csstsConfig.d.ts'

export type {
  CssProgressiveRange,
  CssStepConfig,
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
} from './config/types/cssPropertyConfig.d.ts'

// ==================== 数据导出 ====================
export { CSS_PROPERTY_NAME_MAP } from './data/cssPropertyNameMapping.ts'
export { KEYWORD_NAME_MAP, CSS_GLOBAL_KEYWORDS } from './data/cssKeywordsData.ts'
export { COLOR_NAME_MAP, COLOR_TYPE_COLORS_MAP, ALL_COLOR_TYPES } from './data/cssColorData.ts'
export { PSEUDO_CLASS_NAME_MAP, PSEUDO_ELEMENT_NAME_MAP } from './data/cssPseudoData.ts'
export { ALL_UNITS, ALL_NUMBER_CATEGORIES, CATEGORY_UNITS_MAP } from './data/cssNumberData.ts'
export { PROPERTY_CATEGORIES_MAP } from './data/cssPropertyNumber.ts'
export { PROPERTY_KEYWORDS_MAP } from './data/cssPropertyKeywords.ts'
export { PROPERTY_COLOR_TYPES_MAP } from './data/cssPropertyColorTypes.ts'
export { PROPERTY_PARENT_MAP } from './data/cssPropertyInheritance.ts'

// ==================== 工具函数 ====================
export {
  getUnitCategory,
  getUnitsInCategory,
  isUnitInCategory,
  getUnitsFromCategories,
  getPropertyCategories,
  getPropertyUnits,
} from './utils/unitCategory.ts'
