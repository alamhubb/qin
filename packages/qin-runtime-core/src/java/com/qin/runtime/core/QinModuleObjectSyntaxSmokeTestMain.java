package com.qin.runtime.core;

import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassCompileResult;
import com.qin.lang.pipeline.cfa.QinCfaModuleClassFile;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;

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
                Counter.next();
                """);

        QinCfaModuleClassCompileResult compiled = new QinSlimeCfaCompiler().compileModuleClasses(
                QinCfaCompileRequest.forJvm(entry, root, "probe.QinModuleObjectSyntaxSmoke"));
        Loader loader = new Loader(QinModuleObjectSyntaxSmokeTestMain.class.getClassLoader());

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
