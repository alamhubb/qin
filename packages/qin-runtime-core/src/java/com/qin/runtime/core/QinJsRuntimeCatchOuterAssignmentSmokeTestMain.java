package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsRuntimeCatchOuterAssignmentSmokeTestMain {
    private QinJsRuntimeCatchOuterAssignmentSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-runtime-catch-outer-assignment-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-runtime-catch\" };\n",
                StandardCharsets.UTF_8);
        JavaEsmGlobal.setInterpretedCallCountLimit(50_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(root, """
                    let current = "start";
                    let caught = "none";
                    try {
                      throw "boom";
                    } catch (error) {
                      caught = error;
                      current = "changed";
                    }
                    let shadow = "outer";
                    try {
                      throw "inner";
                    } catch (shadow) {
                      shadow = shadow + "-local";
                    }
                    current + ";" + caught + ";" + shadow;
                    """, "js_runtime_catch_outer_assignment");
            if (!"changed;boom;outer".equals(String.valueOf(result))) {
                throw new IllegalStateException("Expected catch assignment to update outer scope, got: " + result);
            }
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
        System.out.println("QinJsRuntimeCatchOuterAssignmentSmokeTestMain OK");
    }
}
