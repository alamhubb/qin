package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Path;

/**
 * Verifies bare specifier resolution from node_modules.
 */
public final class NpmBareImportResolverTestMain {
    private NpmBareImportResolverTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinJsSdkTestPaths.resolveNpmBareRoot();
        Path entry = root.resolve("main/main.js").normalize();

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        boolean foundMiniPkg = false;
        for (QinModuleSource module : graph.modules()) {
            String path = module.file().toString().replace('\\', '/');
            if (path.endsWith("/node_modules/mini-pkg/index.js")) {
                foundMiniPkg = true;
                break;
            }
        }

        if (!foundMiniPkg) {
            throw new IllegalStateException("Expected mini-pkg module in graph.");
        }

        System.out.println("NpmBareImportResolverTestMain passed.");
        System.out.println("modules: " + graph.modules().size());
    }
}
