/**
 * 配置工具函数
 *
 * 提供配置相关的辅助函数
 */

/** 单位值配置 */
interface UnitValueConfig {
  step?: number;
  max?: number;
  min?: number;
  presets?: number[];
  negative?: boolean;
}

/** 单位分类配置 */
type UnitCategoryConfig = Record<string, UnitValueConfig>;

/** 单位配置值类型 */
type UnitsConfigValue<T extends string> = T | T[] | Partial<Record<T, UnitValueConfig>>;

/**
 * 判断一个值是否应该被包含
 *
 * 优先级规则：
 * 1. 如果 includeList 有值（非空数组），则只包含 includeList 中的值
 * 2. 否则，排除 excludeList 中的值
 *
 * @param value 要检查的值
 * @param includeList 白名单（可选）
 * @param excludeList 黑名单
 * @returns 是否应该包含该值
 */
export function shouldInclude<T>(
    value: T,
    includeList: T[] | undefined,
    excludeList: T[]
): boolean {
    // 如果配置了白名单，只使用白名单
    if (includeList && includeList.length > 0) {
        return includeList.includes(value);
    }
    // 否则使用黑名单
    return !excludeList.includes(value);
}

/**
 * 从配置中获取有效的列表
 *
 * @param allItems 所有可能的值
 * @param includeList 白名单（可选）
 * @param excludeList 黑名单
 * @returns 过滤后的有效列表
 */
export function getEffectiveList<T>(
    allItems: readonly T[],
    includeList: T[] | undefined,
    excludeList: T[]
): T[] {
    // 如果配置了白名单，只使用白名单
    if (includeList && includeList.length > 0) {
        return includeList.filter(item => allItems.includes(item));
    }
    // 否则使用黑名单过滤
    return allItems.filter(item => !excludeList.includes(item));
}

/**
 * 标准化单位配置为 Record 形式
 */
export function normalizeUnitsConfig<T extends string>(
    config: UnitsConfigValue<T> | undefined
): Partial<Record<T, UnitValueConfig>> {
    if (!config) return {};

    // 字符串: 'px'
    if (typeof config === 'string') {
        return {[config]: {}} as Partial<Record<T, UnitValueConfig>>;
    }

    // 字符串数组: ['px', 'em']
    if (Array.isArray(config)) {
        const result: Partial<Record<T, UnitValueConfig>> = {};
        const seenUnits = new Set<string>();

        for (const unit of config) {
            if (seenUnits.has(unit)) {
                console.warn(`⚠️ 重复的单位配置: ${unit}`);
                continue;
            }
            seenUnits.add(unit);
            result[unit as T] = {};
        }
        return result;
    }

    // Record 形式: { px: { max: 500 } }
    return config;
}

/**
 * 标准化单位分类配置为 Record 形式
 */
export function normalizeUnitCategoriesConfig<T extends string>(
    config: UnitsConfigValue<T> | undefined
): Partial<Record<T, UnitCategoryConfig>> {
    if (!config) return {};

    // 字符串: 'pixel'
    if (typeof config === 'string') {
        return { [config]: {} } as Partial<Record<T, UnitCategoryConfig>>;
    }

    // 字符串数组: ['pixel', 'fontRelative']
    if (Array.isArray(config)) {
        const result: Partial<Record<T, UnitCategoryConfig>> = {};
        const seenCategories = new Set<string>();

        for (const category of config) {
            if (seenCategories.has(category)) {
                console.warn(`⚠️ 重复的单位分类配置: ${category}`);
                continue;
            }
            seenCategories.add(category);
            result[category as T] = {};
        }
        return result;
    }

    // Record 形式: { pixel: { max: 500 } }
    return config;
}

/**
 * 从新的层级配置格式中提取单位配置
 * 支持混合数组格式：['px', { em: { step: 0.25 } }]
 */
export function extractUnitConfigsFromArray<T extends string>(
    items: (T | Record<T, UnitValueConfig>)[] | undefined
): Partial<Record<T, UnitValueConfig>> {
    if (!items) return {};

    const result: Partial<Record<T, UnitValueConfig>> = {};

    for (const item of items) {
        if (typeof item === 'string') {
            // 字符串项：使用默认配置
            result[item] = {};
        } else if (typeof item === 'object') {
            // 对象项：提取配置
            Object.assign(result, item);
        }
    }

    return result;
}


/**
 * 从新的层级配置格式中提取单位分类配置
 * 支持混合数组格式：['px', { em: { step: 0.25 } }]
 * 支持跨层级配置：{ px: { step: 0.25 } }
 */
export function extractUnitCategoryConfigsFromArray<T extends string>(
    items: (T | Record<T, Record<string, UnitValueConfig> | UnitValueConfig>)[] | undefined
): Partial<Record<T, Record<string, UnitValueConfig>>> {
    if (!items) return {};

    const result: Partial<Record<T, Record<string, UnitValueConfig>>> = {};

    for (const item of items) {
        if (typeof item === 'string') {
            // 字符串项：使用默认配置
            result[item] = {};
        } else if (typeof item === 'object') {
            // 对象项：提取配置
            for (const [key, value] of Object.entries(item)) {
                if (typeof value === 'object' && value !== null) {
                    // 检查是否是 UnitValueConfig
                    if ('step' in value || 'max' in value || 'min' in value || 'presets' in value || 'negative' in value) {
                        // 这是单位配置，跳过（应该在 includeUnits 中处理）
                    } else {
                        // 这是单位配置对象
                        result[key as T] = value as Record<string, UnitValueConfig>;
                    }
                }
            }
        }
    }

    return result;
}

/**
 * 从新的层级配置格式中提取单位配置
 * 支持混合数组格式：['length', { time: { px: { px: { step: 4 } } } }]
 */
