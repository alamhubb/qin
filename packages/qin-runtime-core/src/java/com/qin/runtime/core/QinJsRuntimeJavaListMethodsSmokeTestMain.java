package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinJsRuntimeJavaListMethodsSmokeTestMain {
    private QinJsRuntimeJavaListMethodsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-runtime-java-list-methods-");
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                const list = [];
                list.add("a");
                list.addAll(["b", "c"]);
                list.addAll(1, ["x", "y"]);
                const removed = list.remove(2);
                const sub = list.subList(1, 3).toArray();
                ({
                  size: list.size(),
                  joined: list.toArray().join(","),
                  removed,
                  subJoined: sub.join(","),
                  emptyAfterClear: (() => {
                    list.clear();
                    return list.isEmpty();
                  })()
                });
                """, "js_runtime_java_list_methods");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Integer.valueOf(4).equals(map.get("size"))
                || !"a,x,b,c".equals(map.get("joined"))
                || !"y".equals(map.get("removed"))
                || !"x,b".equals(map.get("subJoined"))
                || !Boolean.TRUE.equals(map.get("emptyAfterClear"))) {
            throw new IllegalStateException("Unexpected Java list method result: " + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinJsRuntimeJavaListMethodsSmokeTestMain OK");
    }
}
