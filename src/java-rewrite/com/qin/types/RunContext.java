package com.qin.types;

import java.util.List;

/**
 * 运行上下文
 */
public class RunContext extends PluginContext {
    private final List<String> args;
    private final String classpath;

    public RunContext(String root, QinConfig config, boolean isDev,
                      List<String> args, String classpath) {
        super(root, config, isDev);
        this.args = args;
        this.classpath = classpath;
    }

    public List<String> getArgs() { return args; }
    public String getClasspath() { return classpath; }
}
