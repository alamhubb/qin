/**
 * CSS 类名生成工具
 *
 * 提供原子类名到 CSS 类名的转换
 */

import { ConfigLookup } from "../config/ConfigLookup"
import { RuntimeStore, type RuntimeAtomData } from "../store/RuntimeStore"
import { CSSTS_CONFIG } from "cssts-ts"
import { CSS_PROPERTY_NAME_MAP } from "../data/cssPropertyNameMapping"

// 重新导出分隔符配置（供其他模块使用）
export { CSSTS_CONFIG }

// 从生成器获取 properties 映射（懒加载）
let _properties: Record<string, string> | null = null
let _sortedPropertyNames: string[] | null = null

const KEYWORD_VALUES = new Set([
  'auto',
  'block',
  'blue',
  'bold',
  'center',
  'column',
  'flex',
  'grid',
  'hidden',
  'inline',
  'none',
  'normal',
  'red',
  'relative',
  'solid',
  'white'
])

/**
 * 判断标识符是否是内置原子类
 *
 * 使用 CsstsInit 的白名单判断
 *
 * @example
 * isBuiltinAtom('displayFlex') // true
 * isBuiltinAtom('myVariable') // false
 */
export function isBuiltinAtom(name: string): boolean {
  return RuntimeStore.isValidAtomName(name) || parseTsAtomName(name) !== null
}

export function ensureRuntimeAtomData(name: string): RuntimeAtomData | undefined {
  const existing = RuntimeStore.getRuntimeData(name)
  if (existing) return existing
  const parsed = parseTsAtomName(name)
  if (!parsed) return undefined
  const data: RuntimeAtomData = {
    group: 'atom',
    property: parsed.property,
    value: parsed.value
  }
  RuntimeStore.setRuntimeData(name, data)
  return data
}

function getProperties(): Record<string, string> {
  if (!_properties) {
    _properties = { ...CSS_PROPERTY_NAME_MAP } as Record<string, string>
  }
  return _properties
}

function getSortedPropertyNames(): string[] {
  if (!_sortedPropertyNames) {
    const props = getProperties()
    _sortedPropertyNames = Object.keys(props).sort((a, b) => b.length - a.length)
  }
  return _sortedPropertyNames
}

// 符号转义映射（用于 CSS 类名）
const symbolToEscape: Record<string, string> = {
  '.': '\\.',
  '%': '\\%',
  '/': '\\/',
}

/**
 * 驼峰转 kebab-case
 */
export function camelToKebab(str: string): string {
  return str
    .replace(/([a-z])([A-Z])/g, '$1-$2')
    .replace(/([a-zA-Z])(\d)/g, '$1-$2')
    .toLowerCase()
}

/**
 * TS 值转 CSS 值
 */
function tsValueToCSS(tsValue: string): string {
  let result = tsValue
  // 负数处理：N 前缀转为 -
  if (result.startsWith('N') && result.length > 1 && /[0-9]/.test(result[1])) {
    result = '-' + result.slice(1)
  }
  // 特殊字符替换
  result = result.replace(/pct/g, '%')
  result = result.replace(/(\d)p(\d)/g, '$1.$2')
  result = result.replace(/(\d)s(\d)/g, '$1/$2')
  return camelToKebab(result)
}

/**
 * 解析 TS 原子类名为 CSS 属性和值
 *
 * @example
 * parseTsAtomName('displayFlex') // { property: 'display', value: 'flex' }
 * parseTsAtomName('paddingTop16px') // { property: 'padding-top', value: '16px' }
 */
export function parseTsAtomName(tsName: string): { property: string; value: string } | null {
  const properties = getProperties()
  const sortedNames = getSortedPropertyNames()

  for (const propName of sortedNames) {
    if (tsName.startsWith(propName) && tsName.length > propName.length) {
      const valuePart = tsName.slice(propName.length)
      if (/^[A-Z0-9]/.test(valuePart) || /^N[0-9]/.test(valuePart)) {
        const value = tsValueToCSS(valuePart)
        if (
          /^[+-]?\d/.test(value)
          || KEYWORD_VALUES.has(value)
          || value.includes('-')
          || value.includes('%')
          || value.includes('/')
        ) {
          return { property: properties[propName], value }
        }
      }
    }
  }
  return null
}

/**
 * 获取原子类的 CSS 属性名
 *
 * @example
 * getCssProperty('colorRed') // 'color'
 * getCssProperty('paddingTop16px') // 'padding-top'
 */
export function getCssProperty(atomName: string): string | undefined {
  return parseTsAtomName(atomName)?.property
}

/**
 * 获取原子类的 CSS 值
 *
 * @example
 * getCssValue('colorRed') // 'red'
 * getCssValue('paddingTop16px') // '16px'
 */
export function getCssValue(atomName: string): string | undefined {
  const className = getCssClassName(atomName)
  const idx = className.indexOf(CSSTS_CONFIG.SEPARATOR)
  if (idx > 0) {
    return className.slice(idx + 1)
      .replace(/\\\./g, '.')
      .replace(/\\%/g, '%')
      .replace(/\\\//g, '/')
  }
  return undefined
}

/**
 * 生成 CSS 类名
 *
 * @example
 * getCssClassName('displayFlex') // 'display_flex'
 * getCssClassName('colorRed') // 'color_red'
 * getCssClassName('padding16px') // 'padding_16px'
 */
export function getCssClassName(atomName: string): string {
  const parsed = parseTsAtomName(atomName)
  if (parsed) {
    let escapedValue = parsed.value
    for (const [symbol, escaped] of Object.entries(symbolToEscape)) {
      escapedValue = escapedValue.split(symbol).join(escaped)
    }
    return `${parsed.property}${CSSTS_CONFIG.SEPARATOR}${escapedValue}`
  }
  // 回退：简单的 kebab-case
  return camelToKebab(atomName)
}

/**
 * 生成单条 CSS 规则
 *
 * @example
 * generateAtomCssRule('displayFlex', 'cssts_') // '.cssts_display_flex { display: flex; }'
 */
export function generateAtomCssRule(atomName: string, prefix: string): string | null {
  const className = getCssClassName(atomName)
  const property = getCssProperty(atomName)
  const value = getCssValue(atomName)
  if (!property || !value) return null
  const fullClassName = `${prefix}${className}`
  return `.${fullClassName} { ${property}: ${value}; }`
}
