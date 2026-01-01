/**
 * Qin Monorepo ESM Loader Hooks
 * 拦截模块解析，将 workspace 包的入口重定向到源码
 * 
 * 环境变量:
 * - QIN_MONOREPO_CONFIG: monorepo-config.json 的路径
 */
import { readFileSync, existsSync } from 'node:fs';
import { resolve as pathResolve, join } from 'node:path';
import { pathToFileURL } from 'node:url';

// 从环境变量读取配置路径
const configPath = process.env.QIN_MONOREPO_CONFIG;
let config = null;

if (configPath && existsSync(configPath)) {
    try {
        const content = readFileSync(configPath, 'utf-8');
        config = JSON.parse(content);
    } catch (e) {
        console.error('[qin loader] Failed to load config:', e.message);
    }
}

/**
 * 判断是否是包的主入口导入（不是子路径导入）
 */
function isMainEntryImport(specifier) {
    // 跳过相对路径和绝对路径
    if (specifier.startsWith('.') || specifier.startsWith('/')) {
        return false;
    }

    // 跳过 node: 和其他协议
    if (specifier.includes(':')) {
        return false;
    }

    // 处理 scoped 包名 @scope/name
    if (specifier.startsWith('@')) {
        const parts = specifier.split('/');
        // @scope/name 正好两部分是主入口
        return parts.length === 2;
    }

    // 普通包名
    return !specifier.includes('/');
}

/**
 * ESM Loader: resolve hook
 */
export async function resolve(specifier, context, nextResolve) {
    // 没有配置则跳过
    if (!config || !config.packages) {
        return nextResolve(specifier, context);
    }

    // 只处理主入口导入
    if (!isMainEntryImport(specifier)) {
        return nextResolve(specifier, context);
    }

    // 检查是否是配置中的包
    const pkg = config.packages[specifier];

    if (!pkg || !pkg.monorepoEntry) {
        return nextResolve(specifier, context);
    }

    // 构造新的入口路径
    const newEntry = join(pkg.dir, pkg.monorepoEntry);
    const newUrl = pathToFileURL(newEntry).href;

    // 调试日志
    if (process.env.QIN_DEBUG) {
        console.log(`[qin] ${specifier} -> ${pkg.monorepoEntry}`);
    }

    return {
        url: newUrl,
        shortCircuit: true
    };
}
