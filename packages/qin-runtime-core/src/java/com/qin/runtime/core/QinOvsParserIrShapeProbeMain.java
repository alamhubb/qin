package com.qin.runtime.core;

import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public final class QinOvsParserIrShapeProbeMain {
    private QinOvsParserIrShapeProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = args.length > 0 && !args[0].isBlank()
                ? Path.of(args[0]).toAbsolutePath().normalize()
                : Path.of("D:/project/qkyproject/qinall/balance-monitoring").toAbsolutePath().normalize();
        String entryName = args.length > 1 && !args[1].isBlank()
                ? args[1]
                : ".qin/runtime/npm-host/node_modules/ovs-compiler/src/parser/OvsParser.ts";
        Path entry = root.resolve(entryName).toAbsolutePath().normalize();
        if (!Files.isRegularFile(entry)) {
            throw new IllegalStateException("Missing OVS parser entry: " + entry);
        }

        long started = System.nanoTime();
        QinCfaModuleClassCompileResult result = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinOvsParserIrShapeProbe"));
        long compileMs = (System.nanoTime() - started) / 1_000_000L;
        System.out.println("[QinOvsParserIrShapeProbe] compileMs=" + compileMs
                + " modules=" + result.moduleClasses().size());

        String targetSuffix = "/" + entry.getFileName();
        result.moduleClasses().stream()
                .filter(module -> module.sourceFile().toString().replace('\\', '/').endsWith(targetSuffix))
                .sorted(Comparator.comparing(QinCfaModuleClassFile::moduleIndex))
                .forEach(QinOvsParserIrShapeProbeMain::printModuleShape);
    }

    private static void printModuleShape(QinCfaModuleClassFile module) {
        System.out.println("[QinOvsParserIrShapeProbe] moduleIndex=" + module.moduleIndex()
                + " source=" + module.sourceFile()
                + " classes=" + module.loweredProgram().classDeclarations().size()
                + " declarations=" + module.loweredProgram().declarations().size()
                + " expressions=" + module.loweredProgram().expressionStatements().size()
                + " functionModels=" + module.loweredProgram().functionModelArtifacts().size());
        if (module.loweredProgram().classDeclarations().isEmpty()) {
            System.out.println("[QinOvsParserIrShapeProbe] no JVM declaration class for OvsParser; class value is runtime/interpreted");
        }
        for (QinIrConstDeclaration declaration : module.loweredProgram().declarations()) {
            if ("OvsParser".equals(declaration.name()) || "default".equals(declaration.name())) {
                System.out.println("[QinOvsParserIrShapeProbe] declaration " + declaration.name()
                        + " initializer=" + (declaration.initializer() == null
                        ? "null"
                        : declaration.initializer().getClass().getSimpleName()));
            }
        }
        for (QinIrClassDeclaration declaration : module.loweredProgram().classDeclarations()) {
            int runtimeBacked = 0;
            int statementBacked = 0;
            int expressionBacked = 0;
            int empty = 0;
            for (QinIrMethodDeclaration method : declaration.methods()) {
                if (method.runtimeFunctionDefinition() != null) {
                    runtimeBacked++;
                } else if (!method.bodyStatements().isEmpty()) {
                    statementBacked++;
                } else if (method.returnExpression() != null) {
                    expressionBacked++;
                } else {
                    empty++;
                }
            }
            System.out.println("[QinOvsParserIrShapeProbe] class=" + declaration.simpleName()
                    + " methods=" + declaration.methods().size()
                    + " runtimeBacked=" + runtimeBacked
                    + " statementBacked=" + statementBacked
                    + " expressionBacked=" + expressionBacked
                    + " empty=" + empty);
            for (QinIrMethodDeclaration method : declaration.methods()) {
                if (method.runtimeFunctionDefinition() != null) {
                    System.out.println("  runtime " + method.name() + "/" + method.parameters().size());
                } else if (!method.bodyStatements().isEmpty()) {
                    System.out.println("  directStatements " + method.name() + "/" + method.parameters().size()
                            + " statements=" + method.bodyStatements().size());
                } else {
                    System.out.println("  directReturn " + method.name() + "/" + method.parameters().size()
                            + " return=" + (method.returnExpression() == null
                            ? "null"
                            : method.returnExpression().getClass().getSimpleName()));
                }
            }
        }
    }
}
