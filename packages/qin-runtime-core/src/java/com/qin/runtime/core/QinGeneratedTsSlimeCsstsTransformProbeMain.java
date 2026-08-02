package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinGeneratedTsSlimeCsstsTransformProbeMain {
    private QinGeneratedTsSlimeCsstsTransformProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        String fullSource = """
                import { ref } from 'vue'

                const count = ref(0)
                const title = 'Rendered from Vue lang=cssts'
                const panelStyle = css { displayFlex, colorBlue }

                function increment() {
                  count.value++
                }
                """;
        String label = args.length > 0 ? args[0] : "full";
        String source = switch (label) {
            case "import" -> "import { ref } from 'vue'\n";
            case "const" -> "const count = ref(0)\n";
            case "constDirect" -> "const count = ref(0)\n";
            case "twoConst" -> """
                    const count = ref(0)
                    const title = 'Rendered from Vue lang=cssts'
                    """;
            case "cssConst" -> "const panelStyle = css { displayFlex, colorBlue }\n";
            case "importConst" -> """
                    import { ref } from 'vue'

                    const count = ref(0)
                    """;
            case "importTwoConst" -> """
                    import { ref } from 'vue'

                    const count = ref(0)
                    const title = 'Rendered from Vue lang=cssts'
                    """;
            case "importCssConst" -> """
                    import { ref } from 'vue'

                    const panelStyle = css { displayFlex, colorBlue }
                    """;
            case "noFunction" -> """
                    import { ref } from 'vue'

                    const count = ref(0)
                    const title = 'Rendered from Vue lang=cssts'
                    const panelStyle = css { displayFlex, colorBlue }
                    """;
            case "function" -> "function increment() {\n  count.value++\n}\n";
            case "functionDirect" -> "function increment() {\n  count.value++\n}\n";
            case "functionNoUpdate" -> "function increment() {\n  count.value\n}\n";
            case "cssFunctionNoUpdate" -> """
                    const panelStyle = css { displayFlex, colorBlue }

                    function increment() {
                      count.value
                    }
                    """;
            case "cssFunction" -> """
                    const panelStyle = css { displayFlex, colorBlue }

                    function increment() {
                      count.value++
                    }
                    """;
            case "full" -> fullSource;
            case "fullDirect" -> fullSource;
            default -> throw new IllegalArgumentException("Unknown probe case: " + label);
        };

        String wrapper = """
                import CssTsParser from "cssts-compiler/src/parser/CssTsParser.js";
                import { CssTsCstToAst, CssTsCstToAstUtils } from "cssts-compiler/src/factory/index.js";
                import { parseStyleName, transformCssTs } from "cssts-compiler";
                import { parseStyleName as directParseStyleName, transformCssTs as directTransformCssTs } from "cssts-compiler/src/transform/index.ts";
                import { RuntimeStore } from "cssts-compiler/src/store/RuntimeStore.ts";
                import { ensureRuntimeAtomData } from "cssts-compiler/src/utils/cssClassName.ts";
                import { SlimeGenerator } from "slime-generator";
                import { DeclarationParams, ExpressionParams } from "@qin/generated-qin-parser-ts";

                const source = %s;
                const probeRevision = "module-item-diag-20260730-1521";
                const includeDirectDiagnostics = %s.endsWith("Direct");
                function childrenSummary(node, depth = 0) {
                  if (!node || depth > 3) return '';
                  const name = node.name || (node.getName ? node.getName() : null);
                  const value = node.value || (node.getValue ? node.getValue() : null);
                  const children = node.children || (node.getChildren ? node.getChildren() : []) || [];
                  const childText = Array.from(children).map(child => childrenSummary(child, depth + 1)).filter(Boolean).join('|');
                  return name + (value ? ':' + value : '') + (childText ? '[' + childText + ']' : '');
                }
                function findCst(node, target) {
                  if (!node) return null;
                  const name = node.name || (node.getName ? node.getName() : null);
                  if (name === target) return node;
                  const children = node.children || (node.getChildren ? node.getChildren() : []) || [];
                  for (const child of Array.from(children)) {
                    const found = findCst(child, target);
                    if (found) return found;
                  }
                  return null;
                }
                function javaSize(value) {
                  if (!value) return null;
                  if (Array.isArray(value)) return value.length;
                  if (typeof value.size === 'function') return value.size();
                  if (typeof value.size === 'number') return value.size;
                  return null;
                }
                function javaArray(value) {
                  if (!value) return [];
                  if (Array.isArray(value)) return value;
                  if (typeof value.size === 'function' && typeof value.get === 'function') {
                    const result = [];
                    for (let i = 0; i < value.size(); i++) result.push(value.get(i));
                    return result;
                  }
                  if (typeof value[Symbol.iterator] === 'function') return Array.from(value);
                  return [];
                }
                function cstChildren(node) {
                  if (!node) return [];
                  const children = node.children || (node.getChildren ? node.getChildren() : []) || [];
                  return Array.from(children);
                }
                function cstName(node) {
                  return node && (node.name || (node.getName ? node.getName() : null));
                }
                function cstValue(node) {
                  return node && (node.value || (node.getValue ? node.getValue() : null));
                }
                function astInterfaces(node) {
                  return node && node.constructor ? String(node.constructor.__qin_java_interfaces) : null;
                }
                function directModuleItemFacts(cst) {
                  const moduleItems = cstChildren(findCst(cst, 'ModuleItemList'));
                  return moduleItems.map((item, index) => {
                    let ast = null;
                    let error = null;
                    try {
                      ast = new CssTsCstToAst().createStatementListItemAst(item);
                    } catch (err) {
                      error = err && err.stack ? String(err.stack) : String(err);
                    }
                    return {
                      index,
                      name: cstName(item),
                      value: cstValue(item),
                      childNames: cstChildren(item).map(child => cstName(child) + (cstValue(child) ? ':' + cstValue(child) : '')).join('|'),
                      astClass: ast && ast.constructor ? ast.constructor.name : null,
                      astInterfaces: astInterfaces(ast),
                      astType: astTypeName(ast),
                      error
                    };
                  });
                }
                function astField(node, name) {
                  if (!node) return null;
                  const direct = node[name];
                  if (direct !== undefined && typeof direct !== 'function') return direct;
                  const qinField = node['__qin_field_' + name];
                  if (qinField !== undefined) return qinField;
                  if (typeof direct === 'function') return direct.call(node);
                  const getter = node['get' + name.slice(0, 1).toUpperCase() + name.slice(1)];
                  return typeof getter === 'function' ? getter.call(node) : null;
                }
                function astTypeName(node) {
                  if (!node) return null;
                  const type = astField(node, 'type');
                  if (typeof type === 'string') return type;
                  if (type && typeof type.name === 'function') return type.name();
                  if (type && type.__qinEnumName) return type.__qinEnumName;
                  return type == null ? null : String(type);
                }
                function callTransform(fn, text) {
                  const result = { value: null, error: null };
                  try {
                    result.value = fn(text);
                  } catch (error) {
                    result.error = error && error.stack ? String(error.stack).split('\\n').slice(0, 6).join('\\n') : String(error);
                  }
                  return result;
                }
                function localObjectReturnProbe(text) {
                  const result = { code: text };
                  const mapping = [];
                  return {
                    code: result.code,
                    mapping,
                    hasStyles: mapping.length > 0
                  };
                }
                function localTransformClone(text) {
                  const parser = new CssTsParser(text);
                  const cst = parser.Program();
                  const ast = CssTsCstToAstUtils.toFileAst(cst);
                  const localUsedAtoms = CssTsCstToAstUtils.getUsedAtoms();
                  RuntimeStore.addUsedStyles(localUsedAtoms);
                  for (const atomName of localUsedAtoms) {
                    ensureRuntimeAtomData(atomName);
                  }
                  const tokens = parser.parsedTokens;
                  const result = SlimeGenerator.generator(ast, tokens);
                  const mapping = result.mapping.filter(
                    (m) => m.source && m.generate && m.source.length > 0
                  );
                  return {
                    code: result.code,
                    mapping,
                    hasStyles: localUsedAtoms.size > 0
                  };
                }
                function variableDeclarationFacts(node) {
                  const declarations = javaArray(astField(node, 'declarations'));
                  const first = declarations[0] || null;
                  const id = astField(first, 'id');
                  const init = astField(first, 'init');
                  const callee = astField(init, 'callee');
                  return {
                    className: node && node.constructor ? node.constructor.name : null,
                    type: astTypeName(node),
                    kind: astField(node, 'kind'),
                    declarationsSize: declarations.length,
                    firstClassName: first && first.constructor ? first.constructor.name : null,
                    firstType: astTypeName(first),
                    idClassName: id && id.constructor ? id.constructor.name : null,
                    idType: astTypeName(id),
                    idName: astField(id, 'name'),
                    initClassName: init && init.constructor ? init.constructor.name : null,
                    initType: astTypeName(init),
                    calleeClassName: callee && callee.constructor ? callee.constructor.name : null,
                    calleeType: astTypeName(callee),
                    calleeName: astField(callee, 'name'),
                    argCount: javaArray(astField(init, 'arguments')).length,
                    firstKeys: first ? Object.keys(first).join(',') : null,
                    nodeKeys: node ? Object.keys(node).join(',') : null
                  };
                }
                function functionDeclarationFacts(node) {
                  const body = astField(node, 'body');
                  const bodyItems = javaArray(astField(body, 'body'));
                  const firstStatement = bodyItems[0] || null;
                  const expression = astField(firstStatement, 'expression');
                  const argument = astField(expression, 'argument');
                  const operator = astField(expression, 'operator');
                  return {
                    className: node && node.constructor ? node.constructor.name : null,
                    type: astTypeName(node),
                    bodyClassName: body && body.constructor ? body.constructor.name : null,
                    bodyType: astTypeName(body),
                    bodySize: bodyItems.length,
                    firstStatementClassName: firstStatement && firstStatement.constructor ? firstStatement.constructor.name : null,
                    firstStatementType: astTypeName(firstStatement),
                    expressionClassName: expression && expression.constructor ? expression.constructor.name : null,
                    expressionType: astTypeName(expression),
                    expressionKeys: expression ? Object.keys(expression).join(',') : null,
                    operatorValue: operator && typeof operator === 'object' ? astField(operator, 'value') : operator,
                    prefix: astField(expression, 'prefix'),
                    argumentClassName: argument && argument.constructor ? argument.constructor.name : null,
                    argumentType: astTypeName(argument),
                    argumentKeys: argument ? Object.keys(argument).join(',') : null
                  };
                }
                function parserFailValue(parser) {
                  return parser && parser.isParserFail ? parser.isParserFail() : parser.parserFail;
                }
                function parserIndexValue(parser) {
                  if (!parser) return null;
                  if (parser.getCurrentIndex) return parser.getCurrentIndex();
                  if (parser.currentTokenIndex) return parser.currentTokenIndex();
                  return parser.currentIndex || null;
                }
                function tokenAt(parser, offset) {
                  if (!parser || !parser.tokenNameAt) return null;
                  const name = parser.tokenNameAt(offset);
                  if (!name) return null;
                  const token = parser.LA ? parser.LA(offset) : null;
                  const value = token && token.value ? token.value() : (token && token.tokenValue ? token.tokenValue : null);
                  return value ? name + ':' + value : name;
                }
                function directRuleProbe(label, text, invoke) {
                  const parser = new CssTsParser(text);
                  let result = null;
                  let error = null;
                  try {
                    result = invoke(parser);
                  } catch (e) {
                    error = e && e.stack ? String(e.stack).split('\\n').slice(0, 4).join('\\n') : String(e);
                  }
                  const cst = parser.getCurCst ? parser.getCurCst() : null;
                  return {
                    label,
                    error,
                    parserFail: parserFailValue(parser),
                    parseSuccess: parser._parseSuccess,
                    currentIndex: parserIndexValue(parser),
                    token1: tokenAt(parser, 1),
                    token2: tokenAt(parser, 2),
                    token3: tokenAt(parser, 3),
                    resultName: result && (result.name || (result.getName ? result.getName() : null)),
                    cstName: cst && (cst.name || (cst.getName ? cst.getName() : null)),
                    cstSummary: childrenSummary(cst)
                  };
                }
                function directParserDiagnostics(text) {
                  const factsParser = new CssTsParser(text);
                  const expressionParams = new ExpressionParams(false, false, true);
                  const refCallText = "ref(0)" + '\\n';
                  const argumentsText = "(0)" + '\\n';
                  function prototypeChainFacts(parser) {
                    const facts = [];
                    let proto = parser && parser.constructor && parser.constructor.prototype;
                    for (let depth = 0; proto && depth < 8; depth++) {
                      facts.push({
                        depth,
                        constructorName: proto.constructor && proto.constructor.name,
                        hasAssignmentExpression: !!proto.AssignmentExpression,
                        assignmentExpressionSource: proto.AssignmentExpression ? String(proto.AssignmentExpression).slice(0, 180) : null,
                        hasRawAssignmentExpression: !!proto.__qin_subhuti_raw_AssignmentExpression,
                        rawAssignmentExpressionSource: proto.__qin_subhuti_raw_AssignmentExpression ? String(proto.__qin_subhuti_raw_AssignmentExpression).slice(0, 180) : null
                      });
                      proto = Object.getPrototypeOf(proto);
                    }
                    return facts;
                  }
                  function expressionBranchFacts(sample) {
                    const parser = new CssTsParser(sample);
                    const rawAssignmentSource = parser.__qin_subhuti_raw_AssignmentExpression
                      ? String(parser.__qin_subhuti_raw_AssignmentExpression).slice(0, 180)
                      : null;
                    return {
                      constructorName: parser.constructor && parser.constructor.name,
                      rawAssignmentSource,
                      token1: tokenAt(parser, 1),
                      token2: tokenAt(parser, 2),
                      token3: tokenAt(parser, 3),
                      canStartArrowFunctionHead: parser.canStartArrowFunctionHead ? parser.canStartArrowFunctionHead() : null,
                      canStartAsyncArrowFunctionHead: parser.canStartAsyncArrowFunctionHead ? parser.canStartAsyncArrowFunctionHead() : null,
                      hasTopLevelAssignmentOperatorAhead: parser.hasTopLevelAssignmentOperatorAhead ? parser.hasTopLevelAssignmentOperatorAhead() : null,
                      canStartAssignmentExpression: parser.canStartAssignmentExpression ? parser.canStartAssignmentExpression(expressionParams, 1) : null
                    };
                  }
                    function primaryStaticStepFacts(sample) {
                    function primaryGrammar(parser) {
                      let proto = parser && parser.constructor && parser.constructor.prototype;
                      for (let depth = 0; proto && depth < 12; depth++) {
                        const ctor = proto.constructor;
                        if (ctor && ctor.__qin_field_STATIC_PRIMARY_GRAMMAR) {
                          return ctor.__qin_field_STATIC_PRIMARY_GRAMMAR;
                        }
                        proto = Object.getPrototypeOf(proto);
                      }
                      return null;
                    }
                    function tsRootPrimaryGrammar(parser) {
                      let proto = parser && parser.constructor && parser.constructor.prototype;
                      for (let depth = 0; proto && depth < 12; depth++) {
                        const ctor = proto.constructor;
                        if (ctor && ctor.__qin_field_STATIC_TS_ROOT_PRIMARY_GRAMMAR) {
                          return ctor.__qin_field_STATIC_TS_ROOT_PRIMARY_GRAMMAR;
                        }
                        proto = Object.getPrototypeOf(proto);
                      }
                      return null;
                    }
                    function one(label, invoke) {
                      const parser = new CssTsParser(sample);
                      const runtime = parser.primaryStaticRuntime ? parser.primaryStaticRuntime(expressionParams) : null;
                      const beforeFail = parserFailValue(parser);
                      const beforeIndex = parserIndexValue(parser);
                      let value = null;
                      let error = null;
                      try {
                        value = invoke(parser, runtime);
                      } catch (e) {
                        error = e && e.stack ? String(e.stack).split('\\n').slice(0, 4).join('\\n') : String(e);
                      }
                      return {
                        label,
                        beforeFail,
                        beforeIndex,
                        value,
                        error,
                        afterFail: parserFailValue(parser),
                        afterIndex: parserIndexValue(parser),
                        token1: tokenAt(parser, 1),
                        token2: tokenAt(parser, 2)
                      };
                    }
                    const orderedParser = new CssTsParser(sample);
                    const orderedRuntime = orderedParser.primaryStaticRuntime ? orderedParser.primaryStaticRuntime(expressionParams) : null;
                    const ordered = [];
                    function orderedStep(label, invoke) {
                      let value = null;
                      let error = null;
                      const beforeFail = parserFailValue(orderedParser);
                      const beforeIndex = parserIndexValue(orderedParser);
                      try {
                        value = invoke(orderedParser, orderedRuntime);
                      } catch (e) {
                        error = e && e.stack ? String(e.stack).split('\\n').slice(0, 4).join('\\n') : String(e);
                      }
                      ordered.push({
                        label,
                        beforeFail,
                        beforeIndex,
                        value,
                        error,
                        afterFail: parserFailValue(orderedParser),
                        afterIndex: parserIndexValue(orderedParser),
                        token1: tokenAt(orderedParser, 1),
                        token2: tokenAt(orderedParser, 2)
                      });
                    }
                    orderedStep("gate.asyncGenerator", (parser, runtime) => runtime.testStaticGate("primary.asyncGeneratorExpression"));
                    orderedStep("gate.asyncFunction", (parser, runtime) => runtime.testStaticGate("primary.asyncFunctionExpression"));
                    orderedStep("gate.identifierReference", (parser, runtime) => runtime.testStaticGate("primary.identifierReference"));
                    orderedStep("canStart.Literal", (parser, runtime) => runtime.canStartStaticRule("Literal", null, 1));
                    orderedStep("call.Literal", (parser, runtime) => runtime.callStaticRule("Literal", null));
                    return {
                      token1: tokenAt(new CssTsParser(sample), 1),
                      canStartPrimary: one("canStartStaticPrimaryExpression", (parser) => parser.canStartStaticPrimaryExpression(expressionParams, 1)),
                      canStartLiteral: one("runtime.canStartStaticRule.Literal", (parser, runtime) => runtime.canStartStaticRule("Literal", null, 1)),
                      callLiteral: one("runtime.callStaticRule.Literal", (parser, runtime) => runtime.callStaticRule("Literal", null)),
                      executeStandardPrimary: one("StandardPrimaryExpression", (parser) => parser.StandardPrimaryExpression(expressionParams)),
                      executeStandardPrimaryRaw: one("raw.StandardPrimaryExpression", (parser) => parser.__qin_subhuti_raw_StandardPrimaryExpression(expressionParams)),
                      executeTsRootPrimaryRule: one("executeStaticRule.TSRootPrimaryExpression", (parser) => {
                        const grammar = tsRootPrimaryGrammar(parser);
                        const runtime = parser.tsRootPrimaryStaticRuntime ? parser.tsRootPrimaryStaticRuntime(expressionParams) : null;
                        return parser.executeStaticRule(grammar, "PrimaryExpression", runtime);
                      }),
                      executePrimaryRaw: one("raw.PrimaryExpression", (parser) => parser.__qin_subhuti_raw_PrimaryExpression(expressionParams)),
                      executeInheritedPrimaryRaw: one("raw.InheritedPrimaryExpressionOverload", (parser) => parser.__qin_subhuti_raw___qin_overload_PrimaryExpression_1_0(expressionParams)),
                      executePrimaryRule: one("executeStaticRule.PrimaryExpression", (parser, runtime) => {
                        const grammar = primaryGrammar(parser);
                        return parser.executeStaticRule(grammar, "PrimaryExpression", runtime);
                      }),
                      executePrimaryAlternation: one("executeStaticAlternation.PrimaryExpression", (parser, runtime) => {
                        const grammar = primaryGrammar(parser);
                        const rule = grammar && grammar.rule("PrimaryExpression");
                        return parser.executeStaticAlternation(rule.body(), runtime);
                      }),
                      executePrimaryManualChoices: one("executeStaticOrderedChoiceSequences.manualPrimary", (parser, runtime) => {
                        const grammar = primaryGrammar(parser);
                        const rule = grammar && grammar.rule("PrimaryExpression");
                        const children = javaArray(rule.body().children());
                        const choices = children.map(child => parser.staticSequence(child));
                        return parser.executeStaticOrderedChoiceSequences(choices, runtime);
                      }),
                      primaryGrammarShape: one("primaryGrammarShape", (parser) => {
                        const grammar = primaryGrammar(parser);
                        const rule = grammar && grammar.rule("PrimaryExpression");
                        const body = rule && rule.body();
                        const children = body && body.children ? javaArray(body.children()) : [];
                        return {
                          found: !!grammar,
                          bodyKind: body && body.kind ? String(body.kind()) : null,
                          childCount: children.length,
                          childKinds: children.map(child => child && child.kind ? String(child.kind()) : null).join(',')
                        };
                      }),
                      ordered
                    };
                  }
                  return {
                    token1: tokenAt(factsParser, 1),
                    token2: tokenAt(factsParser, 2),
                    token3: tokenAt(factsParser, 3),
                    canStartStandardDeclaration: factsParser.canStartStandardDeclaration ? factsParser.canStartStandardDeclaration() : null,
                    declarationStandardStart: factsParser.declarationStandardStart ? factsParser.declarationStandardStart() : null,
                    canStartStatementListItemAt: factsParser.canStartStatementListItemAt ? factsParser.canStartStatementListItemAt(1, factsParser.moduleStatementListItemParams()) : null,
                    canStartDeclarationAt: factsParser.canStartDeclarationAt ? factsParser.canStartDeclarationAt(1) : null,
                    canStartVariableDeclarationAt2: factsParser.canStartVariableDeclarationAt ? factsParser.canStartVariableDeclarationAt(2, expressionParams) : null,
                    prototypeChain: prototypeChainFacts(factsParser),
                    branchFacts: {
                      num: expressionBranchFacts("0" + '\\n'),
                      refCall: expressionBranchFacts(refCallText)
                    },
                    primaryStaticSteps: {
                      num: primaryStaticStepFacts("0" + '\\n'),
                      refCall: primaryStaticStepFacts(refCallText)
                    },
                    directRules: [
                      directRuleProbe('ModuleItem', text, parser => parser.ModuleItem()),
                      directRuleProbe('ModuleStatementListItem', text, parser => parser.ModuleStatementListItem()),
                      directRuleProbe('StatementListItem', text, parser => parser.StatementListItem(parser.moduleStatementListItemParams())),
                      directRuleProbe('Declaration', text, parser => parser.Declaration(new DeclarationParams(false, true, false))),
                      directRuleProbe('StandardDeclaration', text, parser => parser.StandardDeclaration(new DeclarationParams(false, true, false))),
                      directRuleProbe('LexicalDeclaration', text, parser => parser.LexicalDeclaration(expressionParams))
                    ],
                    expressionRules: [
                      directRuleProbe("AssignmentExpression(ref)", refCallText, parser => parser.AssignmentExpression(expressionParams)),
                      directRuleProbe("Expression(ref)", refCallText, parser => parser.Expression(expressionParams)),
                      directRuleProbe("AssignmentExpression(num)", "0" + '\\n', parser => parser.AssignmentExpression(expressionParams)),
                      directRuleProbe("RawAssignmentExpression(num)", "0" + '\\n', parser => parser.__qin_subhuti_raw_AssignmentExpression(expressionParams)),
                      directRuleProbe("ParseAssignmentExpressionBody(num)", "0" + '\\n', parser => parser.parseAssignmentExpressionBody(expressionParams)),
                      directRuleProbe("ConditionalExpression(num)", "0" + '\\n', parser => parser.ConditionalExpression(expressionParams)),
                      directRuleProbe("ShortCircuitExpression(num)", "0" + '\\n', parser => parser.ShortCircuitExpression(expressionParams)),
                      directRuleProbe("ExponentiationExpression(num)", "0" + '\\n', parser => parser.ExponentiationExpression(expressionParams)),
                      directRuleProbe("UpdateExpression(num)", "0" + '\\n', parser => parser.UpdateExpression(expressionParams)),
                      directRuleProbe("UnaryExpression(num)", "0" + '\\n', parser => parser.UnaryExpression(expressionParams)),
                      directRuleProbe("LeftHandSideExpression(num)", "0" + '\\n', parser => parser.LeftHandSideExpression(expressionParams)),
                      directRuleProbe("PrimaryExpression(num)", "0" + '\\n', parser => parser.PrimaryExpression(expressionParams)),
                      directRuleProbe("Literal(num)", "0" + '\\n', parser => parser.Literal()),
                      directRuleProbe("NumericLiteral(num)", "0" + '\\n', parser => parser.NumericLiteral()),
                      directRuleProbe("LeftHandSideExpression(ref)", refCallText, parser => parser.LeftHandSideExpression(expressionParams)),
                      directRuleProbe("UpdateExpression(ref)", refCallText, parser => parser.UpdateExpression(expressionParams)),
                      directRuleProbe("UnaryExpression(ref)", refCallText, parser => parser.UnaryExpression(expressionParams)),
                      directRuleProbe("CallExpression(ref)", refCallText, parser => parser.CallExpression(expressionParams)),
                      directRuleProbe("CoverCallExpressionAndAsyncArrowHead(ref)", refCallText, parser => parser.CoverCallExpressionAndAsyncArrowHead(expressionParams)),
                      directRuleProbe("MemberExpression(ref)", refCallText, parser => parser.MemberExpression(expressionParams)),
                      directRuleProbe("PrimaryExpression(ref)", refCallText, parser => parser.PrimaryExpression(expressionParams)),
                      directRuleProbe("Arguments(paren)", argumentsText, parser => parser.Arguments(expressionParams)),
                      directRuleProbe("ArgumentList(parenBody)", "0)" + '\\n', parser => parser.ArgumentList(expressionParams)),
                      directRuleProbe("ArgumentListItem(parenBody)", "0)" + '\\n', parser => parser.ArgumentListItem(expressionParams))
                    ]
                  };
                }
                function summarize(label, text) {
                  const parser = new CssTsParser(text);
                  const cst = parser.Program();
                  const tokens = parser.parsedTokens;
                  const directDiagnostics = includeDirectDiagnostics ? directParserDiagnostics(text) : null;
                  if (parser.parserFail || !cst) {
                    return {
                      label,
                      parserFail: parser.parserFail,
                      parseSuccess: parser._parseSuccess,
                      cstName: cst && (cst.name || (cst.getName ? cst.getName() : null)),
                      currentIndex: parser.currentTokenIndex,
                      nextToken: parser.nextToken ? parser.nextToken.tokenName + ':' + parser.nextToken.tokenValue : null,
                      tokenCount: tokens && tokens.length,
                      firstStaticFailure: parser.getFirstStaticFailureReport ? parser.getFirstStaticFailureReport() : null,
                      furthestStaticFailure: parser.getFurthestStaticFailureReport ? parser.getFurthestStaticFailureReport() : null,
                      lastStaticNoMatch: parser.getLastStaticNoMatchReport ? parser.getLastStaticNoMatchReport() : null,
                      directDiagnostics,
                      cstSummary: childrenSummary(cst),
                      astType: null,
                      astBodyLength: null,
                      astBodyTypes: null,
                      generatorCodeLength: 0,
                      generatorCode: ''
                    };
                  }
                  const transformer = new CssTsCstToAst();
                  const importCst = findCst(cst, 'ImportDeclaration');
                  const directImportAst = importCst ? transformer.createImportDeclarationAst(importCst) : null;
                  const statementItemCst = findCst(cst, 'ModuleItem') || findCst(cst, 'StatementListItem') || findCst(cst, 'LexicalDeclaration') || findCst(cst, 'FunctionDeclaration');
                  const directStatementAst = statementItemCst ? transformer.createStatementListItemAst(statementItemCst) : null;
                  const rawProgram = transformer.toProgram(cst);
                  const rawBody = rawProgram && rawProgram.body ? rawProgram.body() : null;
                  const ast = transformer.toFileAst(cst);
                  const generated = SlimeGenerator.generator(ast, tokens);
                  const astFirst = ast && ast.body && ast.body.length ? ast.body[0] : null;
                  return {
                    label,
                    parserFail: parser.parserFail,
                    parseSuccess: parser._parseSuccess,
                    cstName: cst && (cst.name || (cst.getName ? cst.getName() : null)),
                    currentIndex: parser.currentTokenIndex,
                    nextToken: parser.nextToken ? parser.nextToken.tokenName + ':' + parser.nextToken.tokenValue : null,
                    tokenCount: tokens && tokens.length,
                    firstStaticFailure: parser.getFirstStaticFailureReport ? parser.getFirstStaticFailureReport() : null,
                    furthestStaticFailure: parser.getFurthestStaticFailureReport ? parser.getFurthestStaticFailureReport() : null,
                    lastStaticNoMatch: parser.getLastStaticNoMatchReport ? parser.getLastStaticNoMatchReport() : null,
                    directDiagnostics,
                    cstSummary: childrenSummary(cst),
                    directModuleItemFacts: directModuleItemFacts(cst),
                    rawProgramClass: rawProgram && rawProgram.constructor ? rawProgram.constructor.name : null,
                    rawBodySize: javaSize(rawBody),
                    rawBodyTypes: javaArray(rawBody).map(item => item && (item.type ? (typeof item.type === 'function' ? item.type() : item.type) : null)).join(','),
                    directImportAstClass: directImportAst && directImportAst.constructor ? directImportAst.constructor.name : null,
                    directImportAstInterfaces: directImportAst && directImportAst.constructor ? String(directImportAst.constructor.__qin_java_interfaces) : null,
                    directImportAstType: directImportAst && directImportAst.type ? (typeof directImportAst.type === 'function' ? directImportAst.type() : directImportAst.type) : null,
                    directStatementAstClass: directStatementAst && directStatementAst.constructor ? directStatementAst.constructor.name : null,
                    directStatementAstInterfaces: directStatementAst && directStatementAst.constructor ? String(directStatementAst.constructor.__qin_java_interfaces) : null,
                    directStatementAstType: directStatementAst && directStatementAst.type ? (typeof directStatementAst.type === 'function' ? directStatementAst.type() : directStatementAst.type) : null,
                    directVariableFacts: variableDeclarationFacts(directStatementAst),
                    directFunctionFacts: functionDeclarationFacts(directStatementAst),
                    astType: ast && ast.type,
                    astBodyLength: ast && ast.body && ast.body.length,
                    astBodyTypes: ast && ast.body ? ast.body.map(item => item && item.type).join(',') : null,
                    astFirstVariableFacts: variableDeclarationFacts(astFirst),
                    astFirstFunctionFacts: functionDeclarationFacts(astFirst),
                    generatorCodeLength: generated && generated.code ? generated.code.length : 0,
                    generatorCode: generated && generated.code
                  };
                }

                const summary = summarize(%s, source);
                const primaryExpressionNum = summary && summary.directDiagnostics && summary.directDiagnostics.expressionRules
                  ? summary.directDiagnostics.expressionRules.find(item => item.label === 'PrimaryExpression(num)')
                  : null;
                if (primaryExpressionNum && primaryExpressionNum.parserFail) {
                  throw new Error('PrimaryExpression(num) regression: ' + JSON.stringify(primaryExpressionNum));
                }
                let transformed = null;
                let transformedError = null;
                let directTransformed = null;
                let directTransformedError = null;
                const packageParse = callTransform(parseStyleName, "displayFlex");
                const directParse = callTransform(directParseStyleName, "displayFlex");
                const packageFirst = callTransform(transformCssTs, source);
                const directFirst = callTransform(directTransformCssTs, source);
                const localReturn = callTransform(localObjectReturnProbe, "local-ok");
                const localTransform = callTransform(localTransformClone, source);
                if (!summary.parserFail) {
                  const packageCall = callTransform(transformCssTs, source);
                  transformed = packageCall.value;
                  transformedError = packageCall.error;
                  const directCall = callTransform(directTransformCssTs, source);
                  directTransformed = directCall.value;
                  directTransformedError = directCall.error;
                }

                ({
                  probeRevision,
                  summary,
                  transformType: typeof transformed,
                  transformError: transformedError,
                  transformKeys: transformed ? Object.keys(transformed).join(',') : null,
                  transformString: transformed == null ? null : String(transformed),
                  transformCodeMethod: transformed && typeof transformed.code === 'function' ? transformed.code() : null,
                  transformCodeLength: transformed && transformed.code ? transformed.code.length : 0,
                  transformCode: transformed && transformed.code,
                  transformHasStyles: transformed && transformed.hasStyles,
                  transformFunctionType: typeof transformCssTs,
                  transformFunctionName: transformCssTs && transformCssTs.name,
                  transformFunctionLength: transformCssTs && transformCssTs.length,
                  directTransformFunctionType: typeof directTransformCssTs,
                  directTransformFunctionName: directTransformCssTs && directTransformCssTs.name,
                  directTransformFunctionLength: directTransformCssTs && directTransformCssTs.length,
                  packageFirstType: typeof packageFirst.value,
                  packageFirstError: packageFirst.error,
                  packageFirstCodeLength: packageFirst.value && packageFirst.value.code ? packageFirst.value.code.length : 0,
                  packageFirstCode: packageFirst.value && packageFirst.value.code,
                  directFirstType: typeof directFirst.value,
                  directFirstError: directFirst.error,
                  directFirstCodeLength: directFirst.value && directFirst.value.code ? directFirst.value.code.length : 0,
                  directFirstCode: directFirst.value && directFirst.value.code,
                  directTransformType: typeof directTransformed,
                  directTransformError: directTransformedError,
                  directTransformKeys: directTransformed ? Object.keys(directTransformed).join(',') : null,
                  directTransformCodeLength: directTransformed && directTransformed.code ? directTransformed.code.length : 0,
                  directTransformCode: directTransformed && directTransformed.code,
                  directTransformHasStyles: directTransformed && directTransformed.hasStyles
                  ,
                  packageParseType: typeof packageParse.value,
                  packageParseKeys: packageParse.value ? Object.keys(packageParse.value).join(',') : null,
                  packageParseBaseName: packageParse.value && packageParse.value.baseName,
                  packageParseError: packageParse.error,
                  directParseType: typeof directParse.value,
                  directParseKeys: directParse.value ? Object.keys(directParse.value).join(',') : null,
                  directParseBaseName: directParse.value && directParse.value.baseName,
                  directParseError: directParse.error,
                  localReturnType: typeof localReturn.value,
                  localReturnKeys: localReturn.value ? Object.keys(localReturn.value).join(',') : null,
                  localReturnCode: localReturn.value && localReturn.value.code,
                  localReturnError: localReturn.error,
                  localTransformType: typeof localTransform.value,
                  localTransformKeys: localTransform.value ? Object.keys(localTransform.value).join(',') : null,
                  localTransformCodeLength: localTransform.value && localTransform.value.code ? localTransform.value.code.length : 0,
                  localTransformCode: localTransform.value && localTransform.value.code,
                  localTransformError: localTransform.error
                });
                """.formatted(
                QinJsPackageRunner.renderJsLiteral(source),
                QinJsPackageRunner.renderJsLiteral(label),
                QinJsPackageRunner.renderJsLiteral(label));

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "generated_ts_slime_cssts_transform_probe");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
