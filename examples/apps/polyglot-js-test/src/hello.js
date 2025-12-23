/**
 * 基本 JavaScript 执行示例
 * 
 * 运行方式:
 *   qin run src/hello.js
 *   qin src/hello.js
 */

console.log("=== GraalVM Polyglot JavaScript Demo ===");
console.log();

// 基本输出
console.log("Hello from GraalJS!");
console.log("Current time:", new Date().toISOString());

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
console.log("Command line arguments:", args);

console.log();
console.log("=== Demo Complete ===");
