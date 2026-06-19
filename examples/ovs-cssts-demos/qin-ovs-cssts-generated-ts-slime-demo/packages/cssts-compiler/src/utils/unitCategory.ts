/**
 * CSS 单位分类查询工具
 *
 * 基于 data/ 中的数据提供单位和分类的查询功能
 */

import { ALL_NUMBER_CATEGORIES, CATEGORY_UNITS_MAP } from '../data/cssNumberData';
import { PROPERTY_CATEGORIES_MAP } from '../data/cssPropertyNumber';

// 类型定义
export type UnitCategoryName = typeof ALL_NUMBER_CATEGORIES[number];

// 构建反向映射：单位 -> 分类
const CATEGORY_BY_UNIT: Record<string, UnitCategoryName> = {};
for (const [category, units] of Object.entries(CATEGORY_UNITS_MAP)) {
  for (const unit of units) {
    CATEGORY_BY_UNIT[unit] = category as UnitCategoryName;
  }
}

// ==================== 单位分类查询 ====================

/** 获取单位所属分类 */
export function getUnitCategory(unit: string): UnitCategoryName | undefined {
  return CATEGORY_BY_UNIT[unit];
}

/** 获取分类下的所有单位 */
export function getUnitsInCategory(category: UnitCategoryName): readonly string[] {
  return CATEGORY_UNITS_MAP[category] || [];
}

/** 判断单位是否属于某分类 */
export function isUnitInCategory(unit: string, category: UnitCategoryName): boolean {
  return CATEGORY_BY_UNIT[unit] === category;
}

// ==================== Category 链式查询 ====================

/**
 * 根据 unitCategories 获取对应的 units
 *
 * @example
 * getUnitsFromCategories(['pixel', 'fontRelative'])
 * // => ['px', 'em', 'rem', 'ch', 'ex', 'cap', 'ic', 'lh', 'rlh']
 */
export function getUnitsFromCategories(categories: UnitCategoryName[]): string[] {
  const units = new Set<string>();
  for (const cat of categories) {
    const catUnits = CATEGORY_UNITS_MAP[cat];
    if (catUnits) catUnits.forEach((u: string) => units.add(u));
  }
  return Array.from(units);
}

/**
 * 获取属性支持的所有 categories
 *
 * @example
 * getPropertyCategories('width')
 * // => ['fontRelative', 'percentage', 'physical', 'pixel']
 */
export function getPropertyCategories(propertyName: string): UnitCategoryName[] {
  const categories = PROPERTY_CATEGORIES_MAP[propertyName as keyof typeof PROPERTY_CATEGORIES_MAP];
  return categories ? [...categories] : [];
}

/**
 * 获取属性支持的所有 units
 *
 * @example
 * getPropertyUnits('width')
 * // => ['px', 'em', 'rem', '%', 'vw', 'vh', ...]
 */
export function getPropertyUnits(propertyName: string): string[] {
  const categories = getPropertyCategories(propertyName);
  return getUnitsFromCategories(categories);
}
