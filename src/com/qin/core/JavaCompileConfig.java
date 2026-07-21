package com.qin.core;

import com.qin.constants.QinConstants;
import com.qin.types.JavaConfig;
import com.qin.types.QinConfig;

import java.util.List;

/**
 * Normalized Java compilation settings derived from qin.config.js.
 */
public record JavaCompileConfig(
        String version,
        String release,
        String source,
        String target,
        String sourceDir,
        String testDir,
        String outputDir,
        String encoding,
        List<String> sourceExcludes) {

    public static JavaCompileConfig from(QinConfig config) {
        return from(config != null ? config.java() : null);
    }

    public static JavaCompileConfig from(JavaConfig config) {
        JavaConfig javaConfig = config != null ? config : new JavaConfig(QinConstants.DEFAULT_JAVA_VERSION);

        String version = firstNonBlank(javaConfig.version(), QinConstants.DEFAULT_JAVA_VERSION);
        String release = firstNonBlank(javaConfig.release(), null);
        String source = firstNonBlank(javaConfig.source(), null);
        String target = firstNonBlank(javaConfig.target(), null);

        if (isBlank(release) && !isBlank(source) && source.equals(target)) {
            release = source;
        }
        if (isBlank(release) && isBlank(source) && isBlank(target)) {
            release = version;
        }

        return new JavaCompileConfig(
                version,
                release,
                source,
                target,
                firstNonBlank(javaConfig.sourceDir(), QinConstants.DEFAULT_SOURCE_DIR),
                firstNonBlank(javaConfig.testDir(), QinConstants.DEFAULT_TEST_DIR),
                firstNonBlank(javaConfig.outputDir(), QinConstants.BUILD_CLASSES_DIR),
                firstNonBlank(javaConfig.encoding(), QinConstants.CHARSET_UTF8),
                javaConfig.sourceExcludes() != null ? javaConfig.sourceExcludes() : List.of());
    }

    public void appendJavacOptions(List<String> options) {
        options.add("-encoding");
        options.add(encoding);

        if (!isBlank(release)) {
            options.add("--release");
            options.add(release);
            return;
        }

        if (!isBlank(source)) {
            options.add("-source");
            options.add(source);
        }

        if (!isBlank(target)) {
            options.add("-target");
            options.add(target);
        }
    }

    public String effectiveLanguageLevel() {
        if (!isBlank(release)) {
            return release;
        }
        if (!isBlank(target)) {
            return target;
        }
        return version;
    }

    public String effectiveSourceLevel() {
        if (!isBlank(release)) {
            return release;
        }
        if (!isBlank(source)) {
            return source;
        }
        return version;
    }

    private static String firstNonBlank(String value, String fallback) {
        return isBlank(value) ? fallback : value.trim();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

