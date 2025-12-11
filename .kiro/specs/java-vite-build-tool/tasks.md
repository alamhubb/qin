# Implementation Plan

- [x] 1. 项目基础设置 (T0)


  - [x] 1.1 初始化项目结构


    - 确保 package.json 中 bin 字段指向 src/index.ts
    - 创建 src/types.ts 定义 QinConfig 接口
    - _Requirements: 2.1, 2.3_

  - [x] 1.2 安装核心依赖

    - 安装 commander (命令行解析)
    - 安装 chalk (终端美化)
    - _Requirements: 7.1_

  - [x] 1.3 创建 CLI 入口骨架

    - 在 src/index.ts 实现基本的命令行框架
    - 注册 init、run、build 三个子命令
    - _Requirements: 7.1-7.5_

- [x] 2. 配置加载器 (T1)
  - [x] 2.1 定义 QinConfig 类型接口

    - 在 src/types.ts 中定义完整的配置接口
    - 包含 entry、dependencies、output、java 字段
    - _Requirements: 2.3_


  - [x] 2.2 实现 ConfigLoader 类

    - 使用 import() 动态加载 qin.config.ts
    - 处理配置文件不存在的错误
    - _Requirements: 2.1, 2.2_

  - [x] 2.3 实现 Entry 路径解析函数

    - 从 entry 路径提取 srcDir 和 className
    - 例如 "src/Main.java" → { srcDir: "src", className: "Main" }

    - _Requirements: 5.2_


  - [x] 2.4 编写 Entry 路径解析的属性测试

    - **Property 2: Entry 路径解析正确性**
    - **Validates: Requirements 5.2**

- [x] 3. 环境检查器 (T2)




  - [x] 3.1 实现 EnvironmentChecker 类

    - 检查 cs (Coursier) 命令是否可用
    - 检查 javac 命令是否可用




    - _Requirements: 3.1, 3.2_

  - [x] 3.2 实现安装指南生成

    - 根据平台提供 Coursier 安装命令
    - 提供 JDK 安装建议
    - _Requirements: 3.3, 3.4_

- [x] 4. 依赖解析器 (T2)
  - [x] 4.1 实现 DependencyResolver 类

    - 调用 `cs fetch <deps> --classpath` 解析依赖
    - 返回完整的 classpath 字符串
    - _Requirements: 4.1, 4.2_




  - [x] 4.2 实现跨平台 Classpath 构建

    - Windows 使用分号 (;) 分隔
    - Unix 使用冒号 (:) 分隔
    - _Requirements: 8.1, 8.2_




  - [x] 4.3 编写 Classpath 分隔符的属性测试

    - **Property 3: Classpath 分隔符平台一致性**
    - **Validates: Requirements 8.1, 8.2**




  - [x] 4.4 实现错误处理

    - 处理无效依赖坐标
    - 处理网络超时
    - _Requirements: 4.3, 4.4_



- [x] 5. Checkpoint - 确保所有测试通过

  - Ensure all tests pass, ask the user if questions arise.

- [x] 6. Java 运行器 (T3)
  - [x] 6.1 实现 JavaRunner 类基础结构

    - 接收 QinConfig 和 classpath
    - 实现 compile() 方法调用 javac
    - _Requirements: 5.3_
  - [x] 6.2 实现编译命令拼接

    - 将 classpath 和源文件目录拼接
    - 输出 .class 文件到 .qin/classes
    - _Requirements: 5.3_
  - [x] 6.3 实现运行命令拼接

    - 调用 java -cp ... MainClass
    - 传递命令行参数
    - _Requirements: 5.4, 5.5_
  - [x] 6.4 实现 compileAndRun() 方法

    - 先编译，成功后运行
    - 编译失败时显示错误并跳过运行
    - _Requirements: 5.6_

- [x] 7. 实现 qin run 命令
  - [x] 7.1 集成 ConfigLoader、DependencyResolver、JavaRunner

    - 加载配置 → 解析依赖 → 编译运行
    - _Requirements: 5.1-5.5_

  - [x] 7.2 添加终端输出美化

    - 使用 chalk 显示不同阶段的状态
    - _Requirements: 7.1-7.4_


- [x] 8. Checkpoint - 确保所有测试通过

  - Ensure all tests pass, ask the user if questions arise.

- [x] 9. Fat Jar 构建器 (T4)
  - [x] 9.1 实现 FatJarBuilder 类基础结构

    - 创建 .qin/temp 临时目录
    - _Requirements: 6.1_

  - [x] 9.2 实现依赖 JAR 解压

    - 遍历所有 JAR 路径
    - 使用 jar -xf 解压到临时目录
    - _Requirements: 6.2_

  - [x] 9.3 实现签名文件清理

    - 删除 META-INF 下的 *.SF, *.DSA, *.RSA 文件
    - 这是防止 SecurityException 的关键步骤

    - _Requirements: 6.3_
  - [x] 9.4 编写签名文件清理的属性测试

    - **Property 4: 签名文件清理完整性**

    - **Validates: Requirements 6.3**
  - [x] 9.5 实现源码编译到临时目录

    - 将 .class 文件输出到临时目录根
    - _Requirements: 6.4_

  - [x] 9.6 实现 Manifest 生成

    - 生成 META-INF/MANIFEST.MF
    - 写入 Main-Class 条目

    - _Requirements: 6.5_
  - [x] 9.7 编写 Manifest 主类的属性测试

    - **Property 5: Manifest 主类正确性**
    - **Validates: Requirements 6.5**

  - [x] 9.8 实现最终 JAR 打包

    - 调用 jar -cvfm 生成 Fat Jar
    - 输出到 dist 目录

    - _Requirements: 6.6_
  - [x] 9.9 实现临时目录清理

    - 构建完成后清理 .qin/temp
    - debug 模式下保留
    - _Requirements: 6.7_

- [x] 10. 实现 qin build 命令
  - [x] 10.1 集成完整构建流程

    - 加载配置 → 解析依赖 → 构建 Fat Jar
    - _Requirements: 6.1-6.7_

  - [x] 10.2 添加构建进度输出

    - 显示各阶段状态
    - 显示最终输出路径
    - _Requirements: 7.1-7.5_





- [x] 11. Checkpoint - 确保所有测试通过


  - Ensure all tests pass, ask the user if questions arise.

- [x] 12. 项目初始化命令 (T5)




  - [x] 12.1 实现 qin init 命令

    - 生成 qin.config.ts 模板
    - 创建 src 目录和 Main.java 模板
    - _Requirements: 1.1, 1.2, 1.4_





  - [x] 12.2 实现已存在检查
    - 如果 qin.config.ts 已存在，显示警告
    - _Requirements: 1.3_

- [x] 13. 终端体验优化 (T5)



  - [x] 13.1 统一错误处理和输出格式

    - 使用 chalk 区分不同类型的消息
    - 错误、警告、成功使用不同颜色
    - _Requirements: 7.1-7.5_

- [x] 14. Final Checkpoint - 确保所有测试通过

  - Ensure all tests pass, ask the user if questions arise.

