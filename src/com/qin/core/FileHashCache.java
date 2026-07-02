package com.qin.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qin.constants.QinConstants;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件哈希缓存
 * 使用 MD5 哈希跟踪文件内容变化，实现精确的增量编译
 *
 * 缓存文件: .qin/compile-cache.json
 * {
 *   "files": {
 *     "src/Main.java": "a1b2c3d4...",
 *     "src/Utils.java": "e5f6g7h8..."
 *   },
 *   "metadata": {
 *     "java.release": "21",
 *     "java.outputDir": "build/classes"
 *   },
 *   "lastCompileTime": 1234567890
 * }
 */
public class FileHashCache {

    private static final String HASH_ALGORITHM = "MD5";

    private final Path projectDir;
    private final Path cacheFile;
    private final Gson gson;

    // 缓存数据
    private Map<String, String> fileHashes;
    private Map<String, String> metadata;
    private long lastCompileTime;

    public FileHashCache(String projectDir) {
        this.projectDir = Paths.get(projectDir);
        this.cacheFile = QinConstants.getProjectCompileCache(this.projectDir);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.fileHashes = new ConcurrentHashMap<>();
        this.metadata = new ConcurrentHashMap<>();
        this.lastCompileTime = 0;
    }

    /**
     * 加载缓存
     */
    public void load() {
        fileHashes = new ConcurrentHashMap<>();
        metadata = new ConcurrentHashMap<>();
        lastCompileTime = 0;

        if (!Files.exists(cacheFile)) {
            return;
        }

        try {
            String json = Files.readString(cacheFile);
            CacheData data = gson.fromJson(json, CacheData.class);
            if (data != null) {
                if (data.files != null) {
                    fileHashes = new ConcurrentHashMap<>(data.files);
                }
                if (data.metadata != null) {
                    metadata = new ConcurrentHashMap<>(data.metadata);
                }
                lastCompileTime = data.lastCompileTime;
            }
        } catch (Exception e) {
            // 缓存损坏，重置
            fileHashes = new ConcurrentHashMap<>();
            metadata = new ConcurrentHashMap<>();
            lastCompileTime = 0;
        }
    }

    /**
     * 保存缓存
     */
    public void save() throws IOException {
        Files.createDirectories(cacheFile.getParent());

        CacheData data = new CacheData();
        data.files = new HashMap<>(fileHashes);
        data.metadata = new TreeMap<>(metadata);
        data.lastCompileTime = System.currentTimeMillis();

        String json = gson.toJson(data);
        Files.writeString(cacheFile, json);
    }

