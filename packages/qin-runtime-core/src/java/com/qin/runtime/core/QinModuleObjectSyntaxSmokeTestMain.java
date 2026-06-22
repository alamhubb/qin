package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;
import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinModuleObjectSyntaxSmokeTestMain {
    private QinModuleObjectSyntaxSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-module-object-syntax-");
        Path dep = root.resolve("counter.ts");
        Path entry = root.resolve("entry.ts");
        Files.writeString(dep, """
                export object Counter {
                    value = 41;
                    next() {
                        return this.value + 1;
                    }
                }
                """);
        Files.writeString(entry, """
                import { Counter } from './counter.ts';
                Counter.setValue(41);
                Counter.next();
                """);

        QinCfaModuleClassCompileResult compiled = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinModuleObjectSyntaxSmoke"));
        Loader loader = new Loader(QinModuleObjectSyntaxSmokeTestMain.class.getClassLoader());
        defineDeclarations(loader, compiled.initializerClass());
        for (QinCfaModuleClassFile moduleResult : compiled.moduleClasses()) {
            defineDeclarations(loader, moduleResult);
        }

        Class<?> initializerClass = loader.define(
                compiled.initializerClass().className(),
                compiled.initializerClass().classBytes());
        initializerClass.getMethod("run").invoke(null);

        Object result = null;
        for (QinCfaModuleClassFile moduleResult : compiled.moduleClasses()) {
            Class<?> moduleClass = loader.define(moduleResult.className(), moduleResult.classBytes());
            result = moduleClass.getMethod("run").invoke(null);
        }
        if (!(result instanceof Number number) || number.doubleValue() != 42.0d) {
            throw new AssertionError("Expected object singleton method result 42, got " + result);
        }

        System.out.println("QinModuleObjectSyntaxSmokeTestMain OK");
    }

    private static void defineDeclarations(Loader loader, QinCfaCompileResult result) {
        if (result.loweredProgram().classDeclarations().isEmpty()) {
            return;
        }
        Map<String, byte[]> declarationBytes = new QinJvmDeclarationClassEmitter()
                .compileAllClasses(result.loweredProgram());
        for (Map.Entry<String, byte[]> entry : declarationBytes.entrySet()) {
            Class<?> declarationClass = loader.define(entry.getKey(), entry.getValue());
            JavaEsmGlobal.__qin_bind_global__(declarationClass.getSimpleName(), declarationClass);
            JavaEsmGlobal.__qin_bind_global__(entry.getKey(), declarationClass);
        }
    }

    private static void defineDeclarations(Loader loader, QinCfaModuleClassFile result) {
        if (result.loweredProgram().classDeclarations().isEmpty()) {
            return;
        }
        Map<String, byte[]> declarationBytes = new QinJvmDeclarationClassEmitter()
                .compileAllClasses(result.loweredProgram());
        for (Map.Entry<String, byte[]> entry : declarationBytes.entrySet()) {
            Class<?> declarationClass = loader.define(entry.getKey(), entry.getValue());
            JavaEsmGlobal.__qin_bind_global__(declarationClass.getSimpleName(), declarationClass);
            JavaEsmGlobal.__qin_bind_global__(entry.getKey(), declarationClass);
        }
    }

    private static final class Loader extends ClassLoader {
        private final Map<String, Class<?>> defined = new LinkedHashMap<>();

        private Loader(ClassLoader parent) {
            super(parent);
        }

        private Class<?> define(String binaryName, byte[] bytes) {
            Class<?> existing = defined.get(binaryName);
            if (existing != null) {
                return existing;
            }
            Class<?> type = defineClass(binaryName, bytes, 0, bytes.length);
            defined.put(binaryName, type);
            return type;
        }
    }
}
