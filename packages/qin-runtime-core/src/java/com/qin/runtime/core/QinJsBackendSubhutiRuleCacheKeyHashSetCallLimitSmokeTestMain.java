package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsBackendSubhutiRuleCacheKeyHashSetCallLimitSmokeTestMain {
    private QinJsBackendSubhutiRuleCacheKeyHashSetCallLimitSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-js-backend-subhuti-rule-cache-key-call-limit-");
        Files.writeString(
                root.resolve("qin.config.js"),
                "export default { name: \"qin-js-backend-subhuti-rule-cache-key-call-limit\" };\n",
                StandardCharsets.UTF_8);

        JavaEsmGlobal.setInterpretedCallCountLimit(1_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    """
                    import { com_subhuti_parser_SubhutiRuleCacheKey as SubhutiRuleCacheKey } from "@qin/generated-qin-parser-ts/com/subhuti/parser/SubhutiRuleCacheKey.ts";
                    import { com_subhuti_struct_LexerMode as LexerMode } from "@qin/generated-qin-parser-ts/com/subhuti/struct/LexerMode.ts";
                    import { HashSet } from "java:java.util";

                    (() => {
                      const mode = LexerMode.__qin_field_DEFAULT_MODE;
                      const set = new HashSet();
                      for (let i = 0; i < 80; i++) {
                        const key = new SubhutiRuleCacheKey("Rule" + (i % 4), "", i % 9, mode, "IdentifierName");
                        set.add(key);
                        set.contains(new SubhutiRuleCacheKey("Rule" + (i % 4), "", i % 9, mode, "IdentifierName"));
                        set.remove(key);
                      }
                      return set.size();
                    })();
                    """,
                    "js_backend_subhuti_rule_cache_key_hashset_call_limit");
            if (!(result instanceof Number number) || number.intValue() != 0) {
                throw new IllegalStateException("Expected empty HashSet after add/contains/remove loop, got: " + result);
            }
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }

        System.out.println("QinJsBackendSubhutiRuleCacheKeyHashSetCallLimitSmokeTestMain OK");
    }
}
