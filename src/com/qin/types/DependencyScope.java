package com.qin.types;

/**
 * Maven 依赖范围
 */
public enum DependencyScope {
    /** 编译和运行时都需要（默认） */
    COMPILE,
    /** 编译时需要，运行时由容器提供 */
    PROVIDED,
    /** 编译时不需要，运行时需要 */
    RUNTIME,
    /** 仅测试时需要 */
    TEST
}
