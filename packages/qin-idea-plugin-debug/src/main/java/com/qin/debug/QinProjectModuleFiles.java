package com.qin.debug;

import com.qin.bsp.BspHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.qin.constants.QinConstants.DEFAULT_SOURCE_DIR;
import static com.qin.constants.QinConstants.IML_EXCLUDED_DIRS;

public final class QinProjectModuleFiles {
    private QinProjectModuleFiles() {
    }

    public static void generateImlFile(Path projectPath, boolean forceOverwrite) {
        generateImlFile(projectPath, forceOverwrite, null);
    }

    public static void generateImlFile(Path projectPath, boolean forceOverwrite, Path ideaDir) {
        try {
            if (!hasSourceDirectory(projectPath)) {
                QinLogger.info("[iml] Skipping .iml generation for source-less aggregate project: " + projectPath);
                return;
            }

            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");

            QinLogger.info("[iml] Processing project: " + projectPath);
            QinLogger.info("[iml]   iml path: " + imlPath);
            QinLogger.info("[iml]   forceOverwrite: " + forceOverwrite);

            boolean needGenerate = !Files.exists(imlPath) || forceOverwrite;

            if (!needGenerate) {
                repairExistingImlIfNeeded(projectPath, imlPath);
            } else {
                writeImlFromBsp(projectPath, projectName, imlPath);
            }

            if (ideaDir != null) {
                registerModuleToIdeaProject(imlPath, ideaDir);
            }
        } catch (Exception e) {
            QinLogger.error("Failed to generate .iml file: " + e.getMessage());
        }
    }

    public static boolean hasSourceDirectory(Path projectPath) {
        try {
            BspHandler bspHandler = new BspHandler(projectPath.toString());
            String configuredSourceDir = bspHandler.getSourceDir();
            if (configuredSourceDir != null && !configuredSourceDir.isBlank()) {
                Path configuredPath = projectPath.resolve(configuredSourceDir);
                if (Files.exists(configuredPath) && Files.isDirectory(configuredPath)) {
                    return true;
                }
            }
        } catch (Exception e) {
            QinLogger.info("[iml] Failed to inspect configured sourceDir, falling back to directory detection: "
                    + e.getMessage());
        }

        return detectSourceDir(projectPath) != null;
    }

    public static void updateImlLanguageLevel(Path projectPath, String javaVersion) {
        try {
            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");
            if (!Files.exists(imlPath)) {
                return;
            }

            String content = Files.readString(imlPath);
            String languageLevel = "JDK_" + javaVersion;

            if (content.contains("LANGUAGE_LEVEL=")) {
                content = content.replaceAll("LANGUAGE_LEVEL=\"[^\"]*\"",
                        "LANGUAGE_LEVEL=\"" + languageLevel + "\"");
            } else if (content.contains("<component name=\"NewModuleRootManager\"")) {
                content = content.replace(
                        "<component name=\"NewModuleRootManager\"",
                        "<component name=\"NewModuleRootManager\" LANGUAGE_LEVEL=\"" + languageLevel + "\"");
            }

            Files.writeString(imlPath, content);
            QinLogger.info("[iml]   Updated .iml LANGUAGE_LEVEL to " + languageLevel);
        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to update .iml LANGUAGE_LEVEL: " + e.getMessage());
        }
    }

