package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.projectRoots.JavaSdk;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;

import java.nio.file.Files;
import java.nio.file.Paths;

public final class QinProjectSdkSelection {
    private QinProjectSdkSelection() {
    }

    public static Selection selectConfiguredJdk(int desiredVersion) {
        ProjectJdkTable jdkTable = ProjectJdkTable.getInstance();
        Sdk[] allJdks = jdkTable.getAllJdks();
        QinLogger.info("[SDK] Detected " + allJdks.length + " configured JDK(s)");
        for (Sdk sdk : allJdks) {
            QinLogger.info("[SDK]   - " + sdk.getName() + " (" + sdk.getHomePath() + ")");
        }

        Sdk bestSdk = selectBestMatchingJdk(allJdks, desiredVersion);
        if (bestSdk == null) {
            return null;
        }
        return new Selection(bestSdk, sdkVersion(bestSdk));
    }

    public static Sdk registerJavaHomeJdk() {
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome == null || javaHome.isEmpty() || !Files.exists(Paths.get(javaHome))) {
            QinLogger.info("[SDK] JAVA_HOME is unavailable, Project SDK remains unset until configured manually");
            QinLogger.info("[SDK]   JAVA_HOME = " + (javaHome != null ? javaHome : "null"));
            return null;
        }

        QinLogger.info("[SDK] No registered JDK found, trying JAVA_HOME: " + javaHome);
        JavaSdk javaSdkType = JavaSdk.getInstance();
        String sdkName = "JDK-" + System.getProperty("java.version", "auto");
        Sdk newSdk = javaSdkType.createJdk(sdkName, javaHome, false);
        if (newSdk == null) {
            QinLogger.error("[SDK] Unable to create JDK automatically, please configure it manually");
            return null;
        }

        ApplicationManager.getApplication().runWriteAction(() -> {
            ProjectJdkTable.getInstance().addJdk(newSdk);
        });
        QinLogger.info("[SDK]   Registered new JDK in IDE: " + sdkName);
        return newSdk;
    }

    public static int sdkVersion(Sdk sdk) {
        if (sdk == null) {
            return 0;
        }
        String versionStr = JavaSdk.getInstance().getVersionString(sdk);
        return QinWorkspaceSdkDefaults.parseJavaVersion(versionStr);
    }

    private static Sdk selectBestMatchingJdk(Sdk[] allJdks, int desiredVersion) {
        Sdk exactMatch = null;
        Sdk nearestHigher = null;
        int nearestHigherVersion = Integer.MAX_VALUE;
        Sdk nearestLower = null;
        int nearestLowerVersion = Integer.MIN_VALUE;

        for (Sdk sdk : allJdks) {
            if (!(sdk.getSdkType() instanceof JavaSdk)) {
                continue;
            }

            String versionStr = JavaSdk.getInstance().getVersionString(sdk);
            if (versionStr == null) {
                continue;
            }

            int version = QinWorkspaceSdkDefaults.parseJavaVersion(versionStr);
            QinLogger.info("[SDK]   Candidate JDK: " + sdk.getName() + " (version: " + version + ")");

            if (version == desiredVersion) {
                exactMatch = sdk;
                break;
            }
            if (version > desiredVersion && version < nearestHigherVersion) {
                nearestHigherVersion = version;
                nearestHigher = sdk;
            }
            if (version < desiredVersion && version > nearestLowerVersion) {
                nearestLowerVersion = version;
                nearestLower = sdk;
            }
        }

        if (exactMatch != null) {
            return exactMatch;
        }
        if (nearestHigher != null) {
            return nearestHigher;
        }
        return nearestLower;
    }

    public static final class Selection {
        private final Sdk sdk;
        private final int version;

        private Selection(Sdk sdk, int version) {
            this.sdk = sdk;
            this.version = version;
        }

        public Sdk sdk() {
            return sdk;
        }

        public int version() {
            return version;
        }
    }
}
