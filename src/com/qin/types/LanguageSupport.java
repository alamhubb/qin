package com.qin.types;

import java.util.List;
import java.util.Map;

/**
 * 语言插件接口 - 提供语言特定的编译/运行/测试能力
 */
public interface LanguageSupport {
    /** 语言名称 */
    String getName();
    
    /** 支持的文件扩展名 */
    List<String> getExtensions();
    
    /** 编译源代码 */
    default CompileResult compile(CompileContext ctx) { return null; }
    
    /** 运行程序 */
    default void run(RunContext ctx) {}
    
    /** 运行测试 */
    default TestResult test(TestContext ctx) { return null; }
    
    /** 构建产物 */
    default BuildResult build(BuildContext ctx) { return null; }
    
    /** 解析依赖 */
    default String resolveDependencies(Map<String, String> deps) { return ""; }
}
