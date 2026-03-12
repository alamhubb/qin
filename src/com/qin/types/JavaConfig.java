package com.qin.types;

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
        String encoding) {

    public JavaConfig {
        version = version != null && !version.isBlank() ? version : "21";
        release = release != null && !release.isBlank() ? release : null;
        source = source != null && !source.isBlank() ? source : (release != null ? release : version);
        target = target != null && !target.isBlank() ? target : (release != null ? release : version);
        sourceDir = sourceDir != null && !sourceDir.isBlank() ? sourceDir : "src/main/java";
        testDir = testDir != null && !testDir.isBlank() ? testDir : "src/test/java";
        outputDir = outputDir != null && !outputDir.isBlank() ? outputDir : "build/classes";
        encoding = encoding != null && !encoding.isBlank() ? encoding : "UTF-8";
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