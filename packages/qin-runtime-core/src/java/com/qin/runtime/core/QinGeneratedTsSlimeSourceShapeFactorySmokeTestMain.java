package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeSourceShapeFactorySmokeTestMain {
    private QinGeneratedTsSlimeSourceShapeFactorySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-generated-ts-slime-source-shape-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-generated-ts-slime-source-shape\" }\n", StandardCharsets.UTF_8);

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { com_slime_parser_cstToAst_SlimeAstCreateUtils as SlimeAstCreateUtils } from "@qin/generated-qin-parser-ts/SlimeAstCreateUtils";

                const id = SlimeAstCreateUtils.createIdentifier("h", null);
                const prop = SlimeAstCreateUtils.createIdentifier("push", null);
                const array = SlimeAstCreateUtils.createArrayExpression([]);
                const callWithoutLoc = SlimeAstCreateUtils.createCallExpression(id, []);
                const callWithLoc = SlimeAstCreateUtils.createCallExpression(id, [array], null);
                const dotLoc = SlimeAstCreateUtils.createSyntheticSourceLocation("Dot");
                const dot = SlimeAstCreateUtils.createSyntaxToken("Dot", ".", dotLoc);
                const member = SlimeAstCreateUtils.createMemberExpression(id, dot, prop);

                ({
                  arrayPresent: array != null,
                  arraySize: array.elements().size(),
                  callWithoutLocPresent: callWithoutLoc != null,
                  callWithoutLocOptional: callWithoutLoc.optional(),
                  callWithLocArgCount: callWithLoc.__qin_arguments().size(),
                  memberPresent: member != null,
                  memberComputed: member.computed(),
                  memberOptional: member.optional(),
                  memberPropertyName: member.property().name()
                })
                """, "generated_ts_slime_source_shape_factory");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        require(Boolean.TRUE.equals(map.get("arrayPresent")), "Array factory shape failed: " + map);
        require(numberValue(map.get("arraySize")) == 0, "Array factory should preserve empty elements: " + map);
        require(Boolean.TRUE.equals(map.get("callWithoutLocPresent")), "Call factory two-arg shape failed: " + map);
        require(Boolean.FALSE.equals(map.get("callWithoutLocOptional")), "Two-arg call should default optional=false: " + map);
        require(numberValue(map.get("callWithLocArgCount")) == 1, "Three-arg call should preserve arguments: " + map);
        require(Boolean.TRUE.equals(map.get("memberPresent")), "Member factory source shape failed: " + map);
        require(Boolean.FALSE.equals(map.get("memberComputed")), "Dot member should default computed=false: " + map);
        require(Boolean.FALSE.equals(map.get("memberOptional")), "Dot member should default optional=false: " + map);
        require("push".equals(String.valueOf(map.get("memberPropertyName"))), "Member property name mismatch: " + map);

        System.out.println("QinGeneratedTsSlimeSourceShapeFactorySmokeTestMain OK");
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
