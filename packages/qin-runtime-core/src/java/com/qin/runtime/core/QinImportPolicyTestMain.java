package com.qin.runtime.core;

import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJsImport;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrObjectLiteral;
import com.qin.lang.ir.QinIrObjectProperty;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.module.policy.QinImportPolicyChecker;
import com.qin.lang.module.policy.QinImportPolicyException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Manual smoke test for zone-based import policy.
 */
public final class QinImportPolicyTestMain {
    private QinImportPolicyTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path exampleRoot = resolveExampleRoot();
        QinModuleLinker linker = new QinModuleLinker();
        QinImportPolicyChecker checker = new QinImportPolicyChecker();
        QinIrValidator validator = new QinIrValidator();

        expectPolicyPass(linker, checker, exampleRoot, QinImportPolicyTestConstants.FRONTEND_OK);
        expectPolicyPass(linker, checker, exampleRoot, QinImportPolicyTestConstants.BACKEND_OK);
        expectPolicyPass(linker, checker, exampleRoot, QinImportPolicyTestConstants.BACKEND_BAD_JS);
        expectPolicyPass(linker, checker, exampleRoot, QinImportPolicyTestConstants.SHARED_OK);

        expectPolicyFail(linker, checker, exampleRoot, QinImportPolicyTestConstants.FRONTEND_BAD_JAVA, "QIN1001");
        expectPolicyFail(linker, checker, exampleRoot, QinImportPolicyTestConstants.SHARED_BAD_JS, "QIN1003");
        expectPolicyFail(linker, checker, exampleRoot, QinImportPolicyTestConstants.SHARED_BAD_JAVA, "QIN1003");

        QinIrProgram jsImportProgram = buildJsImportProgram();
        validator.validate(jsImportProgram, QinBuildTarget.JS);
        validator.validate(jsImportProgram, QinBuildTarget.JVM);

        System.out.println("Import policy smoke test passed.");
        System.out.println("Example root: " + exampleRoot.toAbsolutePath());
    }

    private static void expectPolicyPass(
            QinModuleLinker linker,
            QinImportPolicyChecker checker,
            Path root,
            String relativeFile) throws Exception {
        Path sourceFile = root.resolve(relativeFile).normalize();
        QinLinkedSource linked = linker.link(sourceFile);
        checker.validate(root, linked.imports());
    }

    private static void expectPolicyFail(
            QinModuleLinker linker,
            QinImportPolicyChecker checker,
            Path root,
            String relativeFile,
            String expectedRuleCode) throws Exception {
        Path sourceFile = root.resolve(relativeFile).normalize();
        try {
            QinLinkedSource linked = linker.link(sourceFile);
            checker.validate(root, linked.imports());
        } catch (QinImportPolicyException ex) {
            boolean matched = ex.violations().stream()
                    .anyMatch(v -> expectedRuleCode.equals(v.ruleCode()));
            if (!matched) {
                throw new IllegalStateException(
                        "Expected rule code " + expectedRuleCode + " but got: " + ex.getMessage(), ex);
            }
            return;
        }
        throw new IllegalStateException("Expected policy failure for: " + sourceFile.toAbsolutePath());
    }

    private static QinIrProgram buildJsImportProgram() {
        QinIrConstDeclaration declaration = new QinIrConstDeclaration(
                "result",
                new QinIrObjectLiteral(List.of(new QinIrObjectProperty("age", new QinIrNumberLiteral(1)))));
        QinIrJsImport jsImport = new QinIrJsImport("./helper.js", "utilValue", "utilValue");
        return new QinIrProgram(
                List.of(declaration),
                List.of(),
                List.of(),
                List.of(jsImport),
                List.of(),
                List.of(),
                List.of());
    }

    private static Path resolveExampleRoot() {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path[] candidates = new Path[] {
                cwd.resolve(QinImportPolicyTestConstants.EXAMPLE_ROOT),
                cwd.resolve(QinImportPolicyTestConstants.ALT_EXAMPLE_ROOT),
                cwd.resolve(QinImportPolicyTestConstants.ALT_EXAMPLE_ROOT_2)
        };
        for (Path candidate : candidates) {
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Cannot locate import-policy examples directory");
    }
}
