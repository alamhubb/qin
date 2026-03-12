package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.*;
import com.qin.utils.QinUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * 构建生命周期管理器
 * 统一管理 compile, test, jar, fatjar, build, clean 等构建流程
 */
public class BuildLifecycle {

    private final String projectDir;
    private final QinConfig config;
    private boolean skipTests = false;

    public BuildLifecycle(String projectDir, QinConfig config) {
        this.projectDir = projectDir;
        this.config = config;
    }

    /**
     * 设置是否跳过测试
     */
    public void setSkipTests(boolean skipTests) {
        this.skipTests = skipTests;
    }

    /**
     * 获取是否跳过测试
     */
    public boolean isSkipTests() {
        return skipTests;
    }

    /**
     * qin compile - 编译源代码
     */
    public CompileResult compile() {
        System.out.println("📦 Compiling project...");

        try {
            JavaCompileConfig javaCompileConfig = JavaCompileConfig.from(config);

            // prepare output directory
            String outputDir = javaCompileConfig.outputDir();

            Path outputPath = Paths.get(projectDir, outputDir);
            Files.createDirectories(outputPath);

            // resolve source directory
            String sourceDir = javaCompileConfig.sourceDir();

            Path sourcePath = Paths.get(projectDir, sourceDir);
            if (!Files.exists(sourcePath)) {
                sourcePath = Paths.get(projectDir, "src"); // 回退到简单结构
            }

            if (!Files.exists(sourcePath)) {
                return CompileResult.failure("Source directory not found: " + sourceDir);
            }

            // 使用现有的 Compiler
            ClasspathBuilder classpathBuilder = new ClasspathBuilder(projectDir, config);
            Compiler compiler = new Compiler(projectDir, outputDir, classpathBuilder, javaCompileConfig);

            // 查找所有 Java 文件
            List<String> javaFiles = compiler.findJavaFiles(sourcePath);
            if (javaFiles.isEmpty()) {
                System.out.println("  No Java files found");
                return CompileResult.success(0, outputDir);
            }

            // 增量编译：过滤需要编译的文件
            List<String> filesToCompile = compiler.filterModifiedFiles(javaFiles, sourcePath.toString());

            if (filesToCompile.isEmpty()) {
                System.out.println("[OK] Everything is up-to-date");
                return CompileResult.success(0, outputDir);
            }

            System.out.println("  Compiling " + filesToCompile.size() + " file(s)...");

            // 执行编译
            CompileResult result = compiler.compile(filesToCompile);

            if (result.isSuccess()) {
                // 复制资源文件
                copyResources();

                System.out.println("[OK] Compiled " + filesToCompile.size() + " files to " + outputDir);
            } else {
                System.err.println("[ERROR] Compilation failed");
                System.err.println(result.getError());
            }

            return result;

        } catch (Exception e) {
            return CompileResult.failure("Compilation error: " + e.getMessage());
        }
    }

    /**
     * qin test - 运行测试
     */
    public TestResult test() {
        System.out.println("🧪 Running tests...");

        // 1. 先编译主代码
        CompileResult compileResult = compile();
        if (!compileResult.isSuccess()) {
            return TestResult.failure("Main compilation failed");
        }

        // 2. 编译测试代码
        CompileResult testCompileResult = compileTests();
        if (!testCompileResult.isSuccess()) {
            return TestResult.failure("Test compilation failed");
        }

        // 3. 运行测试（简化版本，完整实现需要 JUnit 集成）
        System.out.println("  Tests run: 0");
        System.out.println("  Passed: 0");
        System.out.println("  Failed: 0");
        System.out.println("[WARN] Test runner not yet fully implemented");

        return TestResult.success(0, 0, 0);
    }

    /**
     * qin jar - 打包普通 JAR
     */
    public JarResult jar() {
        System.out.println("📦 Building JAR...");

        JarBuilder jarBuilder = new JarBuilder(projectDir, config);
        return jarBuilder.buildJar();
    }

