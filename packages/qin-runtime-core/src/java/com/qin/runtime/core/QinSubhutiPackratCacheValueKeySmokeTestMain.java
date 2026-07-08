package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinSubhutiPackratCacheValueKeySmokeTestMain {
    private QinSubhutiPackratCacheValueKeySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { com_subhuti_cache_SubhutiPackratCache as SubhutiPackratCache }
                  from "@qin/generated-qin-parser-ts/com/subhuti/cache/SubhutiPackratCache.ts";
                import { com_subhuti_parser_SubhutiRuleCacheKey as SubhutiRuleCacheKey }
                  from "@qin/generated-qin-parser-ts/com/subhuti/parser/SubhutiRuleCacheKey.ts";
                import { com_subhuti_struct_LexerMode as LexerMode }
                  from "@qin/generated-qin-parser-ts/com/subhuti/struct/LexerMode.ts";

                const cache = new SubhutiPackratCache(16);
                const mode = LexerMode.__qin_field_DEFAULT_MODE;
                const first = new SubhutiRuleCacheKey("FormalParameters", "[object#value:1]", 3, mode, "LParen");
                const second = new SubhutiRuleCacheKey("FormalParameters", "[object#value:1]", 3, mode, "LParen");
                cache.put(first, "hit");
                const cached = cache.get(second);
                ({
                  keysEqual: first.equals(second),
                  hashEqual: first.hashCode() === second.hashCode(),
                  present: cached.isPresent(),
                  value: cached.isPresent() ? cached.get() : null,
                  size: cache.size()
                });
                """, "subhuti_packrat_cache_value_key");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!Boolean.TRUE.equals(map.get("keysEqual"))
                || !Boolean.TRUE.equals(map.get("hashEqual"))
                || !Boolean.TRUE.equals(map.get("present"))
                || !"hit".equals(map.get("value"))) {
            throw new IllegalStateException("Expected SubhutiPackratCache to hit for equal cache keys: "
                    + QinObjectJsonEncoder.toJson(result));
        }
        System.out.println("QinSubhutiPackratCacheValueKeySmokeTestMain OK");
    }
}
