package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeSubclassDispatchSmokeTestMain {
    private QinGeneratedTsSlimeSubclassDispatchSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { com_slime_parser_SlimeParser as SlimeParser } from "@qin/generated-qin-parser-ts/com/slime/parser/SlimeParser.ts";
                import { com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams } from "@qin/generated-qin-parser-ts/com/slime/parser/base/SlimeJavascriptParserBase.ts";
                import { Subhuti, SubhutiRule } from "subhuti";

                @Subhuti
                class ProbeParser extends SlimeParser {
                  constructor(source) {
                    super(source);
                    this.hitPrimaryOverride = false;
                  }

                  @SubhutiRule
                  PrimaryExpression(params = {}) {
                    this.hitPrimaryOverride = true;
                    this.__qin_field_tokenConsumer.This();
                    return null;
                  }
                }

                const directParser = new ProbeParser("this;");
                directParser.parse();
                const inheritedParser = new ProbeParser("this;");
                inheritedParser.parse();
                const functionBodyParser = new ProbeParser("");
                let functionBodyError = null;
                try {
                  functionBodyParser.FunctionBody(new ExpressionParams(false, false, false));
                } catch (e) {
                  functionBodyError = String(e && e.message ? e.message : e);
                }
                ({
                  directHit: directParser.hitPrimaryOverride,
                  inheritedHit: inheritedParser.hitPrimaryOverride,
                  functionBodyNext: functionBodyParser.LA(1) == null ? "EOF" : functionBodyParser.LA(1).getTokenName(),
                  functionBodyError,
                  methodType: typeof inheritedParser.PrimaryExpression,
                  methodIsRule: inheritedParser.PrimaryExpression.__isSubhutiRule__ === true,
                  methodName: inheritedParser.PrimaryExpression.name
                });
                """, "generated_ts_slime_subclass_dispatch");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.TRUE.equals(map.get("directHit"))) {
            throw new IllegalStateException("Expected direct generated TS Slime subclass PrimaryExpression override to run: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        if (!Boolean.TRUE.equals(map.get("inheritedHit"))) {
            throw new IllegalStateException("Expected generated TS Slime subclass PrimaryExpression override to run");
        }
        if (map.get("functionBodyError") != null || !"EOF".equals(map.get("functionBodyNext"))) {
            throw new IllegalStateException("Expected generated TS Slime subclass to inherit concrete FunctionBody: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinGeneratedTsSlimeSubclassDispatchSmokeTestMain OK");
    }
}
