package com.qin.lang.module.resolver;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmPackageExportsSubpathSmokeTestMain {
    private QinEsmPackageExportsSubpathSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-exports-subpath-");
        Path packageDir = root.resolve("node_modules").resolve("rolldown");
        Files.createDirectories(packageDir.resolve("dist"));
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "rolldown",
                  "type": "module",
                  "exports": {
                    ".": "./dist/index.mjs",
                    "./parseAst": "./dist/parse-ast-index.mjs"
                  },
                  "imports": {
                    "#module-sync-enabled": {
                      "module-sync": "./misc/true.js",
                      "default": "./misc/false.js"
                    }
                  }
                }
                """);
        Files.createDirectories(packageDir.resolve("misc"));
        Path expected = packageDir.resolve("dist").resolve("parse-ast-index.mjs").toAbsolutePath().normalize();
        Files.writeString(expected, "export function parseAst() { return {}; }\n");
        Path expectedImport = packageDir.resolve("misc").resolve("false.js").toAbsolutePath().normalize();
        Files.writeString(packageDir.resolve("misc").resolve("true.js"), "export default true;\n");
        Files.writeString(expectedImport, "export default false;\n");
        Path importer = root.resolve("importer.js");
        Files.writeString(importer, "import { parseAst } from 'rolldown/parseAst';\n");
        Path packageImporter = packageDir.resolve("dist").resolve("node.js");
        Files.writeString(packageImporter, "import enabled from '#module-sync-enabled';\n");
        Path selfReferenceImporter = packageDir.resolve("dist").resolve("plugin.js");
        Files.writeString(selfReferenceImporter, "import rolldown from 'rolldown';\n");
        Path expectedSelfReference = packageDir.resolve("dist").resolve("index.mjs").toAbsolutePath().normalize();
        Files.writeString(expectedSelfReference, "export default 'self';\n");
        Path projectPackageDir = root.resolve("node_modules").resolve("@vitejs").resolve("plugin-vue");
        Path qinHostPackageDir = root.resolve(".qin")
                .resolve("runtime")
                .resolve("npm-host")
                .resolve("node_modules")
                .resolve("@vitejs")
                .resolve("plugin-vue");
        Files.createDirectories(projectPackageDir);
        Files.createDirectories(qinHostPackageDir);
        Files.writeString(projectPackageDir.resolve("package.json"), """
                { "name": "@vitejs/plugin-vue", "type": "module", "exports": "./project.mjs" }
                """);
        Files.writeString(projectPackageDir.resolve("project.mjs"), "export default 'project';\n");
        Files.writeString(qinHostPackageDir.resolve("package.json"), """
                { "name": "@vitejs/plugin-vue", "type": "module", "exports": "./qin-host.mjs" }
                """);
        Path expectedQinHost = qinHostPackageDir.resolve("qin-host.mjs").toAbsolutePath().normalize();
        Files.writeString(expectedQinHost, "export default 'qin-host';\n");
        Path viteConfig = root.resolve("vite.config.js");
        Files.writeString(viteConfig, "import vue from '@vitejs/plugin-vue';\n");

        Path resolved = new QinEsmSpecifierResolver().resolveModule(importer, "rolldown/parseAst");
        if (!expected.equals(resolved)) {
            throw new IllegalStateException("Expected exports subpath to resolve to " + expected
                    + ", got " + resolved);
        }
        Path resolvedImport = new QinEsmSpecifierResolver().resolveModule(packageImporter, "#module-sync-enabled");
        if (!expectedImport.equals(resolvedImport)) {
            throw new IllegalStateException("Expected package import to resolve to " + expectedImport
                    + ", got " + resolvedImport);
        }
        Path resolvedSelfReference = new QinEsmSpecifierResolver().resolveModule(selfReferenceImporter, "rolldown");
        if (!expectedSelfReference.equals(resolvedSelfReference)) {
            throw new IllegalStateException("Expected package self-reference to resolve to " + expectedSelfReference
                    + ", got " + resolvedSelfReference);
        }
        Path resolvedSelfReferenceFromPackageDir = new QinEsmSpecifierResolver().resolveModule(packageDir, "rolldown");
        if (!expectedSelfReference.equals(resolvedSelfReferenceFromPackageDir)) {
            throw new IllegalStateException("Expected package directory self-reference to resolve to "
                    + expectedSelfReference + ", got " + resolvedSelfReferenceFromPackageDir);
        }
        Path resolvedQinHost = new QinEsmSpecifierResolver().resolveModule(viteConfig, "@vitejs/plugin-vue");
        if (!expectedQinHost.equals(resolvedQinHost)) {
            throw new IllegalStateException("Expected Qin npm-host package to take precedence over project node_modules: "
                    + expectedQinHost + ", got " + resolvedQinHost);
        }
        if (!QinEsmSpecifierResolver.isHostRuntimeModule("module")) {
            throw new IllegalStateException("Expected Node module builtin to be treated as a host runtime module");
        }
        if (!QinEsmSpecifierResolver.isHostRuntimeModule("zlib")
                || !QinEsmSpecifierResolver.isHostRuntimeModule("stream")
                || !QinEsmSpecifierResolver.isHostRuntimeModule("crypto")) {
            throw new IllegalStateException("Expected common Node builtins to be treated as host runtime modules");
        }

        System.out.println("QinEsmPackageExportsSubpathSmokeTestMain OK");
    }
}
