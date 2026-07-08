package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinJavaSdkArrayListCapacitySmokeTestMain {
    private QinJavaSdkArrayListCapacitySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { __QinJavaLangString, __QinJavaUtilArrayList } from "@qin/java-sdk-js";
                const list = new __QinJavaUtilArrayList(10000);
                list.add("ok");
                list.add("next");
                list.get(0) + ":" + list.size() + ":" + list.contains("ok") + ":" + list.contains("missing")
                  + ":" + __QinJavaLangString.join("|", list);
                """, "java_sdk_array_list_capacity");
        if (!"ok:2:true:false:ok|next".equals(result)) {
            throw new IllegalStateException("Expected ArrayList capacity constructor result ok:2:true:false:ok|next, got: " + result);
        }
        System.out.println("QinJavaSdkArrayListCapacitySmokeTestMain OK");
    }
}
