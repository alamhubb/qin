package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Verifies Qin can materialize npm dependencies declared in qin.config.js
 * without using npm, Node, or Vite.
 */
public final class QinNpmDependencyMaterializerSmokeTestMain {
    private QinNpmDependencyMaterializerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        verifyFileGeneratedDependencyMaterializedLocally();
        verifyLinkedLocalPackageCacheDoesNotDeleteSource();
        verifyFileDependencyOverridesEarlierRemotePackage();
        verifyRemoteDependencyMaterialized();
        System.out.println("QinNpmDependencyMaterializerSmokeTestMain passed.");
    }

    private static void verifyRemoteDependencyMaterialized() throws Exception {
        Path root = Files.createTempDirectory("qin-npm-materializer-smoke-");
        Files.writeString(root.resolve("qin.config.js"), """
                {
                  "name": "qin-npm-materializer-smoke",
                  "dependencies": {
                    "cssts-ts": "0.2.87"
                  }
                }
                """);
        Path nodeModules = root.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules");
        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, nodeModules);

        Path packageJson = nodeModules.resolve("cssts-ts").resolve("package.json");
        Path packageDir = nodeModules.resolve("cssts-ts");
        boolean hasRuntimeEntry = Files.isRegularFile(packageDir.resolve("dist").resolve("index.mjs"))
                || Files.isRegularFile(packageDir.resolve("src").resolve("index.ts"));
        if (!Files.isRegularFile(packageJson) || !hasRuntimeEntry) {
            throw new IllegalStateException("Expected cssts-ts to be materialized under " + nodeModules);
        }
    }

    private static void verifyFileGeneratedDependencyMaterializedLocally() throws Exception {
        Path root = Files.createTempDirectory("qin-npm-materializer-file-smoke-");
        Path generated = root.resolve("generated").resolve("qin-parser-ts");
        Path child = root.resolve("generated-child").resolve("child-parser");
        Files.createDirectories(generated);
        Files.createDirectories(child);
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-npm-materializer-file-smoke",
                  dependencies: {
                    "@qin/generated-qin-parser-ts": "file:./generated/qin-parser-ts"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(generated.resolve("package.json"), """
                {
                  "name": "@qin/generated-qin-parser-ts",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./index.ts",
                  "module": "./index.ts",
                  "dependencies": {
                    "child-parser": "file:../../generated-child/child-parser"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(generated.resolve("index.ts"), "export const parser = true;\n", StandardCharsets.UTF_8);
        Files.createDirectories(generated.resolve("libs"));
        Files.writeString(generated.resolve("libs").resolve("should-not-copy.jar"), "not a runtime npm file\n",
                StandardCharsets.UTF_8);
        Files.writeString(child.resolve("package.json"), """
                {
                  "name": "child-parser",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./index.js"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(child.resolve("index.js"), "export const child = true;\n", StandardCharsets.UTF_8);

        Path nodeModules = root.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules");
        Path staleGeneratedPackage = nodeModules.resolve("@qin").resolve("generated-qin-parser-ts");
        Files.createDirectories(staleGeneratedPackage);
        Files.writeString(staleGeneratedPackage.resolve("package.json"), """
                {
                  "name": "@qin/generated-qin-parser-ts",
                  "version": "stale",
                  "dependencies": {
                    "child-parser": "file:../../generated-child/child-parser"
                  }
                }
                """, StandardCharsets.UTF_8);
        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, nodeModules);

        Path generatedPackage = nodeModules.resolve("@qin").resolve("generated-qin-parser-ts");
        if (!Files.isRegularFile(generatedPackage.resolve("package.json"))
                || !Files.isRegularFile(generatedPackage.resolve("index.ts"))
                || !Files.isRegularFile(generatedPackage.resolve(".qin-source-root"))) {
            throw new IllegalStateException("Expected generated parser file dependency under " + generatedPackage);
        }
        if (Files.exists(generatedPackage.resolve("libs"))) {
            throw new IllegalStateException("Local npm materializer copied JVM libs directory: "
                    + generatedPackage.resolve("libs"));
        }
        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, nodeModules);
        Path childPackage = nodeModules.resolve("child-parser");
        if (!Files.isRegularFile(childPackage.resolve("package.json"))
                || !Files.isRegularFile(childPackage.resolve("index.js"))) {
            throw new IllegalStateException("Expected nested file dependency under " + childPackage);
        }
    }

    private static void verifyLinkedLocalPackageCacheDoesNotDeleteSource() throws Exception {
        Path root = Files.createTempDirectory("qin-npm-materializer-linked-file-smoke-");
        Path source = root.resolve("source-package");
        Files.createDirectories(source);
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-npm-materializer-linked-file-smoke",
                  dependencies: {
                    "linked-local": "file:./source-package"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(source.resolve("package.json"), """
                {
                  "name": "linked-local",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./index.ts"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(source.resolve("index.ts"), "export const linked = true;\n", StandardCharsets.UTF_8);

        Path nodeModules = root.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules");
        Path linkedCache = nodeModules.resolve("linked-local");
        Files.createDirectories(nodeModules);
        createDirectoryLink(linkedCache, source);

        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, nodeModules);

        if (!Files.isRegularFile(source.resolve("package.json"))
                || !Files.isRegularFile(source.resolve("index.ts"))) {
            throw new IllegalStateException("Local package source was deleted through a linked node_modules cache: "
                    + source);
        }
        if (!Files.isRegularFile(linkedCache.resolve("package.json"))
                || !Files.isRegularFile(linkedCache.resolve("index.ts"))
                || !Files.isRegularFile(linkedCache.resolve(".qin-source-root"))) {
            throw new IllegalStateException("Expected linked cache to be replaced with copied package: "
                    + linkedCache);
        }
    }

    private static void verifyFileDependencyOverridesEarlierRemotePackage() throws Exception {
        Path root = Files.createTempDirectory("qin-npm-materializer-file-overrides-remote-smoke-");
        Path parent = root.resolve("parent-package");
        Path source = root.resolve("local-subhuti");
        Files.createDirectories(parent);
        Files.createDirectories(source);
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-npm-materializer-file-overrides-remote-smoke",
                  dependencies: {
                    "parent-package": "file:./parent-package",
                    "subhuti": "file:./local-subhuti"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(parent.resolve("package.json"), """
                {
                  "name": "parent-package",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./index.ts",
                  "dependencies": {
                    "subhuti": "^0.2.86"
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(parent.resolve("index.ts"), "export const parent = true;\n", StandardCharsets.UTF_8);
        Files.writeString(source.resolve("package.json"), """
                {
                  "name": "subhuti",
                  "version": "0.0.0-local",
                  "type": "module",
                  "main": "./dist/index.mjs"
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(source.resolve("index.ts"), "export const local = true;\n", StandardCharsets.UTF_8);
        Files.writeString(source.resolve("qin.config.js"), """
                export default {
                  name: "subhuti",
                  entry: "index.ts"
                }
                """, StandardCharsets.UTF_8);

        Path nodeModules = root.resolve(".qin").resolve("runtime").resolve("npm-host").resolve("node_modules");
        new QinNpmDependencyMaterializer().materializeProjectDependencies(root, nodeModules);

        Path subhuti = nodeModules.resolve("subhuti");
        if (!Files.isRegularFile(subhuti.resolve("index.ts"))
                || !Files.isRegularFile(subhuti.resolve("qin.config.js"))
                || !Files.isRegularFile(subhuti.resolve(".qin-source-root"))) {
            throw new IllegalStateException("Expected top-level file dependency to override earlier remote package: "
                    + subhuti);
        }
    }

    private static void createDirectoryLink(Path link, Path target) throws Exception {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            Process process = new ProcessBuilder(
                    "cmd",
                    "/c",
                    "mklink",
                    "/J",
                    link.toString(),
                    target.toString())
                    .redirectErrorStream(true)
                    .start();
            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IllegalStateException("Failed to create Windows junction for smoke test: " + output);
            }
            return;
        }
        Files.createSymbolicLink(link, target);
    }
}
