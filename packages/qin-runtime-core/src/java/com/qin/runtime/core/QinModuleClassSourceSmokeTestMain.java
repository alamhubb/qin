package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;
import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinModuleClassSourceSmokeTestMain {
    private QinModuleClassSourceSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-module-class-source-");
        Path dep = root.resolve("dep.ts");
        Path entry = root.resolve("entry.ts");
        Files.writeString(dep, "export const value = 41;\n");
        Files.writeString(entry, "import { value } from './dep.ts';\nconst result = value + 1;\n");

        QinCfaModuleClassCompileResult compiled = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinModuleClassSourceSmoke"));
        if (compiled.moduleClasses().size() != 2) {
            throw new IllegalStateException("Expected two module classes");
        }
        if (compiled.initializerClass() == null) {
            throw new IllegalStateException("Expected initializer class for exported value slot");
        }

        Loader loader = new Loader(QinModuleClassSourceSmokeTestMain.class.getClassLoader());
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
            throw new AssertionError("Expected final module result 42, got " + result);
        }

        System.out.println("QinModuleClassSourceSmokeTestMain OK");
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
