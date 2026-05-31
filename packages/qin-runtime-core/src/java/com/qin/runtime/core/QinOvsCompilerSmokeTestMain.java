package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsCompilerSmokeTestMain {
    private QinOvsCompilerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-compiler-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-ovs-compiler-smoke\" }\n", StandardCharsets.UTF_8);

        String source = """
                div(class = css { colorBlue, fontWeight700, padding12px }) {
                  "Hello OVS + CSSTS"
                }
                """;

        QinOvsCompiler.QinOvsCompileResult result = new QinOvsCompiler().compile(root, source);
        if (!result.code().contains("defineOvsComponent")
                || !result.code().contains("$OvsHtmlTag")
                || !result.code().contains("csstsAtom")) {
            throw new IllegalStateException("OVS compiler output missed expected Vue/OVS/CSSTS code:\n" + result.code());
        }
        if (!result.css().contains("color: blue")
                || !result.atomModule().contains("fontWeight700")) {
            throw new IllegalStateException("OVS compiler did not emit expected CSSTS assets:\ncss:\n"
                    + result.css()
                    + "\natom:\n"
                    + result.atomModule());
        }
        System.out.println("QinOvsCompilerSmokeTestMain passed.");
    }
}
