package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;

import java.util.List;
import java.util.Map;

/**
 * Proves static __qin_instanceof__(value, ClassName) lowers to JVM class bytes.
 */
public final class QinJvmStaticInstanceofSmokeTestMain {
    private QinJvmStaticInstanceofSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                class Probe {
                  isProbe(value: any): boolean {
                    return __qin_instanceof__(value, Probe)
                  }
                }
                """;

        QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
        Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);

        ByteArrayClassLoader loader = new ByteArrayClassLoader();
        Class<?> probeClass = loader.define("Probe", compiled.get("Probe"));
        Object probe = probeClass.getDeclaredConstructor().newInstance();
        Object trueResult = probeClass.getMethod("isProbe", Object.class).invoke(probe, probe);
        Object falseResult = probeClass.getMethod("isProbe", Object.class).invoke(probe, "no");
        if (!Boolean.TRUE.equals(trueResult) || !Boolean.FALSE.equals(falseResult)) {
            throw new IllegalStateException("Unexpected instanceof results: " + trueResult + ", " + falseResult);
        }

        QinIrProgram exportSlotProgram = programWithClasses(List.of(new QinIrClassDeclaration(
                        null,
                        "ExportedProbe",
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of(),
                        List.of(),
                        List.of(new QinIrMethodDeclaration(
                                "isExportedProbe",
                                QinIrTypeRef.booleanType(),
                                List.of(new QinIrParameter("value", QinIrTypeRef.classType("java.lang.Object"), List.of())),
                                List.of(),
                                new QinIrBuiltinCallExpression(
                                        "Global",
                                        "__qin_instanceof__",
                                        List.of(
                                                new QinIrIdentifierReference("value"),
                                                new QinIrBuiltinCallExpression(
                                                        "Global",
                                                        "__qin_export_get__",
                                                        List.of(new QinIrIdentifierReference("__qesm_m0_e_ExportedProbe"))))))),
                        List.of())));
        Map<String, byte[]> exportSlotCompiled = new QinJvmDeclarationClassEmitter().compileAllClasses(exportSlotProgram);
        Class<?> exportedProbeClass = new ByteArrayClassLoader()
                .define("ExportedProbe", exportSlotCompiled.get("ExportedProbe"));
        Object exportedProbe = exportedProbeClass.getDeclaredConstructor().newInstance();
        Object exportSlotResult = exportedProbeClass.getMethod("isExportedProbe", Object.class)
                .invoke(exportedProbe, exportedProbe);
        if (!Boolean.TRUE.equals(exportSlotResult)) {
            throw new IllegalStateException("Unexpected export-slot instanceof result: " + exportSlotResult);
        }

        QinIrClassDeclaration packagePatternDeclaration = new QinIrClassDeclaration(
                "pkg",
                "__QinJavaUtilRegexPattern",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of());
        QinIrMethodDeclaration packagePatternMethod = new QinIrMethodDeclaration(
                "isPattern",
                QinIrTypeRef.booleanType(),
                List.of(new QinIrParameter(
                        "value",
                        QinIrTypeRef.classType("java.lang.Object"),
                        List.of())),
                List.of(),
                new QinIrBuiltinCallExpression(
                        "Global",
                        "__qin_instanceof__",
                        List.of(
                                new QinIrIdentifierReference("value"),
                                new QinIrBuiltinCallExpression(
                                        "Global",
                                        "__qin_export_get__",
                                        List.of(new QinIrIdentifierReference(
                                                "__qesm_m15_e___QinJavaUtilRegexPattern"))))));
        QinIrClassDeclaration packagePatternUserDeclaration = new QinIrClassDeclaration(
                null,
                "UsesExternalPattern",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(packagePatternMethod));
        QinIrProgram packageExportSlotProgram = programWithClasses(
                List.of(packagePatternDeclaration, packagePatternUserDeclaration));
        Map<String, byte[]> packageExportSlotCompiled = new QinJvmDeclarationClassEmitter()
                .compileAllClasses(packageExportSlotProgram);
        ByteArrayClassLoader packageLoader = new ByteArrayClassLoader();
        Class<?> patternClass = packageLoader.define(
                "pkg.__QinJavaUtilRegexPattern",
                packageExportSlotCompiled.get("pkg.__QinJavaUtilRegexPattern"));
        Class<?> userClass = packageLoader.define(
                "UsesExternalPattern",
                packageExportSlotCompiled.get("UsesExternalPattern"));
        Object pattern = patternClass.getDeclaredConstructor().newInstance();
        Object user = userClass.getDeclaredConstructor().newInstance();
        Object packageExportSlotResult = userClass.getMethod("isPattern", Object.class).invoke(user, pattern);
        if (!Boolean.TRUE.equals(packageExportSlotResult)) {
            throw new IllegalStateException("Unexpected package export-slot instanceof result: "
                    + packageExportSlotResult);
        }

        System.out.println("QinJvmStaticInstanceofSmokeTestMain passed.");
    }

    private static QinIrProgram programWithClasses(List<QinIrClassDeclaration> classDeclarations) {
        return new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                classDeclarations);
    }

    private static final class ByteArrayClassLoader extends ClassLoader {
        private Class<?> define(String binaryName, byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                throw new IllegalStateException("Missing class bytes for " + binaryName);
            }
            return defineClass(binaryName, bytes, 0, bytes.length);
        }
    }
}
