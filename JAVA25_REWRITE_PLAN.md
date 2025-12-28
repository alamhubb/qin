# Qin Java 25 重写计划

## 📋 项目信息

- **原版本**: Java 17
- **目标版本**: Java 25 (LTS)
- **重写时间**: 2025-12-29
- **预计完成**: 2 天

## 🎯 升级目标

### 性能提升
- ✅ 启动速度提升 2-3x（AOT Profiling）
- ✅ 内存占用减少 20-30%（Compact Headers）
- ✅ 并发性能提升 3-5x（Virtual Threads + Structured Concurrency）

### 代码简化
- ✅ 代码量减少 40-50%
- ✅ 可读性大幅提升
- ✅ 类型安全性增强

## 🚀 Java 25 新特性应用清单

### ✅ 核心特性（必须使用）

| 特性 | JEP | 应用场景 | 优先级 |
|------|-----|---------|--------|
| Flexible Constructor Bodies | 513 | QinConfig 验证、Plugin 初始化 | 🔥🔥🔥🔥🔥 |
| Module Import Declarations | 511 | 所有类的 import 简化 | 🔥🔥🔥🔥🔥 |
| Primitive Patterns | 507 | 配置数值处理、命令参数解析 | 🔥🔥🔥🔥🔥 |
| Structured Concurrency | 505 | 并行依赖下载、编译 | 🔥🔥🔥🔥🔥 |
| Compact Object Headers | 519 | 自动优化（JVM 级别） | 🔥🔥🔥🔥 |
| AOT Method Profiling | 515 | CLI 启动优化 | 🔥🔥🔥🔥 |

### ✅ 继承自 Java 21 的特性

| 特性 | JEP | 应用场景 |
|------|-----|---------|
| Record Patterns | 440 | 配置解构、依赖解析 |
| Pattern Switch | 441 | 命令分发、插件检测 |
| Virtual Threads | 444 | 所有异步操作 |
| Sequenced Collections | 431 | 插件加载顺序 |

## 📂 重写文件清单

### Phase 1: 类型系统（Java 25 Records + Flexible Constructors）

#### 1.1 核心配置类
- [x] `types/QinConfig.java` → **Record** + Flexible Constructors
- [ ] `types/JavaConfig.java` → Record
- [ ] `types/OutputConfig.java` → Record
- [ ] `types/Repository.java` → Record
- [ ] `types/ClientConfig.java` → Record
- [ ] `types/FrontendConfig.java` → Record
- [ ] `types/GraalVMConfig.java` → Record

#### 1.2 插件系统类型
- [ ] `types/QinPlugin.java` → Interface (保持不变)
- [ ] `types/PluginContext.java` → Record
- [ ] `types/BuildResult.java` → Record
- [ ] `types/CompileResult.java` → Record
- [ ] `types/ResolveResult.java` → Record

### Phase 2: 核心模块（Java 25 Features）

#### 2.1 配置加载器
- [ ] `core/ConfigLoader.java`
  - ✅ Module Import
  - ✅ Flexible Constructors 用于验证
  - ✅ Primitive Patterns 处理配置数值

#### 2.2 依赖解析器
- [ ] `core/DependencyResolver.java`
  - ✅ Virtual Threads 并行下载
  - ✅ Structured Concurrency 可靠性
  - ✅ Record Patterns 依赖解构

#### 2.3 Java 构建器
- [ ] `java/JavaBuilder.java`
  - ✅ Virtual Threads 并行编译
  - ✅ Structured Concurrency 任务管理
  - ✅ Pattern Switch 编译选项

#### 2.4 插件管理器
- [ ] `core/PluginManager.java`
  - ✅ Pattern Switch 插件类型分发
  - ✅ Sequenced Collections 加载顺序
  - ✅ Record Patterns 插件配置

### Phase 3: CLI 系统（Java 25 Instance Main）

#### 3.1 主入口
- [ ] `cli/QinCli.java`
  - ✅ Instance Main Method (JEP 512)
  - ✅ Module Import
  - ✅ Pattern Switch 命令分发

#### 3.2 命令实现
- [ ] `commands/InitCommand.java` → Pattern Switch
- [ ] `commands/EnvCommand.java` → Pattern Switch
- [ ] `commands/CompileCommand.java` (新增)
- [ ] `commands/RunCommand.java` (新增)
- [ ] `commands/BuildCommand.java` (新增)

### Phase 4: 工具类（Java 25 优化）

#### 4.1 Java 工具
- [ ] `java/ClasspathUtils.java` → Sequenced Collections
- [ ] `java/PackageManager.java` → Virtual Threads