    public static boolean needsRepair(Path projectPath, Path ideaDir) {
        try {
            if (!hasSourceDirectory(projectPath)) {
                QinLogger.info("[iml]   No source directory detected, skipping IDEA module repair check: "
                        + projectPath.getFileName());
                return false;
            }

            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");
            if (!Files.exists(imlPath)) {
                QinLogger.info("[iml]   Missing .iml file: " + imlPath);
                return true;
            }

            String imlContent = Files.readString(imlPath);
            if (!imlContent.contains("<sourceFolder ")) {
                QinLogger.info("[iml]   Missing sourceFolder in .iml: " + imlPath);
                return true;
            }

            if (ideaDir == null || ideaDir.getParent() == null) {
                QinLogger.info("[iml]   IDEA project directory is unavailable for module repair check");
                return true;
            }

            Path modulesXml = ideaDir.resolve("modules.xml");
            if (!Files.exists(modulesXml)) {
                QinLogger.info("[iml]   Missing IDEA modules.xml: " + modulesXml);
                return true;
            }

            String modulesContent = Files.readString(modulesXml);
            Path relativeImlPath = ideaDir.getParent().relativize(imlPath);
            String moduleEntry = relativeImlPath.toString().replace("\\", "/");
            if (!modulesContent.contains(moduleEntry)) {
                QinLogger.info("[iml]   Module entry missing from modules.xml: " + moduleEntry);
                return true;
            }

            return hasMissingClasspathEntries(projectPath, imlContent);
        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to validate IDEA module metadata: " + e.getMessage());
            return true;
        }
    }

    private static void repairExistingImlIfNeeded(Path projectPath, Path imlPath) throws Exception {
        QinLogger.info("[iml]   Existing .iml found, checking whether repair is needed...");
        String existingContent = Files.readString(imlPath);
        if (!existingContent.contains("<sourceFolder")) {
            QinLogger.info("[iml]   Missing sourceFolder configuration, attempting repair...");
            String fixedContent = fixMissingSourceFolder(existingContent, projectPath);
            if (fixedContent != null && !fixedContent.equals(existingContent)) {
                Files.writeString(imlPath, fixedContent);
                QinLogger.info("[iml]   sourceFolder configuration repaired");
            }
        } else {
            QinLogger.info("[iml]   sourceFolder configuration already present");
        }
    }

    private static void writeImlFromBsp(Path projectPath, String projectName, Path imlPath) throws Exception {
        BspHandler bspHandler = new BspHandler(projectPath.toString());

        String sourceDir = bspHandler.getSourceDir();
        String testDir = bspHandler.getTestDir();

        if (sourceDir == null || !Files.exists(projectPath.resolve(sourceDir))) {
            sourceDir = detectSourceDir(projectPath);
        }
        QinLogger.info("[iml]   sourceDir: " + sourceDir);
        QinLogger.info("[iml]   testDir: " + testDir);

        if (sourceDir == null) {
            QinLogger.info("[iml]   Source directory not found");
            return;
        }

        String outputDir = bspHandler.getOutputDir();
        QinLogger.info("[iml]   outputDir: " + outputDir);

        StringBuilder excludeFolders = new StringBuilder();
        for (String excludeDir : IML_EXCLUDED_DIRS) {
            excludeFolders.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                    .append(excludeDir)
                    .append("\" />\n");
        }

        StringBuilder sourceFolders = new StringBuilder();
        sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(sourceDir)
                .append("\" isTestSource=\"false\" />\n");
        if (testDir != null && Files.exists(projectPath.resolve(testDir))) {
            sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(testDir)
                    .append("\" isTestSource=\"true\" />\n");
        }

        List<String> classpath = bspHandler.getClasspath();
        StringBuilder dependencyEntries = dependencyEntries(classpath);

        String imlContent = """
                <?xml version="1.0" encoding="UTF-8"?>
                <module type="JAVA_MODULE" version="4">
                  <component name="NewModuleRootManager" inherit-compiler-output="false">
                    <exclude-output />
                    <output url="file://$MODULE_DIR$/%s" />
                    <output-test url="file://$MODULE_DIR$/%s" />
                    <content url="file://$MODULE_DIR$">
                %s%s    </content>
                    <orderEntry type="inheritedJdk" />
                    <orderEntry type="sourceFolder" forTests="false" />
                %s  </component>
                </module>
                """.formatted(outputDir, outputDir.replace("classes", "test-classes"),
                sourceFolders.toString(), excludeFolders.toString(), dependencyEntries.toString());

        Files.writeString(imlPath, imlContent);
        QinLogger.info("Generated .iml file via BSP: " + projectName + ".iml");
    }

