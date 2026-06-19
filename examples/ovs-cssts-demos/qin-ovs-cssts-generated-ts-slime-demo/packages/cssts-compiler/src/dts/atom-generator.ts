/**
 * 原子类生成器
 *
 * 根据配置生成 CSS 原子类定义，用于 DTS 类型生成
 */

import { ConfigLookup } from '../config/ConfigLookup';
import { PROPERTY_CATEGORIES_MAP } from '../data/cssPropertyNumber';
import { CATEGORY_UNITS_MAP, ALL_NUMBER_CATEGORIES } from '../data/cssNumberData';
import { PROPERTY_KEYWORDS_MAP } from '../data/cssPropertyKeywords';
import { CSS_PROPERTY_NAME_MAP } from '../data/cssPropertyNameMapping';
import { PROPERTY_PARENT_MAP } from '../data/cssPropertyInheritance';
import { PROPERTY_COLOR_TYPES_MAP } from '../data/cssPropertyColorTypes';
import { COLOR_TYPE_COLORS_MAP, ALL_COLOR_TYPES } from '../data/cssColorData';
import type { CssStepConfig, CssProgressiveRange, CssNumberCategoryName, CssPropertyName, CssColorTypeName, CssColorName, GroupConfig, NumberPropertyName, KeywordIterationPropertyConfig } from '../config/types/cssPropertyConfig';

// 所有 CSS 属性名列表
const ALL_PROPERTY_NAMES = Object.values(CSS_PROPERTY_NAME_MAP) as CssPropertyName[];

// 构建 unit → category 的反向映射
const UNIT_TO_CATEGORY_MAP: Record<string, string> = {};
for (const [category, units] of Object.entries(CATEGORY_UNITS_MAP)) {
  for (const unit of units) {
    UNIT_TO_CATEGORY_MAP[unit] = category;
  }
}

// ==================== 类型定义 ====================

/** 生成的原子类定义 */
export interface AtomDefinition {
  /** 原子类名称 (camelCase) */
  name: string;
  /** CSS 属性名 (kebab-case) */
  property: string;
  /** CSS 值 */
  value: string;
  /** 单位 (可选) */
  unit?: string;
  /** 数值 (可选) */
  number?: number;
}

/** Group 原子类定义（多属性组合） */
export interface GroupAtomDefinition {
  /** 原子类名称 */
  name: string;
  /** CSS 属性和值的映射 */
  styles: Record<string, string>;
  /** 是否是数值类型 group */
  isNumber?: boolean;
}

// ==================== 工具函数 ====================

/** camelCase 转 kebab-case */
function camelToKebab(str: string): string {
  return str.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase();
}

/** 获取有效的属性列表 */
function getEffectiveProperties(): CssPropertyName[] {
  const properties = ConfigLookup.properties;
  const excludeProperties = ConfigLookup.excludeProperties ?? [];

  if (properties && properties.length > 0) {
    return properties;
  }

  if (excludeProperties.length > 0) {
    return ALL_PROPERTY_NAMES.filter(p => !excludeProperties.includes(p));
  }

  return ALL_PROPERTY_NAMES;
}

/** 获取有效的数值类别列表 */
function getEffectiveCategories(): CssNumberCategoryName[] {
  const categories = ConfigLookup.numberCategories ?? [...ALL_NUMBER_CATEGORIES];
  const excludeCategories = ConfigLookup.excludeNumberCategories ?? [];

  return categories.filter((c: CssNumberCategoryName) => !excludeCategories.includes(c)) as CssNumberCategoryName[];
}

