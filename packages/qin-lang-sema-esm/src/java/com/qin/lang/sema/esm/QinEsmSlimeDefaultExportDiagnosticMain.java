package com.qin.lang.sema.esm;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.module.resolver.QinModuleSource;

import java.nio.file.Path;

public final class QinEsmSlimeDefaultExportDiagnosticMain {
    private QinEsmSlimeDefaultExportDiagnosticMain() {
    }

    public static void main(String[] args) throws Exception {
        Path entry = args.length > 0
                ? Path.of(args[0])
                : Path.of("D:/project/qkyproject/qinall/slime/slime-parser/src/SlimeParser.ts");
        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);
        for (QinModuleSource source : graph.modules()) {
            if (source.file().toString().replace('\\', '/').endsWith("deprecated/SlimeJavascriptParser.ts")) {
                QinEsmModuleSemantic semantic = model.modules().get(source.file());
                System.out.println("module=" + source.file());
                for (QinEsmExportBinding exportBinding : semantic.exports()) {
                    System.out.println(exportBinding.kind() + " " + exportBinding.exportName() + " local=" + exportBinding.localName());
                }
            }
        }
        new QinEsmLinkValidator().validate(model);
        System.out.println("QinEsmSlimeDefaultExportDiagnosticMain passed.");
    }
}
