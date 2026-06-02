package com.qin.lang.module.resolver;

import com.qin.lang.module.policy.QinImportKind;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmViteVirtualSpecifierSmokeTestMain {
    private QinEsmViteVirtualSpecifierSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-vite-virtual-resolver-");
        Path importer = root.resolve("importer.js");
        Files.writeString(importer, "import value from 'virtual:qin-message';\n");

        QinEsmSpecifierResolver resolver = new QinEsmSpecifierResolver();
        if (resolver.resolveModule(importer, "virtual:qin-message") != null) {
            throw new IllegalStateException("Expected virtual: specifier to stay unresolved for plugin load");
        }
        if (resolver.resolveModule(importer, "\0qin-message") != null) {
            throw new IllegalStateException("Expected \\0 virtual specifier to stay unresolved for plugin load");
        }
        if (QinImportKind.fromSpecifier("virtual:qin-message") != QinImportKind.VIRTUAL) {
            throw new IllegalStateException("Expected virtual: specifier to be classified as VIRTUAL");
        }
        if (QinImportKind.fromSpecifier("\0qin-message") != QinImportKind.VIRTUAL) {
            throw new IllegalStateException("Expected \\0 specifier to be classified as VIRTUAL");
        }
        QinModuleGraph graph = new QinModuleGraphBuilder().build(importer);
        if (graph.modules().size() != 1) {
            throw new IllegalStateException("Expected graph builder not to visit virtual modules, got "
                    + graph.modules().size());
        }

        System.out.println("QinEsmViteVirtualSpecifierSmokeTestMain OK");
    }
}