/** 获取有效的颜色列表 */
function getEffectiveColors(propertyColorTypes: readonly string[]): string[] {
  if (ConfigLookup.colors && ConfigLookup.colors.length > 0) {
    const excludeColors = ConfigLookup.excludeColors ?? [];
    return ConfigLookup.colors.filter((c: CssColorName) => !excludeColors.includes(c));
  }

  const colors = new Set<string>();
  const excludeColorTypes = ConfigLookup.excludeColorTypes ?? [];

  if (!ConfigLookup.colorTypes || ConfigLookup.colorTypes.length === 0) {
    for (const colorType of ALL_COLOR_TYPES) {
      if (excludeColorTypes.includes(colorType)) continue;
      if (!propertyColorTypes.includes(colorType)) continue;

      const typeColors = COLOR_TYPE_COLORS_MAP[colorType as keyof typeof COLOR_TYPE_COLORS_MAP];
      if (typeColors) {
        for (const color of typeColors) {
          colors.add(color);
        }
      }
    }
  } else {
    for (const item of ConfigLookup.colorTypes) {
      if (typeof item === 'string') {
        const colorType = item as CssColorTypeName;
        if (excludeColorTypes.includes(colorType)) continue;
        if (!propertyColorTypes.includes(colorType)) continue;

        const typeColors = COLOR_TYPE_COLORS_MAP[colorType as keyof typeof COLOR_TYPE_COLORS_MAP];
        if (typeColors) {
          for (const color of typeColors) {
            colors.add(color);
          }
        }
      } else {
        for (const [colorType, colorList] of Object.entries(item)) {
          if (excludeColorTypes.includes(colorType as CssColorTypeName)) continue;
          if (!propertyColorTypes.includes(colorType)) continue;

          if (Array.isArray(colorList)) {
            for (const color of colorList) {
              colors.add(color);
            }
          }
        }
      }
    }
  }

  const excludeColors = ConfigLookup.excludeColors ?? [];
  return Array.from(colors).filter(c => !excludeColors.includes(c as any));
}

/** 从属性配置中查找 category 或 unit 级别的配置 */
function findCategoryOrUnitConfig(
  propertyConfig: any,
  categoryName: string
): CssStepConfig | undefined {
  if (!propertyConfig) return undefined;

  const categoryUnits = CATEGORY_UNITS_MAP[categoryName as keyof typeof CATEGORY_UNITS_MAP];
  if (categoryUnits) {
    for (const unit of categoryUnits) {
      if (unit in propertyConfig) {
        return propertyConfig[unit];
      }
    }
  }

  if (categoryName in propertyConfig) {
    return propertyConfig[categoryName];
  }

  return undefined;
}

/** 获取属性的 category 配置（使用 ConfigLookup 静态方法） */
function getPropertyCategoryConfig(
  propertyName: string,
  categoryName: string
): CssStepConfig | undefined {
  // 1. 先从属性配置查找
  const propertyConfig = ConfigLookup.getPropertyConfig(propertyName);
  const propResult = findCategoryOrUnitConfig(propertyConfig, categoryName);
  if (propResult) return propResult;

  // 2. 查找父属性配置
  const parentProperty = PROPERTY_PARENT_MAP[propertyName];
  if (parentProperty) {
    const parentConfig = ConfigLookup.getPropertyConfig(parentProperty);
    const parentResult = findCategoryOrUnitConfig(parentConfig, categoryName);
    if (parentResult) return parentResult;
  }

  // 3. 从 category 全局配置查找
  return ConfigLookup.getCategoryConfig(categoryName);
}

/** 根据步长配置生成数值列表 */
function generateNumbers(stepConfig: CssStepConfig, progressiveRanges?: CssProgressiveRange[]): number[] {
  const { min = 0, max = 100, step, presets = [] } = stepConfig;
  const numbers = new Set<number>();

  presets.forEach(p => {
    if (p >= min && p <= max) {
      numbers.add(p);
    }
  });

  if (step === undefined) {
    if (progressiveRanges) {
      generateFromProgressiveRanges(numbers, min, max, progressiveRanges);
    } else {
      generateFromStep(numbers, min, max, 1);
    }
  } else if (typeof step === 'number') {
    generateFromStep(numbers, min, max, step);
  } else if (Array.isArray(step)) {
    if (step.length > 0 && typeof step[0] === 'number') {
      generateFromMultipleSteps(numbers, min, max, step as number[]);
    } else {
      generateFromProgressiveRanges(numbers, min, max, step as CssProgressiveRange[]);
    }
  }

  return Array.from(numbers).sort((a, b) => a - b);
}

/** 从单一步长生成数值 */
function generateFromStep(numbers: Set<number>, min: number, max: number, step: number): void {
  if (min <= 0 && max >= 0) {
    numbers.add(0);
  }

  for (let i = step; i <= max; i += step) {
    if (i >= min) {
      numbers.add(roundNumber(i, step));
    }
  }

  for (let i = -step; i >= min; i -= step) {
    if (i <= max) {
      numbers.add(roundNumber(i, step));
    }
  }
}

