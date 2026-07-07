package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSubhutiOrPredictionSmokeTestMain {
    private QinGeneratedTsSubhutiOrPredictionSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { com_subhuti_parser_SubhutiParserFinal as SubhutiParserFinal }
                  from "@qin/generated-qin-parser-ts/com/subhuti/parser/SubhutiParserFinal.ts";
                import { com_subhuti_parser_Alternative as Alternative }
                  from "@qin/generated-qin-parser-ts/com/subhuti/parser/Alternative.ts";
                import { com_subhuti_lookahead_SubhutiTokenConsumer as SubhutiTokenConsumer }
                  from "@qin/generated-qin-parser-ts/com/subhuti/lookahead/SubhutiTokenConsumer.ts";
                import { com_subhuti_struct_SubhutiCreateToken as SubhutiCreateToken }
                  from "@qin/generated-qin-parser-ts/com/subhuti/struct/SubhutiCreateToken.ts";
                import { __qin_java_class_info__, __QinJavaUtilList } from "@qin/java-sdk-js";

                const TOKENS = __QinJavaUtilList.of(
                  SubhutiCreateToken.createKeywordToken("A", "a"),
                  SubhutiCreateToken.createKeywordToken("B", "b"),
                  SubhutiCreateToken.createKeywordToken("C", "c"),
                  SubhutiCreateToken.createKeywordToken("D", "d"),
                  SubhutiCreateToken.createValueRegToken("WhiteSpace", "[ \\\\t\\\\r\\\\n]+", "", true)
                );

                class Tokens extends SubhutiTokenConsumer {
                  A() { return this.consume("A"); }
                  B() { return this.consume("B"); }
                  C() { return this.consume("C"); }
                  D() { return this.consume("D"); }
                }

                class Parser extends SubhutiParserFinal {
                  constructor(source) {
                    super(source, __qin_java_class_info__(Tokens, { name: "Tokens" }), TOKENS);
                    this.aRuns = 0;
                    this.bRuns = 0;
                    this.cRuns = 0;
                    this.abRuns = 0;
                    this.acRuns = 0;
                    this.abcRuns = 0;
                    this.abdRuns = 0;
                  }

                  parseDistinct() {
                    return this.Rule("Distinct", () => this.Distinct());
                  }

                  parseWithUnknownAlternative() {
                    return this.Rule("WithUnknown", () => this.WithUnknown());
                  }

                  parseCommonPrefix() {
                    return this.Rule("CommonPrefix", () => this.CommonPrefix());
                  }

                  parseDeepCommonPrefix() {
                    return this.Rule("DeepCommonPrefix", () => this.DeepCommonPrefix());
                  }

                  Distinct() {
                    return this.Or(
                      Alternative.of(() => this.A()),
                      Alternative.of(() => this.B()),
                      Alternative.of(() => this.C())
                    );
                  }

                  WithUnknown() {
                    return this.Or(
                      Alternative.of(() => this.A()),
                      Alternative.of(() => this.Unknown()),
                      Alternative.of(() => this.B()),
                      Alternative.of(() => this.C())
                    );
                  }

                  CommonPrefix() {
                    return this.Or(
                      Alternative.of(() => this.AB()),
                      Alternative.of(() => this.AC()),
                      Alternative.of(() => this.B())
                    );
                  }

                  DeepCommonPrefix() {
                    return this.Or(
                      Alternative.of(() => this.ABD()),
                      Alternative.of(() => this.ABC())
                    );
                  }

                  A() {
                    this.__qin_field_tokenConsumer.A();
                    if (this.isParserFail()) return null;
                    this.aRuns++;
                    return "A";
                  }

                  B() {
                    this.__qin_field_tokenConsumer.B();
                    if (this.isParserFail()) return null;
                    this.bRuns++;
                    return "B";
                  }

                  C() {
                    this.__qin_field_tokenConsumer.C();
                    if (this.isParserFail()) return null;
                    this.cRuns++;
                    return "C";
                  }

                  AB() {
                    this.__qin_field_tokenConsumer.A();
                    this.__qin_field_tokenConsumer.B();
                    if (this.isParserFail()) return null;
                    this.abRuns++;
                    return "AB";
                  }

                  AC() {
                    this.__qin_field_tokenConsumer.A();
                    this.__qin_field_tokenConsumer.C();
                    if (this.isParserFail()) return null;
                    this.acRuns++;
                    return "AC";
                  }

                  ABC() {
                    this.__qin_field_tokenConsumer.A();
                    this.__qin_field_tokenConsumer.B();
                    this.__qin_field_tokenConsumer.C();
                    if (this.isParserFail()) return null;
                    this.abcRuns++;
                    return "ABC";
                  }

                  ABD() {
                    this.__qin_field_tokenConsumer.A();
                    this.__qin_field_tokenConsumer.B();
                    this.__qin_field_tokenConsumer.D();
                    if (this.isParserFail()) return null;
                    this.abdRuns++;
                    return "ABD";
                  }

                  Unknown() {
                    this.LA(1);
                    this.setParseFail();
                    return null;
                  }

                  snapshot(result) {
                    return {
                      result,
                      aRuns: this.aRuns,
                      bRuns: this.bRuns,
                      cRuns: this.cRuns,
                      abRuns: this.abRuns,
                      acRuns: this.acRuns,
                      abcRuns: this.abcRuns,
                      abdRuns: this.abdRuns,
                      stats: this.getOrPredictionStats()
                    };
                  }
                }

                function runCase(source, method) {
                  const parser = new Parser(source);
                  const value = parser[method]();
                  return parser.snapshot(value);
                }

                ({
                  distinct: runCase("c", "parseDistinct"),
                  unknown: runCase("b", "parseWithUnknownAlternative"),
                  commonPrefix: runCase("a c", "parseCommonPrefix"),
                  deepPrefix: runCase("a b c", "parseDeepCommonPrefix")
                });
                """, "generated_ts_subhuti_or_prediction");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        requireResult(map, "distinct", "C");
        requireStats(map, "distinct", "orPredictionSkippedAlternatives=0");
        requireStats(map, "distinct", "runtimePruningEnabled=false");
        requireRunCount(map, "distinct", "cRuns", 1);

        requireResult(map, "unknown", "B");
        requireStats(map, "unknown", "orPredictionSkippedAlternatives=0");
        requireRunCount(map, "unknown", "bRuns", 1);

        requireResult(map, "commonPrefix", "AC");
        requireStats(map, "commonPrefix", "orPredictionSkippedAlternatives=2");
        requireStats(map, "commonPrefix", "runtimePruningEnabled=true");
        requireRunCount(map, "commonPrefix", "abRuns", 0);
        requireRunCount(map, "commonPrefix", "acRuns", 1);

        requireResult(map, "deepPrefix", "ABC");
        requireStats(map, "deepPrefix", "orPredictionSkippedAlternatives=1");
        requireStats(map, "deepPrefix", "runtimePruningEnabled=true");
        requireRunCount(map, "deepPrefix", "abdRuns", 0);
        requireRunCount(map, "deepPrefix", "abcRuns", 1);

        System.out.println("QinGeneratedTsSubhutiOrPredictionSmokeTestMain OK "
                + QinObjectJsonEncoder.toJson(map));
    }

    private static void requireResult(Map<?, ?> root, String name, String expected) {
        Map<?, ?> probe = requireProbe(root, name);
        if (!expected.equals(probe.get("result"))) {
            throw new IllegalStateException("Unexpected result for " + name + ": "
                    + QinObjectJsonEncoder.toJson(probe));
        }
    }

    private static void requireStats(Map<?, ?> root, String name, String marker) {
        Map<?, ?> probe = requireProbe(root, name);
        Object stats = probe.get("stats");
        if (!(stats instanceof String text) || !text.contains(marker)) {
            throw new IllegalStateException("Expected stats marker " + marker + " for " + name + ": "
                    + QinObjectJsonEncoder.toJson(probe));
        }
    }

    private static void requireRunCount(Map<?, ?> root, String name, String key, int expected) {
        Map<?, ?> probe = requireProbe(root, name);
        Object value = probe.get(key);
        if (!(value instanceof Number number) || number.intValue() != expected) {
            throw new IllegalStateException("Unexpected " + key + " for " + name + ": "
                    + QinObjectJsonEncoder.toJson(probe));
        }
    }

    private static Map<?, ?> requireProbe(Map<?, ?> root, String name) {
        Object value = root.get(name);
        if (!(value instanceof Map<?, ?> probe)) {
            throw new IllegalStateException("Expected probe object for " + name + ", got: " + value);
        }
        return probe;
    }
}
