package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaSdkStringPrimitiveHotPathSmokeTestMain {
    private QinJavaSdkStringPrimitiveHotPathSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-java-sdk-string-primitive-hot-path-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-java-sdk-string-primitive-hot-path\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(500);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    """
                    import { __QinJavaLangString } from "@qin/java-sdk-js";

                    let ok = true;
                    for (let i = 0; i < 2000; i++) {
                      ok = ok
                        && __QinJavaLangString.equals("EOF", "EOF")
                        && !__QinJavaLangString.equals("EOF", "IdentifierName")
                        && __QinJavaLangString.isEmpty("")
                        && __QinJavaLangString.length("token") === 5;
                    }
                    ok;
                    """,
                    "java_sdk_string_primitive_hot_path");
            if (!Boolean.TRUE.equals(result)) {
                throw new IllegalStateException("Expected primitive String helper hot path to return true, got: " + result);
            }
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }

        System.out.println("QinJavaSdkStringPrimitiveHotPathSmokeTestMain OK");
    }
}