#### 4.2 核心工具
- [ ] `core/EnvironmentChecker.java` → Pattern Switch
- [ ] `core/FatJarBuilder.java` → Virtual Threads
- [ ] `core/JavaRunner.java` → Flexible Constructors

## 🔧 构建配置更新

### 4.1 编译脚本
- [ ] 更新 `build-java.bat` → Java 25 编译参数
- [ ] 更新 `build-java.sh` → Java 25 编译参数

### 4.2 运行时配置
- [ ] 添加 AOT Profiling 脚本
- [ ] 添加 Compact Headers 验证

## 📚 文档更新

- [ ] `README.md` → Java 25 特性说明
- [ ] `src/java-rewrite/README.md` → 重写说明
- [ ] 添加 `JAVA25_FEATURES.md` → 特性示例
- [ ] 添加性能对比数据

## ✅ 验证清单

### 功能验证
- [ ] `qin compile` - 编译 Java 项目
- [ ] `qin run` - 运行 Java 项目
- [ ] `qin build` - 构建 Fat JAR
- [ ] `qin test` - 运行测试
- [ ] `qin sync` - 同步依赖

### 性能验证
- [ ] 启动时间对比（Java 17 vs 25）
- [ ] 内存占用对比
- [ ] 并行编译速度对比
- [ ] 依赖下载速度对比

### 兼容性验证
- [ ] subhuti-java 项目编译测试
- [ ] Spring Boot 项目测试
- [ ] Monorepo 多项目测试

## 📅 时间表

### Day 1: 核心重写（2025-12-29）
- **上午**: Phase 1 - 类型系统 (4h)
- **下午**: Phase 2 - 核心模块 (4h)

### Day 2: CLI & 测试（2025-12-30）
- **上午**: Phase 3 - CLI 系统 (3h)
- **下午**: Phase 4 - 工具类 + 验证 (5h)

## 🎓 代码示例对比

### 示例 1: QinConfig (Java 17 → Java 25)

**Before (Java 17)**:
```java
public class QinConfig {
    private String name;
    private String version;
    private Map<String, String> dependencies;
    
    // 20 行 getters/setters...
    
    public QinConfig() {
        // 无法在 super() 前验证
    }
}
```

**After (Java 25)**:
```java
import module java.base;  // ✨ Module Import

public record QinConfig(
    String name,
    String version,
    Map<String, String> dependencies
) {
    public QinConfig {  // ✨ Compact Constructor + Flexible Body
        if (name == null || name.isBlank()) {  // ✨ 验证在 super() 前
            throw new IllegalArgumentException("name cannot be blank");
        }
        Objects.requireNonNull(version, "version");
        dependencies = Map.copyOf(dependencies);  // 不可变
    }
}
```

### 示例 2: 并行编译 (Java 17 → Java 25)

**Before (Java 17)**:
```java
ExecutorService executor = Executors.newFixedThreadPool(10);
try {
    List<Future<CompileResult>> futures = new ArrayList<>();
    for (Path file : files) {
        futures.add(executor.submit(() -> compileFile(file)));
    }
    for (Future<CompileResult> f : futures) {
        f.get();  // 可能泄漏异常
    }
} finally {
    executor.shutdown();
}
```

**After (Java 25)**:
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {  // ✨
    var tasks = files.stream()
        .map(file -> scope.fork(() -> compileFile(file)))
        .toList();
    
    scope.join().throwIfFailed();  // ✨ 统一异常处理
    return tasks.stream().map(Subtask::get).toList();
}
```

### 示例 3: 命令分发 (Java 17 → Java 25)

**Before (Java 17)**:
```java
if ("compile".equals(command)) {
    new CompileCommand().execute(args);
} else if ("run".equals(command)) {
    new RunCommand().execute(args);
} else if ("build".equals(command)) {
    new BuildCommand().execute(args);
} else {
    throw new IllegalArgumentException("Unknown: " + command);
}
```

**After (Java 25)**:
```java
switch (command) {  // ✨ Pattern Switch
    case "compile" -> new CompileCommand().execute(args);
    case "run" -> new RunCommand().execute(args);
    case "build" -> new BuildCommand().execute(args);
    case null -> throw new IllegalArgumentException("command is null");
    default -> throw new IllegalArgumentException(STR."Unknown: \{command}");
}
```

---

**开始时间**: 2025-12-29 04:31  
**预计完成**: 2025-12-30 18:00  
**总工时**: 16 小时
