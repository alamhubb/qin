package com.qin.debug;

import java.util.Set;

/**
 * Qin IDEA 插件常量配置
 *
 * 注：通用常量已全部迁移到 com.qin.constants.QinConstants
 */
public final class QinConstants {

    private QinConstants() {
    } // 禁止实例化

    // ==================== IDEA 插件特有常量 ====================

    /**
     * 自动编译防抖时间（毫秒）
     * 文件变化后等待此时间再触发编译，避免频繁编译
     */
    public static final long AUTO_COMPILE_DEBOUNCE_MS = 3000;

    /**
     * .iml 文件中要排除的目录
     */
    public static final Set<String> IML_EXCLUDED_DIRS = Set.of(
            ".qin",
            "build",
            "libs",
            "dist",
            "out",
            "target",
            "node_modules");

    // ==================== 树节点名称 ====================

    /**
     * 任务节点名称
     */
    public static final String NODE_TASKS = "Tasks";

    /**
     * 依赖节点名称
     */
    public static final String NODE_DEPENDENCIES = "Dependencies";
}
