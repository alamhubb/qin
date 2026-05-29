package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsArgumentsObjectSmokeTestMain {
    private QinJsArgumentsObjectSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                function getArg(aArgs, aName, aDefaultValue) {
                  if (aName in aArgs) {
                    return aArgs[aName];
                  } else if (arguments.length === 3) {
                    return aDefaultValue;
                  }
                  return "missing";
                }
                getArg({}, "skipValidation", false) === false ? 42 : 0;
                """;
        Path root = Files.createTempDirectory("qin-js-arguments-object-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-js-arguments-object\" }\n", StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(root, source, "arguments_object");
        if (!Double.valueOf(42.0d).equals(result)) {
            throw new IllegalStateException("Expected 42, got: " + result);
        }
        System.out.println("QinJsArgumentsObjectSmokeTestMain OK");
    }
}
