package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinSubhutiRuleCacheValueKeySmokeTestMain {
    private QinSubhutiRuleCacheValueKeySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath().getParent().resolve("qin-ovs-cssts-generated-ts-slime-demo");
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { __qin_subhuti_rule_cache_key } from "@qin/java-sdk-js/tooling";
                import { com_slime_parser_base_SlimeJavascriptParserBase$DeclarationParams as DeclarationParams }
                  from "../../../packages/slime-parser/com/slime/parser/base/SlimeJavascriptParserBase.ts";

                class Params {
                  constructor(yieldFlag, awaitFlag, isDefault) {
                    this.yieldFlag = yieldFlag;
                    this.awaitFlag = awaitFlag;
                    this.isDefault = isDefault;
                  }

                  equals(other) {
                    return other instanceof Params
                      && this.yieldFlag === other.yieldFlag
                      && this.awaitFlag === other.awaitFlag
                      && this.isDefault === other.isDefault;
                  }

                  hashCode() {
                    return (this.yieldFlag ? 1231 : 1237)
                      * 31
                      + (this.awaitFlag ? 1231 : 1237)
                      * 31
                      + (this.isDefault ? 1231 : 1237);
                  }
                }

                const first = new Params(false, true, false);
                const second = new Params(false, true, false);
                const firstKey = __qin_subhuti_rule_cache_key([first]);
                const secondKey = __qin_subhuti_rule_cache_key([second]);
                const generatedFirst = new DeclarationParams(false, true, false);
                const generatedSecond = new DeclarationParams(false, true, false);
                const generatedFirstKey = __qin_subhuti_rule_cache_key([generatedFirst]);
                const generatedSecondKey = __qin_subhuti_rule_cache_key([generatedSecond]);
                ({
                  firstEqualsSecond: first.equals(second),
                  secondInstanceOfParams: second instanceof Params,
                  firstHash: first.hashCode(),
                  secondHash: second.hashCode(),
                  firstKey,
                  secondKey,
                  keysMatch: firstKey === secondKey,
                  generatedEquals: generatedFirst.equals(generatedSecond),
                  generatedInstanceOf: generatedSecond instanceof DeclarationParams,
                  generatedFirstHash: generatedFirst.hashCode(),
                  generatedSecondHash: generatedSecond.hashCode(),
                  generatedFirstKey,
                  generatedSecondKey,
                  generatedKeysMatch: generatedFirstKey === generatedSecondKey
                });
                """, "subhuti_rule_cache_value_key");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.TRUE.equals(map.get("firstEqualsSecond"))
                || !Boolean.TRUE.equals(map.get("secondInstanceOfParams"))
                || !Boolean.TRUE.equals(map.get("keysMatch"))
                || !Boolean.TRUE.equals(map.get("generatedEquals"))
                || !Boolean.TRUE.equals(map.get("generatedInstanceOf"))
                || !Boolean.TRUE.equals(map.get("generatedKeysMatch"))) {
            throw new IllegalStateException("Expected Subhuti value cache keys to reuse equal hash objects: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinSubhutiRuleCacheValueKeySmokeTestMain OK");
    }
}
