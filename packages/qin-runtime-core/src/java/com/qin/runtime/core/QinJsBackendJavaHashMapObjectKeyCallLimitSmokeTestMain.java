package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaHashMapObjectKeyCallLimitSmokeTestMain {
    private QinJsBackendJavaHashMapObjectKeyCallLimitSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport("java:java.util", "HashMap", "HashMap", "java.util.HashMap")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        Path root = Files.createTempDirectory("qin-js-backend-hashmap-object-key-call-limit-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-js-backend-hashmap-object-key-call-limit\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(5_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                            class Key {
                              constructor(name) { this.name = name; }
                              hashCode() { return this.name === "same" ? 7 : 7; }
                              equals(other) { return other instanceof Key && other.name === this.name; }
                            }
                            const map = new HashMap();
                            map.put(new Key("same"), "first");
                            const getEqual = map.get(new Key("same"));
                            const existing = map.putIfAbsent(new Key("same"), "changed");
                            map.put(new Key("other"), "second");
                            const removed = map.remove(new Key("same"));
                            [
                              getEqual,
                              existing,
                              map.containsKey(new Key("same")),
                              removed,
                              map.containsKey(new Key("other")),
                              map.get(new Key("other")),
                              map.size()
                            ].join(":");
                            """,
                    "js_backend_hashmap_object_key_call_limit");
            String text = String.valueOf(result);
            if (!"first:first:false:first:true:second:1.0".equals(text)
                    && !"first:first:false:first:true:second:1".equals(text)) {
                throw new IllegalStateException("Expected HashMap object-key result, got: " + result);
            }
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }

        System.out.println("QinJsBackendJavaHashMapObjectKeyCallLimitSmokeTestMain OK");
    }
}