/** 从多个步长值生成数值 */
function generateFromMultipleSteps(numbers: Set<number>, min: number, max: number, steps: number[]): void {
  if (min <= 0 && max >= 0) {
    numbers.add(0);
  }

  const minStep = Math.min(...steps);

  for (let i = minStep; i <= max; i += minStep) {
    if (i >= min && steps.some(s => i % s === 0 || Math.abs(i % s) < 0.0001)) {
      numbers.add(roundNumber(i, minStep));
    }
  }

  for (let i = -minStep; i >= min; i -= minStep) {
    if (i <= max && steps.some(s => Math.abs(i) % s === 0 || Math.abs(Math.abs(i) % s) < 0.0001)) {
      numbers.add(roundNumber(i, minStep));
    }
  }
}

/** 从渐进步长范围生成数值 */
function generateFromProgressiveRanges(
  numbers: Set<number>,
  min: number,
  max: number,
  ranges: CssProgressiveRange[]
): void {
  if (min <= 0 && max >= 0) {
    numbers.add(0);
  }

  let current = 1;
  for (const range of ranges) {
    const rangeMax = Math.min(range.max, max);
    while (current <= rangeMax && current >= min) {
      if (range.divisors.some(d => current % d === 0)) {
        numbers.add(current);
      }
      current++;
    }
    if (current > max) break;
  }

  current = -1;
  for (const range of ranges) {
    const rangeMin = Math.max(-range.max, min);
    while (current >= rangeMin && current <= max) {
      if (range.divisors.some(d => Math.abs(current) % d === 0)) {
        numbers.add(current);
      }
      current--;
    }
    if (current < min) break;
  }
}

/** 四舍五入到指定精度 */
function roundNumber(num: number, step: number): number {
  const precision = getPrecision(step);
  return Number(num.toFixed(precision));
}

/** 获取数字的小数位数 */
function getPrecision(num: number): number {
  const str = num.toString();
  const dotIndex = str.indexOf('.');
  return dotIndex === -1 ? 0 : str.length - dotIndex - 1;
}

/** 格式化数值为类名部分 */
function formatNumberForClassName(num: number): string {
  if (num < 0) {
    return `N${formatPositiveNumber(Math.abs(num))}`;
  }
  return formatPositiveNumber(num);
}

/** 格式化正数为类名部分 */
function formatPositiveNumber(num: number): string {
  return num.toString().replace('.', 'p');
}

/** 格式化单位为类名部分 */
function formatUnitForClassName(unit: string): string {
  if (unit === 'unitless' || unit === '') return '';
  if (unit === 'percent') return 'pct';
  return unit;
}

/** 格式化 keyword 为类名部分 (kebab-case → PascalCase) */
function formatKeywordForClassName(keyword: string): string {
  const normalized = keyword.startsWith('-') ? keyword.slice(1) : keyword;

  return normalized
    .split('-')
    .map(part => part.charAt(0).toUpperCase() + part.slice(1))
    .join('');
}

/** 格式化颜色名为类名部分 */
function formatColorForClassName(color: string): string {
  return color
    .split('-')
    .map(part => part.charAt(0).toUpperCase() + part.slice(1))
    .join('');
}


// ==================== 主生成函数 ====================

