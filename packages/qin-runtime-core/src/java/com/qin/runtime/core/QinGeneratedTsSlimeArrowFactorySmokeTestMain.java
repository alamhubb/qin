package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeArrowFactorySmokeTestMain {
    private QinGeneratedTsSlimeArrowFactorySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-generated-ts-slime-arrow-factory-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-generated-ts-slime-arrow-factory\" }\n", StandardCharsets.UTF_8);

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { __qin_java_implements } from "@qin/java-sdk-js";
                import { com_slime_parser_cstToAst_SlimeAstCreateUtils as SlimeAstCreateUtils } from "@qin/generated-qin-parser-ts/SlimeAstCreateUtils";
                import { com_slime_parser_SlimeParser as SlimeParser } from "@qin/generated-qin-parser-ts/com/slime/parser/SlimeParser.ts";
                import { SlimeCstToAstUtils } from "@qin/generated-qin-parser-ts/SlimeCstToAstBridge";
                import { normalizeGeneratedAst } from "cssts-compiler/src/parser/generated-runtime-adapter.ts";

                const body = SlimeAstCreateUtils.createBlockStatement([], null);
                const arrow = SlimeAstCreateUtils.createArrowFunctionExpression([], body, false, false, null);
                const parser = new SlimeParser("const normalizeRootUrl = (value) => { const text = String(value || '').trim(); return text }\\nconst handlers = { onClick() { console.log(normalizeRootUrl('x')); return 1 } }");
                const program = SlimeCstToAstUtils.toProgram(parser.Program());
                const firstDecl = program.body().get(0);
                const parsedArrow = firstDecl.declarations().get(0).init();
                const secondDecl = program.body().get(1);
                const parsedObject = secondDecl.declarations().get(0).init();
                const parsedMethodProperty = parsedObject.properties().get(0);
                const parsedMethodValue = parsedMethodProperty.value();
                const normalizedProgram = normalizeGeneratedAst(program);
                const normalizedArrow = normalizedProgram.body[0].declarations[0].init;
                const normalizedObject = normalizedProgram.body[1].declarations[0].init;
                const normalizedMethodProperty = normalizedObject.properties[0].property || normalizedObject.properties[0];
                const normalizedMethodValue = normalizedMethodProperty.value;
                ({
                  bodyType: body.type().name(),
                  bodyImplementsAstNode: __qin_java_implements(body, "com.slime.ast.AstNode"),
                  arrowBodyPresent: arrow.body() != null,
                  arrowBodyType: arrow.body() == null ? "" : arrow.body().type().name(),
                  parsedArrowBodyPresent: parsedArrow.body() != null,
                  parsedArrowBodyType: parsedArrow.body() == null ? "" : parsedArrow.body().type().name(),
                  parsedArrowBodyStatementCount: parsedArrow.body() == null ? -1 : parsedArrow.body().body().size(),
                  parsedArrowExpression: parsedArrow.expression(),
                  parsedMethodBodyPresent: parsedMethodValue.body() != null,
                  parsedMethodBodyType: parsedMethodValue.body() == null ? "" : parsedMethodValue.body().type().name(),
                  parsedMethodBodyStatementCount: parsedMethodValue.body() == null ? -1 : parsedMethodValue.body().body().size(),
                  normalizedArrowBodyPresent: normalizedArrow.body != null,
                  normalizedArrowBodyType: normalizedArrow.body == null ? "" : normalizedArrow.body.type,
                  normalizedArrowBodyStatementCount: normalizedArrow.body == null ? -1 : normalizedArrow.body.body.length,
                  normalizedArrowExpression: normalizedArrow.expression,
                  normalizedMethodPropertyOwnValue: Object.prototype.hasOwnProperty.call(normalizedMethodProperty, "value"),
                  normalizedMethodPropertyValueKind: typeof normalizedMethodProperty.value,
                  normalizedMethodPropertyKeys: Object.keys(normalizedMethodProperty).join(","),
                  normalizedMethodPropertyInternalValueType: normalizedMethodProperty.__qin_field_value == null ? "" : normalizedMethodProperty.__qin_field_value.type().name(),
                  normalizedMethodBodyPresent: normalizedMethodValue.body != null,
                  normalizedMethodBodyType: normalizedMethodValue.body == null ? "" : normalizedMethodValue.body.type,
                  normalizedMethodBodyStatementCount: normalizedMethodValue.body == null ? -1 : normalizedMethodValue.body.body.length
                })
                """, "generated_ts_slime_arrow_factory");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        require(Boolean.TRUE.equals(map.get("bodyImplementsAstNode")),
                "Generated BlockStatement must implement com.slime.ast.AstNode: " + map);
        require(Boolean.TRUE.equals(map.get("arrowBodyPresent")),
                "Generated ArrowFunctionExpression factory dropped body: " + map);
        require(String.valueOf(map.get("arrowBodyType")).contains("BLOCK_STATEMENT"),
                "Generated ArrowFunctionExpression body type is wrong: " + map);
        require(Boolean.TRUE.equals(map.get("parsedArrowBodyPresent")),
                "Generated Slime parser dropped parsed arrow body: " + map);
        require(String.valueOf(map.get("parsedArrowBodyType")).contains("BLOCK_STATEMENT"),
                "Generated Slime parser parsed arrow body type is wrong: " + map);
        require(numberValue(map.get("parsedArrowBodyStatementCount")) == 2,
                "Generated Slime parser dropped arrow block statements: " + map);
        require(Boolean.FALSE.equals(map.get("parsedArrowExpression")),
                "Generated Slime parser parsed block arrow as expression: " + map);
        require(Boolean.TRUE.equals(map.get("parsedMethodBodyPresent")),
                "Generated Slime parser dropped object method body: " + map);
        require(String.valueOf(map.get("parsedMethodBodyType")).contains("BLOCK_STATEMENT"),
                "Generated Slime parser parsed object method body type is wrong: " + map);
        require(numberValue(map.get("parsedMethodBodyStatementCount")) == 2,
                "Generated Slime parser dropped object method block statements: " + map);
        require(Boolean.TRUE.equals(map.get("normalizedArrowBodyPresent")),
                "Generated AST normalizer dropped parsed arrow body: " + map);
        require("BlockStatement".equals(String.valueOf(map.get("normalizedArrowBodyType"))),
                "Generated AST normalizer parsed arrow body type is wrong: " + map);
        require(numberValue(map.get("normalizedArrowBodyStatementCount")) == 2,
                "Generated AST normalizer dropped arrow block statements: " + map);
        require(Boolean.FALSE.equals(map.get("normalizedArrowExpression")),
                "Generated AST normalizer parsed block arrow as expression: " + map);
        require(Boolean.TRUE.equals(map.get("normalizedMethodBodyPresent")),
                "Generated AST normalizer dropped parsed object method body: " + map);
        require("BlockStatement".equals(String.valueOf(map.get("normalizedMethodBodyType"))),
                "Generated AST normalizer parsed object method body type is wrong: " + map);
        require(numberValue(map.get("normalizedMethodBodyStatementCount")) == 2,
                "Generated AST normalizer dropped object method block statements: " + map);

        System.out.println("QinGeneratedTsSlimeArrowFactorySmokeTestMain OK");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    private static int numberValue(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.parseInt(String.valueOf(value));
    }
}
