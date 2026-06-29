package com.qin.lang.backend.jvm;

/**
 * Unified JVM class-declaration emission corpus for the IDEA/LSP gate.
 */
public final class QinJvmClassDeclarationCorpusSmokeTestMain {
    private QinJvmClassDeclarationCorpusSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinJvmJavaExtendsEndToEndSmokeTestMain.main(args);
        QinJvmFieldAnnotationEndToEndSmokeTestMain.main(args);
        QinJvmFieldDeclarationSmokeTestMain.main(args);
        QinJvmMemberAccessReturnSmokeTestMain.main(args);
        QinJvmThisFieldMethodCallSmokeTestMain.main(args);
        QinJvmRuntimeFunctionMethodSmokeTestMain.main(args);
        QinJvmStaticMethodCallSmokeTestMain.main(args);
        QinJvmJavaNewReturnSmokeTestMain.main(args);
        QinJvmJavaSlimeParserExtendsSmokeTestMain.main(args);
        QinJvmParsedMethodBodyClassSmokeTestMain.main(args);
        QinJvmParsedEarlyReturnMethodBodySmokeTestMain.main(args);
        QinJvmParsedNestedBranchMethodBodySmokeTestMain.main(args);
        QinJvmParsedLocalDtoMemberAccessSmokeTestMain.main(args);
        QinJvmParsedLocalInheritanceSmokeTestMain.main(args);
        QinJvmParsedFieldsConstructorSmokeTestMain.main(args);
        QinJvmParsedSelfMethodCallSmokeTestMain.main(args);
        QinJvmParsedTryCatchMethodBodySmokeTestMain.main(args);
        QinJvmParsedWhileMethodBodySmokeTestMain.main(args);
        QinJvmParsedWhileMutableLocalSmokeTestMain.main(args);
        QinJvmParsedForBreakContinueSmokeTestMain.main(args);
        QinJvmParsedDoWhileSmokeTestMain.main(args);
        QinJvmParsedForOfSmokeTestMain.main(args);
        QinJvmSequenceConsoleReturnSmokeTestMain.main(args);

        System.out.println("Qin JVM class declaration corpus smoke passed: 23 cases");
    }
}