/** 为单个属性生成原子类定义 */
function generateAtomsForProperty(
  propertyName: CssPropertyName,
  effectiveCategories: CssNumberCategoryName[]
): AtomDefinition[] {
  const atoms: AtomDefinition[] = [];
  const seenNames = new Set<string>();
  const kebabProperty = camelToKebab(propertyName);
  const excludeKeywords = ConfigLookup.excludeKeywords ?? [];

  // 1. 生成 keyword 原子类
  const keywords = PROPERTY_KEYWORDS_MAP[propertyName as keyof typeof PROPERTY_KEYWORDS_MAP];
  if (keywords) {
    for (const keyword of keywords) {
      const camelKeyword = formatKeywordForClassName(keyword).charAt(0).toLowerCase() + formatKeywordForClassName(keyword).slice(1);
      if (excludeKeywords.includes(camelKeyword as any)) continue;

      const atomName = `${propertyName}${formatKeywordForClassName(keyword)}`;

      if (seenNames.has(atomName)) continue;
      seenNames.add(atomName);

      atoms.push({
        name: atomName,
        property: kebabProperty,
        value: keyword,
      });
    }
  }

  // 2. colors 和 numberTypes 互斥
  const propertyColorTypes = PROPERTY_COLOR_TYPES_MAP[propertyName as keyof typeof PROPERTY_COLOR_TYPES_MAP];
  const hasColors = propertyColorTypes && propertyColorTypes.length > 0;

  if (hasColors) {
    const effectiveColors = getEffectiveColors(propertyColorTypes);

    for (const colorName of effectiveColors) {
      const atomName = `${propertyName}${formatColorForClassName(colorName)}`;

      if (seenNames.has(atomName)) continue;
      seenNames.add(atomName);

      atoms.push({
        name: atomName,
        property: kebabProperty,
        value: colorName,
      });
    }
  } else {
    const propertyCategories = PROPERTY_CATEGORIES_MAP[propertyName as keyof typeof PROPERTY_CATEGORIES_MAP];
    if (propertyCategories) {
      const validCategories = propertyCategories.filter(c =>
        effectiveCategories.includes(c as CssNumberCategoryName)
      );

      for (const category of validCategories) {
        const categoryConfig = getPropertyCategoryConfig(propertyName, category);
        const stepConfig: CssStepConfig = categoryConfig ?? { min: 0, max: 100 };

        let units = CATEGORY_UNITS_MAP[category as keyof typeof CATEGORY_UNITS_MAP];
        if (!units) continue;

        if (stepConfig.units && stepConfig.units.length > 0) {
          const filteredUnits = units.filter(u => stepConfig.units!.includes(u as any));
          if (filteredUnits.length === 0) continue;
          units = filteredUnits as unknown as typeof units;
        }

        const numbers = generateNumbers(stepConfig, ConfigLookup.progressiveRanges);

        for (const unit of units) {
          for (const num of numbers) {
            const numStr = formatNumberForClassName(num);

            let atomName: string;
            let cssValue: string;

            if (num === 0) {
              atomName = `${propertyName}0`;
              cssValue = '0';
            } else {
              const unitSuffix = formatUnitForClassName(unit);
              atomName = `${propertyName}${numStr}${unitSuffix}`;

              if (unit === 'unitless') {
                cssValue = num.toString();
              } else if (unit === 'percent') {
                cssValue = `${num}%`;
              } else {
                cssValue = `${num}${unit}`;
              }
            }

            if (seenNames.has(atomName)) continue;
            seenNames.add(atomName);

            atoms.push({
              name: atomName,
              property: kebabProperty,
              value: cssValue,
              unit: num === 0 ? undefined : (unit === 'unitless' ? undefined : unit),
              number: num,
            });
          }
        }
      }
    }
  }

  return atoms;
}

/** 生成所有原子类定义 */
export function generateAtoms(): AtomDefinition[] {
  const effectiveProperties = getEffectiveProperties();
  const effectiveCategories = getEffectiveCategories();

  const allAtoms: AtomDefinition[] = [];

  for (const property of effectiveProperties) {
    const atoms = generateAtomsForProperty(property, effectiveCategories);
    allAtoms.push(...atoms);
  }

  return allAtoms;
}

/** 生成 CSS 类名（prefix + property_value 格式） */
export function generateCssClassName(atom: AtomDefinition, prefix: string): string {
  return `${prefix}${atom.property}_${atom.value}`;
}

/** 生成 DTS 内容（全局常量声明格式） */
export function generateDts(): string {
  const prefix = ConfigLookup.classPrefix;
  const atoms = generateAtoms();

  const lines: string[] = [
    '/**',
    ' * CSSTS 原子类全局常量声明（自动生成）',
    ' * ',
    ' * 这些全局常量用于 css { } 语法中的 IDE 自动补全',
    ' */',
    '',
  ];

  for (const atom of atoms) {
    const cssClassName = generateCssClassName(atom, prefix);
    lines.push(`declare const ${atom.name}: { '${cssClassName}': '${atom.property}' };`);
  }

  // 添加伪类原子类声明
  lines.push(generatePseudoDts(prefix));

  // 添加类组合原子类声明
  lines.push(generateClassGroupDts(prefix));

  lines.push('');

  return lines.join('\n');
}

