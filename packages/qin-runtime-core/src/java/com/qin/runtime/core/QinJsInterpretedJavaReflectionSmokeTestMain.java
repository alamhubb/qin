package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsInterpretedJavaReflectionSmokeTestMain {
    private QinJsInterpretedJavaReflectionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-interpreted-java-reflection-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-interpreted-java-reflection\" };\n",
                StandardCharsets.UTF_8);
        String source = """
                class Base {
                  baseValue() {
                    return "base";
                  }
                }
                class Box extends Base {
                  read(value) {
                    return "read:" + value;
                  }
                }
                const box = new Box();
                const clazz = box.getClass();
                const method = clazz.getDeclaredMethod("read", String);
                method.setAccessible(true);
                clazz.getSimpleName()
                  + ":" + clazz.getName()
                  + ":" + clazz.getSuperclass().getSimpleName()
                  + ":" + method.getName()
                  + ":" + method.invoke(box, "ok");
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_interpreted_java_reflection");
        if (!"Box:Box:Base:read:read:ok".equals(result)) {
            throw new IllegalStateException("Expected interpreted Java reflection result, got: " + result);
        }
        System.out.println("QinJsInterpretedJavaReflectionSmokeTestMain OK");
    }
}
