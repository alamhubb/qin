package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;
import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinDirectClassValueModuleSmokeTestMain {
    private QinDirectClassValueModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-direct-class-value-");
        Path entry = root.resolve("entry.ts");
        Files.writeString(entry, """
                class Foo {
                  value() {
                    return 42
                  }
                }

                const parser = new Foo()
                export const result = parser.value()
                """);

        QinCfaModuleClassCompileResult compiled = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinDirectClassValueModuleSmoke"));
        QinCfaModuleClassFile module = compiled.moduleClasses().get(0);
        if (module.loweredProgram().classDeclarations().size() != 1) {
            throw new IllegalStateException("Expected one direct JVM class declaration, got "
                    + module.loweredProgram().classDeclarations().size());
        }
        QinIrConstDeclaration fooDeclaration = module.loweredProgram().declarations().stream()
                .filter(declaration -> "Foo".equals(declaration.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing Foo declaration"));
        if (!(fooDeclaration.initializer() instanceof QinIrJavaClassLiteralExpression)) {
            throw new IllegalStateException("Expected Foo class value to be Java class literal, got "
                    + fooDeclaration.initializer().getClass().getSimpleName());
        }

        Loader loader = new Loader(QinDirectClassValueModuleSmokeTestMain.class.getClassLoader());
        defineDeclarations(loader, module);
        Class<?> moduleClass = loader.define(module.className(), module.classBytes());
        Object result = moduleClass.getMethod("run").invoke(null);
        if (!(result instanceof Number number) || number.doubleValue() != 42.0d) {
            throw new IllegalStateException("Expected direct class module result 42, got " + result);
        }

        System.out.println("QinDirectClassValueModuleSmokeTestMain OK");
    }

    private static void defineDeclarations(Loader loader, QinCfaModuleClassFile result) {
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
