/**
 * Java 互操作示例
 * 演示如何在 JavaScript 中使用 Java 类
 * 
 * 运行方式:
 *   qin run src/java-interop.js
 *   qin src/java-interop.js
 */

console.log("=== Java Interop Demo ===");
console.log();

// 使用 Java.type() 获取 Java 类
const ArrayList = Java.type('java.util.ArrayList');
const HashMap = Java.type('java.util.HashMap');
const StringBuilder = Java.type('java.lang.StringBuilder');
const System = Java.type('java.lang.System');
const Math = Java.type('java.lang.Math');

// 1. ArrayList 示例
console.log("1. ArrayList:");
const list = new ArrayList();
list.add("Apple");
list.add("Banana");
list.add("Cherry");
console.log("   Size:", list.size());
console.log("   Items:", list.toString());
console.log();

// 2. HashMap 示例
console.log("2. HashMap:");
const map = new HashMap();
map.put("name", "GraalJS");
map.put("version", "23.0");
map.put("language", "JavaScript");
console.log("   Name:", map.get("name"));
console.log("   All:", map.toString());
console.log();

// 3. StringBuilder 示例
console.log("3. StringBuilder:");
const sb = new StringBuilder();
sb.append("Hello");
sb.append(" ");
sb.append("from");
sb.append(" ");
sb.append("Java!");
console.log("   Result:", sb.toString());
console.log();

// 4. System 属性
console.log("4. System Properties:");
console.log("   Java Version:", System.getProperty("java.version"));
console.log("   OS Name:", System.getProperty("os.name"));
console.log("   User Home:", System.getProperty("user.home"));
console.log();

// 5. Math 运算
console.log("5. Java Math:");
console.log("   PI:", Math.PI);
console.log("   sqrt(16):", Math.sqrt(16));
console.log("   random():", Math.random());
console.log();

console.log("=== Demo Complete ===");
