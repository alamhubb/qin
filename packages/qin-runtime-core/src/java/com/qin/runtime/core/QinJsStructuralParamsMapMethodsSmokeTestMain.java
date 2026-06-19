package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsStructuralParamsMapMethodsSmokeTestMain {
    private QinJsStructuralParamsMapMethodsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const declarationParams = { Default: false, Yield: true };
                const expressionParams = { In: false, Await: true };
                const statementParams = { Return: true };
                const templateParams = { In: false, Yield: true, Await: false, Tagged: true };

                const updated = expressionParams.withIn(true).withYield(true).withAwait(false);
                const fromTemplate = templateParams.expressionParams();
                [
                  declarationParams.isDefault(),
                  declarationParams.yield(),
                  declarationParams.await(),
                  expressionParams.in(),
                  expressionParams.await(),
                  statementParams.returnAllowed(),
                  updated.in(),
                  updated.yield(),
                  updated.await(),
                  templateParams.tagged(),
                  fromTemplate.in(),
                  fromTemplate.yield(),
                  fromTemplate.await()
                ].join(",");
                """;
        Path root = Files.createTempDirectory("qin-js-structural-params-map-methods-");
        Path sourceFile = root.resolve("main.ts");
        Files.writeString(sourceFile, source, StandardCharsets.UTF_8);
        Object result = new QinInMemoryJvmRunner().compileAndRun(
                sourceFile,
                root,
                "com.qin.runtime.generated.JsStructuralParamsMapMethodsSmoke");
        if (!"false,true,false,false,true,true,true,true,false,true,false,true,false".equals(result)) {
            throw new IllegalStateException(
                    "Expected false,true,false,false,true,true,true,true,false,true,false,true,false, got: " + result);
        }
        System.out.println("QinJsStructuralParamsMapMethodsSmokeTestMain OK");
    }
}