    private static StringBuilder dependencyEntries(List<String> classpath) {
        StringBuilder dependencyEntries = new StringBuilder();

        for (String path : classpath) {
            String entryPath = path.replace("\\", "/");

            if (entryPath.endsWith(".jar")) {
                appendJarDependency(dependencyEntries, entryPath);
            } else {
                appendLocalDependency(dependencyEntries, entryPath);
            }
        }

        return dependencyEntries;
    }

    private static void appendJarDependency(StringBuilder dependencyEntries, String entryPath) {
        String sourcesPath = findSourcesJar(entryPath);
        String javadocPath = findJavadocJar(entryPath);

        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                .append("      <library>\n")
                .append("        <CLASSES>\n")
                .append("          <root url=\"jar://").append(entryPath).append("!/\" />\n")
                .append("        </CLASSES>\n");

        if (javadocPath != null) {
            dependencyEntries.append("        <JAVADOC>\n")
                    .append("          <root url=\"jar://").append(javadocPath).append("!/\" />\n")
                    .append("        </JAVADOC>\n");
        } else {
            dependencyEntries.append("        <JAVADOC />\n");
        }

        if (sourcesPath != null) {
            dependencyEntries.append("        <SOURCES>\n")
                    .append("          <root url=\"jar://").append(sourcesPath).append("!/\" />\n")
                    .append("        </SOURCES>\n");
        } else {
            dependencyEntries.append("        <SOURCES />\n");
        }

        dependencyEntries.append("      </library>\n")
                .append("    </orderEntry>\n");
        QinLogger.info("[iml]   Added JAR dependency: " + entryPath
                + (sourcesPath != null ? " (+sources)" : "")
                + (javadocPath != null ? " (+javadoc)" : ""));
    }

    private static void appendLocalDependency(StringBuilder dependencyEntries, String entryPath) {
        String sourcePath = computeSourcePath(entryPath);

        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                .append("      <library>\n")
                .append("        <CLASSES>\n")
                .append("          <root url=\"file://").append(entryPath).append("\" />\n")
                .append("        </CLASSES>\n");

        if (sourcePath != null) {
            dependencyEntries.append("        <SOURCES>\n")
                    .append("          <root url=\"file://").append(sourcePath).append("\" />\n")
                    .append("        </SOURCES>\n");
            QinLogger.info("[iml]   Added local classpath entry: " + entryPath + " (sources: " + sourcePath + ")");
        } else {
            QinLogger.info("[iml]   Added local classpath entry: " + entryPath + " (sources not found)");
        }

        dependencyEntries.append("      </library>\n")
                .append("    </orderEntry>\n");
    }

    private static String findSourcesJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        String basePath = jarPath.substring(0, jarPath.length() - 4);
        String sourcesPath = basePath + "-sources.jar";

        if (Files.exists(Paths.get(sourcesPath))) {
            return sourcesPath.replace("\\", "/");
        }

