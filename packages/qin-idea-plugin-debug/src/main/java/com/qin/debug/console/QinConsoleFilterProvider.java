package com.qin.debug.console;

import com.intellij.execution.filters.ConsoleFilterProvider;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * 控制台过滤器提供者
 * 为所有控制台自动添加 Qin 编译错误过滤器
 */
public class QinConsoleFilterProvider implements ConsoleFilterProvider {

    @NotNull
    @Override
    public Filter[] getDefaultFilters(@NotNull Project project) {
        return new Filter[]{new QinConsoleFilter(project)};
    }
}
