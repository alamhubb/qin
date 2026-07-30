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

public final class QinDirectClassDefaultImportModuleSmokeTestMain {
    private QinDirectClassDefaultImportModuleSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-direct-class-default-import-");
        Path base = root.resolve("base.ts");
        Path barrel = root.resolve("barrel.ts");
        Path reexport = root.resolve("reexport.ts");
        Path entry = root.resolve("entry.ts");
        Files.writeString(base, """
                export default class Base {
                  message: string = "unset"
                  constructor(message: string = "base") {
                    this.message = message
                  }
                  read() {
                    return this.message
                  }
                }
                """, StandardCharsets.UTF_8);
        Files.writeString(barrel, """
                import Base from './base.ts'
                export default Base
                """, StandardCharsets.UTF_8);
        Files.writeString(reexport, """
                export { default as ReExportedBase } from './barrel.ts'
                """, StandardCharsets.UTF_8);
        Files.writeString(entry, """
                import BaseDefault from './barrel.ts'
                import { ReExportedBase } from './reexport.ts'

                class DirectChild extends BaseDefault {
                  constructor(message: string = "direct") {
                    super(message)
                  }
                }

                class ReExportChild extends ReExportedBase {
                  constructor(message: string = "reexport") {
                    super(message)
                  }
                }

                export const result = new DirectChild("ok").read() + "/" + new ReExportChild("again").read()
                """, StandardCharsets.UTF_8);

        QinCfaModuleClassCompileResult compiled = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinDirectClassDefaultImportModuleSmoke"));
        if (compiled.moduleClasses().size() != 4) {
            throw new IllegalStateException("Expected four module classes, got " + compiled.moduleClasses().size());
        }
        requireClassLiteral(compiled, "Base");
        requireClassLiteral(compiled, "DirectChild");
        requireClassLiteral(compiled, "ReExportChild");

        Loader loader = new Loader(QinDirectClassDefaultImportModuleSmokeTestMain.class.getClassLoader());
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
        if (!"ok/again".equals(result)) {
            throw new IllegalStateException("Expected default class import result ok/again, got " + result);
        }

        System.out.println("QinDirectClassDefaultImportModuleSmokeTestMain OK");
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
            if (moduleClassFile.loweredProgram().classDeclarations().isEmpty()) {
                continue;
            }
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
