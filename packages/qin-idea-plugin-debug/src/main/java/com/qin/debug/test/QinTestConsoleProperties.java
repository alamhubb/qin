package com.qin.debug.test;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties;
import org.jetbrains.annotations.NotNull;

/**
 * Qin 测试控制台属性
 * 配置测试结果显示方式，支持 TeamCity 格式解析
 */
public class QinTestConsoleProperties extends SMTRunnerConsoleProperties {

    public QinTestConsoleProperties(@NotNull RunConfiguration config,
                                     @NotNull Executor executor) {
        super(config, "Qin", executor);

        // 启用基于 ID 的测试树（更好的性能）
        setIdBasedTestTree(true);

        // 显示测试开始时间
        setPrintTestingStartedTime(true);

        // 使用服务消息格式（TeamCity 格式）
        setUsePredefinedMessageFilter(true);
    }
}
