/**
 * CSSTS 运行时数据存储
 *
 * 独立文件，不依赖其他模块，避免循环依赖
 */

// ==================== 运行时数据类型 ====================

/**
 * 运行时原子类数据（包含生成 CSS 所需的所有信息）
 */
export class RuntimeAtomData {
    group: 'atom' | 'group' | 'pseudo' | 'classGroup'
    property: string | undefined
    value: string | undefined
    styles: Record<string, string> | undefined
    pseudo: string | undefined
    items: string[] | undefined
}

// ==================== RuntimeStore 类 ====================

/**
 * 运行时数据存储（单例）
 *
 * 职责：
 * 1. 存储运行时数据
 * 2. 提供数据查询接口
 * 3. 存储环境标志
 */
export class RuntimeStore {
    // 运行时数据：所有原子类信息
    private static _runtimeMap: Map<string, RuntimeAtomData> | null = null

    // 是否在 Vite 环境中运行
    private static _isVite: boolean = false

    // 使用过的原子类集合（全局累加）
    private static _usedStyles: Set<string> = new Set()

    /**
     * 设置运行时数据
     */
    static setRuntimeMap(map: Map<string, RuntimeAtomData>): void {
        RuntimeStore._runtimeMap = map
    }

    static setRuntimeData(name: string, data: RuntimeAtomData): void {
        if (!RuntimeStore._runtimeMap) {
            RuntimeStore._runtimeMap = new Map()
        }
        RuntimeStore._runtimeMap.set(name, data)
    }

    /**
     * 获取运行时数据
     */
    static getRuntimeData(name: string): RuntimeAtomData | undefined {
        const runtimeMap: Map<string, RuntimeAtomData> | null = RuntimeStore._runtimeMap
        if (runtimeMap === null) return undefined
        return runtimeMap.get(name)
    }

    /**
     * 判断是否是合法的原子类名
     */
    static isValidAtomName(name: string): boolean {
        const runtimeMap: Map<string, RuntimeAtomData> | null = RuntimeStore._runtimeMap
        if (runtimeMap === null) return false
        return runtimeMap.has(name)
    }

    /**
     * 获取原子类的类型
     */
    static getAtomGroup(name: string): 'atom' | 'group' | 'pseudo' | 'classGroup' | undefined {
        const data: RuntimeAtomData | undefined = RuntimeStore.getRuntimeData(name)
        if (data === undefined) return undefined
        return data.group
    }

    /**
     * 获取所有原子类名称
     */
    static getAllAtomNames(filterGroup?: 'atom' | 'group' | 'pseudo' | 'classGroup'): string[] {
        const runtimeMap: Map<string, RuntimeAtomData> | null = RuntimeStore._runtimeMap
        if (runtimeMap === null) return []

        const result: string[] = []
        for (const name of runtimeMap.keys()) {
            const data: RuntimeAtomData | undefined = RuntimeStore.getRuntimeData(name)
            if (!filterGroup) {
                result.push(name)
                continue
            }
            if (data !== undefined && data.group === filterGroup) {
                result.push(name)
            }
        }
        return result
    }

    /**
     * 设置 Vite 环境标志
     */
    static setViteEnvironment(isVite: boolean): void {
        RuntimeStore._isVite = isVite
    }

    /**
     * 判断是否在 Vite 环境中运行
     */
    static isViteEnvironment(): boolean {
        return RuntimeStore._isVite
    }

    // ==================== usedStyles 管理 ====================

    /**
     * 设置使用的原子类集合（支持外部传入）
     *
     * @param set - 外部传入的 Set，如果不传则使用内部默认 Set
     */
    static setUsedStyles(set?: Set<string>): void {
        if (set) {
            RuntimeStore._usedStyles = set
        }
    }

    /**
     * 添加使用的原子类
     */
    static addUsedStyle(name: string): void {
        RuntimeStore._usedStyles.add(name)
    }

    /**
     * 批量添加使用的原子类
     */
    static addUsedStyles(names: Iterable<string>): void {
        for (const name of names) {
            RuntimeStore._usedStyles.add(name)
        }
    }

    /**
     * 获取所有使用过的原子类
     */
    static getUsedStyles(): Set<string> {
        return RuntimeStore._usedStyles
    }

    /**
     * 清空使用过的原子类
     */
    static clearUsedStyles(): void {
        RuntimeStore._usedStyles.clear()
    }

    /**
     * 重置
     */
    static reset(): void {
        RuntimeStore._runtimeMap = null
        RuntimeStore._isVite = false
        RuntimeStore._usedStyles.clear()
    }
}
