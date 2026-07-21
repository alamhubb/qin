package com.qin.types;

import com.qin.constants.QinConstants;

import java.util.List;

/**
 * Java-specific configuration.
 */
public record JavaConfig(
        String version,
        String release,
        String source,
        String target,
        String sourceDir,
        String testDir,
        String outputDir,
        String encoding,
        List<String> sourceExcludes) {

    public JavaConfig {
        version = version != null && !version.isBlank() ? version : QinConstants.DEFAULT_JAVA_VERSION;
        release = release != null && !release.isBlank() ? release : null;
        source = source != null && !source.isBlank() ? source : (release != null ? release : version);
        target = target != null && !target.isBlank() ? target : (release != null ? release : version);
        sourceDir = sourceDir != null && !sourceDir.isBlank() ? sourceDir : QinConstants.JAVA_SOURCE_DIR;
        testDir = testDir != null && !testDir.isBlank() ? testDir : QinConstants.DEFAULT_TEST_DIR;
        outputDir = outputDir != null && !outputDir.isBlank() ? outputDir : QinConstants.BUILD_CLASSES_DIR;
        encoding = encoding != null && !encoding.isBlank() ? encoding : QinConstants.CHARSET_UTF8;
        sourceExcludes = sourceExcludes != null
                ? sourceExcludes.stream()
                        .filter(item -> item != null && !item.isBlank())
                        .map(String::trim)
                        .toList()
                : List.of();
    }

    public JavaConfig(
            String version,
            String release,
            String source,
            String target,
            String sourceDir,
            String testDir,
            String outputDir,
            String encoding) {
        this(version, release, source, target, sourceDir, testDir, outputDir, encoding, null);
    }

    public JavaConfig() {
        this(null, null, null, null, null, null, null, null);
    }

    public JavaConfig(String version) {
        this(version, null, null, null, null, null, null, null);
    }

    public String getEffectiveRelease() {
        return release != null ? release : version;
    }

    public String getEffectiveSource() {
        return source != null ? source : getEffectiveRelease();
    }

    public String getEffectiveTarget() {
        return target != null ? target : getEffectiveRelease();
    }

    @Override
    public String toString() {
        return String.format(
                "JavaConfig[version=%s, release=%s, source=%s, target=%s, sourceDir=%s]",
                version,
                release,
                source,
                target,
                sourceDir);
    }
}
