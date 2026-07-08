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

public final class QinDirectClassConstructorModuleSmokeTestMain {
    private QinDirectClassConstructorModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-direct-class-constructor-");
        Path entry = root.resolve("entry.ts");
        Files.writeString(entry, """
                class Base {
                  message: string = "unset"
                  constructor(message: string = "base") {
                    this.message = message
                  }
                  read() {
                    return this.message
                  }
                }

                class Child extends Base {
                  constructor(message: string = "child") {
                    super(message)
                  }
                }

                const parser = new Child("ok")
                export const result = parser.read()
                """);

        QinCfaModuleClassCompileResult compiled = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinDirectClassConstructorModuleSmoke"));
        QinCfaModuleClassFile module = compiled.moduleClasses().get(0);
        if (module.loweredProgram().classDeclarations().size() != 2) {
            throw new IllegalStateException("Expected two direct JVM class declarations, got "
                    + module.loweredProgram().classDeclarations().size());
        }
        requireClassLiteral(module, "Base");
        requireClassLiteral(module, "Child");

        Loader loader = new Loader(QinDirectClassConstructorModuleSmokeTestMain.class.getClassLoader());
        defineDeclarations(loader, module);
        Class<?> moduleClass = loader.define(module.className(), module.classBytes());
        Object result = moduleClass.getMethod("run").invoke(null);
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected direct class constructor result ok, got " + result);
        }

        System.out.println("QinDirectClassConstructorModuleSmokeTestMain OK");
    }

    private static void requireClassLiteral(QinCfaModuleClassFile module, String name) {
        QinIrConstDeclaration declaration = module.loweredProgram().declarations().stream()
                .filter(item -> name.equals(item.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing " + name + " declaration"));
        if (!(declaration.initializer() instanceof QinIrJavaClassLiteralExpression)) {
            throw new IllegalStateException("Expected " + name + " class value to be Java class literal, got "
                    + declaration.initializer().getClass().getSimpleName());
        }
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
