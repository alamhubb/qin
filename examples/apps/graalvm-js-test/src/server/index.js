/**
 * GraalVM Node.js Hello World
 * 使用 GraalVM Node.js 运行时的 JavaScript 示例
 * 
 * 运行方式:
 *   方案 1 (Polyglot API): qin run src/server/index.js
 *   方案 2 (GraalVM Node.js): qin gral src/server/index.js
 * 
 * 方案对比:
 * - 方案 1: 任何 Java 11+ 都能运行，但不支持 Node.js 内置模块
 * - 方案 2: 需要 GraalVM + nodejs 组件，但支持完整 Node.js API
 */

console.log("=== GraalVM JavaScript Hello World ===");
console.log();

// 基本输出
console.log("Hello World from GraalJS!");
console.log("Current time:", new Date().toISOString());
console.log();

// ES6+ 特性演示
const greet = (name) => `Hello, ${name}!`;
console.log(greet("World"));

// 解构赋值
const { a, b } = { a: 1, b: 2 };
console.log("Destructuring:", a, b);

// 展开运算符
const arr = [1, 2, 3];
console.log("Spread:", ...arr);

// 模板字符串
const version = "1.0.0";
console.log(`Version: ${version}`);

// 命令行参数
console.log();
console.log("Command line arguments:", typeof args !== "undefined" ? args : process.argv.slice(2));

// ============================================================================
// Node.js API 演示 (仅在 GraalVM Node.js 模式下可用)
// ============================================================================

// 检测运行环境
const isGraalVMNode = typeof process !== "undefined" && process.versions && process.versions.graalvm;
const isPolyglotAPI = typeof Java !== "undefined";

console.log();
console.log("=== Runtime Detection ===");
console.log("GraalVM Node.js:", isGraalVMNode ? "Yes" : "No");
console.log("Polyglot API:", isPolyglotAPI ? "Yes" : "No");

if (isGraalVMNode || (typeof process !== "undefined" && process.versions)) {
  console.log();
  console.log("=== Node.js API Demo ===");
  
  try {
    // 文件系统 API
    const fs = require("fs");
    const path = require("path");
    
    console.log("Current directory:", process.cwd());
    console.log("Script path:", __filename);
    
    // 读取当前文件
    const content = fs.readFileSync(__filename, "utf-8");
    console.log("This file has", content.split("\n").length, "lines");
    
    // 环境变量
    console.log();
    console.log("=== Environment ===");
    console.log("NODE_ENV:", process.env.NODE_ENV || "(not set)");
    console.log("GRAALVM_HOME:", process.env.GRAALVM_HOME || "(not set)");
    
    // 进程信息
    console.log();
    console.log("=== Process Info ===");
    console.log("PID:", process.pid);
    console.log("Platform:", process.platform);
    console.log("Arch:", process.arch);
    console.log("Node version:", process.version);
    
    if (process.versions.graalvm) {
      console.log("GraalVM version:", process.versions.graalvm);
    }
  } catch (e) {
    console.log("Node.js API not available:", e.message);
  }
}

// ============================================================================
// Java 互操作演示 (两种模式都支持)
// ============================================================================

try {
  // 方案 1 (Polyglot API): Java.type() 直接可用
  // 方案 2 (GraalVM Node.js): 需要 --polyglot --jvm 参数
  const System = Java.type("java.lang.System");
  console.log();
  console.log("=== Java Interop ===");
  console.log("Java Version:", System.getProperty("java.version"));
  console.log("OS Name:", System.getProperty("os.name"));
  console.log("User Home:", System.getProperty("user.home"));
  
  // 使用 Java 集合
  const ArrayList = Java.type("java.util.ArrayList");
  const list = new ArrayList();
  list.add("Hello");
  list.add("from");
  list.add("Java");
  console.log("Java ArrayList:", list.toString());
} catch (e) {
  console.log();
  console.log("=== Java Interop ===");
  console.log("Java interop not available");
  console.log("  For Polyglot API (qin run): Java interop is built-in");
  console.log("  For GraalVM Node.js (qin gral): Use --polyglot flag");
}

console.log();
console.log("=== Demo Complete ===");
