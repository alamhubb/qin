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
                class __QinJavaLangThrowable {
                }
                class __QinJavaLangException extends __QinJavaLangThrowable {
                }
                class __QinJavaLangReflectiveOperationException extends __QinJavaLangException {
                }
                class __QinJavaLangNoSuchMethodException extends __QinJavaLangReflectiveOperationException {
                }
                class Base {
                  baseValue() {
                    return "base";
                  }
                  setParseFail() {
                    this.failed = true;
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
                const inherited = clazz.getMethod("baseValue");
                let declaredMiss = false;
                try {
                  clazz.getDeclaredMethod("baseValue");
                } catch (e) {
                  declaredMiss = e instanceof __QinJavaLangNoSuchMethodException
                    && e instanceof __QinJavaLangReflectiveOperationException;
                }
                const alias = clazz.getSuperclass().getDeclaredMethod("_markParseFail");
                method.setAccessible(true);
                alias.invoke(box);
                clazz.getSimpleName()
                  + ":" + clazz.getName()
                  + ":" + clazz.getSuperclass().getSimpleName()
                  + ":" + method.getName()
                  + ":" + method.invoke(box, "ok")
                  + ":" + inherited.invoke(box)
                  + ":" + declaredMiss
                  + ":" + alias.getName()
                  + ":" + box.failed;
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "js_interpreted_java_reflection");
        if (!"Box:Box:Base:read:read:ok:base:true:_markParseFail:true".equals(result)) {
            throw new IllegalStateException("Expected interpreted Java reflection result, got: " + result);
        }
        System.out.println("QinJsInterpretedJavaReflectionSmokeTestMain OK");
    }
}
