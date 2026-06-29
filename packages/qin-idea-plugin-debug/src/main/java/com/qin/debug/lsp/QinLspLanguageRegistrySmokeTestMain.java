package com.qin.debug.lsp;

import com.qin.core.ConfigLoader;
import com.qin.types.LanguageConfig;
import com.qin.types.LanguageServerConfig;
import com.qin.types.QinConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;

public final class QinLspLanguageRegistrySmokeTestMain {
    private QinLspLanguageRegistrySmokeTestMain() {
    }

    public static void main(String[] args) {
        Path workspaceRoot = args.length > 0
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : QinLspLanguageRegistry.resolveWorkspaceRoot(Path.of("."));

        Map<String, String> expectedIds = Map.of(
                "qin", "qin",
                "ovs", "ovs",
                "cssts", "cssts");

        for (Path projectRelativePath : QinLspLanguageRegistry.LANGUAGE_PROJECTS) {
            Path projectRoot = workspaceRoot.resolve(projectRelativePath).normalize();
            QinConfig config = loadConfig(projectRoot);
            LanguageConfig metadata = config.language();
            require(metadata != null, projectRelativePath + " must declare language metadata");
            LanguageServerConfig serverMetadata = config.languageServer();
            require(serverMetadata != null, projectRelativePath + " must declare languageServer metadata");

            String extension = normalizedExtension(metadata.extension());
            QinLspLanguage language = QinLspLanguageRegistry.fromExtension(workspaceRoot, extension);
            require(language != null, "Missing language for ." + extension);
            require(expectedIds.get(extension).equals(language.id()), "Unexpected language id for ." + extension);
            require(metadata.id().equals(language.id()), language.id() + " id must come from qin.config.js");
            require(extension.equals(language.extension()), language.id() + " extension must come from qin.config.js");
            require(normalizedExtension(serverMetadata.sourceExtension()).equals(language.extension()),
                    language.id() + " extension must come from qin.config.js languageServer.sourceExtension");
            require(serverMetadata.serviceExtension().equals(language.serviceExtension()),
                    language.id() + " service extension must come from qin.config.js languageServer");
            require(serverMetadata.generatedParserTarget().equals(language.generatedParserTarget()),
                    language.id() + " generated parser target must come from qin.config.js languageServer");
            require(equalsNullable(serverMetadata.parserPackage(), language.parserPackage()),
                    language.id() + " parser package must come from qin.config.js languageServer");
            require(equalsNullable(serverMetadata.compilerPackage(), language.compilerPackage()),
                    language.id() + " compiler package must come from qin.config.js languageServer");
            require(projectRelativePath.equals(language.projectRelativePath()),
                    language.id() + " project root must come from registry language project inventory");
            require(Path.of(metadata.serverBundle()).equals(language.serverBundlePath()),
                    language.id() + " server bundle must come from qin.config.js language.serverBundle");
            require(language.matchesExtension(extension.toUpperCase()), "Extension match must be case-insensitive");

            Path serverPath = language.resolveServerPath(workspaceRoot);
            require(Files.isRegularFile(serverPath), "Missing server bundle: " + serverPath);
            require(serverPath.startsWith(workspaceRoot), "Server bundle must stay inside workspace: " + serverPath);
        }

        require(QinLspLanguageRegistry.fromExtension(workspaceRoot, "txt") == null, "Unexpected language for .txt");
        assertWorkspaceRootUsesQinConfigInventory();
        assertNoHardcodedServerBundles(Path.of("src", "main", "java", "com", "qin", "debug", "lsp"));
        System.out.println("Qin LSP language registry smoke passed");
    }

    private static QinConfig loadConfig(Path projectRoot) {
        try {
            return new ConfigLoader(projectRoot.toString()).load();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load " + projectRoot.resolve("qin.config.js"), e);
        }
    }

    private static void assertNoHardcodedServerBundles(Path lspSourceRoot) {
        try (var files = Files.walk(lspSourceRoot)) {
            for (Path sourceFile : files.filter(Files::isRegularFile).toList()) {
                if (sourceFile.getFileName().toString().endsWith("SmokeTestMain.java")) {
                    continue;
                }
                String source = Files.readString(sourceFile);
                require(!source.contains("dist/language-server") && !source.contains("dist\\language-server"),
                        "IDEA LSP production code must resolve language.serverBundle from qin.config.js, "
                                + "not hardcode server bundle paths: " + sourceFile);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to inspect IDEA LSP source files under " + lspSourceRoot, e);
        }
    }

    private static void assertWorkspaceRootUsesQinConfigInventory() {
        Path tempRoot = null;
        try {
            tempRoot = Files.createTempDirectory("qin-lsp-workspace-root-");
            for (Path projectRelativePath : QinLspLanguageRegistry.LANGUAGE_PROJECTS) {
                Path projectRoot = tempRoot.resolve(projectRelativePath).normalize();
                Files.createDirectories(projectRoot);
                Files.writeString(projectRoot.resolve("qin.config.js"), "export default {}\n");
            }

            Path nestedBasePath = tempRoot.resolve("qin")
                    .resolve("packages")
                    .resolve("qin-language")
                    .resolve("src")
                    .normalize();
            Files.createDirectories(nestedBasePath);
            Path resolved = QinLspLanguageRegistry.resolveWorkspaceRoot(nestedBasePath);
            require(tempRoot.equals(resolved),
                    "Workspace root must resolve from qin.config.js inventory without requiring built dist bundles");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to verify qin.config.js workspace root inventory", e);
        } finally {
            if (tempRoot != null) {
                deleteRecursively(tempRoot);
            }
        }
    }

    private static void deleteRecursively(Path root) {
        try (var paths = Files.walk(root)) {
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to clean temporary workspace root fixture: " + root, e);
        }
    }

    private static String normalizedExtension(String extension) {
        require(extension != null && !extension.isBlank(), "language.extension is required");
        return extension.startsWith(".") ? extension.substring(1) : extension;
    }

    private static boolean equalsNullable(String left, String right) {
        return left == null ? right == null : left.equals(right);
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
