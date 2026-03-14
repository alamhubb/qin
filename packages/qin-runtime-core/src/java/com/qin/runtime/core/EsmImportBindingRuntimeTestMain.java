package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

/**
 * Runtime check for default/named/namespace/re-export bindings.
 */
public final class EsmImportBindingRuntimeTestMain {
    private EsmImportBindingRuntimeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinEsmTestPaths.resolveStage1Root();
        Path source = root.resolve("main/runtime-import-alias.js").normalize().toAbsolutePath();
        Object runResult = new QinInMemoryJvmRunner()
                .compileAndRun(source, "com.qin.runtime.generated.EsmImportBindingRuntime");

        if (!(runResult instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected run() map result, got: " + runResult);
        }
        Object age = map.get("age");
        if (!(age instanceof Number number) || number.intValue() != 7) {
            throw new IllegalStateException("Expected result.age == 7, got: " + age);
        }

        System.out.println("EsmImportBindingRuntimeTestMain passed.");
        System.out.println("result: " + runResult);
    }
}
