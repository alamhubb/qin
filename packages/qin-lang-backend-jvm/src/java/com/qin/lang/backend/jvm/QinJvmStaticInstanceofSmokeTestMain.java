package com.qin.lang.backend.jvm;

import com.qin.lang.frontend.adapter.QinFrontendLowerer;
import com.qin.lang.ir.QinIrBuiltinCallExpression;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrMethodDeclaration;
import com.qin.lang.ir.QinIrParameter;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrTypeRef;
import com.qin.lang.ir.QinJavaSdkAliasSupport;
import com.slime.ast.Position;

import java.util.List;
import java.util.Map;

/**
 * Proves static __qin_instanceof__(value, ClassName) lowers to JVM class bytes.
 */
public final class QinJvmStaticInstanceofSmokeTestMain {
    private QinJvmStaticInstanceofSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        if (QinJavaSdkAliasSupport.isKnownAlias("__QinJavaUtilRegexPattern")) {
            throw new IllegalStateException("__QinJavaUtilRegexPattern is a generated JS SDK facade, not a native alias");
        }

        String source = """
                class Probe {
                  isProbe(value: any): boolean {
                    return __qin_instanceof__(value, Probe)
                  }

                  isObject(value: any): boolean {
                    return __qin_instanceof__(value, Object)
                  }

                  accepts(flag: boolean, value: any): boolean {
                    if (flag && (value == null || __qin_instanceof__(value, Probe))) {
                      return true
                    }
                    return false
                  }

                  acceptsStructural(flag: boolean, value: any): boolean {
                    if (flag && (value == null || __qin_instanceof__(value, Probe) || __qin_structural_object__(value))) {
                      return true
                    }
                    return false
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
        Object stringObjectResult = probeClass.getMethod("isObject", Object.class).invoke(probe, "yes");
        Object instanceObjectResult = probeClass.getMethod("isObject", Object.class).invoke(probe, probe);
        Object nullObjectResult = probeClass.getMethod("isObject", Object.class).invoke(probe, new Object[] { null });
        if (!Boolean.TRUE.equals(stringObjectResult)
                || !Boolean.TRUE.equals(instanceObjectResult)
                || !Boolean.FALSE.equals(nullObjectResult)) {
            throw new IllegalStateException(
                    "Unexpected Object instanceof results: "
                            + stringObjectResult + ", " + instanceObjectResult + ", " + nullObjectResult);
        }
        Object nullAcceptResult = probeClass.getMethod("accepts", boolean.class, Object.class)
                .invoke(probe, true, null);
        Object instanceAcceptResult = probeClass.getMethod("accepts", boolean.class, Object.class)
                .invoke(probe, true, probe);
        Object falseAcceptResult = probeClass.getMethod("accepts", boolean.class, Object.class)
                .invoke(probe, false, probe);
        if (!Boolean.TRUE.equals(nullAcceptResult)
                || !Boolean.TRUE.equals(instanceAcceptResult)
                || !Boolean.FALSE.equals(falseAcceptResult)) {
            throw new IllegalStateException(
                    "Unexpected logical instanceof results: "
                            + nullAcceptResult + ", " + instanceAcceptResult + ", " + falseAcceptResult);
        }
        Object structuralMapResult = probeClass.getMethod("acceptsStructural", boolean.class, Object.class)
                .invoke(probe, true, Map.of("in", Boolean.TRUE));
        Object structuralFalseResult = probeClass.getMethod("acceptsStructural", boolean.class, Object.class)
                .invoke(probe, true, "x");
        if (!Boolean.TRUE.equals(structuralMapResult) || !Boolean.FALSE.equals(structuralFalseResult)) {
            throw new IllegalStateException(
                    "Unexpected structural guard results: " + structuralMapResult + ", " + structuralFalseResult);
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

        QinIrMethodDeclaration strippedJavaSdkAliasMethod = new QinIrMethodDeclaration(
                "isIntegerAlias",
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
                                                "__qesm_m3_e___QinJavaLangInteger"))))));
        QinIrClassDeclaration strippedJavaSdkAliasDeclaration = new QinIrClassDeclaration(
                null,
                "UsesStrippedJavaSdkAlias",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(strippedJavaSdkAliasMethod));
        Map<String, byte[]> strippedJavaSdkAliasCompiled = new QinJvmDeclarationClassEmitter()
                .compileAllClasses(programWithClasses(List.of(strippedJavaSdkAliasDeclaration)));
        Class<?> strippedJavaSdkAliasClass = new ByteArrayClassLoader().define(
                "UsesStrippedJavaSdkAlias",
                strippedJavaSdkAliasCompiled.get("UsesStrippedJavaSdkAlias"));
        Object strippedJavaSdkAliasUser = strippedJavaSdkAliasClass.getDeclaredConstructor().newInstance();
        Object integerAliasTrue = strippedJavaSdkAliasClass.getMethod("isIntegerAlias", Object.class)
                .invoke(strippedJavaSdkAliasUser, Integer.valueOf(3));
        Object integerAliasFalse = strippedJavaSdkAliasClass.getMethod("isIntegerAlias", Object.class)
                .invoke(strippedJavaSdkAliasUser, Double.valueOf(3.5d));
        if (!Boolean.TRUE.equals(integerAliasTrue) || !Boolean.FALSE.equals(integerAliasFalse)) {
            throw new IllegalStateException("Unexpected stripped Java SDK alias instanceof result: "
                    + integerAliasTrue + ", " + integerAliasFalse);
        }

        QinIrClassDeclaration flattenedPackageDeclaration = new QinIrClassDeclaration(
                "com.subhuti.struct",
                "SubhutiTokenLookahead",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of());
        QinIrMethodDeclaration flattenedPackageMethod = new QinIrMethodDeclaration(
                "isLookahead",
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
                                                "__qesm_m26_e_com_subhuti_struct_SubhutiTokenLookahead"))))));
        QinIrClassDeclaration flattenedPackageUserDeclaration = new QinIrClassDeclaration(
                null,
                "UsesFlattenedPackageExport",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(flattenedPackageMethod));
        QinIrProgram flattenedPackageProgram = programWithClasses(
                List.of(flattenedPackageDeclaration, flattenedPackageUserDeclaration));
        Map<String, byte[]> flattenedPackageCompiled = new QinJvmDeclarationClassEmitter()
                .compileAllClasses(flattenedPackageProgram);
        ByteArrayClassLoader flattenedPackageLoader = new ByteArrayClassLoader();
        Class<?> lookaheadClass = flattenedPackageLoader.define(
                "com.subhuti.struct.SubhutiTokenLookahead",
                flattenedPackageCompiled.get("com.subhuti.struct.SubhutiTokenLookahead"));
        Class<?> flattenedUserClass = flattenedPackageLoader.define(
                "UsesFlattenedPackageExport",
                flattenedPackageCompiled.get("UsesFlattenedPackageExport"));
        Object lookahead = lookaheadClass.getDeclaredConstructor().newInstance();
        Object flattenedPackageResult = flattenedUserClass.getMethod("isLookahead", Object.class)
                .invoke(flattenedUserClass.getDeclaredConstructor().newInstance(), lookahead);
        if (!Boolean.TRUE.equals(flattenedPackageResult)) {
            throw new IllegalStateException("Unexpected flattened package export-slot instanceof result: "
                    + flattenedPackageResult);
        }

        QinIrMethodDeclaration flattenedGeneratedIdentifierMethod = new QinIrMethodDeclaration(
                "isPosition",
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
                                new QinIrIdentifierReference("com_slime_ast_Position"))));
        QinIrClassDeclaration flattenedGeneratedIdentifierDeclaration = new QinIrClassDeclaration(
                null,
                "UsesFlattenedGeneratedIdentifier",
                QinIrTypeRef.classType("java.lang.Object"),
                List.of(),
                List.of(),
                List.of(flattenedGeneratedIdentifierMethod));
        Map<String, byte[]> flattenedGeneratedIdentifierCompiled = new QinJvmDeclarationClassEmitter()
                .compileAllClasses(programWithClasses(List.of(flattenedGeneratedIdentifierDeclaration)));
        Class<?> flattenedGeneratedIdentifierClass = new ByteArrayClassLoader().define(
                "UsesFlattenedGeneratedIdentifier",
                flattenedGeneratedIdentifierCompiled.get("UsesFlattenedGeneratedIdentifier"));
        Object flattenedGeneratedIdentifierUser = flattenedGeneratedIdentifierClass
                .getDeclaredConstructor()
                .newInstance();
        Object flattenedGeneratedIdentifierTrue = flattenedGeneratedIdentifierClass
                .getMethod("isPosition", Object.class)
                .invoke(flattenedGeneratedIdentifierUser, new Position(1, 2));
        Object flattenedGeneratedIdentifierFalse = flattenedGeneratedIdentifierClass
                .getMethod("isPosition", Object.class)
                .invoke(flattenedGeneratedIdentifierUser, "no");
        if (!Boolean.TRUE.equals(flattenedGeneratedIdentifierTrue)
                || !Boolean.FALSE.equals(flattenedGeneratedIdentifierFalse)) {
            throw new IllegalStateException("Unexpected flattened generated identifier instanceof result: "
                    + flattenedGeneratedIdentifierTrue + ", " + flattenedGeneratedIdentifierFalse);
        }

        verifyGeneratedOverloadGuardStaysStatic();

        System.out.println("QinJvmStaticInstanceofSmokeTestMain passed.");
    }

    private static void verifyGeneratedOverloadGuardStaysStatic() throws Exception {
        String source = """
                class GeneratedOverloadGuardProbe {
                  accepts(value: any): boolean {
                    return value === null || Array.isArray(value) || value instanceof GeneratedOverloadGuardProbe
                  }
                }
                """;
        String previousMode = System.getProperty("qin.dynamicSemanticMode");
        System.setProperty("qin.dynamicSemanticMode", "error");
        try {
            QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
            Map<String, byte[]> compiled = new QinJvmDeclarationClassEmitter().compileAllClasses(program);
            ByteArrayClassLoader loader = new ByteArrayClassLoader();
            Class<?> type = loader.define("GeneratedOverloadGuardProbe", compiled.get("GeneratedOverloadGuardProbe"));
            Object instance = type.getDeclaredConstructor().newInstance();
            Object arrayResult = type.getMethod("accepts", Object.class).invoke(instance, new Object[] { new Object[] { "x" } });
            Object selfResult = type.getMethod("accepts", Object.class).invoke(instance, instance);
            Object falseResult = type.getMethod("accepts", Object.class).invoke(instance, "x");
            if (!Boolean.TRUE.equals(arrayResult)
                    || !Boolean.TRUE.equals(selfResult)
                    || !Boolean.FALSE.equals(falseResult)) {
                throw new IllegalStateException("Unexpected generated overload guard results: "
                        + arrayResult + ", " + selfResult + ", " + falseResult);
            }
        } finally {
            if (previousMode == null) {
                System.clearProperty("qin.dynamicSemanticMode");
            } else {
                System.setProperty("qin.dynamicSemanticMode", previousMode);
            }
        }
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
