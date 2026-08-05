package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedQinParserSubhutiLexerKeywordBoundarySmokeTestMain {
    private QinGeneratedQinParserSubhutiLexerKeywordBoundarySmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = findQinRoot();
        Path packageRoot = qinRoot.resolve(".qin")
                .resolve("generated")
                .resolve("qin-parser")
                .resolve("ts-esm");
        Path smokeRoot = Files.createTempDirectory("qin-generated-subhuti-lexer-keyword-boundary-");
        Files.writeString(smokeRoot.resolve("qin.config.js"), """
                export default {
                  name: "qin-generated-subhuti-lexer-keyword-boundary",
                  type: "library",
                  entry: "main.ts",
                  packageOverrides: {
                    "@qin/generated-qin-parser-ts": "%s",
                    "@qin/java-sdk-js": "%s"
                  }
                }
                """.formatted(
                        jsPath(packageRoot),
                        jsPath(packageRoot.getParent().resolve("java-sdk-js"))),
                StandardCharsets.UTF_8);

        Object result = new QinJsPackageRunner().runModuleSource(smokeRoot, """
                import { com_slime_token_JavaScriptTokens as JavaScriptTokens }
                  from "@qin/generated-qin-parser-ts/com/slime/token/JavaScriptTokens.ts";
                import { com_subhuti_lexer_SubhutiLexer as SubhutiLexer }
                  from "@qin/generated-qin-parser-ts/com/subhuti/lexer/SubhutiLexer.ts";
                import { com_subhuti_struct_LexerMode as LexerMode }
                  from "@qin/generated-qin-parser-ts/com/subhuti/struct/LexerMode.ts";

                const lexer = new SubhutiLexer(JavaScriptTokens.getTokens());
                const entry = lexer.readTokenAt("ifx", 0, 1, 1, LexerMode.DEFAULT_MODE, null);
                const token = entry == null ? null : entry.getToken();
                ({
                  tokenName: token == null ? null : token.getTokenName(),
                  tokenValue: token == null ? null : token.getTokenValue(),
                  nextIndex: entry == null ? null : entry.getNextCodeIndex()
                })
                """, "generated_qin_parser_subhuti_lexer_keyword_boundary");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        if (!"IdentifierName".equals(map.get("tokenName")) || !"ifx".equals(map.get("tokenValue"))) {
            throw new IllegalStateException("Expected keyword boundary to fall through to IdentifierName, got: "
                    + QinObjectJsonEncoder.toJson(map));
        }
        if (!Double.valueOf(3.0).equals(map.get("nextIndex"))) {
            throw new IllegalStateException("Expected token to advance to index 3, got: "
                    + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinGeneratedQinParserSubhutiLexerKeywordBoundarySmokeTestMain OK");
    }

    private static Path findQinRoot() {
        Path search = Path.of("").toAbsolutePath().normalize();
        while (search != null) {
            if (Files.isDirectory(search.resolve("packages").resolve("qin-runtime-core"))
                    && Files.isDirectory(search.resolve("packages").resolve("qin-parser"))
                    && Files.isRegularFile(search.resolve("qin.config.js"))) {
                return search;
            }
            search = search.getParent();
        }
        throw new IllegalStateException("Cannot find Qin repo root");
    }

    private static String jsPath(Path path) {
        return path.toAbsolutePath().normalize().toString().replace('\\', '/');
    }
}