/** 伪类原子类定义 */
export interface PseudoAtomDefinition {
  /** 原子类名称 (camelCase)，如 hover */
  name: string;
  /** CSS 类名，如 hover */
  className: string;
  /** 伪类名称，如 hover */
  pseudo: string;
  /** CSS 样式属性和值 */
  styles: Record<string, string>;
}

/**
 * 生成伪类原子类定义
 *
 * 根据 pseudoClassConfig 配置生成伪类原子类：
 * - hover → .hover:hover { filter: brightness(1.15) }
 * - active → .active:active { filter: brightness(0.85) }
 */
export function generatePseudoAtoms(): PseudoAtomDefinition[] {
  const pseudoConfig = ConfigLookup.pseudoClassConfig;
  if (!pseudoConfig) return [];

  const result: PseudoAtomDefinition[] = [];

  for (const [pseudo, styles] of Object.entries(pseudoConfig)) {
    // 名称：直接使用伪类名（如 hover, active）
    const name = pseudo;
    // 类名：不添加前缀，由调用方统一处理
    const className = pseudo;

    result.push({
      name,
      className,
      pseudo,
      styles: styles as Record<string, string>
    });
  }

  return result;
}

/**
 * 生成伪类原子类的 DTS 内容
 */
export function generatePseudoDts(prefix: string): string {
  const pseudoAtoms = generatePseudoAtoms();
  if (pseudoAtoms.length === 0) return '';

  const lines: string[] = [
    '',
    '// ==================== 伪类原子类 ====================',
    '// 用于 $$hover/$$active 等伪类语法',
    '',
  ];

  for (const atom of pseudoAtoms) {
    const fullClassName = `${prefix}${atom.className}`;
    // 伪类原子类的 property 为 :pseudo（如 :hover），支持同伪类去重覆盖
    lines.push(`declare const ${atom.name}: { '${fullClassName}': ':${atom.pseudo}' };`);
  }

  return lines.join('\n');
}

// ==================== 类组合 ====================

/** 类组合原子类定义 */
export interface ClassGroupAtomDefinition {
  /** 原子类名称（camelCase），如 click */
  name: string;
  /** CSS 类名（kebab-case），如 cssts_click */
  className: string;
  /** 组合的原子类名称列表 */
  items: string[];
}

/**
 * 生成类组合原子类定义
 */
export function generateClassGroupAtoms(): ClassGroupAtomDefinition[] {
  const classGroup = ConfigLookup.classGroup;
  if (!classGroup) return [];

  const result: ClassGroupAtomDefinition[] = [];

  for (const [name, items] of Object.entries(classGroup)) {
    result.push({
      name,
      className: name,  // 不添加前缀，由调用方统一处理
      items
    });
  }

  return result;
}

/**
 * 生成类组合的 DTS 内容
 */
export function generateClassGroupDts(prefix: string): string {
  const classGroupAtoms = generateClassGroupAtoms();
  if (classGroupAtoms.length === 0) return '';

  const lines: string[] = [
    '',
    '// ==================== 类组合原子类 ====================',
    '// 用于 classGroup 配置',
    '',
  ];

  for (const atom of classGroupAtoms) {
    const fullClassName = `${prefix}${atom.className}`;
    // 类组合不参与属性替换，property 为 true（必须是 truthy 值）
    lines.push(`declare const ${atom.name}: { '${fullClassName}': true };`);
  }

  return lines.join('\n');
}

/** 生成统计信息 */
export function generateStats(): {
  totalAtoms: number;
  byProperty: Record<string, number>;
  byCategory: Record<string, number>;
} {
  const atoms = generateAtoms();

  const byProperty: Record<string, number> = {};
  const byCategory: Record<string, number> = {};

  for (const atom of atoms) {
    byProperty[atom.property] = (byProperty[atom.property] || 0) + 1;
    const category = atom.unit || 'keyword';
    byCategory[category] = (byCategory[category] || 0) + 1;
  }

  return { totalAtoms: atoms.length, byProperty, byCategory };
}

/** 按属性分组的原子类 */
export interface AtomsByProperty {
  [propertyName: string]: AtomDefinition[];
}

