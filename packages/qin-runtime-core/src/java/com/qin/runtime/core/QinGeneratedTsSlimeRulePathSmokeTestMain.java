package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeRulePathSmokeTestMain {
    private QinGeneratedTsSlimeRulePathSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath().getParent().resolve("qin-ovs-cssts-generated-ts-slime-demo");
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import SlimeParser from "slime-parser";
                import { Subhuti, SubhutiRule } from "subhuti";
                import {
                  com_slime_parser_base_SlimeJavascriptParserBase$ExpressionParams as ExpressionParams,
                  com_slime_parser_base_SlimeJavascriptParserBase$StatementParams as StatementParams
                } from "slime-parser/com/slime/parser/base/SlimeJavascriptParserBase.ts";
                import { __qin_java_functional } from "@qin/java-sdk-js";

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

                class PlainParser extends SlimeParser {
                  constructor(source) {
                    super(source);
                  }
                }

                class UndecoratedProbeParser extends SlimeParser {
                  constructor(source) {
                    super(source);
                    this.hitPrimaryOverride = false;
                  }

                  PrimaryExpression(params = {}) {
                    this.hitPrimaryOverride = true;
                    this.__qin_field_tokenConsumer.This();
                    return null;
                  }
                }

                function probe(source, run) {
                  const parser = new ProbeParser(source);
                  let error = null;
                  try {
                    run(parser);
                  } catch (e) {
                    error = String(e && e.message ? e.message : e);
                  }
                  return {
                    hit: parser.hitPrimaryOverride,
                    next: parser.LA(1) == null ? "EOF" : parser.LA(1).getTokenName(),
                    error
                  };
                }

                const direct = probe("this", parser => parser.PrimaryExpression({}));
                const directParams = probe("this", parser => parser.PrimaryExpression(new ExpressionParams(true, false, true)));
                const statement = probe("this;", parser => parser.StatementListItem(new StatementParams(false, true, false)));
                const statementOnly = probe("this;", parser => parser.Statement(new StatementParams(false, true, false)));
                const expressionStatement = probe("this;", parser => parser.ExpressionStatement(new StatementParams(false, true, false)));
                const expression = probe("this", parser => parser.Expression(new ExpressionParams(true, false, true)));
                const assignment = probe("this", parser => parser.AssignmentExpression(new ExpressionParams(true, false, true)));
                const conditional = probe("this", parser => parser.ConditionalExpression(new ExpressionParams(true, false, true)));
                const shortCircuit = probe("this", parser => parser.ShortCircuitExpression(new ExpressionParams(true, false, true)));
                const logicalAnd = probe("this", parser => parser.LogicalANDExpression(new ExpressionParams(true, false, true)));
                const unary = probe("this", parser => parser.UnaryExpression(new ExpressionParams(true, false, true)));
                const leftHandSide = probe("this", parser => parser.LeftHandSideExpression(new ExpressionParams(true, false, true)));
                const member = probe("this", parser => parser.MemberExpression(new ExpressionParams(true, false, true)));
                const memberNoMemo = probe("this", parser => {
                  parser.__qin_field_enableMemoization = false;
                  parser.MemberExpression(new ExpressionParams(true, false, true));
                });
                const rawMember = probe("this", parser => parser.__qin_subhuti_raw_MemberExpression(new ExpressionParams(true, false, true)));
                const basePrimary = (() => {
                  const parser = new SlimeParser("this");
                  let error = null;
                  try {
                    parser.PrimaryExpression(new ExpressionParams(true, false, true));
                  } catch (e) {
                    error = String(e && e.message ? e.message : e);
                  }
                  return {
                    next: parser.LA(1) == null ? "EOF" : parser.LA(1).getTokenName(),
                    error
                  };
                })();
                const baseMember = (() => {
                  const parser = new SlimeParser("this");
                  let error = null;
                  try {
                    parser.MemberExpression(new ExpressionParams(true, false, true));
                  } catch (e) {
                    error = String(e && e.message ? e.message : e);
                  }
                  return {
                    next: parser.LA(1) == null ? "EOF" : parser.LA(1).getTokenName(),
                    error
                  };
                })();
                const directOr = (() => {
                  const parser = new SlimeParser("this");
                  let error = null;
                  try {
                    parser.Or(__qin_java_functional(() => parser.__qin_field_tokenConsumer.This()));
                  } catch (e) {
                    error = String(e && e.message ? e.message : e);
                  }
                  return {
                    next: parser.LA(1) == null ? "EOF" : parser.LA(1).getTokenName(),
                    error
                  };
                })();
                const plainMember = (() => {
                  const parser = new PlainParser("this");
                  let error = null;
                  try {
                    parser.MemberExpression(new ExpressionParams(true, false, true));
                  } catch (e) {
                    error = String(e && e.message ? e.message : e);
                  }
                  return {
                    next: parser.LA(1) == null ? "EOF" : parser.LA(1).getTokenName(),
                    error
                  };
                })();
                const undecoratedMember = (() => {
                  const parser = new UndecoratedProbeParser("this");
                  let error = null;
                  try {
                    parser.MemberExpression(new ExpressionParams(true, false, true));
                  } catch (e) {
                    error = String(e && e.message ? e.message : e);
                  }
                  return {
                    hit: parser.hitPrimaryOverride,
                    next: parser.LA(1) == null ? "EOF" : parser.LA(1).getTokenName(),
                    error
                  };
                })();

                ({
                  direct,
                  directParams,
                  statement,
                  statementOnly,
                  expressionStatement,
                  expression,
                  assignment,
                  conditional,
                  shortCircuit,
                  logicalAnd,
                  unary,
                  leftHandSide,
                  member,
                  memberNoMemo,
                  rawMember,
                  basePrimary,
                  baseMember,
                  directOr,
                  plainMember,
                  undecoratedMember
                });
                """, "generated_ts_slime_rule_path");
        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        requireProbe(map, "direct", true);
        requireProbe(map, "directParams", true);
        requireProbe(map, "statement", true);
        requireProbe(map, "statementOnly", true);
        requireProbe(map, "expressionStatement", true);
        requireProbe(map, "expression", true);
        requireProbe(map, "assignment", true);
        requireProbe(map, "conditional", true);
        requireProbe(map, "shortCircuit", true);
        requireProbe(map, "logicalAnd", true);
        requireProbe(map, "unary", true);
        requireProbe(map, "leftHandSide", true);
        requireProbe(map, "member", true);
        requireProbe(map, "memberNoMemo", true);
        requireProbe(map, "rawMember", true);
        requireEof(map, "basePrimary");
        requireEof(map, "baseMember");
        requireEof(map, "directOr");
        requireEof(map, "plainMember");
        requireProbe(map, "undecoratedMember", true);
        System.out.println("QinGeneratedTsSlimeRulePathSmokeTestMain OK");
    }

    private static void requireProbe(Map<?, ?> root, String name, boolean expectedHit) {
        Object value = root.get(name);
        if (!(value instanceof Map<?, ?> probe)) {
            throw new IllegalStateException("Expected probe object for " + name + ", got: " + value);
        }
        if (!Boolean.valueOf(expectedHit).equals(probe.get("hit"))) {
            throw new IllegalStateException("Unexpected hit for " + name + ": " + QinObjectJsonEncoder.toJson(probe));
        }
        requireEof(root, name);
    }

    private static void requireEof(Map<?, ?> root, String name) {
        Object value = root.get(name);
        if (!(value instanceof Map<?, ?> probe)) {
            throw new IllegalStateException("Expected probe object for " + name + ", got: " + value);
        }
        if (!"EOF".equals(probe.get("next")) || probe.get("error") != null) {
            throw new IllegalStateException("Expected EOF without error for " + name + ": "
                    + QinObjectJsonEncoder.toJson(probe));
        }
    }
}
