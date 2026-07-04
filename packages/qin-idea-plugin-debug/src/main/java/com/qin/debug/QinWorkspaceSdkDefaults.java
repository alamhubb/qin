package com.qin.debug;

import com.qin.types.QinConfig;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.qin.constants.QinConstants.DEFAULT_JAVA_VERSION;

public final class QinWorkspaceSdkDefaults {
    private QinWorkspaceSdkDefaults() {
    }

    public static boolean hasQinSdkContext(Path basePath) {
        if (basePath == null) {
            return false;
        }
        if (QinConfigSupport.loadNearest(basePath) != null) {
            return true;
        }
        try {
            return !QinProjectDiscovery.discoverQinProjects(basePath).isEmpty();
        } catch (Exception e) {
            QinLogger.info("[SDK] Failed to detect Qin project context: " + e.getMessage());
            return false;
        }
    }

    public static String preferredJavaVersion(Path basePath) {
        List<Path> qinProjects = QinProjectDiscovery.discoverQinProjects(basePath);
        if (qinProjects.isEmpty()) {
            QinConfig nearestConfig = QinConfigSupport.loadNearest(basePath);
            if (nearestConfig != null) {
                String version = QinConfigSupport.javaVersion(nearestConfig);
                QinLogger.info("[SDK] Resolved Java version from nearest Qin config: " + version);
                return version;
            }
            QinLogger.info("[SDK] No Qin project config found, using default Java version: " + DEFAULT_JAVA_VERSION);
            return DEFAULT_JAVA_VERSION;
        }

        Map<String, Integer> versionCounts = new LinkedHashMap<>();
        int maxVersion = parseJavaVersion(DEFAULT_JAVA_VERSION);
        String maxVersionStr = DEFAULT_JAVA_VERSION;

        for (Path projectPath : qinProjects) {
            QinConfig config = QinConfigSupport.load(projectPath);
            if (config == null) {
                continue;
            }

            String version = QinConfigSupport.javaVersion(config);
            versionCounts.merge(version, 1, Integer::sum);
            int parsed = parseJavaVersion(version);
            if (parsed > maxVersion) {
                maxVersion = parsed;
                maxVersionStr = version;
            }
        }

        if (versionCounts.isEmpty()) {
            QinLogger.info("[SDK] Workspace Qin configs did not provide a Java version, using default: "
                    + DEFAULT_JAVA_VERSION);
            return DEFAULT_JAVA_VERSION;
        }

        if (versionCounts.size() > 1) {
            QinLogger.info("[SDK] Multiple Java versions detected in workspace: " + versionCounts
                    + ". Using highest required version: " + maxVersionStr);
        } else {
            QinLogger.info("[SDK] Resolved Java version from workspace Qin projects: " + maxVersionStr);
        }
        return maxVersionStr;
    }

    public static int parseJavaVersion(String versionStr) {
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)");
            java.util.regex.Matcher matcher = pattern.matcher(versionStr);
            if (matcher.find()) {
                int version = Integer.parseInt(matcher.group(1));
                if (version == 1 && matcher.find()) {
                    version = Integer.parseInt(matcher.group(1));
                }
                return version;
            }
        } catch (Exception e) {
            // Keep SDK auto-configuration best-effort when the IDE reports an unusual version string.
        }
        return 0;
    }
}
