package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.ir.QinIrConstDeclaration;
import com.qin.lang.ir.QinIrIdentifierReference;
import com.qin.lang.ir.QinIrInstanceMethodCallExpression;
import com.qin.lang.ir.QinIrJavaImport;
import com.qin.lang.ir.QinIrProgram;
import com.qin.lang.ir.QinIrStaticMethodCallExpression;
import com.qin.lang.ir.QinIrStringLiteral;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsBackendJavaRegexSmokeTestMain {
    private QinJsBackendJavaRegexSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinIrProgram(
                List.of(
                        new QinIrConstDeclaration(
                                "quoted",
                                new QinIrStaticMethodCallExpression(
                                        "Pattern",
                                        "java.util.regex.Pattern",
                                        "quote",
                                        List.of(new QinIrStringLiteral("?.*")))),
                        new QinIrConstDeclaration(
                                "pattern",
                                new QinIrStaticMethodCallExpression(
                                        "Pattern",
                                        "java.util.regex.Pattern",
                                        "compile",
                                        List.of(new QinIrStringLiteral("\\b[a-z]+\\b")))),
                        new QinIrConstDeclaration(
                                "matcher",
                                new QinIrInstanceMethodCallExpression(
                                        new QinIrIdentifierReference("pattern"),
                                        "matcher",
                                        List.of(new QinIrStringLiteral("12 abc 34"))))),
                List.of(),
                List.of(),
                List.of(),
                List.of(new QinIrJavaImport(
                        "java:java.util.regex",
                        "Pattern",
                        "Pattern",
                        "java.util.regex.Pattern")),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of());

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaUtilRegexPattern"), "Pattern runtime shim");
        require(generated.contains("const Pattern = __QinJavaUtilRegexPattern;"), "Pattern import alias");
        require(generated.contains("Pattern.quote(\"?.*\")"), "Pattern.quote call");
        require(generated.contains("Pattern.compile(\"\\\\b[a-z]+\\\\b\")"), "Pattern.compile call");

        Path root = Files.createTempDirectory("qin-js-backend-regex-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-js-backend-regex\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst regionHit = matcher.region(3, 6).lookingAt();\n"
                        + "const regionGroup = matcher.group();\n"
                        + "const leadingMiss = Pattern.compile(\"[ ]+\").matcher(\"a b\").lookingAt();\n"
                        + "const second = pattern.matcher(\"xx yy\");\n"
                        + "const found = second.find();\n"
                        + "const replaced = pattern.matcher(\"ab cd\").replaceAll(\"X\");\n"
                        + "[quoted, pattern.pattern(), pattern.flags(), regionHit, regionGroup, leadingMiss, found, second.group(), replaced].join(\":\");\n",
                "js_backend_regex");
        if (!"\\Q?.*\\E:\\b[a-z]+\\b:0.0:true:abc:false:true:xx:X X".equals(result)) {
            throw new IllegalStateException("Expected generated regex result, got: " + result);
        }
        System.out.println("QinJsBackendJavaRegexSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
