package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeSubclassDispatchSmokeTestMain {
    private QinGeneratedTsSlimeSubclassDispatchSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath().getParent().resolve("qin-ovs-cssts-generated-ts-slime-demo");
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import SlimeParser from "slime-parser";
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
                ({
                  directHit: directParser.hitPrimaryOverride,
                  inheritedHit: inheritedParser.hitPrimaryOverride,
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
        System.out.println("QinGeneratedTsSlimeSubclassDispatchSmokeTestMain OK");
    }
}