/** 生成按属性分组的原子类 */
export function generateAtomsByProperty(): AtomsByProperty {
  const effectiveProperties = getEffectiveProperties();
  const effectiveCategories = getEffectiveCategories();

  const result: AtomsByProperty = {};

  for (const property of effectiveProperties) {
    const atoms = generateAtomsForProperty(property, effectiveCategories);
    if (atoms.length > 0) {
      result[property] = atoms;
    }
  }

  return result;
}

/** 生成单个属性的 DTS 内容 */
export function generatePropertyDts(propertyName: string, atoms: AtomDefinition[], prefix: string): string {
  const lines: string[] = [
    '/**',
    ` * ${propertyName} 原子类类型定义（自动生成）`,
    ' */',
    '',
    `export interface ${propertyName.charAt(0).toUpperCase() + propertyName.slice(1)}Atoms {`,
  ];

  let lastProperty: string | undefined;
  let lastUnit: string | undefined;

  for (const atom of atoms) {
    const cssClassName = generateCssClassName(atom, prefix);

    if (lastProperty !== undefined) {
      if (atom.property !== lastProperty) {
        lines.push('');
      } else if (atom.unit !== lastUnit && atom.unit !== undefined && lastUnit !== undefined) {
        lines.push('');
      }
    }

    lines.push(`  ${atom.name}: '${cssClassName}';`);
    lastProperty = atom.property;
    lastUnit = atom.unit;
  }

  lines.push('}', '');

  return lines.join('\n');
}

/** 生成聚合索引文件内容 */
export function generateIndexDts(propertyNames: string[]): string {
  const lines: string[] = [
    '/**',
    ' * CSSTS 原子类类型定义索引（自动生成）',
    ' */',
    '',
  ];

  for (const prop of propertyNames) {
    const typeName = prop.charAt(0).toUpperCase() + prop.slice(1) + 'Atoms';
    lines.push(`export { ${typeName} } from './${prop}';`);
  }

  lines.push('');
  lines.push('/** 所有原子类类型 */');
  lines.push('export interface CsstsAtoms extends');

  const typeNames = propertyNames.map(p => p.charAt(0).toUpperCase() + p.slice(1) + 'Atoms');
  for (let i = 0; i < typeNames.length; i++) {
    const isLast = i === typeNames.length - 1;
    lines.push(`  ${typeNames[i]}${isLast ? ' {}' : ','}`);
  }

  lines.push('');

  return lines.join('\n');
}


// ==================== Group Atoms 生成 ====================

/** 生成 group 原子类名称：prefix + name + autoGenerated + suffix */
function generateGroupAtomName(
  prefix: string | undefined,
  name: string | undefined,
  autoGenerated: string,
  suffix: string | undefined
): string {
  let result = '';
  if (prefix) result += prefix;
  if (name) result += name;
  result += autoGenerated;
  if (suffix) result += suffix;

  // 确保首字母小写（camelCase）
  if (result.length > 0) {
    result = result.charAt(0).toLowerCase() + result.slice(1);
  }

  return result;
}