        return null;
    }

    private static String findJavadocJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        String basePath = jarPath.substring(0, jarPath.length() - 4);
        String javadocPath = basePath + "-javadoc.jar";

        if (Files.exists(Paths.get(javadocPath))) {
            return javadocPath.replace("\\", "/");
        }

        return null;
    }

    private static void registerModuleToIdeaProject(Path imlPath, Path ideaDir) {
        try {
            Path modulesXml = ideaDir.resolve("modules.xml");
            Path ideaParent = ideaDir.getParent();
            Path relativePath = ideaParent.relativize(imlPath);
            String moduleEntry = relativePath.toString().replace("\\", "/");

            String content;
            if (!Files.exists(modulesXml)) {
                QinLogger.info("[iml]   modules.xml not found, creating a new file");
                content = """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project version="4">
                          <component name="ProjectModuleManager">
                            <modules>
                            </modules>
                          </component>
                        </project>
                        """;
            } else {
                content = Files.readString(modulesXml);
            }

            if (content.contains(moduleEntry)) {
                QinLogger.info("[iml]   Module already registered in modules.xml");
                return;
            }

            String newModule = String.format(
                    "      <module fileurl=\"file://$PROJECT_DIR$/%s\" filepath=\"$PROJECT_DIR$/%s\" />",
                    moduleEntry, moduleEntry);
            String newContent = content.replace("    </modules>", newModule + "\n    </modules>");

            Files.writeString(modulesXml, newContent);
            QinLogger.info("[iml]   Registered module in modules.xml: " + moduleEntry);
        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to update modules.xml: " + e.getMessage());
        }
    }

    private static String detectSourceDir(Path projectPath) {
        Path mavenSrc = projectPath.resolve(DEFAULT_SOURCE_DIR);
        if (Files.exists(mavenSrc)) {
            return DEFAULT_SOURCE_DIR;
        }
        Path simpleSrc = projectPath.resolve("src");
        if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            return "src";
        }
        return null;
    }

    private static String computeSourcePath(String classPath) {
        try {
            Path classDir = Paths.get(classPath);
            Path current = classDir;
            while (current != null && !current.getFileName().toString().equals("build")) {
                current = current.getParent();
            }

            if (current != null && current.getParent() != null) {
                Path projectRoot = current.getParent();

                Path mavenSrc = projectRoot.resolve(DEFAULT_SOURCE_DIR);
                if (Files.exists(mavenSrc)) {
                    return mavenSrc.toString().replace("\\", "/");
                }

                Path simpleSrc = projectRoot.resolve("src");
                if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
                    return simpleSrc.toString().replace("\\", "/");
                }
            }
        } catch (Exception e) {
            // Keep module generation best-effort for unusual classpath entries.
        }
        return null;
    }

    private static boolean hasMissingClasspathEntries(Path projectPath, String imlContent) {
        try {
            BspHandler bspHandler = new BspHandler(projectPath.toString());
            List<String> classpath = bspHandler.getClasspath();
            if (classpath == null || classpath.isEmpty()) {
                return false;
            }

            for (String path : classpath) {
                if (path == null || path.isBlank()) {
                    continue;
                }
                String entryPath = path.replace("\\", "/");
                String expectedRoot = entryPath.endsWith(".jar")
                        ? "jar://" + entryPath + "!/"
                        : "file://" + entryPath;
                if (!imlContent.contains(expectedRoot)) {
                    QinLogger.info("[iml]   Missing classpath entry in .iml: " + expectedRoot);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to validate classpath entries in .iml: " + e.getMessage());
            return true;
        }
    }

    private static String fixMissingSourceFolder(String imlContent, Path projectPath) {
        try {
            String sourceDir = detectSourceDir(projectPath);
            if (sourceDir == null) {
                QinLogger.info("[iml]   Could not detect source directory, skipping sourceFolder repair");
                return imlContent;
            }

            Pattern selfClosingPattern = Pattern.compile("<content\\s+url=\"[^\"]*\"\\s*/>");
            Matcher matcher = selfClosingPattern.matcher(imlContent);

            if (matcher.find()) {
                String originalTag = matcher.group();
                int urlStart = originalTag.indexOf("url=\"") + 5;
                int urlEnd = originalTag.indexOf("\"", urlStart);
                String url = originalTag.substring(urlStart, urlEnd);

                StringBuilder newContent = new StringBuilder();
                newContent.append("<content url=\"").append(url).append("\">\n");
                newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                        .append(sourceDir).append("\" isTestSource=\"false\" />\n");

                Path testDir = projectPath.resolve("src/test/java");
                if (Files.exists(testDir)) {
                    newContent.append(
                            "      <sourceFolder url=\"file://$MODULE_DIR$/src/test/java\" isTestSource=\"true\" />\n");
                }

                for (String excludeDir : IML_EXCLUDED_DIRS) {
                    newContent.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                            .append(excludeDir).append("\" />\n");
                }

                newContent.append("    </content>");

                return imlContent.replace(originalTag, newContent.toString());
            }

            return imlContent;
        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to repair sourceFolder configuration: " + e.getMessage());
            return imlContent;
        }
    }
}
