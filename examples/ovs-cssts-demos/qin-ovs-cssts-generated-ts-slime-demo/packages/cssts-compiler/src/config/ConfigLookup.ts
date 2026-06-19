/**
 * 配置查找器（静态方法模式）
 *
 * 使用方式：
 * 1. 调用 ConfigLookup.init(userConfig) 初始化
 * 2. 之后直接调用 ConfigLookup.xxx 静态方法获取配置
 *
 * 配置策略：
 * - 顶级配置：用户有就用用户的，没有就用默认的
 * - 嵌套配置（numberCategoriesConfig, propertiesConfig）：按名称查找，先用户后默认
 */

import type { CsstsConfig } from './types/csstsConfig';
import type {
    CssPropertyName,
    CssNumberCategoryName,
    CssColorName,
    CssProgressiveRange,
    GroupConfig,
    CssStepConfig,
} from './types/cssPropertyConfig';
import { csstsDefaultConfig } from './CsstsDefaultConfig';

/**
 * 从配置数组中查找指定 key 的配置
 */
function findInConfigArray<T extends Record<string, any>>(
    configArray: T[] | undefined,
    key: string
): any | undefined {
    if (!configArray) return undefined;

    for (const item of configArray) {
        if (key in item) {
            return item[key];
        }
    }
    return undefined;
}

/**
 * 配置查找器（静态方法模式）
 */
export class ConfigLookup {
    // 内部存储
    private static userConfig: Partial<CsstsConfig> | undefined;
    private static defaultConfig: CsstsConfig = csstsDefaultConfig;

    /**
     * 初始化配置（在入口处调用一次）
     */
    static init(userConfig?: Partial<CsstsConfig>): void {
        ConfigLookup.userConfig = userConfig;
    }

    /**
     * 重置配置（用于测试或重新初始化）
     */
    static reset(): void {
        ConfigLookup.userConfig = undefined;
    }

    // ==================== 顶级覆盖（静态 getter） ====================

    /** 支持的属性列表 */
    static get properties(): CssPropertyName[] | undefined {
        return this.userConfig?.properties ?? this.defaultConfig.properties;
    }

    /** 排除的属性列表 */
    static get excludeProperties(): CssPropertyName[] | undefined {
        return this.userConfig?.excludeProperties ?? this.defaultConfig.excludeProperties;
    }

    /** 支持的颜色列表 */
    static get colors(): CssColorName[] | undefined {
        return this.userConfig?.colors ?? this.defaultConfig.colors;
    }

    /** 排除的颜色列表 */
    static get excludeColors(): CssColorName[] | undefined {
        return this.userConfig?.excludeColors ?? this.defaultConfig.excludeColors;
    }

    /** 渐进步长范围 */
    static get progressiveRanges(): CssProgressiveRange[] | undefined {
        return this.userConfig?.progressiveRanges ?? this.defaultConfig.progressiveRanges;
    }

    /** 组合原子类配置 */
    static get groups(): GroupConfig[] | undefined {
        return this.userConfig?.groups ?? this.defaultConfig.groups;
    }

    /** 数值类别列表 */
    static get numberCategories(): CssNumberCategoryName[] | undefined {
        return this.userConfig?.numberCategories ?? this.defaultConfig.numberCategories;
    }

    /** 排除的数值类别 */
    static get excludeNumberCategories(): CssNumberCategoryName[] | undefined {
        return this.userConfig?.excludeNumberCategories ?? this.defaultConfig.excludeNumberCategories;
    }

    /** 类名前缀（自动添加 _ 分隔符，无配置时返回空字符串） */
    static get classPrefix(): string {
        const raw = this.userConfig?.classPrefix ?? this.defaultConfig.classPrefix;
        return raw ? `${raw}_` : '';
    }

    /** 排除的关键字 */
    static get excludeKeywords() {
        return this.userConfig?.excludeKeywords ?? this.defaultConfig.excludeKeywords;
    }

    /** 颜色类型列表 */
    static get colorTypes() {
        return this.userConfig?.colorTypes ?? this.defaultConfig.colorTypes;
    }

    /** 排除的颜色类型 */
    static get excludeColorTypes() {
        return this.userConfig?.excludeColorTypes ?? this.defaultConfig.excludeColorTypes;
    }

    /** 伪类配置 */
    static get pseudoClassConfig() {
        return this.userConfig?.pseudoClassConfig ?? this.defaultConfig.pseudoClassConfig;
    }

    /** 类组合配置 */
    static get classGroup(): Record<string, string[]> | undefined {
        return this.userConfig?.classGroup ?? this.defaultConfig.classGroup;
    }

    // ==================== 按名称覆盖（静态方法） ====================

    /**
     * 获取数值类别配置（先用户后默认）
     * @param categoryName 类别名，如 'pixel', 'fontRelative'
     * @returns 配置值，如 { min: 0, max: 1000 }
     */
    static getCategoryConfig(categoryName: string): CssStepConfig | undefined {
        // 1. 先从用户配置查找
        const userResult = findInConfigArray(
            this.userConfig?.numberCategoriesConfig,
            categoryName
        );
        if (userResult !== undefined) return userResult;

        // 2. 用户没有，从默认配置查找
        return findInConfigArray(
            this.defaultConfig.numberCategoriesConfig,
            categoryName
        );
    }

    /**
     * 获取属性配置（先用户后默认）
     * @param propertyName 属性名，如 'width', 'height'
     * @returns 配置值，如 { px: { min: 0, max: 10000 } }
     */
    static getPropertyConfig(propertyName: string): Record<string, CssStepConfig> | undefined {
        // 1. 先从用户配置查找
        const userResult = findInConfigArray(
            this.userConfig?.propertiesConfig,
            propertyName
        );
        if (userResult !== undefined) return userResult;

        // 2. 用户没有，从默认配置查找
        return findInConfigArray(
            this.defaultConfig.propertiesConfig,
            propertyName
        );
    }
}