/** 为 numberProperties 生成 group atoms */
function generateNumberGroupAtoms(
  groupConfig: GroupConfig,
  effectiveCategories: CssNumberCategoryName[]
): GroupAtomDefinition[] {
  const { prefix, name, suffix, numberProperties } = groupConfig;
  if (!numberProperties || numberProperties.length === 0) return [];

  const atoms: GroupAtomDefinition[] = [];
  const seenNames = new Set<string>();

  // 找到所有属性共同支持的 categories
  const commonCategories = new Set<string>();
  for (const propName of numberProperties) {
    const propCategories = PROPERTY_CATEGORIES_MAP[propName as keyof typeof PROPERTY_CATEGORIES_MAP];
    if (propCategories) {
      if (commonCategories.size === 0) {
        propCategories.forEach(c => commonCategories.add(c));
      } else {
        const newCommon = new Set<string>();
        for (const c of propCategories) {
          if (commonCategories.has(c)) newCommon.add(c);
        }
        commonCategories.clear();
        newCommon.forEach(c => commonCategories.add(c));
      }
    }
  }

  // 过滤有效的 categories
  const validCategories = Array.from(commonCategories).filter(c =>
    effectiveCategories.includes(c as CssNumberCategoryName)
  );

  for (const category of validCategories) {
    // 使用第一个属性的配置作为基准
    const firstProp = numberProperties[0];
    const categoryConfig = getPropertyCategoryConfig(firstProp, category);
    const stepConfig: CssStepConfig = categoryConfig ?? { min: 0, max: 100 };

    let units = CATEGORY_UNITS_MAP[category as keyof typeof CATEGORY_UNITS_MAP];
    if (!units) continue;

    if (stepConfig.units && stepConfig.units.length > 0) {
      const filteredUnits = units.filter(u => stepConfig.units!.includes(u as any));
      if (filteredUnits.length === 0) continue;
      units = filteredUnits as unknown as typeof units;
    }

    const numbers = generateNumbers(stepConfig, ConfigLookup.progressiveRanges);

    for (const unit of units) {
      for (const num of numbers) {
        const numStr = formatNumberForClassName(num);

        let autoGenerated: string;
        let cssValue: string;

        if (num === 0) {
          autoGenerated = '0';
          cssValue = '0';
        } else {
          const unitSuffix = formatUnitForClassName(unit);
          autoGenerated = `${numStr}${unitSuffix}`;

          if (unit === 'unitless') {
            cssValue = num.toString();
          } else if (unit === 'percent') {
            cssValue = `${num}%`;
          } else {
            cssValue = `${num}${unit}`;
          }
        }

        const atomName = generateGroupAtomName(prefix, name, autoGenerated, suffix);

        if (seenNames.has(atomName)) continue;
        seenNames.add(atomName);

        // 生成所有属性的样式
        const styles: Record<string, string> = {};
        for (const propName of numberProperties) {
          styles[camelToKebab(propName)] = cssValue;
        }

        atoms.push({
          name: atomName,
          styles,
          isNumber: true,
        });
      }
    }
  }

  return atoms;
}

/** 为 keywordProperties 生成 group atoms（固定组合） */
function generateKeywordGroupAtoms(groupConfig: GroupConfig): GroupAtomDefinition[] {
  const { prefix, name, suffix, keywordProperties } = groupConfig;
  if (!keywordProperties || Object.keys(keywordProperties).length === 0) return [];

  const atomName = generateGroupAtomName(prefix, name, '', suffix);

  const styles: Record<string, string> = {};
  for (const [propName, value] of Object.entries(keywordProperties)) {
    styles[camelToKebab(propName)] = value as string;
  }

  return [{
    name: atomName,
    styles,
  }];
}

/** 为 keywordIterations 生成 group atoms（遍历组合） */
function generateKeywordIterationAtoms(groupConfig: GroupConfig): GroupAtomDefinition[] {
  const { prefix, name, suffix, keywordIterations } = groupConfig;
  if (!keywordIterations || Object.keys(keywordIterations).length === 0) return [];

  const atoms: GroupAtomDefinition[] = [];

  // 收集每个属性的关键字列表和别名/前缀
  const propertyKeywords: Array<{
    property: string;
    items: Array<{ value: string; alias?: string; prefix?: string }>;
  }> = [];

  for (const [propName, config] of Object.entries(keywordIterations)) {
    if (!config) continue;

    // 解析属性级别的 prefix/alias 和 values
    let propPrefix: string | undefined;
    let propAlias: string | undefined;
    let values: any[];

    if (Array.isArray(config)) {
      // 简写形式：直接是数组
      values = config;
    } else if (config && typeof config === 'object' && 'values' in config) {
      // 详细配置：{ values, prefix?, alias? }
      const detailConfig = config as { values: any[]; prefix?: string; alias?: string };
      values = detailConfig.values ?? [];
      propPrefix = detailConfig.prefix;
      propAlias = detailConfig.alias;
    } else {
      continue;
    }

    if (!values || values.length === 0) continue;

    const items: Array<{ value: string; alias?: string; prefix?: string }> = [];

    for (const item of values) {
      if (typeof item === 'string' || typeof item === 'number') {
        // 简写形式：直接写值，使用属性级别的 prefix/alias
        items.push({
          value: String(item),
          prefix: propPrefix,
          alias: propAlias
        });
      } else if (item && typeof item === 'object' && 'value' in item) {
        // 详细配置：{ value, alias?, prefix? }，里层覆盖外层
        items.push({
          value: String(item.value),
          alias: item.alias ?? propAlias,
          prefix: item.prefix ?? propPrefix
        });
      }
    }

    if (items.length > 0) {
      propertyKeywords.push({ property: propName, items });
    }
  }

  if (propertyKeywords.length === 0) return [];

  // 生成所有关键字组合的笛卡尔积
  function cartesianProduct<T>(arrays: T[][]): T[][] {
    return arrays.reduce<T[][]>(
      (acc, arr) => acc.flatMap(x => arr.map(y => [...x, y])),
      [[]]
    );
  }

  const itemArrays = propertyKeywords.map(pk => pk.items);
  const combinations = cartesianProduct(itemArrays);

  for (const combination of combinations) {
    // 生成类名：拼接所有关键字（使用别名或前缀+值）
    const nameParts: string[] = [];
    const styles: Record<string, string> = {};

    for (let i = 0; i < propertyKeywords.length; i++) {
      const { property } = propertyKeywords[i];
      const { value, alias, prefix: itemPrefix } = combination[i];

      // 优先使用别名，否则使用前缀+格式化值
      let displayName: string;
      if (alias !== undefined) {
        // 别名首字母大写
        displayName = alias ? alias.charAt(0).toUpperCase() + alias.slice(1) : '';
      } else {
        const formattedValue = formatKeywordForClassName(value);
        // 前缀首字母大写
        const formattedPrefix = itemPrefix ? itemPrefix.charAt(0).toUpperCase() + itemPrefix.slice(1) : '';
        displayName = formattedPrefix + formattedValue;
      }

      nameParts.push(displayName);
      styles[camelToKebab(property)] = value;
    }

    const autoGenerated = nameParts.join('');
    const atomName = generateGroupAtomName(prefix, name, autoGenerated, suffix);

    atoms.push({
      name: atomName,
      styles,
    });
  }

  return atoms;
}

