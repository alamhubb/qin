package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeParserPackageAbiSmokeTestMain {
    private QinGeneratedTsSlimeParserPackageAbiSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("..", "qin-ovs-cssts-generated-ts-slime-demo")
                .toAbsolutePath()
                .normalize();
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { SlimeParser } from "slime-parser";

                const qinRead = (value, name) => {
                  const member = value && value[name];
                  return typeof member === "function" ? member.call(value) : member;
                };
                const parser = new SlimeParser("const x = 1;");
                const cst = parser.Program();
                const tokens = parser.parsedTokens;
                ({
                  cstName: qinRead(cst, "name"),
                  tokenCount: tokens ? tokens.length : -1,
                  firstTokenName: tokens && tokens.length ? qinRead(tokens[0], "tokenName") : null,
                  firstTokenValue: tokens && tokens.length ? qinRead(tokens[0], "getTokenValue") : null,
                  hasTokenConsumer: !!parser.tokenConsumer
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
