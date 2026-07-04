package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrNumberLiteral;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaCharacterSmokeTestMain {
    private QinJsBackendJavaCharacterSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "blank",
                                new QinIrStaticMethodCallExpression(
                                        "Character",
                                        "java.lang.Character",
                                        "isWhitespace",
                                        List.of(new QinIrStringLiteral(" ")))),
                        new QinIrConstDeclaration(
                                "upper",
                                new QinIrStaticMethodCallExpression(
                                        "Character",
                                        "java.lang.Character",
                                        "toUpperCase",
                                        List.of(new QinIrStringLiteral("q")))),
                        new QinIrConstDeclaration(
                                "count",
                                new QinIrStaticMethodCallExpression(
                                        "Character",
                                        "java.lang.Character",
                                        "charCount",
                                        List.of(new QinIrNumberLiteral(0x1F600))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new QinIrJavaImport(
                                "java:java.lang",
                                "Character",
                                "Character",
                                "java.lang.Character")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("const __QinJavaLangCharacter"), "Character runtime shim");
        require(generated.contains("const Character = __QinJavaLangCharacter;"), "Character import alias");
        require(generated.contains("Character.isWhitespace"), "Character.isWhitespace call");

        Path root = Files.createTempDirectory("qin-js-backend-character-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-character\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nblank + ':' + upper + ':' + count;\n",
                "js_backend_character");
        if (!"true:Q:2".equals(result)) {
            throw new IllegalStateException("Expected generated Character result, got: " + result);
        }
        System.out.println("QinJsBackendJavaCharacterSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}