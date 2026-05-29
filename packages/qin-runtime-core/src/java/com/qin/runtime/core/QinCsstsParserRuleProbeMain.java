package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsParserRuleProbeMain {
    private QinCsstsParserRuleProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-parser-rule-probe-");
        Files.writeString(root.resolve("qin.config.json"), "{ \"name\": \"qin-cssts-parser-rule-probe\" }\n", StandardCharsets.UTF_8);

        String wrapper = """
                import CssTsParser from "cssts-compiler/src/parser/CssTsParser.js";
                import { SubhutiRule } from "subhuti";

                const source = "const buttonBase = css { colorRed, fontBold }\\n";
                class ProbeCssTsParser extends CssTsParser {
                  @SubhutiRule
                  SequenceCommaThenAssignment(params = {}) {
                    this.Identifier();
                    this.tokenConsumer.Comma();
                    this.AssignmentExpression({ ...params, In: true });
                    return this.curCst;
                  }

                  @SubhutiRule
                  SequenceManyCommaThenAssignment(params = {}) {
                    this.Identifier();
                    this.Many(() => {
                      this.tokenConsumer.Comma();
                      this.AssignmentExpression({ ...params, In: true });
                    });
                    return this.curCst;
                  }

                  @SubhutiRule
                  SequenceManyCommaOptionThenAssignment(params = {}) {
                    this.Identifier();
                    this.Many(() => {
                      this.tokenConsumer.Comma();
                      this.Option(() => this.Elision());
                      this.AssignmentExpression({ ...params, In: true });
                    });
                    return this.curCst;
                  }

                  @SubhutiRule
                  SequenceManyCommaThenOr(params = {}) {
                    this.Identifier();
                    this.Many(() => {
                      this.tokenConsumer.Comma();
                      this.Or([
                        { alt: () => this.AssignmentExpression({ ...params, In: true }) },
                        { alt: () => this.SpreadElement(params) }
                      ]);
                    });
                    return this.curCst;
                  }

                  @SubhutiRule
                  SequenceManyCommaThenManualAlt(params = {}) {
                    this.Identifier();
                    this.Many(() => {
                      this.tokenConsumer.Comma();
                      const alt = { alt: () => this.AssignmentExpression({ ...params, In: true }) };
                      alt.alt();
                    });
                    return this.curCst;
                  }

                  @SubhutiRule
                  ExactElementListClone(params = {}) {
                    this.Option(() => this.Elision());
                    this.Or([
                      { alt: () => this.AssignmentExpression({ ...params, In: true }) },
                      { alt: () => this.SpreadElement(params) }
                    ]);
                    this.Many(() => {
                      this.tokenConsumer.Comma();
                      this.Option(() => this.Elision());
                      this.Or([
                        { alt: () => this.AssignmentExpression({ ...params, In: true }) },
                        { alt: () => this.SpreadElement(params) }
                      ]);
                    });
                    return this.curCst;
                  }

                  @SubhutiRule
                  LexicalDeclarationClone(params = {}) {
                    this.LetOrConst();
                    this.BindingList(params);
                    this.Option(() => this.tokenConsumer.Semicolon());
                    return this.curCst;
                  }

                  @SubhutiRule
                  DeclarationClone(params = {}) {
                    this.Or([
                      { alt: () => this.HoistableDeclaration({ ...params, Default: false }) },
                      { alt: () => this.ClassDeclaration({ ...params, Default: false }) },
                      { alt: () => this.LexicalDeclaration({ ...params, In: true }) }
                    ]);
                    return this.curCst;
                  }

                  @SubhutiRule
                  DeclarationTsClone(params = {}) {
                    this.Or([
                      { alt: () => this.TSDeclareStatement() },
                      { alt: () => this.TSModuleDeclaration() },
                      { alt: () => this.TSInterfaceDeclaration() },
                      { alt: () => this.TSTypeAliasDeclaration() },
                      { alt: () => this.TSEnumDeclaration() },
                      { alt: () => this.HoistableDeclaration({ ...params, Default: false }) },
                      { alt: () => this.ClassDeclaration({ ...params, Default: false }) },
                      { alt: () => this.LexicalDeclaration({ ...params, In: true }) }
                    ]);
                    return this.curCst;
                  }

                  @SubhutiRule
                  StatementListItemClone(params = {}) {
                    this.Or([
                      { alt: () => this.Declaration({ Yield: params.Yield, Await: params.Await, Default: false }) },
                      { alt: () => this.Statement(params) }
                    ]);
                    return this.curCst;
                  }

                  @SubhutiRule
                  ModuleItemClone() {
                    this.Or([
                      { alt: () => this.ImportDeclaration() },
                      { alt: () => this.ExportDeclaration() },
                      { alt: () => this.StatementListItem({ Yield: false, Await: true, Return: false }) }
                    ]);
                    return this.curCst;
                  }

                  @SubhutiRule
                  ModuleItemStatementClone() {
                    this.Or([
                      { alt: () => this.ImportDeclaration() },
                      { alt: () => this.ExportDeclaration() },
                      { alt: () => this.StatementListItemClone({ Yield: false, Await: true, Return: false }) }
                    ]);
                    return this.curCst;
                  }

                  @SubhutiRule
                  ModuleItemListClone() {
                    this.Many(() => this.ModuleItem());
                    return this.curCst;
                  }

                  @SubhutiRule
                  ModuleItemListStatementClone() {
                    this.Many(() => this.ModuleItemStatementClone());
                    return this.curCst;
                  }

                  @SubhutiRule
                  ProgramClone() {
                    this.Option(() => this.tokenConsumer.HashbangComment());
                    this.Option(() => this.ModuleItemStatementClone());
                    return this.curCst;
                  }

                  @SubhutiRule
                  ProgramModuleListClone() {
                    this.Option(() => this.tokenConsumer.HashbangComment());
                    this.Option(() => this.ModuleItemListClone());
                    return this.curCst;
                  }
                }
                function probe(name, invoke) {
                  const parser = new CssTsParser(source);
                  return probeParser(name, parser, invoke);
                }
                function probeParser(name, parser, invoke) {
                  let thrownName = null;
                  let thrownExpected = null;
                  let thrownFound = null;
                  let methodName = null;
                  try {
                    const direct = parser[name];
                    methodName = direct && direct.name;
                  } catch (_) {
                    methodName = null;
                  }
                  try {
                    invoke(parser);
                  } catch (error) {
                    thrownName = error && error.name;
                    thrownExpected = error && error.expected;
                    thrownFound = error && error.found ? error.found.tokenName + ":" + error.found.tokenValue : String(error);
                  }
                  return {
                    name,
                    parserFail: parser.parserFail,
                    parseSuccess: parser._parseSuccess,
                    currentTokenIndex: parser.currentTokenIndex,
                    nextToken: parser.nextToken ? parser.nextToken.tokenName + ":" + parser.nextToken.tokenValue : null,
                    curCstName: parser.curCst ? parser.curCst.name : null,
                    curCstChildren: parser.curCst && parser.curCst.children ? parser.curCst.children.map(child => child.name + ":" + child.value).join("|") : "",
                    methodName,
                    thrownName,
                    thrownExpected,
                    thrownFound
                  };
                }
                function traceParser(name, parser, steps) {
                  const trace = [];
                  function snapshot(label) {
                    trace.push({
                      label,
                      parserFail: parser.parserFail,
                      parseSuccess: parser._parseSuccess,
                      currentTokenIndex: parser.currentTokenIndex,
                      nextToken: parser.nextToken ? parser.nextToken.tokenName + ":" + parser.nextToken.tokenValue : null,
                      curCstName: parser.curCst ? parser.curCst.name : null
                    });
                  }
                  snapshot("start");
                  let thrownName = null;
                  let thrownExpected = null;
                  let thrownFound = null;
                  for (const step of steps) {
                    try {
                      step.run(parser);
                      snapshot(step.label);
                    } catch (error) {
                      thrownName = error && error.name;
                      thrownExpected = error && error.expected;
                      thrownFound = error && error.found ? error.found.tokenName + ":" + error.found.tokenValue : String(error);
                      snapshot(step.label + ":thrown");
                      break;
                    }
                  }
                  return {
                    name,
                    parserFail: parser.parserFail,
                    parseSuccess: parser._parseSuccess,
                    currentTokenIndex: parser.currentTokenIndex,
                    nextToken: parser.nextToken ? parser.nextToken.tokenName + ":" + parser.nextToken.tokenValue : null,
                    trace,
                    thrownName,
                    thrownExpected,
                    thrownFound
                  };
                }

                ({
                  letOrConst: probe("LetOrConst", parser => parser.LetOrConst()),
                  identifierNameDirect: probeParser("IdentifierName", new CssTsParser("buttonBase"), parser => parser.IdentifierName()),
                  identifierDirect: probeParser("Identifier", new CssTsParser("buttonBase"), parser => parser.Identifier()),
                  identifierAfterLetOrConst: probe("IdentifierAfterLetOrConst", parser => {
                    parser.LetOrConst();
                    parser.Identifier();
                  }),
                  bindingIdentifierAfterLetOrConst: probe("BindingIdentifierAfterLetOrConst", parser => {
                    parser.LetOrConst();
                    parser.BindingIdentifier({ In: true });
                  }),
                  bindingIdentifier: probe("BindingIdentifier", parser => parser.BindingIdentifier({ In: true })),
                  initializer: probe("Initializer", parser => {
                    parser.LetOrConst();
                    parser.BindingIdentifier({ In: true });
                    parser.Initializer({ In: true });
                  }),
                  cssExpressionDirect: probeParser("CssExpression", new CssTsParser("css { colorRed, fontBold }"), parser => parser.CssExpression({ In: true })),
                  cssStyleObjectDirect: probeParser("CssStyleObject", new CssTsParser("{ colorRed, fontBold }"), parser => parser.CssStyleObject({ In: true })),
                  elementListDirect: probeParser("ElementList", new CssTsParser("colorRed, fontBold"), parser => parser.ElementList({ In: true })),
                  manyCommaAfterIdentifier: probeParser("ManyCommaAfterIdentifier", new CssTsParser("colorRed, fontBold"), parser => {
                    parser.Identifier();
                    parser.Many(() => parser.tokenConsumer.Comma());
                  }),
                  manualCommaAfterIdentifier: probeParser("ManualCommaAfterIdentifier", new CssTsParser("colorRed, fontBold"), parser => {
                    parser.Identifier();
                    parser.tokenConsumer.Comma();
                  }),
                  tryAndRestoreCommaAfterIdentifier: probeParser("TryAndRestoreCommaAfterIdentifier", new CssTsParser("colorRed, fontBold"), parser => {
                    parser.Identifier();
                    parser.tryAndRestore(() => parser.tokenConsumer.Comma());
                  }),
                  assignmentFontBoldDirect: probeParser("AssignmentFontBoldDirect", new CssTsParser("fontBold"), parser => parser.AssignmentExpression({ In: true })),
                  assignmentFontBoldSpreadParams: probeParser("AssignmentFontBoldSpreadParams", new CssTsParser("fontBold"), parser => {
                    const params = {};
                    parser.AssignmentExpression({ ...params, In: true });
                  }),
                  commaThenAssignmentDirect: probeParser("CommaThenAssignmentDirect", new CssTsParser("colorRed, fontBold"), parser => {
                    parser.Identifier();
                    parser.tokenConsumer.Comma();
                    parser.AssignmentExpression({ In: true });
                  }),
                  commaThenAssignmentSpreadParams: probeParser("CommaThenAssignmentSpreadParams", new CssTsParser("colorRed, fontBold"), parser => {
                    parser.Identifier();
                    parser.tokenConsumer.Comma();
                    const params = {};
                    parser.AssignmentExpression({ ...params, In: true });
                  }),
                  manyCommaThenAssignmentSpreadParams: probeParser("ManyCommaThenAssignmentSpreadParams", new CssTsParser("colorRed, fontBold"), parser => {
                    const params = {};
                    parser.Identifier();
                    parser.Many(() => {
                      parser.tokenConsumer.Comma();
                      parser.AssignmentExpression({ ...params, In: true });
                    });
                  }),
                  ruleSequenceCommaThenAssignment: probeParser("RuleSequenceCommaThenAssignment", new ProbeCssTsParser("colorRed, fontBold"), parser => parser.SequenceCommaThenAssignment()),
                  ruleSequenceManyCommaThenAssignment: probeParser("RuleSequenceManyCommaThenAssignment", new ProbeCssTsParser("colorRed, fontBold"), parser => parser.SequenceManyCommaThenAssignment()),
                  ruleSequenceManyCommaOptionThenAssignment: probeParser("RuleSequenceManyCommaOptionThenAssignment", new ProbeCssTsParser("colorRed, fontBold"), parser => parser.SequenceManyCommaOptionThenAssignment()),
                  ruleSequenceManyCommaThenOr: probeParser("RuleSequenceManyCommaThenOr", new ProbeCssTsParser("colorRed, fontBold"), parser => parser.SequenceManyCommaThenOr()),
                  ruleSequenceManyCommaThenManualAlt: probeParser("RuleSequenceManyCommaThenManualAlt", new ProbeCssTsParser("colorRed, fontBold"), parser => parser.SequenceManyCommaThenManualAlt()),
                  exactElementListClone: probeParser("ExactElementListClone", new ProbeCssTsParser("colorRed, fontBold"), parser => parser.ExactElementListClone()),
                  lexicalDeclarationClone: probeParser("LexicalDeclarationClone", new ProbeCssTsParser(source), parser => parser.LexicalDeclarationClone({ In: true })),
                  declarationClone: probeParser("DeclarationClone", new ProbeCssTsParser(source), parser => parser.DeclarationClone({ Yield: false, Await: true })),
                  declarationTsClone: probeParser("DeclarationTsClone", new ProbeCssTsParser(source), parser => parser.DeclarationTsClone({ Yield: false, Await: true })),
                  statementListItemClone: probeParser("StatementListItemClone", new ProbeCssTsParser(source), parser => parser.StatementListItemClone({ Yield: false, Await: true, Return: false })),
                  moduleItem: probeParser("ModuleItem", new ProbeCssTsParser(source), parser => parser.ModuleItem()),
                  moduleItemList: probeParser("ModuleItemList", new ProbeCssTsParser(source), parser => parser.ModuleItemList()),
                  moduleItemClone: probeParser("ModuleItemClone", new ProbeCssTsParser(source), parser => parser.ModuleItemClone()),
                  moduleItemStatementClone: probeParser("ModuleItemStatementClone", new ProbeCssTsParser(source), parser => parser.ModuleItemStatementClone()),
                  moduleItemListClone: probeParser("ModuleItemListClone", new ProbeCssTsParser(source), parser => parser.ModuleItemListClone()),
                  moduleItemListStatementClone: probeParser("ModuleItemListStatementClone", new ProbeCssTsParser(source), parser => parser.ModuleItemListStatementClone()),
                  programClone: probeParser("ProgramClone", new ProbeCssTsParser(source), parser => parser.ProgramClone()),
                  programModuleListClone: probeParser("ProgramModuleListClone", new ProbeCssTsParser(source), parser => parser.ProgramModuleListClone()),
                  tsDeclareStatement: probeParser("TSDeclareStatement", new ProbeCssTsParser(source), parser => parser.TSDeclareStatement()),
                  tsModuleDeclaration: probeParser("TSModuleDeclaration", new ProbeCssTsParser(source), parser => parser.TSModuleDeclaration()),
                  tsInterfaceDeclaration: probeParser("TSInterfaceDeclaration", new ProbeCssTsParser(source), parser => parser.TSInterfaceDeclaration()),
                  tsTypeAliasDeclaration: probeParser("TSTypeAliasDeclaration", new ProbeCssTsParser(source), parser => parser.TSTypeAliasDeclaration()),
                  tsEnumDeclaration: probeParser("TSEnumDeclaration", new ProbeCssTsParser(source), parser => parser.TSEnumDeclaration()),
                  traceCommaThenAssignmentSpreadParams: traceParser("TraceCommaThenAssignmentSpreadParams", new CssTsParser("colorRed, fontBold"), [
                    { label: "Identifier", run: parser => parser.Identifier() },
                    { label: "Comma", run: parser => parser.tokenConsumer.Comma() },
                    { label: "AssignmentExpression", run: parser => {
                      const params = {};
                      parser.AssignmentExpression({ ...params, In: true });
                    } }
                  ]),
                  traceManyBody: traceParser("TraceManyBody", new CssTsParser("colorRed, fontBold"), [
                    { label: "Identifier", run: parser => parser.Identifier() },
                    { label: "ManyBody", run: parser => {
                      const params = {};
                      parser.Many(() => {
                        parser.tokenConsumer.Comma();
                        parser.AssignmentExpression({ ...params, In: true });
                      });
                    } }
                  ]),
                  primaryExpressionDirect: probeParser("PrimaryExpression", new CssTsParser("css { colorRed, fontBold }"), parser => parser.PrimaryExpression({ In: true })),
                  assignmentExpressionDirect: probeParser("AssignmentExpression", new CssTsParser("css { colorRed, fontBold }"), parser => parser.AssignmentExpression({ In: true })),
                  initializerExpressionAfterName: probe("InitializerExpressionAfterName", parser => {
                    parser.LetOrConst();
                    parser.BindingIdentifier({ In: true });
                    parser.tokenConsumer.Assign();
                    parser.AssignmentExpression({ In: true });
                  }),
                  lexicalBinding: probe("LexicalBinding", parser => {
                    parser.LetOrConst();
                    parser.LexicalBinding({ In: true });
                  }),
                  bindingList: probe("BindingList", parser => {
                    parser.LetOrConst();
                    parser.BindingList({ In: true });
                  }),
                  lexicalDeclaration: probe("LexicalDeclaration", parser => parser.LexicalDeclaration({ In: true })),
                  declaration: probe("Declaration", parser => parser.Declaration({ Yield: false, Await: true })),
                  statementListItem: probe("StatementListItem", parser => parser.StatementListItem({ Yield: false, Await: true, Return: false })),
                  program: probe("Program", parser => parser.Program())
                });
                """;

        Object result = new QinJsPackageRunner().runModuleSource(root, wrapper, "cssts_parser_rule_probe");
        System.out.println(QinObjectJsonEncoder.toJson(result));
    }
}
