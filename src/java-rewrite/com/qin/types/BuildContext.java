package com.qin.types;

/**
 * 构建上下文
 */
public class BuildContext extends PluginContext {
    private final String outputDir;
    private final String outputName;

    public BuildContext(String root, QinConfig config, boolean isDev,
                        String outputDir, String outputName) {
        super(root, config, isDev);
        this.outputDir = outputDir;
        this.outputName = outputName;
    }

    public String getOutputDir() { return outputDir; }
    public String getOutputName() { return outputName; }
}