/** 生成所有 group atoms */
export function generateGroupAtoms(): GroupAtomDefinition[] {
  const groups = ConfigLookup.groups;

  if (!groups || groups.length === 0) return [];

  const effectiveCategories = getEffectiveCategories();
  const allAtoms: GroupAtomDefinition[] = [];

  for (const groupConfig of groups) {
    // numberProperties
    if (groupConfig.numberProperties) {
      const atoms = generateNumberGroupAtoms(groupConfig, effectiveCategories);
      allAtoms.push(...atoms);
    }

    // keywordProperties（固定组合）
    if (groupConfig.keywordProperties) {
      const atoms = generateKeywordGroupAtoms(groupConfig);
      allAtoms.push(...atoms);
    }

    // keywordIterations（遍历组合）
    if (groupConfig.keywordIterations) {
      const atoms = generateKeywordIterationAtoms(groupConfig);
      allAtoms.push(...atoms);
    }
  }

  return allAtoms;
}

/** 生成 group atoms 的 DTS 内容 */
export function generateGroupAtomsDts(atoms: GroupAtomDefinition[]): string {
  if (atoms.length === 0) return '';

  const lines: string[] = [
    '/**',
    ' * CSSTS Group 原子类全局常量声明（自动生成）',
    ' */',
    '',
  ];

  for (const atom of atoms) {
    // 生成 CSS 类名：每个属性_值单独一个 key，value 为 null（Group 不参与同属性替换）
    const cssClassParts = Object.entries(atom.styles)
      .map(([prop, value]) => `'${prop}_${value}': null`)
      .join('; ');

    lines.push(`declare const ${atom.name}: { ${cssClassParts} };`);
  }

  lines.push('');

  return lines.join('\n');
}

/**
 * 生成原子类名 → CSS 属性的映射
 *
 * - 普通原子类：name → property（如 'displayFlex' → 'display'）
 * - Group 原子类：name → null（不参与同属性替换）
 *
 * @returns Map<原子类名, CSS属性 | null>
 */
export function generateAtomPropertyMap(): Map<string, string | null> {
  const map = new Map<string, string | null>();

  // 普通原子类：有明确的 CSS 属性
  for (const atom of generateAtoms()) {
    map.set(atom.name, atom.property);
  }

  // Group 原子类：属性为 null（不参与同属性替换）
  for (const group of generateGroupAtoms()) {
    map.set(group.name, null);
  }

  return map;
}
