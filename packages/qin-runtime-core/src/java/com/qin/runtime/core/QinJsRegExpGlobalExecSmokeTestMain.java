package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRegExpGlobalExecSmokeTestMain {
    private QinJsRegExpGlobalExecSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const pattern = /([^\\s=]+)(?:\\s*=\\s*("[^"]*"|'[^']*'|[^\\s"']+))?/g;
                const raw = 'type="button" @click="count++"';
                const names = [];
                let match;
                while ((match = pattern.exec(raw))) {
                  names.push(match[1]);
                }
                const sticky = /b/y;
                sticky.lastIndex = 1;
                const stickyMatch = sticky.exec("abc");
                ({
                  names: names.join(","),
                  lastIndex: pattern.lastIndex,
                  sticky: stickyMatch ? stickyMatch[0] : null,
                  stickyLastIndex: sticky.lastIndex
                });
                """;
        Path root = Files.createTempDirectory("qin-js-regexp-global-exec-");
        Path sourceFile = root.resolve("main.js");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsRegExpGlobalExecSmoke");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!"type,@click".equals(map.get("names"))
                || !numberEquals(map.get("lastIndex"), 0)
                || !"b".equals(map.get("sticky"))
                || !numberEquals(map.get("stickyLastIndex"), 2)) {
            throw new IllegalStateException("Unexpected RegExp global exec result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsRegExpGlobalExecSmokeTestMain OK");
    }

    private static boolean numberEquals(Object value, int expected) {
        return value instanceof Number number && number.intValue() == expected;
    }
}
