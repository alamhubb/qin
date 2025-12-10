# Implementation Plan

- [x] 1. 修复现有代码的类型错误并建立项目基础





  - [x] 1.1 修复 JavaPackageManager 中的 TypeScript 类型错误


    - 修复 dependency 解构时的 undefined 类型问题
    - 添加类型守卫确保类型安全
    - _Requirements: 4.1, 4.5, 4.6_

  - [x] 1.2 修复 JavaBuilder 中的未使用变量警告

    - 移除未使用的 imports (basename, dirname)
    - 处理 result 变量
    - _Requirements: 1.1_
  - [x] 1.3 设置测试框架和配置


    - 配置 bun test
    - 安装 fast-check 属性测试库
    - 创建测试目录结构
    - _Requirements: Testing Strategy_

- [x] 2. 实现项目配置的序列化和反序列化



  - [x] 2.1 增强 JavaProject 接口，添加完整的配置字段

    - 添加 java 配置块 (version, srcDir, outDir, wasmOutDir)
    - 确保所有字段有合理的默认值
    - _Requirements: 4.5, 4.6_
  - [ ]* 2.2 编写配置序列化往返的属性测试
    - **Property 1: 项目配置序列化往返一致性**
    - **Validates: Requirements 4.5, 4.6**
  - [x] 2.3 实现配置文件的读写逻辑


    - 实现 loadProject 和 saveProject 方法
    - 处理文件不存在的情况
    - _Requirements: 4.5, 4.6_

- [x] 3. 完善依赖管理功能



  - [x] 3.1 实现依赖格式验证

    - 验证 groupId:artifactId:version 格式
    - 返回明确的错误信息
    - _Requirements: 4.4_
  - [ ]* 3.2 编写依赖格式验证的属性测试
    - **Property 3: 依赖格式验证**
    - **Validates: Requirements 4.4**
  - [x] 3.3 实现依赖去重逻辑


    - 相同 groupId:artifactId 只保留最新版本
    - _Requirements: 4.2_
  - [ ]* 3.4 编写依赖去重的属性测试
    - **Property 2: 依赖去重一致性**
    - **Validates: Requirements 4.2**
  - [ ]* 3.5 编写依赖列表显示的属性测试
    - **Property 12: 依赖列表显示完整性**
    - **Validates: Requirements 6.2**

- [x] 4. Checkpoint - 确保所有测试通过


  - Ensure all tests pass, ask the user if questions arise.

- [x] 5. 实现跨平台 Classpath 构建



  - [x] 5.1 实现平台感知的 classpath 分隔符

    - Windows 使用分号 (;)
    - Unix 使用冒号 (:)
    - _Requirements: 8.1, 8.2_
  - [ ]* 5.2 编写 classpath 分隔符的属性测试
    - **Property 4: Classpath 分隔符平台一致性**
    - **Validates: Requirements 8.1, 8.2**

  - [x] 5.3 实现 classpath 完整性构建

    - 包含输出目录和所有依赖 JAR
    - _Requirements: 2.4_
  - [ ]* 5.4 编写 classpath 完整性的属性测试
    - **Property 5: Classpath 完整性**
    - **Validates: Requirements 2.4**

- [x] 6. 实现 WasmBridge 核心组件


  - [x] 6.1 创建 WasmBridge 类的基础结构


    - 定义 WasmBridgeConfig 接口
    - 实现构造函数和配置合并
    - _Requirements: 9.1_

  - [x] 6.2 实现源文件 hash 计算和缓存检查

    - 计算 .java 文件的 hash
    - 比较 hash 判断是否需要重编译
    - _Requirements: 13.2, 13.3_
  - [ ]* 6.3 编写缓存一致性的属性测试
    - **Property 10: 缓存一致性**
    - **Validates: Requirements 13.2, 13.3**


  - [ ] 6.4 实现 TeaVM 编译调用
    - 调用 TeaVM 将 .class 编译为 .wasm
    - 生成 JS glue 文件
    - _Requirements: 9.1, 9.2, 9.3_

- [x] 7. Checkpoint - 确保所有测试通过


  - Ensure all tests pass, ask the user if questions arise.

