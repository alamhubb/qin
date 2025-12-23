package com.qin.types;

import java.util.List;

/**
 * 编译上下文
 */
public class CompileContext extends PluginContext {
    private final List<String> sourceFiles;
    private final String outputDir;
    private final String classpath;

    public CompileContext(String root, QinConfig config, boolean isDev,
                          List<String> sourceFiles, String outputDir, String classpath) {
        super(root, config, isDev);
        this.sourceFiles = sourceFiles;
        this.outputDir = outputDir;
        this.classpath = classpath;
    }

    public List<String> getSourceFiles() { return sourceFiles; }
    public String getOutputDir() { return outputDir; }
    public String getClasspath() { return classpath; }
}
