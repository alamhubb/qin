package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.LanguageServerConfig;
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
        assertNoLegacyLocalIdeaClients(workspaceRoot);

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
        } else if (project.kind() == ProjectKind.GENERATED_TS) {
            verifyGeneratedTsProject(project, config);
        } else if (project.kind() == ProjectKind.WORKSPACE) {
            verifyWorkspaceProject(workspaceRoot, project, projectRoot, config);
        } else if (project.kind() == ProjectKind.TOOLING) {
            verifyToolingProject(project, projectRoot, config);
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
        verifyLanguageServerMetadata(project, config);
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

    private static void verifyLanguageServerMetadata(InventoryProject project, QinConfig config) {
        LanguageServerConfig languageServer = config.languageServer();
        require(languageServer != null,
                project.id() + " must declare shared languageServer metadata");
        require(project.extension().equals(languageServer.sourceExtension()),
                project.id() + " languageServer.sourceExtension mismatch: "
                        + languageServer.sourceExtension());
        require(".ts".equals(languageServer.serviceExtension()),
                project.id() + " languageServer.serviceExtension must be .ts");
        require("@qin/generated-qin-parser-ts".equals(languageServer.generatedParserTarget()),
                project.id() + " languageServer.generatedParserTarget must be @qin/generated-qin-parser-ts");
        if ("qin".equals(project.languageId())) {
            require("com.qin:qin-parser".equals(languageServer.parserPackage()),
                    project.id() + " languageServer.parserPackage must be com.qin:qin-parser");
            require(languageServer.compilerPackage() == null || languageServer.compilerPackage().isBlank(),
                    project.id() + " languageServer.compilerPackage must stay blank for Qin");
        } else {
            require(project.expectedCompilerPackage().equals(languageServer.compilerPackage()),
                    project.id() + " languageServer.compilerPackage mismatch: "
                            + languageServer.compilerPackage());
            require(languageServer.parserPackage() == null || languageServer.parserPackage().isBlank(),
                    project.id() + " languageServer.parserPackage must stay blank for compiler-backed languages");
        }
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

    private static void verifyGeneratedTsProject(InventoryProject project, QinConfig config) {
        require(config.entry() != null && !config.entry().isBlank(),
                project.id() + " generated TypeScript project must declare entry");
        require(config.generated() != null,
                project.id() + " generated TypeScript project must declare generated metadata");
        require("com.qin.parser.QinParser".equals(config.generated().entryBinaryName()),
                project.id() + " generated TypeScript entry must be com.qin.parser.QinParser");
    }

    private static void verifyWorkspaceProject(
            Path workspaceRoot,
            InventoryProject project,
            Path projectRoot,
            QinConfig config) {
        require(config.packages().size() == project.workspaceMembers().size(),
                project.id() + " workspace member count mismatch: " + config.packages());
        for (String member : project.workspaceMembers()) {
            require(config.packages().contains(member),
                    project.id() + " qin.config.js must include workspace member " + member);
            Path memberRoot = projectRoot.resolve(member).normalize();
            require(memberRoot.startsWith(workspaceRoot),
                    project.id() + " workspace member must stay inside workspace: " + memberRoot);
            require(Files.isRegularFile(memberRoot.resolve("qin.config.js")),
                    project.id() + " workspace member must be managed by qin.config.js: " + memberRoot);
        }
        LanguageConfig language = config.language();
        require(language != null, project.id() + " workspace must declare language metadata");
        require(project.languageId().equals(language.id()),
                project.id() + " workspace language.id mismatch: " + language.id());
        require(config.scripts().containsKey("build"),
                project.id() + " workspace must declare build script");
        require(config.scripts().containsKey("test"),
                project.id() + " workspace must declare test script");
        for (var script : config.scripts().entrySet()) {
            require(!script.getValue().contains("npm run"),
                    project.id() + " workspace script " + script.getKey()
                            + " must run through Qin project metadata, not npm run forwarding: "
                            + script.getValue());
        }
    }

    private static void verifyToolingProject(InventoryProject project, Path projectRoot, QinConfig config) {
        require(config.entry() != null && !config.entry().isBlank(),
                project.id() + " tooling project must declare entry");
        require(Files.exists(projectRoot.resolve(config.entry()).normalize()),
                project.id() + " tooling entry must exist: " + config.entry());
        LanguageConfig language = config.language();
        require(language != null, project.id() + " tooling project must declare language metadata");
        require(project.languageId().equals(language.id()),
                project.id() + " tooling language.id mismatch: " + language.id());
        if (project.extension() != null) {
            require(project.extension().equals(language.extension()),
                    project.id() + " tooling language.extension mismatch: " + language.extension());
        }
        require(config.scripts().containsKey("build"),
                project.id() + " tooling project must declare build script");
        require(config.scripts().containsKey("test"),
                project.id() + " tooling project must declare test script");
        for (var script : config.scripts().entrySet()) {
            require(!script.getValue().contains("npm run"),
                    project.id() + " tooling script " + script.getKey()
                            + " must run directly through Qin script metadata, not npm run forwarding: "
                            + script.getValue());
        }
        if ("create-ovs".equals(project.id()) || "create-cssts".equals(project.id())) {
            verifyScaffoldTemplateQinConfig(project, projectRoot);
        }
    }

    private static void verifyScaffoldTemplateQinConfig(InventoryProject project, Path projectRoot) {
        Path templateConfig = projectRoot.resolve("template").resolve("qin.config.js").normalize();
        require(Files.isRegularFile(templateConfig),
                project.id() + " scaffold template must include qin.config.js");
        String source;
        try {
            source = Files.readString(templateConfig);
        } catch (Exception e) {
            throw new IllegalStateException(project.id() + " scaffold template qin.config.js must be readable", e);
        }
        require(source.contains("type: \"fullstack\""),
                project.id() + " scaffold template must create a Qin fullstack project");
        require(source.contains("frontend:"),
                project.id() + " scaffold template must declare Qin frontend metadata");
        for (String scriptName : List.of("dev", "build", "preview", "test")) {
            require(source.contains(scriptName + ": "),
                    project.id() + " scaffold template qin.config.js must declare script " + scriptName);
        }
        for (String forbidden : List.of("npm run", "pnpm ", "yarn ")) {
            require(!source.contains(forbidden),
                    project.id() + " scaffold template qin.config.js must not forward to external package scripts: "
                            + forbidden);
        }
    }

    private static List<InventoryProject> inventory() {
        return List.of(
                InventoryProject.workspace("ovs-workspace", Path.of("ovsjs"),
                        "ovsjs-workspace", "ovs", List.of(
                                "ovs/ovs-runtime",
                                "ovs/ovs-compiler",
                                "create-ovs",
                                "vite-plugin-ovs",
                                "ovs-language")),
                InventoryProject.workspace("cssts-workspace", Path.of("cssts"),
                        "cssts-workspace", "cssts", List.of(
                                "cssts/cssts-runtime",
                                "cssts/cssts-compiler",
                                "vite-plugin-cssts",
                                "language-plugin-cssts",
                                "cssts-language",
                                "create-cssts",
                                "cssts-theme-element")),
                InventoryProject.language("qin-language", Path.of("qin", "packages", "qin-language"),
                        "qin-language", "qin", ".qin", null),
                InventoryProject.language("ovs-language", Path.of("ovsjs", "ovs-language"),
                        "ovs-language", "ovs", ".ovs", "ovs-compiler"),
                InventoryProject.language("cssts-language", Path.of("cssts", "cssts-language"),
                        "cssts-language", "cssts", ".cssts", "cssts-compiler"),
                InventoryProject.compiler("ovs-compiler", Path.of("ovsjs", "ovs", "ovs-compiler"),
                        "ovs-compiler", "ovs", ".ovs"),
                InventoryProject.compiler("cssts-compiler", Path.of("cssts", "cssts", "cssts-compiler"),
                        "cssts-compiler", "cssts", ".cssts"),
                InventoryProject.tooling("ovs-runtime", Path.of("ovsjs", "ovs", "ovs-runtime"),
                        "ovsjs", "ovs", null),
                InventoryProject.tooling("vite-plugin-ovs", Path.of("ovsjs", "vite-plugin-ovs"),
                        "vite-plugin-ovs", "ovs", ".ovs"),
                InventoryProject.tooling("create-ovs", Path.of("ovsjs", "create-ovs"),
                        "create-ovs", "ovs", null),
                InventoryProject.tooling("cssts-runtime", Path.of("cssts", "cssts", "cssts-runtime"),
                        "cssts-ts", "cssts", null),
                InventoryProject.tooling("vite-plugin-cssts", Path.of("cssts", "vite-plugin-cssts"),
                        "vite-plugin-cssts", "cssts", ".cssts"),
                InventoryProject.tooling("language-plugin-cssts", Path.of("cssts", "language-plugin-cssts"),
                        "language-plugin-cssts", "cssts", ".cssts"),
                InventoryProject.tooling("create-cssts", Path.of("cssts", "create-cssts"),
                        "create-cssts", "cssts", null),
                InventoryProject.tooling("cssts-theme-element", Path.of("cssts", "cssts-theme-element"),
                        "cssts-theme-element", "cssts", null),
                InventoryProject.generatedTs("qin-generated-parser-ts",
                        Path.of("qin", "packages", "qin-language", "generated", "qin-parser-ts"),
                        "@qin/generated-qin-parser-ts"),
                InventoryProject.javaRuntime("subhuti-java", Path.of("slime", "java-slime", "subhuti-java"),
                        "com.subhuti:subhuti-java"),
                InventoryProject.javaRuntime("java-slime-token", Path.of("slime", "java-slime", "slime-token"),
                        "com.slime:slime-token"),
                InventoryProject.javaRuntime("java-slime-parser", Path.of("slime", "java-slime", "slime-parser"),
                        "com.slime:slime-parser"),
                InventoryProject.javaRuntime("qin-parser", Path.of("qin", "packages", "qin-parser"),
                        "com.qin:qin-parser"),
                InventoryProject.javaRuntime("qin-lang-cli", Path.of("qin", "packages", "qin-lang-cli"),
                        "com.qin:qin-lang-cli"),
                InventoryProject.javaRuntime("qin-runtime-core", Path.of("qin", "packages", "qin-runtime-core"),
                        "com.qin:qin-runtime-core"));
    }

    private static void assertNoLegacyLocalIdeaClients(Path workspaceRoot) throws Exception {
        for (Path languageProject : List.of(
                Path.of("ovsjs", "ovs-language"),
                Path.of("cssts", "cssts-language"))) {
            Path languageRoot = workspaceRoot.resolve(languageProject).normalize();
            require(languageRoot.startsWith(workspaceRoot),
                    languageProject + " must stay inside workspace");
            try (var children = Files.list(languageRoot)) {
                for (Path child : children.toList()) {
                    String name = child.getFileName().toString();
                    require(!name.endsWith("-intellij-client"),
                            "IDEA support must live in qin/packages/qin-idea-plugin-debug pure LSP client, "
                                    + "not legacy local IDEA client project: " + child);
                }
            }
        }
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private enum ProjectKind {
        LANGUAGE,
        COMPILER,
        JAVA_RUNTIME,
        GENERATED_TS,
        WORKSPACE,
        TOOLING
    }

    private record InventoryProject(
            String id,
            Path path,
            String expectedName,
            ProjectKind kind,
            String languageId,
            String extension,
            String expectedCompilerPackage,
            List<String> workspaceMembers) {

        static InventoryProject language(
                String id,
                Path path,
                String expectedName,
                String languageId,
                String extension,
                String expectedCompilerPackage) {
            return new InventoryProject(
                    id,
                    path,
                    expectedName,
                    ProjectKind.LANGUAGE,
                    languageId,
                    extension,
                    expectedCompilerPackage,
                    List.of());
        }

        static InventoryProject compiler(
                String id,
                Path path,
                String expectedName,
                String languageId,
                String extension) {
            return new InventoryProject(
                    id,
                    path,
                    expectedName,
                    ProjectKind.COMPILER,
                    languageId,
                    extension,
                    null,
                    List.of());
        }

        static InventoryProject javaRuntime(String id, Path path, String expectedName) {
            return new InventoryProject(id, path, expectedName, ProjectKind.JAVA_RUNTIME, null, null, null, List.of());
        }

        static InventoryProject generatedTs(String id, Path path, String expectedName) {
            return new InventoryProject(id, path, expectedName, ProjectKind.GENERATED_TS, null, null, null, List.of());
        }

        static InventoryProject workspace(
                String id,
                Path path,
                String expectedName,
                String languageId,
                List<String> workspaceMembers) {
            return new InventoryProject(
                    id,
                    path,
                    expectedName,
                    ProjectKind.WORKSPACE,
                    languageId,
                    null,
                    null,
                    workspaceMembers);
        }

        static InventoryProject tooling(
                String id,
                Path path,
                String expectedName,
                String languageId,
                String extension) {
            return new InventoryProject(id, path, expectedName, ProjectKind.TOOLING, languageId, extension, null, List.of());
        }
    }
}
