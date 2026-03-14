package com.qin.runtime.core;

import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.sema.esm.QinEsmExportKind;
import com.qin.lang.sema.esm.QinEsmImportKind;
import com.qin.lang.sema.esm.QinEsmModuleSemantic;
import com.qin.lang.sema.esm.QinEsmSemanticAnalyzer;
import com.qin.lang.sema.esm.QinEsmSemanticModel;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

/**
 * Matrix test for ESM import/export syntax coverage.
 */
public final class EsmSyntaxMatrixTestMain {
    private EsmSyntaxMatrixTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinEsmTestPaths.resolveStage1Root();
        Path entry = root.resolve("main/main.js").normalize().toAbsolutePath();

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinEsmSemanticModel model = new QinEsmSemanticAnalyzer().analyze(graph);

        EnumSet<QinEsmImportKind> importKinds = EnumSet.noneOf(QinEsmImportKind.class);
        EnumSet<QinEsmExportKind> exportKinds = EnumSet.noneOf(QinEsmExportKind.class);
        for (QinEsmModuleSemantic module : model.modules().values()) {
            module.imports().forEach(i -> importKinds.add(i.kind()));
            module.exports().forEach(e -> exportKinds.add(e.kind()));
        }

        requireAll(importKinds, Set.of(
                QinEsmImportKind.NAMED,
                QinEsmImportKind.DEFAULT,
                QinEsmImportKind.NAMESPACE));
        requireAll(exportKinds, Set.of(
                QinEsmExportKind.LOCAL_NAMED,
                QinEsmExportKind.LOCAL_DEFAULT,
                QinEsmExportKind.RE_EXPORT_NAMED,
                QinEsmExportKind.RE_EXPORT_ALL,
                QinEsmExportKind.RE_EXPORT_NAMESPACE));

        System.out.println("EsmSyntaxMatrixTestMain passed.");
        System.out.println("import kinds: " + importKinds);
        System.out.println("export kinds: " + exportKinds);
    }

    private static <E extends Enum<E>> void requireAll(Set<E> actual, Set<E> expected) {
        if (!actual.containsAll(expected)) {
            throw new IllegalStateException("Missing expected kinds. expected=" + expected + ", actual=" + actual);
        }
    }
}

