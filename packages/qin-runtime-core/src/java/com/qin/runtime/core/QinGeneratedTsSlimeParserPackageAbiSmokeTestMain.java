package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeParserPackageAbiSmokeTestMain {
    private QinGeneratedTsSlimeParserPackageAbiSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { SlimeParser } from "slime-parser";

                const parser = new SlimeParser("const x = 1;");
                const cst = parser.Program();
                const tokens = parser.getParsedTokens();
                const firstToken = tokens && tokens.size() ? tokens.get(0) : null;
                ({
                  cstName: cst ? cst.getName() : null,
                  tokenCount: tokens ? tokens.size() : -1,
                  firstTokenName: firstToken ? firstToken.getTokenName() : null,
                  firstTokenValue: firstToken ? firstToken.getTokenValue() : null,
                  hasTokenConsumer: !!parser.getTokenConsumer()
                });
                """, "generated_ts_slime_parser_package_abi");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected generated TS Slime parser ABI result object, got: " + result);
        }
        Object tokenCount = map.get("tokenCount");
        if (!(tokenCount instanceof Number count) || count.intValue() <= 0) {
            throw new IllegalStateException("Expected generated TS Slime parser to expose parsedTokens array, got: " + map);
        }
        if (!Boolean.TRUE.equals(map.get("hasTokenConsumer"))) {
            throw new IllegalStateException("Expected generated TS Slime parser to expose tokenConsumer, got: " + map);
        }
        System.out.println("QinGeneratedTsSlimeParserPackageAbiSmokeTestMain OK " + map);
    }
}
