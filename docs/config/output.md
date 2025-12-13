# 输出配置

构建产物输出配置。

## output

输出配置对象。

```ts
export default defineConfig({
  output: {
    dir: "dist",
    jarName: "my-app.jar",
  },
});
```

## output.dir

构建输出目录。

- 类型: `string`
- 默认: `"dist"`

```ts
output: {
  dir: "build",
}
```

输出结构：

```
build/
├── my-app.jar      # Fat Jar
└── static/         # 前端资源
```

## output.jarName

生成的 JAR 文件名。

- 类型: `string`
- 默认: `"{name}.jar"`

```ts
output: {
  jarName: "server.jar",
}
```

## 构建产物

执行 `qin build` 后：

```
dist/
├── my-app.jar      # 可执行 Fat Jar
│   ├── BOOT-INF/
│   │   ├── classes/    # 编译后的类
│   │   └── lib/        # 依赖 JAR
│   ├── META-INF/
│   │   └── MANIFEST.MF
│   └── static/         # 前端资源
└── static/             # 前端构建产物（独立）
```

## Fat Jar 内容

Fat Jar 包含：

| 目录 | 内容 |
|------|------|
| `BOOT-INF/classes/` | 编译后的 Java 类 |
| `BOOT-INF/lib/` | 所有 Maven 依赖 |
| `static/` | 前端构建产物 |
| `META-INF/MANIFEST.MF` | JAR 元信息 |

## 运行 Fat Jar

```bash
java -jar dist/my-app.jar
```

带 JVM 参数：

```bash
java -Xmx512m -Dserver.port=9090 -jar dist/my-app.jar
```

## 清理输出

```bash
rm -rf dist/
# 或
qin clean  # 计划中
```

## 完整示例

```ts
export default defineConfig({
  name: "production-app",
  
  output: {
    dir: "target",
    jarName: "app-1.0.0.jar",
  },
  
  client: {
    outDir: "target/static",
  },
});
```

构建后：

```
target/
├── app-1.0.0.jar
└── static/
    ├── index.html
    └── assets/
```
