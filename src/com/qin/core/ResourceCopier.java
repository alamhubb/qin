package com.qin.core;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * 资源文件复制器
 * 负责复制资源文件到输出目录
 */
public class ResourceCopier {
    private final String cwd;
    private final String srcDir;
    private final String outputDir;

    public ResourceCopier(String cwd, String srcDir, String outputDir) {
        this.cwd = cwd;
        this.srcDir = srcDir;
        this.outputDir = outputDir;
    }

    /**
     * 复制资源文件到输出目录
     * 查找多个可能的资源目录
     */
    public void copyResources() throws IOException {
        String[] resourceDirs = {
                Paths.get(cwd, "src", "resources").toString(),
                Paths.get(cwd, "src", "main", "resources").toString(),
                Paths.get(cwd, srcDir, "resources").toString()
        };

        for (String resourceDir : resourceDirs) {
            Path resPath = Paths.get(resourceDir);
            if (Files.exists(resPath)) {
                copyDir(resPath, Paths.get(outputDir));
            }
        }
    }

    /**
     * 递归复制目录
     */
    private void copyDir(Path src, Path dest) throws IOException {
        try (Stream<Path> walk = Files.walk(src)) {
            walk.forEach(source -> {
                try {
                    Path target = dest.resolve(src.relativize(source));
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(target);
                    } else {
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }
}
