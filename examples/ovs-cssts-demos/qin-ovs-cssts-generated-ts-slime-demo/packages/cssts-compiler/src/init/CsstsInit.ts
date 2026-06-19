/**
 * CSSTS 统一初始化入口
 *
 * 职责：
 * 1. 初始化配置（ConfigLookup）
 * 2. 生成并缓存运行时白名单（存储到 RuntimeStore）
 * 3. 调用 DTS 生成工具（传递完整数据）
 * 4. 提供白名单判断接口（委托给 RuntimeStore）
 */

import { ConfigLookup } from '../config/ConfigLookup'
import type { CsstsCompilerConfig } from '../config/types/csstsConfig'
import {
    generateAtoms,
    generateGroupAtoms,
    generatePseudoAtoms,
    generateClassGroupAtoms,
} from '../dts/atom-generator'
import { generateDtsFiles } from '../dts/dts-writer'
import { RuntimeStore, type RuntimeAtomData } from '../store/RuntimeStore'
import Glog from 'glogjs'

// 重新导出类型
export type { RuntimeAtomData } from '../store/RuntimeStore'

// ==================== CsstsInit 类 ====================

export class CsstsInit {
    /**
     * 设置 Vite 环境标志
     */
    static setViteEnvironment(isVite: boolean): void {
        RuntimeStore.setViteEnvironment(isVite)
    }

    /**
     * 判断是否在 Vite 环境中运行
     */
    static isViteEnvironment(): boolean {
        return RuntimeStore.isViteEnvironment()
    }

    /**
     * 初始化
     *
     * @param config - 用户配置
     */
    static init(config?: Partial<CsstsCompilerConfig>): void {
        CsstsInit.reset()
        // 1. 初始化配置
        ConfigLookup.init(config)

        // 1.5 设置 usedStyles（如果用户传入了自己的 Set）
        if (config?.usedStyles) {
            RuntimeStore.setUsedStyles(config.usedStyles)
        }

        // 2. 生成完整数据（临时变量，用完即丢）
        const fullAtoms = generateAtoms()
        const fullGroups = generateGroupAtoms()
        const fullPseudos = generatePseudoAtoms()
        const fullClassGroups = generateClassGroupAtoms()

        // 3. 提取运行时数据并存储到 RuntimeStore
        const runtimeMap = new Map<string, RuntimeAtomData>()

        // 3.1 atom：存储 property 和 value
        for (const atom of fullAtoms) {
            runtimeMap.set(atom.name, {
                group: 'atom',
                property: atom.property,
                value: atom.value
            })
        }

        // 3.2 group：存储 styles
        for (const group of fullGroups) {
            runtimeMap.set(group.name, {
                group: 'group',
                styles: group.styles
            })
        }

        // 3.3 pseudo：存储 pseudo 和 styles
        for (const pseudo of fullPseudos) {
            runtimeMap.set(pseudo.name, {
                group: 'pseudo',
                pseudo: pseudo.pseudo,
                styles: pseudo.styles
            })
        }

        // 3.4 classGroup：存储 items
        for (const classGroup of fullClassGroups) {
            runtimeMap.set(classGroup.name, {
                group: 'classGroup',
                items: classGroup.items
            })
        }

        // 存储到 RuntimeStore
        Glog.debug(`[CsstsInit] 设置 RuntimeStore.runtimeMap，共 ${runtimeMap.size} 个条目`)
        RuntimeStore.setRuntimeMap(runtimeMap)

        // 4. 调用 DTS 生成工具（传递完整数据，不保留引用）
        if (config?.dts !== false) {
            // 异步生成，不阻塞
            Promise.resolve().then(() => {
                return generateDtsFiles({
                    config,
                    atoms: fullAtoms,
                    groups: fullGroups,
                    pseudos: fullPseudos,
                    classGroups: fullClassGroups
                })
            }).catch(err => {
                console.error('[cssts] DTS generation failed:', err)
            })
        }

        // fullAtoms 等变量超出作用域后会被 GC 回收
    }

    /**
     * 重置（用于测试或重新初始化）
     */
    static reset(): void {
        ConfigLookup.reset()
        RuntimeStore.reset()
    }

    /**
     * 判断是否是合法的原子类名
     */
    static isValidAtomName(name: string): boolean {
        return RuntimeStore.isValidAtomName(name)
    }

    /**
     * 获取运行时数据
     */
    static getRuntimeData(name: string): RuntimeAtomData | undefined {
        return RuntimeStore.getRuntimeData(name)
    }

    /**
     * 获取原子类的类型
     */
    static getAtomGroup(name: string): 'atom' | 'group' | 'pseudo' | 'classGroup' | undefined {
        return RuntimeStore.getAtomGroup(name)
    }

    /**
     * 获取所有原子类名称
     */
    static getAllAtomNames(filterGroup?: 'atom' | 'group' | 'pseudo' | 'classGroup'): string[] {
        return RuntimeStore.getAllAtomNames(filterGroup)
    }
}
