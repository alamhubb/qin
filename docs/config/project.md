# 项目配置

项目基本信息和 Java 相关配置。

## name

项目名称，用于生成 JAR 文件名。

```ts
export default defineConfig({
  name: "my-app",  // 生成 my-app.jar
});
```

## version

项目版本号，遵循语义化版本。

```ts
export default defineConfig({
  version: "1.0.0",
});
```

## description

项目描述。

```ts
export default defineConfig({
  description: "A Spring Boot application",
});
```

## port

后端服务器端口。

- 类型: `number`
- 默认: `8080`

```ts
export default defineConfig({
  port: 3000,
});
```

## entry

Java 入口文件路径。

- 类型: `string`
- 默认: 自动检测

自动检测顺序：
1. `src/Main.java`
2. `src/server/Main.java`
3. `src/{subdir}/Main.java`

```ts
export default defineConfig({
  entry: "src/app/Application.java",
});
```

## java

Java 编译相关配置。

```ts
export default defineConfig({
  java: {
    version: "17",      // Java 版本
    sourceDir: "src",   // 源码目录
  },
});
```

### java.version

- 类型: `string`
- 默认: `"17"`

指定 Java 版本，用于编译参数。

### java.sourceDir

- 类型: `string`
- 默认: 从 entry 推断

Java 源码目录。

## scripts

自定义脚本命令。

```ts
export default defineConfig({
  scripts: {
    test: "java -jar junit.jar",
    lint: "checkstyle src/",
  },
});
```

运行：

```bash
qin run test
qin run lint
```

## 完整示例

```ts
export default defineConfig({
  name: "spring-app",
  version: "2.0.0",
  description: "Spring Boot REST API",
  
  port: 8080,
  entry: "src/server/Application.java",
  
  java: {
    version: "21",
    sourceDir: "src/server",
  },
  
  scripts: {
    test: "mvn test",
  },
});
```
