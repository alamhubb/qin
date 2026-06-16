package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.module.resolver.QinLinkedModuleSection;
import com.qin.lang.module.resolver.QinLinkedModuleSource;
import com.qin.lang.module.resolver.QinLinkedModuleSourceEmitter;
import com.qin.lang.module.resolver.QinModuleGraph;
import com.qin.lang.module.resolver.QinModuleGraphBuilder;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaCompileResult;
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

        QinModuleGraph graph = new QinModuleGraphBuilder().build(entry);
        QinLinkedModuleSource linkedSource = new QinLinkedModuleSourceEmitter().emit(graph);
        if (linkedSource.moduleSections().size() != 2) {
            throw new IllegalStateException("Expected two module sections");
        }

        QinSlimeCfaCompiler compiler = new QinSlimeCfaCompiler();
        Path classSourceRoot = Files.createDirectory(root.resolve("class-sources"));
        Path initializerSource = classSourceRoot.resolve("__qesm_initializer.ts");
        Files.writeString(initializerSource, linkedSource.moduleInitializerSource());
        QinCfaCompileResult initializer = compiler.compile(QinCfaCompileRequest.forJvm(
                initializerSource,
                classSourceRoot,
                "probe.QinModuleInitializer"));

        Map<String, QinCfaCompileResult> moduleResults = new LinkedHashMap<>();
        int classIndex = 0;
        for (QinLinkedModuleSection section : linkedSource.moduleSections()) {
            String className = "probe.QinModule" + classIndex;
            Path sourceFile = classSourceRoot.resolve("module" + classIndex + ".ts");
            Files.writeString(sourceFile, section.classSource());
            moduleResults.put(className, compiler.compile(QinCfaCompileRequest.forJvm(
                    sourceFile,
                    classSourceRoot,
                    className)));
            classIndex++;
        }

        Loader loader = new Loader(QinModuleClassSourceSmokeTestMain.class.getClassLoader());
        defineDeclarations(loader, initializer);
        for (QinCfaCompileResult moduleResult : moduleResults.values()) {
            defineDeclarations(loader, moduleResult);
        }

        Class<?> initializerClass = loader.define("probe.QinModuleInitializer", initializer.classBytes());
        initializerClass.getMethod("run").invoke(null);

        Object result = null;
        for (Map.Entry<String, QinCfaCompileResult> moduleResult : moduleResults.entrySet()) {
            Class<?> moduleClass = loader.define(moduleResult.getKey(), moduleResult.getValue().classBytes());
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
