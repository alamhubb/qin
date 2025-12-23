package com.qin.types;

/**
 * 测试上下文
 */
public class TestContext extends PluginContext {
    private final String filter;
    private final boolean verbose;

    public TestContext(String root, QinConfig config, boolean isDev,
                       String filter, boolean verbose) {
        super(root, config, isDev);
        this.filter = filter;
        this.verbose = verbose;
    }

    public String getFilter() { return filter; }
    public boolean isVerbose() { return verbose; }
}