    /**
     * 获取需要重新编译的文件
     *
     * @param sourceDir 源代码目录
     * @return 需要编译的文件列表（相对路径）
     */
    public List<Path> getChangedFiles(String sourceDir) throws IOException {
        List<Path> changedFiles = new ArrayList<>();
        Path srcPath = projectDir.resolve(sourceDir);

        if (!Files.exists(srcPath)) {
            return changedFiles;
        }

        // 扫描所有 .java 文件
        Set<String> currentFiles = new HashSet<>();

        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".java")) {
                    String relativePath = projectDir.relativize(file).toString()
                        .replace('\\', '/');
                    currentFiles.add(relativePath);

                    try {
                        String currentHash = computeHash(file);
                        String cachedHash = fileHashes.get(relativePath);

                        // 哈希不同或不存在 → 需要编译
                        if (cachedHash == null || !cachedHash.equals(currentHash)) {
                            changedFiles.add(file);
                        }
                    } catch (Exception e) {
                        // 计算哈希失败，保守起见加入编译列表
                        changedFiles.add(file);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return changedFiles;
    }

    /**
     * 检查 sourceDir 下是否存在已删除的 .java 文件（相对于缓存）
     */
    public boolean hasDeletedFiles(String sourceDir) throws IOException {
        Path srcPath = projectDir.resolve(sourceDir);
        String normalizedPrefix = normalizeSourcePrefix(sourceDir);
        if (normalizedPrefix == null) {
            return false;
        }

        Set<String> currentFiles = new HashSet<>();
        if (Files.exists(srcPath)) {
            Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".java")) {
                        currentFiles.add(normalizeCacheKey(projectDir.relativize(file).toString()));
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        for (String cachedPath : fileHashes.keySet()) {
            if (cachedPath.startsWith(normalizedPrefix) && cachedPath.endsWith(".java")
                    && !currentFiles.contains(cachedPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否需要编译（任何文件变化）
     */
    public boolean needsCompilation(String sourceDir) throws IOException {
        return !getChangedFiles(sourceDir).isEmpty();
    }

    /**
     * 更新文件哈希（编译成功后调用）
     */
    public void updateHash(Path file) {
        try {
            String relativePath = projectDir.relativize(file).toString()
                .replace('\\', '/');
            String hash = computeHash(file);
            fileHashes.put(relativePath, hash);
        } catch (Exception e) {
            // 忽略
        }
    }

    /**
     * 批量更新文件哈希
     */
    public void updateHashes(List<Path> files) {
        for (Path file : files) {
            updateHash(file);
        }
    }

    /**
     * 更新整个源目录的哈希
     */
    public void updateAllHashes(String sourceDir) throws IOException {
        Path srcPath = projectDir.resolve(sourceDir);
        String normalizedPrefix = normalizeSourcePrefix(sourceDir);
        if (normalizedPrefix != null) {
            removeEntriesByPrefix(normalizedPrefix);
        }

        if (!Files.exists(srcPath)) {
            return;
        }

        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".java")) {
                    updateHash(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * 检查缓存元数据是否与当前编译配置一致。
     */
    public boolean hasMetadata(Map<String, String> expectedMetadata) {
        Map<String, String> expected = expectedMetadata != null ? expectedMetadata : Map.of();
        return metadata.equals(expected);
    }

    /**
     * 更新编译配置元数据。配置变化会让调用方触发全量重编译。
     */
    public void updateMetadata(Map<String, String> nextMetadata) {
        metadata.clear();
        if (nextMetadata != null) {
            metadata.putAll(nextMetadata);
        }
    }

    /**
     * 清除缓存
     */
    public void clear() throws IOException {
        fileHashes.clear();
        metadata.clear();
        lastCompileTime = 0;
        Files.deleteIfExists(cacheFile);
    }

    /**
     * 计算文件 MD5 哈希
     */
    private String computeHash(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] content = Files.readAllBytes(file);
        byte[] hash = md.digest(content);
        return bytesToHex(hash);
    }

    /**
     * 字节数组转十六进制字符串
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 获取缓存统计信息
     */
    public CacheStats getStats() {
        return new CacheStats(
            fileHashes.size(),
            metadata.size(),
            lastCompileTime
        );
    }

    // ==================== 内部类 ====================

    /**
     * 缓存数据结构
     */
    private static class CacheData {
        Map<String, String> files;
        Map<String, String> metadata;
        long lastCompileTime;
    }

    /**
     * 缓存统计信息
     */
    public record CacheStats(int fileCount, int metadataCount, long lastCompileTime) {
        @Override
        public String toString() {
            return String.format("CacheStats{files=%d, metadata=%d, lastCompile=%s}",
                fileCount,
                metadataCount,
                lastCompileTime > 0
                    ? new java.util.Date(lastCompileTime).toString()
                    : "never");
        }
    }

    private String normalizeSourcePrefix(String sourceDir) {
        Path sourcePath = Paths.get(sourceDir);
        Path resolved = sourcePath.isAbsolute() ? sourcePath.normalize() : projectDir.resolve(sourcePath).normalize();
        try {
            String relative = normalizeCacheKey(projectDir.relativize(resolved).toString());
            if (relative.isEmpty()) {
                return relative;
            }
            return relative.endsWith("/") ? relative : relative + "/";
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private void removeEntriesByPrefix(String prefix) {
        Iterator<String> iterator = fileHashes.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.startsWith(prefix)) {
                iterator.remove();
            }
        }
    }

    private String normalizeCacheKey(String path) {
        return path.replace('\\', '/');
    }
}
