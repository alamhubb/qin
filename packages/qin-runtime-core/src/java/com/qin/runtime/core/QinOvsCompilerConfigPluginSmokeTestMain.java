package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsCompilerConfigPluginSmokeTestMain {
    private QinOvsCompilerConfigPluginSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-config-plugin-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-ovs-config-plugin-smoke",
                  plugins: [{
                    name: "vite-plugin-ovs",
                    transform(code, id) {
                      return {
                        code: "export const qinConfigOvsPlugin = " + JSON.stringify(id + "::" + code.trim())
                      }
                    }
                  }]
                }
                """, StandardCharsets.UTF_8);

        String source = "div { \"Java OVS path\" }";
        QinOvsCompiler.QinOvsCompileResult result = new QinOvsCompiler().compile(root, source);
        if (!result.code().contains("$OvsHtmlTag.div")
                || result.code().contains("qinConfigOvsPlugin")
                || result.code().contains("JSON.stringify")) {
            throw new IllegalStateException("Qin OVS compiler must keep the standard path Java-native when qin.config.js contains a JavaScript hook:\n"
                    + result.code());
        }

        System.out.println("QinOvsCompilerConfigPluginSmokeTestMain passed.");
    }
}
