package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaHashSetRemoveCallLimitSmokeTestMain {
    private QinJsBackendJavaHashSetRemoveCallLimitSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaImport("java:java.util", "HashSet", "HashSet", "java.util.HashSet"),
                        new QinIrJavaImport("java:java.lang", "String", "JString", "java.lang.String")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        Path root = Files.createTempDirectory("qin-js-backend-hashset-remove-call-limit-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-js-backend-hashset-remove-call-limit\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(5_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                            const set = new HashSet();
                            const key = "AsyncFunctionDeclaration:0.0:null:<null>";
                            set.add(key);
                            const containsViaStringShim = JString.contains(set, key);
                            const emptyBeforeRemove = JString.isEmpty(set);
                            const removed = set.remove(key);
                            const emptyAfterRemove = JString.isEmpty(set);
                            String(containsViaStringShim) + ":"
                              + String(emptyBeforeRemove) + ":"
                              + String(removed) + ":"
                              + String(emptyAfterRemove) + ":"
                              + set.size();
                            """,
                    "js_backend_hashset_remove_call_limit");
            if (!"true:false:true:true:0.0".equals(result) && !"true:false:true:true:0".equals(result)) {
                throw new IllegalStateException(
                        "Expected HashSet contains/isEmpty/remove result true:false:true:true:0, got: " + result);
            }
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }

        System.out.println("QinJsBackendJavaHashSetRemoveCallLimitSmokeTestMain OK");
    }
}
