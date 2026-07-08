package com.qin.runtime.core;

import com.qin.lang.backend.jvm.QinJvmDeclarationClassEmitter;
import com.qin.lang.ir.QinIrClassDeclaration;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaClassLiteralExpression;
import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;
import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class QinDirectClassImportModuleSmokeTestMain {
    private QinDirectClassImportModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-direct-class-import-");
        Path base = root.resolve("base.ts");
        Path entry = root.resolve("entry.ts");
        Files.writeString(base, """
                export class Base {
                  message: string = "unset"
                  constructor(message: string = "base") {
                    this.message = message
                  }
                  read() {
                    return this.message
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(entry, """
                import { Base } from './base.ts'

                class Child extends Base {
                  constructor(message: string = "child") {
                    super(message)
                  }
                }

                const parser = new Child("ok")
                export const result = parser.read()
                """, StandardCharsets.UTF_8);

        QinCfaModuleClassCompileResult compiled = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinDirectClassImportModuleSmoke"));
        if (compiled.moduleClasses().size() != 2) {
            throw new IllegalStateException("Expected two module classes, got " + compiled.moduleClasses().size());
        }
        requireClassLiteral(compiled, "Base");
        requireClassLiteral(compiled, "Child");

        Loader loader = new Loader(QinDirectClassImportModuleSmokeTestMain.class.getClassLoader());
        defineDeclarations(loader, compiled);
        if (compiled.initializerClass() != null) {
            loader.define(compiled.initializerClass().className(), compiled.initializerClass().classBytes())
                    .getMethod("run")
                    .invoke(null);
        }

        Object result = null;
        for (QinCfaModuleClassFile moduleClassFile : compiled.moduleClasses()) {
            Class<?> moduleClass = loader.define(moduleClassFile.className(), moduleClassFile.classBytes());
            result = moduleClass.getMethod("run").invoke(null);
        }
        if (!"ok".equals(result)) {
            throw new IllegalStateException("Expected direct class import result ok, got " + result);
        }

        System.out.println("QinDirectClassImportModuleSmokeTestMain OK");
    }

    private static void requireClassLiteral(QinCfaModuleClassCompileResult compiled, String name) {
        QinIrConstDeclaration declaration = compiled.moduleClasses().stream()
                .flatMap(module -> module.loweredProgram().declarations().stream())
                .filter(item -> name.equals(item.name()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing " + name + " declaration"));
        if (!(declaration.initializer() instanceof QinIrJavaClassLiteralExpression)) {
            throw new IllegalStateException("Expected " + name + " class value to be Java class literal, got "
                    + declaration.initializer().getClass().getSimpleName());
        }
    }

    private static void defineDeclarations(Loader loader, QinCfaModuleClassCompileResult compiled) {
        Map<String, QinIrClassDeclaration> declarationIndex = new LinkedHashMap<>();
        for (QinCfaModuleClassFile moduleClassFile : compiled.moduleClasses()) {
            for (QinIrClassDeclaration declaration : moduleClassFile.loweredProgram().classDeclarations()) {
                declarationIndex.put(declaration.binaryName(), declaration);
            }
        }
        for (QinCfaModuleClassFile moduleClassFile : compiled.moduleClasses()) {
            Map<String, byte[]> declarationBytes = new QinJvmDeclarationClassEmitter()
                    .compileAllClasses(moduleClassFile.loweredProgram(), declarationIndex);
            for (Map.Entry<String, byte[]> entry : declarationBytes.entrySet()) {
                Class<?> declarationClass = loader.define(entry.getKey(), entry.getValue());
                JavaEsmGlobal.__qin_bind_global__(declarationClass.getSimpleName(), declarationClass);
                JavaEsmGlobal.__qin_bind_global__(entry.getKey(), declarationClass);
            }
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
