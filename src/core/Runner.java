package com.qin.core;

import com.qin.types.QinConfig;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Java程序运行器
 * 负责运行编译后的Java程序
 */
public class Runner {
    private final String outputDir;
    private final ClasspathBuilder classpathBuilder;

    public Runner(String outputDir, ClasspathBuilder classpathBuilder) {
        this.outputDir = outputDir;
        this.classpathBuilder = classpathBuilder;
    }

    /**
     * 运行编译后的Java程序
     */
    public void run(String className, List<String> args) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(classpathBuilder.buildRuntimeClasspath());
        command.add(className);
        if (args != null && !args.isEmpty()) {
            command.addAll(args);
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Program exited with code " + exitCode);
        }
    }

    /**
     * 运行指定的Java文件
     */
    public void runFile(String javaFilePath, String srcDir, List<String> args)
            throws IOException, InterruptedException {
        String className = javaFilePathToClassName(javaFilePath, srcDir);
        run(className, args);
    }

    /**
     * 将Java文件路径转换为完全限定类名
     */
    private String javaFilePathToClassName(String javaFilePath, String srcDir) {
        // 规范化路径
        String normalizedPath = javaFilePath.replace("\\", "/");
        String normalizedSrcDir = srcDir.replace("\\", "/");

        // 移除srcDir前缀
        String relativePath = normalizedPath;
        if (normalizedPath.startsWith(normalizedSrcDir)) {
            relativePath = normalizedPath.substring(normalizedSrcDir.length());
        }

        // 移除前导斜杠
        if (relativePath.startsWith("/")) {
            relativePath = relativePath.substring(1);
        }

        // 移除.java后缀
        if (relativePath.endsWith(".java")) {
            relativePath = relativePath.substring(0, relativePath.length() - 5);
        }

        // 将路径分隔符替换为.
        return relativePath.replace("/", ".");
    }
}