export function extractNumberTypeConfigsFromArray<T extends string>(
    items: (T | Record<T, Record<string, Record<string, UnitValueConfig>>>)[] | undefined
): Partial<Record<T, Record<string, Record<string, UnitValueConfig>>>> {
    if (!items) return {};

    const result: Partial<Record<T, Record<string, Record<string, UnitValueConfig>>>> = {};

    for (const item of items) {
        if (typeof item === 'string') {
            // 字符串项：使用默认配置
            result[item] = {};
        } else if (typeof item === 'object') {
            // 对象项：提取配置
            Object.assign(result, item);
        }
    }

    return result;
}


/**
 * 从混合数组中提取字符串列表
 * 用于 shouldInclude 函数
 */
export function extractStringsFromArray<T extends string>(
    items: (T | Record<T, any>)[] | undefined
): T[] {
    if (!items) return [];

    const result: T[] = [];

    for (const item of items) {
        if (typeof item === 'string') {
            result.push(item);
        } else if (typeof item === 'object') {
            // 从对象中提取 key
            result.push(...(Object.keys(item) as T[]));
        }
    }

    return result;
}


/**
 * 从数值类型配置中智能提取单位配置
 * 支持跨层级配置：
 * - { time: { px: { px: { step: 4 } } } } - 完整三层
 * - { time: { em: { step: 4 } } } - 跨越 unitCategory 层级
 */
export function extractUnitConfigsFromNumberTypeConfig(
    numberTypeConfigs: Record<string, Record<string, Record<string, UnitValueConfig> | UnitValueConfig>> | undefined,
    unitToCategory: Record<string, string>  // 单位到分类的映射
): Partial<Record<string, UnitValueConfig>> {
    if (!numberTypeConfigs) return {};

    const result: Partial<Record<string, UnitValueConfig>> = {};

    for (const [_numberType, categoryOrUnitConfigs] of Object.entries(numberTypeConfigs)) {
        for (const [key, value] of Object.entries(categoryOrUnitConfigs)) {
            if (typeof value === 'object' && value !== null) {
                // 检查 value 是否是 UnitValueConfig（有 step、max、presets 等属性）
                if ('step' in value || 'max' in value || 'min' in value || 'presets' in value || 'negative' in value) {
                    // 这是单位配置
                    result[key] = value as UnitValueConfig;
                } else {
                    // 这是 unitCategory 配置，需要递归提取
                    for (const [unitKey, unitValue] of Object.entries(value)) {
                        if (typeof unitValue === 'object' && unitValue !== null) {
                            result[unitKey] = unitValue as UnitValueConfig;
                        }
                    }
                }
            }
        }
    }

    return result;
}

/**
 * 从排除数值类型数组中提取要排除的项目名字
 * 支持混合数组格式：['angle', { time: ['ms', 's'] }, { length: { pixel: ['px'] } }]
 */
export function extractStringsFromNumberTypeExcludeArray<T extends string>(
    items: (T | Record<T, string[] | Record<string, string[]>>)[] | undefined
): T[] {
    if (!items) return [];

    const result: T[] = [];

    for (const item of items) {
        if (typeof item === 'string') {
            result.push(item);
        } else if (typeof item === 'object') {
            // 从对象中提取 key（数值类型名）
            result.push(...(Object.keys(item) as T[]));
        }
    }

    return result;
}

/**
 * 从排除单位分类数组中提取要排除的项目名字
 * 支持混合数组格式：['resolution', { pixel: ['px'] }]
 */
export function extractStringsFromUnitCategoryExcludeArray<T extends string>(
    items: (T | Record<T, string[]>)[] | undefined
): T[] {
    if (!items) return [];

    const result: T[] = [];

    for (const item of items) {
        if (typeof item === 'string') {
            result.push(item);
        } else if (typeof item === 'object') {
            // 从对象中提取 key（分类名）
            result.push(...(Object.keys(item) as T[]));
        }
    }

    return result;
}

/**
 * 从排除单位数组中提取要排除的单位名字
 * 支持混合数组格式：['px', { em: {} }]
 */
export function extractStringsFromUnitExcludeArray<T extends string>(
    items: (T | Record<T, {}>)[] | undefined
): T[] {
    if (!items) return [];

    const result: T[] = [];

    for (const item of items) {
        if (typeof item === 'string') {
            result.push(item);
        } else if (typeof item === 'object') {
            // 从对象中提取 key（单位名）
            result.push(...(Object.keys(item) as T[]));
        }
    }

    return result;
}

/**
 * 从单位分类配置中智能提取单位配置
 * 支持跨层级配置：
 * - { px: { px: { step: 4 } } } - 完整两层
 * - { px: { step: 4 } } - 直接配置
 */
export function extractUnitConfigsFromCategoryConfig(
    categoryConfigs: Record<string, Record<string, UnitValueConfig> | UnitValueConfig> | undefined
): Partial<Record<string, UnitValueConfig>> {
    if (!categoryConfigs) return {};

    const result: Partial<Record<string, UnitValueConfig>> = {};

    for (const [_category, value] of Object.entries(categoryConfigs)) {
        if (typeof value === 'object' && value !== null) {
            // 检查是否是 UnitValueConfig
            if ('step' in value || 'max' in value || 'min' in value || 'presets' in value || 'negative' in value) {
                // 这是单位配置，但应用到分类级别
                // 暂时跳过，因为这需要特殊处理
            } else {
                // 这是单位配置对象
                for (const [unitKey, unitValue] of Object.entries(value)) {
                    if (typeof unitValue === 'object' && unitValue !== null) {
                        result[unitKey] = unitValue as UnitValueConfig;
                    }
                }
            }
        }
    }

    return result;
}
