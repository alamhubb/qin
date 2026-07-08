package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinJavaSdkHashSetAddAllSmokeTestMain {
    private QinJavaSdkHashSetAddAllSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { __QinJavaUtilHashSet, __QinJavaUtilList } from "@qin/java-sdk-js";
                const set = new __QinJavaUtilHashSet();
                const first = set.addAll(__QinJavaUtilList.of("a", "b", "a"));
                const second = set.addAll(__QinJavaUtilList.of("a", "b"));
                const retained = set.retainAll(__QinJavaUtilList.of("b", "c"));
                const retainedAgain = set.retainAll(__QinJavaUtilList.of("b", "c"));
                `${first}:${second}:${retained}:${retainedAgain}:${set.size()}:${set.contains("a")}:${set.contains("b")}`;
                """, "java_sdk_hash_set_add_all");
        if (!"true:false:true:false:1:false:true".equals(result)) {
            throw new IllegalStateException("Expected HashSet addAll/retainAll result true:false:true:false:1:false:true, got: " + result);
        }
        System.out.println("QinJavaSdkHashSetAddAllSmokeTestMain OK");
    }
}
