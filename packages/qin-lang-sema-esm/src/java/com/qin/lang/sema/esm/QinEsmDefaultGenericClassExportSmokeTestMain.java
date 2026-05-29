package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinEsmDefaultGenericClassExportSmokeTestMain {
    private QinEsmDefaultGenericClassExportSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-esm-default-generic-class-");
        Path dep = root.resolve("Dep.ts");
        Path main = root.resolve("main.ts");
        Files.writeString(dep, "export default class Dep<T extends object = object> {}\n", StandardCharsets.UTF_8);
        Files.writeString(main, "import Dep from './Dep.ts';\nexport const ok = Dep;\n", StandardCharsets.UTF_8);
        QinModuleGraph graph = new QinModuleGraphBuilder().build(main);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        new QinEsmLinkValidator().validate(model);
        System.out.println("QinEsmDefaultGenericClassExportSmokeTestMain passed.");
    }
}