    /**
     * qin fatjar - 打包 Fat JAR
     */
    public JarResult fatjar() {
        System.out.println("📦 Building Fat JAR...");

        // 先编译
        CompileResult compileResult = compile();
        if (!compileResult.isSuccess()) {
            return JarResult.failure("Compilation failed: " + compileResult.getError());
        }

        // 使用现有的 FatJarBuilder
        FatJarBuilder fatJarBuilder = new FatJarBuilder(config, false, projectDir);
        BuildResult buildResult = fatJarBuilder.build();

        if (buildResult.isSuccess()) {
            try {
                long jarSize = Files.size(Paths.get(buildResult.getOutputPath()));
                return JarResult.success(buildResult.getOutputPath(), jarSize);
            } catch (IOException e) {
                return JarResult.success(buildResult.getOutputPath(), 0);
            }
        } else {
            return JarResult.failure(buildResult.getError());
        }
    }

    /**
     * qin build - 完整构建
     * 等价于: compile + test + jar
     */
    public BuildResult build() {
        System.out.println("🚀 Building project...");
        System.out.println();

        long startTime = System.currentTimeMillis();

        // 1. 编译
        CompileResult compileResult = compile();
        if (!compileResult.isSuccess()) {
            return BuildResult.failure("Compilation failed: " + compileResult.getError());
        }
        System.out.println();

        // 2. 测试（如果配置了跳过测试则跳过）
        if (!isSkipTests()) {
            TestResult testResult = test();
            if (!testResult.isSuccess()) {
                return BuildResult.failure("Tests failed");
            }
            System.out.println();
        } else {
            System.out.println("[WARN] Skipping tests");
            System.out.println();
        }

        // 3. 打包
        JarResult jarResult = jar();
        if (!jarResult.isSuccess()) {
            return BuildResult.failure("Packaging failed: " + jarResult.getError());
        }
        System.out.println();

        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("[OK] BUILD SUCCESS");
        System.out.println("  Total time: " + formatDuration(totalTime));

        return BuildResult.success(jarResult.getJarPath());
    }

    /**
     * qin clean - 清理构建产物
     */
    public void clean() {
        System.out.println("🧹 Cleaning...");

        Path buildDir = Paths.get(projectDir, "build");
        if (Files.exists(buildDir)) {
            try {
                QinUtils.deleteDir(buildDir);
                System.out.println("[OK] Deleted: build/");
            } catch (IOException e) {
                System.err.println("[ERROR] Failed to delete build directory: " + e.getMessage());
            }
        } else {
            System.out.println("  Nothing to clean");
        }
    }

    /**
     * 编译测试代码
     */
    private CompileResult compileTests() {
        try {
            JavaCompileConfig javaCompileConfig = JavaCompileConfig.from(config);
            String testDir = javaCompileConfig.testDir();

            Path testPath = Paths.get(projectDir, testDir);
            if (!Files.exists(testPath)) {
                // 没有测试目录，跳过
                return CompileResult.success(0, "build/test-classes");
            }

            System.out.println("  Compiling tests...");

            String testOutputDir = "build/test-classes";
            Path testOutputPath = Paths.get(projectDir, testOutputDir);
            Files.createDirectories(testOutputPath);

            ClasspathBuilder classpathBuilder = new ClasspathBuilder(projectDir, config);
            Compiler compiler = new Compiler(projectDir, testOutputDir, classpathBuilder, javaCompileConfig);

            List<String> testFiles = compiler.findJavaFiles(testPath);
            if (testFiles.isEmpty()) {
                return CompileResult.success(0, testOutputDir);
            }

            CompileResult result = compiler.compile(testFiles);

            if (result.isSuccess()) {
                System.out.println("  [OK] Compiled " + testFiles.size() + " test files");
            }

            return result;

        } catch (Exception e) {
            return CompileResult.failure("Test compilation error: " + e.getMessage());
        }
    }

    /**
     * 复制资源文件
     */
    private void copyResources() {
        try {
            Path resourcesDir = Paths.get(projectDir, "src/main/resources");
            if (Files.exists(resourcesDir)) {
                String outputDir = JavaCompileConfig.from(config).outputDir();

                Path outputPath = Paths.get(projectDir, outputDir);

                ResourceCopier copier = new ResourceCopier();
                copier.copy(resourcesDir, outputPath);
            }
        } catch (Exception e) {
            System.err.println("  Warning: Failed to copy resources: " + e.getMessage());
        }
    }

    /**
     * 格式化持续时间
     */
    private String formatDuration(long millis) {
        if (millis < 1000) {
            return millis + "ms";
        } else if (millis < 60000) {
            return String.format("%.1fs", millis / 1000.0);
        } else {
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            seconds = seconds % 60;
            return String.format("%dm %ds", minutes, seconds);
        }
    }
}
