package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmCjsDefaultInteropSmokeTestMain {
    private QinEsmCjsDefaultInteropSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cjs-default-interop-");
        Path packageDir = root.resolve("node_modules").resolve("picomatch");
        Files.createDirectories(packageDir);
        Files.writeString(packageDir.resolve("package.json"), """
                {
                  "name": "picomatch",
                  "main": "index.js"
                }
                """);
        Files.writeString(packageDir.resolve("index.js"), """
                function picomatch() {}
                module.exports = picomatch;
                """);
        Path entry = root.resolve("entry.js");
        Files.writeString(entry, """
                import picomatch from "picomatch";
                export const value = picomatch;
                """);

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        Path cjsModule = packageDir.resolve("index.js").toAbsolutePath().normalize();
        QinEsmModuleSemantic cjsSemantic = model.modules().get(cjsModule);
        if (cjsSemantic == null) {
            throw new IllegalStateException("Expected CJS module in semantic model. Modules: " + model.modules().keySet());
        }
        String cjsSource = graph.modules().stream()
                .filter(module -> cjsModule.equals(module.file()))
                .findFirst()
                .map(module -> module.source())
                .orElse("");
        if (cjsSemantic.exports().stream().noneMatch(exportBinding -> "default".equals(exportBinding.exportName()))) {
            throw new IllegalStateException("Expected CJS module to expose default export, got: "
                    + cjsSemantic.exports() + ", source=" + cjsSource);
        }
        new QinEsmLinkValidator().validate(model);

        System.out.println("QinEsmCjsDefaultInteropSmokeTestMain OK");
    }
}
