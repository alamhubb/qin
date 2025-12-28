package com.qin.types;

/**
 * Maven 仓库配置 (Java 25 Record)
 * 
 * @param id        仓库唯一标识
 * @param url       仓库 URL
 * @param name      仓库名称（可选）
 * @param releases  是否启用 release 版本
 * @param snapshots 是否启用 snapshot 版本
 */
public record Repository(
        String id,
        String url,
        String name,
        boolean releases,
        boolean snapshots) {

    /**
     * Compact Constructor with validation
     */
    public Repository {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Repository URL cannot be null or blank");
        }

        // 如果没有提供 ID，从 URL 生成
        if (id == null || id.isBlank()) {
            id = generateIdFromUrl(url);
        }

        // name 默认使用 id
        if (name == null || name.isBlank()) {
            name = id;
        }
    }

    /**
     * 简化构造器 - 只需 URL
     */
    public Repository(String url) {
        this(null, url, null, true, false);
    }

    /**
     * 简化构造器 - ID 和 URL
     */
    public Repository(String id, String url) {
        this(id, url, null, true, false);
    }

    /**
     * 从 URL 生成仓库 ID
     */
    private static String generateIdFromUrl(String url) {
        // 从 URL 中提取域名作为 ID
        try {
            String domain = url.replaceAll("https?://", "")
                    .replaceAll("/.*", "")
                    .replaceAll("\\.", "-");
            return domain.isEmpty() ? "repo-" + url.hashCode() : domain;
        } catch (Exception e) {
            return "repo-" + url.hashCode();
        }
    }

    @Override
    public String toString() {
        return String.format("Repository[id=%s, url=%s]", id, url);
    }
}
