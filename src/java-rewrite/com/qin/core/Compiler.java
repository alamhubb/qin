package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.CompileResult;

import javax.tools.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java编译器
 * 负责编译Java源文件
 */
public class Compiler {
    private final String cwd;
    private final String outputDir;
    private final ClasspathBuilder classpathBuilder;

    public Compiler(String cwd, String outputDir, ClasspathBuilder classpathBuilder) {
        this.cwd = cwd;
        this.outputDir = outputDir;
        this.classpathBuilder = classpathBuilder;
    }

    /**
     * 编译Java文件
     */
    public CompileResult compile(List<String> javaFiles) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            return CompileResult.failure("No Java compiler available. Make sure you're using JDK, not JRE.");
        }

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            // 准备源文件
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(javaFiles);

            // 编译选项
            List<String> options = new ArrayList<>();
            options.add("-d");
            options.add(outputDir);
            options.add("-encoding");
            options.add(QinConstants.CHARSET_UTF8);

            String fullCp = classpathBuilder.buildCompileClasspath();
            System.out.println("  [DEBUG] Compile classpath: " + fullCp);
            if (fullCp != null && !fullCp.isEmpty()) {
                options.add("-cp");
                options.add(fullCp);
            }

            // 收集诊断信息
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

            // 执行编译
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, diagnostics, options, null, compilationUnits);

            boolean success = task.call();

            if (!success) {
                StringBuilder errorMsg = new StringBuilder();
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                        errorMsg.append(diagnostic.getMessage(null)).append("\n");
                    }
                }
                return CompileResult.failure(errorMsg.toString().trim());
            }

            return CompileResult.success(javaFiles.size(), outputDir);
        } catch (IOException e) {
            return CompileResult.failure(e.getMessage());
        }
    }

    /**
     * 过滤出需要编译的文件（增量编译）
     */
    public List<String> filterModifiedFiles(List<String> javaFiles, String srcDir) {
        return javaFiles.stream()
                .filter(javaFile -> isModified(javaFile, srcDir))
                .collect(Collectors.toList());
    }

    /**
     * 检查Java文件是否被修改（需要重新编译）
     */
    private boolean isModified(String javaFilePath, String srcDir) {
        try {
            Path javaFile = Paths.get(javaFilePath);

            // 计算对应的.class文件路径
            String relativePath = javaFilePath;
            if (javaFilePath.startsWith(srcDir)) {
                relativePath = javaFilePath.substring(srcDir.length());
                if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
                    relativePath = relativePath.substring(1);
                }
            }
            String classRelativePath = relativePath.replace(".java", ".class");
            Path classFile = Paths.get(outputDir, classRelativePath);

            // .class不存在，需要编译
            if (!Files.exists(classFile)) {
                return true;
            }

            // 比较修改时间
            FileTime javaTime = Files.getLastModifiedTime(javaFile);
            FileTime classTime = Files.getLastModifiedTime(classFile);
            return javaTime.compareTo(classTime) > 0;
        } catch (IOException e) {
            return true; // 出错时默认需要编译
        }
    }

    /**
     * 查找所有Java文件
     */
    public List<String> findJavaFiles(Path dir) throws IOException {
        if (!Files.exists(dir)) {
            return new ArrayList<>();
        }
        try (Stream<Path> walk = Files.walk(dir)) {
            return walk
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
    }
}
