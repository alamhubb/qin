package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.QinConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinLspWorkspaceInventorySmokeTestMain {
    private QinLspWorkspaceInventorySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path workspaceRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of("."));

        for (InventoryProject project : inventory()) {
            verifyProject(workspaceRoot, project);
        }

        System.out.println("Qin LSP workspace inventory smoke passed");
    }

    private static void verifyProject(Path workspaceRoot, InventoryProject project) throws Exception {
        Path projectRoot = workspaceRoot.resolve(project.path()).normalize();
        require(projectRoot.startsWith(workspaceRoot), project.id() + " must stay inside workspace");
        require(Files.isDirectory(projectRoot), project.id() + " project directory not found: " + projectRoot);
        require(Files.isRegularFile(projectRoot.resolve("qin.config.js")),
                project.id() + " must be managed by qin.config.js");

        QinConfig config = new ConfigLoader(projectRoot.toString()).load();
        require(project.expectedName().equals(config.name()),
                project.id() + " qin.config.js name mismatch: " + config.name());
        require(config.version() != null && !config.version().isBlank(),
                project.id() + " qin.config.js must declare version");

        if (project.kind() == ProjectKind.LANGUAGE) {
            verifyLanguageProject(project, config);
        } else if (project.kind() == ProjectKind.COMPILER) {
            verifyCompilerProject(project, config);
        } else if (project.kind() == ProjectKind.JAVA_RUNTIME) {
            verifyJavaRuntimeProject(project, config);
        }
    }

    private static void verifyLanguageProject(InventoryProject project, QinConfig config) {
        LanguageConfig language = config.language();
        require(language != null, project.id() + " must declare language metadata");
        require(project.languageId().equals(language.id()),
                project.id() + " language.id mismatch: " + language.id());
        require(project.extension().equals(language.extension()),
                project.id() + " language.extension mismatch: " + language.extension());
        require(language.server() != null && !language.server().isBlank(),
                project.id() + " language.server is required");
        require(language.serverBundle() != null && !language.serverBundle().isBlank(),
                project.id() + " language.serverBundle is required");
        require("tsdown".equals(config.scripts().get("build")),
                project.id() + " language build must be managed by Qin script: " + config.scripts());
        require(config.scripts().containsKey("test"),
                project.id() + " language test script is required");
    }

    private static void verifyCompilerProject(InventoryProject project, QinConfig config) {
        LanguageConfig language = config.language();
        require(language != null, project.id() + " compiler must declare language metadata");
        require(project.languageId().equals(language.id()),
                project.id() + " compiler language.id mismatch: " + language.id());
        require(project.extension().equals(language.extension()),
                project.id() + " compiler language.extension mismatch: " + language.extension());
        require("src/index.ts".equals(language.compiler()),
                project.id() + " compiler language.compiler must point at src/index.ts");
        require("tsdown".equals(config.scripts().get("build")),
                project.id() + " compiler build must be managed by Qin script: " + config.scripts());
        require(config.scripts().containsKey("test"),
                project.id() + " compiler test script is required");
    }

    private static void verifyJavaRuntimeProject(InventoryProject project, QinConfig config) {
        require(config.entry() != null && !config.entry().isBlank(),
                project.id() + " Java/runtime project must declare entry");
        require(config.java() != null, project.id() + " Java/runtime project must declare java config");
        require("UTF-8".equalsIgnoreCase(config.java().encoding()),
                project.id() + " Java/runtime project must use UTF-8 encoding");
        require(config.java().outputDir() != null && config.java().outputDir().contains("classes"),
                project.id() + " Java/runtime project must compile to classes output: "
                        + config.java().outputDir());
    }

    private static List<InventoryProject> inventory() {
        return List.of(
                InventoryProject.language("qin-language", Path.of("qin", "packages", "qin-language"),
                        "qin-language", "qin", ".qin"),
                InventoryProject.language("ovs-language", Path.of("ovsjs", "ovs-language"),
                        "ovs-language", "ovs", ".ovs"),
                InventoryProject.language("cssts-language", Path.of("cssts", "cssts-language"),
                        "cssts-language", "cssts", ".cssts"),
                InventoryProject.compiler("ovs-compiler", Path.of("ovsjs", "ovs", "ovs-compiler"),
                        "ovs-compiler", "ovs", ".ovs"),
                InventoryProject.compiler("cssts-compiler", Path.of("cssts", "cssts", "cssts-compiler"),
                        "cssts-compiler", "cssts", ".cssts"),
                InventoryProject.javaRuntime("qin-parser", Path.of("qin", "packages", "qin-parser"),
                        "com.qin:qin-parser"),
                InventoryProject.javaRuntime("qin-lang-cli", Path.of("qin", "packages", "qin-lang-cli"),
                        "com.qin:qin-lang-cli"),
                InventoryProject.javaRuntime("qin-runtime-core", Path.of("qin", "packages", "qin-runtime-core"),
                        "com.qin:qin-runtime-core"));
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private enum ProjectKind {
        LANGUAGE,
        COMPILER,
        JAVA_RUNTIME
    }

    private record InventoryProject(
            String id,
            Path path,
            String expectedName,
            ProjectKind kind,
            String languageId,
            String extension) {

        static InventoryProject language(
                String id,
                Path path,
                String expectedName,
                String languageId,
                String extension) {
            return new InventoryProject(id, path, expectedName, ProjectKind.LANGUAGE, languageId, extension);
        }

        static InventoryProject compiler(
                String id,
                Path path,
                String expectedName,
                String languageId,
                String extension) {
            return new InventoryProject(id, path, expectedName, ProjectKind.COMPILER, languageId, extension);
        }

        static InventoryProject javaRuntime(String id, Path path, String expectedName) {
            return new InventoryProject(id, path, expectedName, ProjectKind.JAVA_RUNTIME, null, null);
        }
    }
}