- [-] 8. 实现 WASM 模块加载和类型转换

  - [x] 8.1 实现 WASM 模块加载器


    - 使用 WebAssembly.instantiate 加载 .wasm
    - 解析导出的函数列表
    - _Requirements: 10.1_

  - [ ] 8.2 实现基本类型转换函数
    - number ↔ i32/f64
    - string ↔ WASM 内存
    - boolean ↔ i32
    - _Requirements: 10.2, 10.3, 10.5_
  - [ ]* 8.3 编写类型转换往返的属性测试
    - **Property 9: 类型转换往返一致性**
    - **Validates: Requirements 10.2, 10.3, 10.5**

  - [ ] 8.4 实现 Java 类代理对象
    - 创建 Proxy 包装 WASM 导出
    - 支持静态方法调用
    - 支持实例化和实例方法
    - _Requirements: 12.2, 12.3, 12.4_

- [x] 9. 实现 public 方法自动导出



  - [x] 9.1 实现 Java 字节码解析获取方法信息

    - 解析 .class 文件获取方法签名
    - 识别 public/private/protected 修饰符
    - _Requirements: 11.1, 11.2_
  - [ ]* 9.2 编写 public 方法导出的属性测试
    - **Property 7: Public 方法自动导出**
    - **Validates: Requirements 9.5, 11.1, 11.2**

  - [ ] 9.3 实现 public 字段暴露
    - 将 public 字段映射为 JS 属性
    - _Requirements: 11.5_
  - [ ]* 9.4 编写 public 字段暴露的属性测试
    - **Property 8: Public 字段自动暴露**
    - **Validates: Requirements 11.5**

- [x] 10. Checkpoint - 确保所有测试通过

  - Ensure all tests pass, ask the user if questions arise.

- [-] 11. 实现 Bun Plugin 和 TypeScript 类型生成

  - [x] 11.1 创建 Bun Plugin 注册 .java loader


    - 拦截 .java 文件导入
    - 触发编译并返回加载代码
    - _Requirements: 12.1, 13.1_

  - [ ] 11.2 实现 WASM 实例共享
    - 缓存已加载的 WASM 实例
    - 多个导入共享同一实例
    - _Requirements: 13.4_
  - [ ]* 11.3 编写 WASM 实例共享的属性测试
    - **Property 11: WASM 实例共享**
    - **Validates: Requirements 13.4**

  - [ ] 11.4 实现 TypeScript 类型定义生成
    - 从 Java 类信息生成 .d.ts 文件
    - 映射 Java 类型到 TypeScript 类型
    - _Requirements: 11.3_

- [ ] 12. 实现 CLI 命令接口
  - [x] 12.1 创建 CLI 入口和命令路由

    - 解析命令行参数
    - 路由到对应的处理函数
    - _Requirements: 7.1-7.6_

  - [ ] 12.2 实现 java compile 命令
    - 调用 JavaBuilder.compile()
    - 显示编译结果

    - _Requirements: 7.1, 1.1-1.5_
  - [ ] 12.3 实现 java run 命令
    - 调用 JavaBuilder.run()
    - 传递命令行参数
    - _Requirements: 7.2, 2.1-2.5_
  - [x] 12.4 实现 java wasm 命令

    - 调用 WasmBridge.compileClass()
    - 显示生成的文件路径
    - _Requirements: 9.1-9.5_

  - [ ] 12.5 实现依赖管理命令 (add/install/list)
    - 调用 JavaPackageManager 对应方法

    - _Requirements: 7.3, 7.4, 7.5_
  - [ ] 12.6 实现帮助信息和错误处理
    - 未知命令显示帮助
    - 统一错误码和消息格式
    - _Requirements: 7.6_

- [x] 13. Checkpoint - 确保所有测试通过

  - Ensure all tests pass, ask the user if questions arise.

- [x] 14. 集成测试和文档


  - [x] 14.1 编写端到端集成测试

    - 测试完整的编译→WASM→调用流程
    - 测试 CLI 命令
    - _Requirements: All_
  - [x] 14.2 更新 README 文档


    - 添加使用说明
    - 添加示例代码

    - _Requirements: Documentation_

- [x] 15. Final Checkpoint - 确保所有测试通过


  - Ensure all tests pass, ask the user if questions arise.
