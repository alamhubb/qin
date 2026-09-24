import { ConfigLookup } from '../config/ConfigLookup'
import type { CsstsCompilerConfig } from '../config/types/csstsConfig'
import { RuntimeStore, type RuntimeAtomData } from '../store/RuntimeStore'

export type { RuntimeAtomData } from '../store/RuntimeStore'

export class CsstsInit {
    static setViteEnvironment(isVite: boolean): void {
        RuntimeStore.setViteEnvironment(isVite)
    }

    static isViteEnvironment(): boolean {
        return RuntimeStore.isViteEnvironment()
    }

    static init(config?: CsstsCompilerConfig): void {
        CsstsInit.reset()
        ConfigLookup.init()
        RuntimeStore.setRuntimeMap(new Map<string, RuntimeAtomData>())
    }

    static reset(): void {
        ConfigLookup.reset()
        RuntimeStore.reset()
    }

    static isValidAtomName(name: string): boolean {
        return RuntimeStore.isValidAtomName(name)
    }

    static getRuntimeData(name: string): RuntimeAtomData | undefined {
        return RuntimeStore.getRuntimeData(name)
    }

    static getAtomGroup(name: string): 'atom' | 'group' | 'pseudo' | 'classGroup' | undefined {
        return RuntimeStore.getAtomGroup(name)
    }

    static getAllAtomNames(filterGroup?: 'atom' | 'group' | 'pseudo' | 'classGroup'): string[] {
        return RuntimeStore.getAllAtomNames(filterGroup)
    }
}
