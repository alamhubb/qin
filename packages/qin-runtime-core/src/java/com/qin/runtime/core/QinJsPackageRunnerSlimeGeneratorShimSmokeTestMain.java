package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJsPackageRunnerSlimeGeneratorShimSmokeTestMain {
    private QinJsPackageRunnerSlimeGeneratorShimSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path projectRoot = Files.createTempDirectory("qin-js-slime-generator-shim-");
        Object result = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate",
                List.of());
        if (!"\"ok\";".equals(String.valueOf(result))) {
            throw new IllegalStateException("Unexpected slime-generator shim result: " + result);
        }
        Object javaStyleResult = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate_java_style_ast",
                List.of());
        if (!"\"java-style\";".equals(String.valueOf(javaStyleResult))) {
            throw new IllegalStateException("Unexpected slime-generator Java-style AST result: " + javaStyleResult);
        }
        Object normalizedResult = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate_normalized_over_qin_fields",
                List.of());
        if (!"\"normalized\";".equals(String.valueOf(normalizedResult))) {
            throw new IllegalStateException("Unexpected slime-generator normalized AST result: " + normalizedResult);
        }
        Object escapedArgumentsResult = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate_escaped_arguments",
                List.of());
        if (!"fn(\"a\", \"b\");".equals(String.valueOf(escapedArgumentsResult))) {
            throw new IllegalStateException(
                    "Unexpected slime-generator escaped arguments result: " + escapedArgumentsResult);
        }
        Object wrappedItemsResult = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate_wrapped_object_array_items",
                List.of());
        if (!"fn({ id: \"balance-panel\" }, [\"Loading balance monitor...\"]);"
                .equals(String.valueOf(wrappedItemsResult))) {
            throw new IllegalStateException(
                    "Unexpected slime-generator wrapped item result: " + wrappedItemsResult);
        }
        Object tokenKindExportResult = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate_token_kind_export",
                List.of());
        String tokenKindExportCode = String.valueOf(tokenKindExportResult);
        if (!tokenKindExportCode.startsWith("export const SummaryGrid = () => {")
                || tokenKindExportCode.contains("type=Const")
                || tokenKindExportCode.contains("[object Object]")) {
            throw new IllegalStateException(
                    "Unexpected slime-generator token kind export result: " + tokenKindExportResult);
        }
        Object parenthesizedConditionalResult = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate_parenthesized_conditional",
                List.of());
        if (!"ready ? (cached ? \"cache\" : \"live\") : \"waiting\";"
                .equals(String.valueOf(parenthesizedConditionalResult))) {
            throw new IllegalStateException(
                    "Unexpected slime-generator parenthesized conditional result: "
                            + parenthesizedConditionalResult);
        }
        Object updateExpressionResult = new QinJsPackageRunner().invokeNamedExport(
                projectRoot,
                "slime-generator",
                "__qin_smoke_generate_update_expression",
                List.of());
        if (!"count.value++;\n--remaining;".equals(String.valueOf(updateExpressionResult))) {
            throw new IllegalStateException(
                    "Unexpected slime-generator update expression result: " + updateExpressionResult);
        }
        System.out.println("QinJsPackageRunnerSlimeGeneratorShimSmokeTestMain OK");
    }
}
