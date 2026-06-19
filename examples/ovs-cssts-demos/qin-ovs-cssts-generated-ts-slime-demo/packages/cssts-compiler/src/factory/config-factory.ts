/**
 * 配置工厂函数
 *
 * 提供创建配置实例的便捷方法
 */

import { csstsDefaultConfig } from '../config/CsstsDefaultConfig';
import type { CsstsConfig, CsstsConfigRequired } from '../config/types/csstsConfig';

/**
 * 创建 CSSTS 配置实例
 *
 * @param options 用户配置，会与系统默认配置合并
 * @returns CsstsConfigRequired 实例
 */
export function createCsstsConfig(options?: Partial<CsstsConfig>): CsstsConfigRequired {
    return {
        ...csstsDefaultConfig,
        ...options,
    } as CsstsConfigRequired;
}
