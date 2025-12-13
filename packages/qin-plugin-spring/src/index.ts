/**
 * qin-plugin-spring
 * Spring Boot 支持插件
 * 
 * 功能：
 * - 生成 application.yml（如果不存在且有配置）
 * - TypeScript 配置与 Spring yml 格式一致
 * - 检测 DevTools 依赖，通知 Qin 禁用热重载
 */

import { existsSync } from "fs";
import { writeFile, mkdir } from "fs/promises";
import { join } from "path";
import * as yaml from "yaml";

// Spring Boot 相关依赖前缀
const SPRING_BOOT_PREFIX = "org.springframework.boot:spring-boot";

/**
 * Spring 配置类型（与 application.yml 结构一致）
 */
export interface SpringConfig {
  /** 数据源配置 */
  datasource?: {
    url?: string;
    username?: string;
    password?: string;
    driverClassName?: string;
  };
  /** JPA 配置 */
  jpa?: {
    hibernate?: {
      ddlAuto?: "none" | "validate" | "update" | "create" | "create-drop";
    };
    showSql?: boolean;
    properties?: Record<string, any>;
  };
  /** Redis 配置 */
  redis?: {
    host?: string;
    port?: number;
    password?: string;
  };
  /** 其他 Spring 配置 */
  [key: string]: any;
}

/**
 * 服务器配置
 */
export interface ServerConfig {
  /** 端口，默认 8080 */
  port?: number;
  /** Servlet 配置 */
  servlet?: {
    contextPath?: string;
  };
  /** 其他服务器配置 */
  [key: string]: any;
}

/**
 * 日志配置
 */
export interface LoggingConfig {
  level?: {
    root?: string;
    [key: string]: string | undefined;
  };
  file?: {
    name?: string;
    path?: string;
  };
  [key: string]: any;
}

/**
 * Spring 插件配置
 * 
 * 注意：DevTools、Actuator 等依赖需要用户在 qin.config.ts 的 dependencies 中显式声明
 * 插件只负责检测和配置，不会自动添加依赖
 */
export interface SpringBootPluginOptions {
  // === 以下配置与 application.yml 结构一致 ===
  
  /** 服务器配置 */
  server?: ServerConfig;
  /** Spring 配置 */
  spring?: SpringConfig;
  /** 日志配置 */
  logging?: LoggingConfig;
  /** 其他自定义配置 */
  [key: string]: any;
}

/**
 * 检测是否是 Spring Boot 项目
 */
function isSpringBootProject(dependencies: Record<string, string> | undefined): boolean {
  if (!dependencies) return false;
  
  return Object.keys(dependencies).some(dep => 
    dep.startsWith(SPRING_BOOT_PREFIX)
  );
}

/**
 * 检测是否已有 DevTools
 */
function hasDevTools(dependencies: Record<string, string> | undefined): boolean {
  if (!dependencies) return false;
  
  return Object.keys(dependencies).some(dep => 
    dep.includes("spring-boot-devtools")
  );
}

/**
 * 检测是否存在 application.yml 或 application.properties
 */
function hasApplicationConfig(cwd: string): boolean {
  const candidates = [
    join(cwd, "src", "resources", "application.yml"),
    join(cwd, "src", "resources", "application.yaml"),
    join(cwd, "src", "resources", "application.properties"),
    join(cwd, "src", "main", "resources", "application.yml"),
    join(cwd, "src", "main", "resources", "application.yaml"),
    join(cwd, "src", "main", "resources", "application.properties"),
  ];
  
  return candidates.some(path => existsSync(path));
}

/**
 * 将 camelCase 转换为 kebab-case（Spring yml 风格）
 */
function toKebabCase(str: string): string {
  return str.replace(/([a-z])([A-Z])/g, "$1-$2").toLowerCase();
}

/**
 * 递归转换对象的 key 为 kebab-case
 */
function convertKeysToKebab(obj: any): any {
  if (obj === null || typeof obj !== "object") {
    return obj;
  }
  
  if (Array.isArray(obj)) {
    return obj.map(convertKeysToKebab);
  }
  
  const result: any = {};
  for (const [key, value] of Object.entries(obj)) {
    const kebabKey = toKebabCase(key);
    result[kebabKey] = convertKeysToKebab(value);
  }
  return result;
}

/**
 * 从插件配置生成 application.yml 内容
 */
function generateApplicationYml(options: SpringBootPluginOptions): string {
  // 转换为 kebab-case
  const config = convertKeysToKebab(options);
  
  return yaml.stringify(config);
}

/**
 * 插件接口（本地定义）
 */
interface QinPlugin {
  name: string;
  config?: (config: any) => any;
  configResolved?: (config: any) => void | Promise<void>;
  beforeRun?: (ctx: any) => void | Promise<void>;
  devServer?: (ctx: any) => Promise<void>;
}

/**
 * 创建 Spring 插件
 */
export function spring(options: SpringBootPluginOptions = {}): QinPlugin {
  let isSpringBoot = false;
  let devToolsEnabled = false;
  const cwd = process.cwd();

  return {
    name: "qin-plugin-spring",

    config(config) {
      const deps = config.dependencies || {};
      
      // 检测是否是 Spring Boot 项目
      isSpringBoot = isSpringBootProject(deps);
      
      if (!isSpringBoot) {
        console.log("[spring] 未检测到 Spring Boot 依赖，插件不生效");
        return config;
      }

      // 检测用户是否声明了 DevTools
      devToolsEnabled = hasDevTools(deps);

      // 获取端口配置
      const port = options.server?.port || 8080;

      return {
        ...config,
        port,
        // 标记使用 DevTools，让 Qin 热重载插件知道应该禁用
        _useDevTools: devToolsEnabled,
      };
    },

    async configResolved(config) {
      if (!isSpringBoot) return;

      // 如果没有 application.yml，根据插件配置生成
      if (!hasApplicationConfig(cwd)) {
        const hasSpringConfig = options.server || options.spring || options.logging;
        
        if (hasSpringConfig) {
          console.log("[spring] 生成 application.yml");
          
          // 确保 resources 目录存在
          const resourcesDir = join(cwd, "src", "resources");
          await mkdir(resourcesDir, { recursive: true });
          
          // 生成 yml 文件
          const ymlContent = generateApplicationYml(options);
          await writeFile(join(resourcesDir, "application.yml"), ymlContent);
        }
      }
    },
  };
}

/**
 * 检测配置是否使用 DevTools
 */
export function isUsingDevTools(config: any): boolean {
  return config._useDevTools === true;
}

export default spring;
