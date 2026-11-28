package com.qin.parser;

import com.subhuti.struct.SubhutiCst;
import com.slime.parser.SlimeParser;
import com.slime.ast.nodes.expressions.ArrayExpression;
import com.slime.ast.nodes.expressions.ArrowFunctionExpression;
import com.slime.ast.nodes.expressions.CallExpression;
import com.slime.ast.nodes.expressions.MemberExpression;
import com.slime.ast.nodes.expressions.ObjectExpression;
import com.slime.ast.AstNode;
import com.slime.ast.nodes.misc.ImportSpecifier;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.declarations.VariableDeclaration;
import com.slime.ast.nodes.modules.ExportAllDeclaration;
import com.slime.ast.nodes.modules.ExportDefaultDeclaration;
import com.slime.ast.nodes.modules.ExportNamedDeclaration;
import com.slime.ast.nodes.modules.ImportDeclaration;
import com.slime.ast.nodes.statements.IfStatement;
import com.slime.ast.nodes.statements.ExpressionStatement;
import com.slime.ast.nodes.statements.BlockStatement;
import com.slime.ast.nodes.statements.ReturnStatement;
import com.slime.ast.nodes.typescript.TSTypeAssertion;
import com.slime.generator.SlimeGenerator;

public final class OvsParserJavaSmokeTestMain {
    private OvsParserJavaSmokeTestMain() {
    }

    public static void main(String[] args) {
        assertParses("ordinary JavaScript", "let count = 0;");
        assertParses("simple OVS statement", "div { \"x\" }");
        assertParses("CSS expression and nested OVS render", """
            import { ref } from 'vue'

            let count = ref(0)
            const pageStyle = css { displayFlex, colorBlue, padding16px }

            div(class = "ovs-card"; style = pageStyle) {
              h2 { "Rendered by Java OVS parser" }
            button(class = "ovs-button"; onClick() { count.value++ }) {
                "Increment"
              }
            }
            """);

        assertParses("view declaration and no-render block", """
            view Card(props) {
              #{ const local = props.title; }
              return div(class = css { card }) { props.title }
            }
            """);

        assertParses("OVS render expression", """
            const card = div(class = "card") { "content" }
            """);

        assertParses("view identifier across an automatic semicolon", """
            const view = "ordinary identifier";
            view Counter(props) {
              button(title = props.label) {
                view
                props.label
              }
            }
            export { Counter as NamedCounter };
            """);

        assertRejects("reserved word cannot be a tag", """
            if { "not a tag" }
            """);

        assertRejects("OVS props use semicolon separators", """
            div(class = "card", id = "main") { "content" }
            """);

        assertParses("class heritage is ordinary JavaScript", """
            class Card extends Base { }
            """);

        assertParses("contextual loop heads", """
            let total = 0;
            const obj = { of: 0, in: true };
            for (let of = 0; of < 2; of++) { total += of; }
            for (obj.of = 0; obj.of < 1 && obj.in; obj.of++) { total++; }
            for (const of of [2, 3]) { total += of; }
            for (const key in obj) { total += key.length; }
            for await (const item of [4]) { total += item; }
            """);
        assertParses("nested expressions in loop heads", """
            const obj = { of: 0, in: 1 };
            for (let of = (`${`${obj.of}`} ${obj.in}`).length; of < 0; of++) { }
            for (const item of (`${`${obj.of}`} ${obj.in}`).split(" ")) { }
            """);
        assertParses("regular expressions in loop heads", """
            for (let of = /[;)]/.test(")") ? 0 : 1; of < 0; of++) { }
            for (const item of /[;)]/.exec(")")) { }
            for await (const of of /a/g.exec("a")) { }
            for (const [item] of /a/g.exec("a")) { }
            for (const item of (/[;)]/.exec(")"))) { }
            for (const key in /[;)]/) { }
            for (const item of [10 / 2]) { }
            const of = 8, a = 2, g = 2;
            const obj = { of };
            for (of / a / g; ; ) { break; }
            for (obj.of / a / g; ; ) { break; }
            for (const item of of / a / g) { }
            for (let value = of / a / g; value > 0; value--) { }
            for (await of / a / g; ; ) { break; }
            for (1 + await of / a / g; ; ) { break; }
            for (1 + !of / a / g; ; ) { break; }
            for (obj.await of /a/g.exec("a")) { }
            """);

        assertDirectJavaCstToAst("div(class = \"card\"; id = name) { \"hello\" }");
        assertDirectJavaCstToAst("if (ready) { div { \"nested\" } }");
        assertComputedPropertiesAndMethods();
        assertDirectJavaViewConversion();
        assertDirectJavaViewParametersAndReturnConversion();
        assertDirectJavaReactiveChildConversion();
        assertDirectJavaSideEffectRenderConversion();
        assertDirectJavaReactiveIfConversion();
        assertDirectJavaReactiveLoopConversion();
        assertNestedRenderControlFlowBoundary();
        assertDirectJavaReactiveSwitchAndTryConversion();
        assertDirectJavaNoRenderInsideRender();
        assertNoRenderReactiveControlFlow();
        assertDirectJavaCssConversion();
        assertCsstsBindingScopes();
        assertTypeImportClassification();
        assertTypeExportClassification();
        assertTypeAssertions();
        assertParameterPropertyModifiers();
        assertRuntimeImportsAreDeduplicated();

        System.out.println("OvsParserJavaSmokeTestMain passed.");
    }

    private static void assertParses(String label, String source) {
        OvsParser parser = new OvsParser(source);
        SubhutiCst cst = parser.Program(SlimeParser.SourceType.MODULE);
        if (parser.isParserFail() || cst == null || !parser.isAtEnd()) {
            throw new AssertionError(label + " failed at offset " + parser.getCurrentIndex()
                + ", diagnostics=" + parser.getDiagnostics());
        }
    }

    private static void assertParameterPropertyModifiers() {
        var program = new QinOvsCstToAst().toOvsFileAst(parseOvs("""
                class Base { id: string = "base"; }
                export class Child extends Base {
                    constructor(public override readonly id: string = "child") { super(); }
                }
                export class Plain {
                    constructor(public override: string, readonly: string) {}
                }
                """));
        String typescript = new SlimeGenerator().generate(program);
        String javascript = new SlimeGenerator(SlimeGenerator.OutputLanguage.JAVASCRIPT).generate(program);
        if (!typescript.contains("public override readonly id")
                || !typescript.contains("public override: string, readonly: string")
                || !javascript.contains("constructor(id = \"child\")")
                || !javascript.contains("this.id =")
                || !javascript.contains("constructor(override, readonly)")
                || !javascript.contains("this.override =")
                || javascript.contains("this.readonly =")) {
            throw new AssertionError("incorrect parameter property output:\n" + typescript + "\n" + javascript);
        }
    }

    private static void assertRejects(String label, String source) {
        OvsParser parser = new OvsParser(source);
        parser.Program(SlimeParser.SourceType.MODULE);
        if (!parser.isParserFail()) {
            throw new AssertionError(label + " unexpectedly parsed");
        }
    }

    private static void assertTypeAssertions() {
        var program = new QinOvsCstToAst().toOvsFileAst(parseOvs("""
            const plain = <number>value;
            const grouped = <Box<Item<number>>>(2 + 3);
            const unary = <number>-2 * 3;
            const arrow = <T = number>(value: T): T => value;
            const comparison = left < right && right > left;
            export const style = css { <string>colorRed };
            """));
        var declaration = program.body().stream()
                .filter(VariableDeclaration.class::isInstance)
                .map(VariableDeclaration.class::cast)
                .findFirst().orElseThrow();
        if (!(declaration.declarations().getFirst().init() instanceof TSTypeAssertion assertion)
                || assertion.location().start().index() != "const plain = ".length()) {
            throw new AssertionError("expected a located type assertion");
        }
        String typescript = new SlimeGenerator().generate(program);
        String js = new SlimeGenerator(SlimeGenerator.OutputLanguage.JAVASCRIPT).generate(program);
        if (!typescript.contains("<number>value") || !typescript.contains("<Box<Item<number>>>")
                || js.contains("<number>") || js.contains("<Box")
                || !js.contains("csstsAtom.colorRed") || !js.contains("left < right")) {
            throw new AssertionError("incorrect type assertion output: " + typescript + "\n" + js);
        }
        assertRejects("type assertion requires an operand", "const missing = <number>;");
    }

    private static void assertDirectJavaCstToAst(String source) {
        SubhutiCst cst = parseOvs(source);
        Program program = new QinOvsCstToAst().toOvsProgram(cst);
        ExpressionStatement render;
        if (program.body().getFirst() instanceof ExpressionStatement directRender) {
            render = directRender;
        } else if (program.body().getFirst() instanceof IfStatement ifStatement
                && ifStatement.consequent() instanceof com.slime.ast.nodes.statements.BlockStatement block
                && block.body().size() == 1 && block.body().getFirst() instanceof ExpressionStatement nestedRender) {
            render = nestedRender;
        } else {
            throw new AssertionError("direct Java OVS visitor lost the render statement: " + program);
        }
        if (!(render.expression() instanceof CallExpression call)
                || !(call.callee() instanceof MemberExpression member)
                || !(call.arguments().getFirst() instanceof ObjectExpression props)
                || !(call.arguments().get(1) instanceof ArrayExpression children)
                || props.properties().size() != (source.contains("class =") ? 2 : 0)
                || children.elements().size() != 1
                || !"$OvsHtmlTag".equals(((com.slime.ast.nodes.expressions.Identifier) member.object()).name())
                || !"div".equals(((com.slime.ast.nodes.expressions.Identifier) member.property()).name())) {
            throw new AssertionError("direct Java OVS visitor produced an unexpected AST: " + program);
        }

        Program fileAst = new QinOvsCstToAst().toOvsFileAst(cst);
        boolean wrapsComponent = source.startsWith("div(");
        if (!(fileAst.body().getFirst() instanceof ImportDeclaration)
                || (wrapsComponent && !(fileAst.body().getLast() instanceof ExportDefaultDeclaration))
                || (!wrapsComponent && fileAst.body().stream().anyMatch(ExportDefaultDeclaration.class::isInstance))) {
            throw new AssertionError("OVS file AST is missing runtime import or default component wrapper: " + fileAst);
        }
    }

    private static SubhutiCst parseOvs(String source) {
        OvsParser parser = new OvsParser(source);
        SubhutiCst cst = parser.Program(SlimeParser.SourceType.MODULE);
        if (parser.isParserFail() || cst == null || !parser.isAtEnd()) {
            throw new AssertionError("direct Java OVS parse failed: " + parser.getDiagnostics());
        }
        return cst;
    }

    private static void assertComputedPropertiesAndMethods() {
        var cst = parseOvs("""
                const label = "title";
                export const Component = () => div([label] = "hello"; onClick() { return "clicked"; }) { "text" };
                """);
        var ast = new QinOvsCstToAst().toOvsFileAst(cst);
        String code = new SlimeGenerator().generate(ast);
        if (!code.contains("[label]: \"hello\"") || !code.contains("onClick()")
                || !code.contains("return \"clicked\"")) {
            throw new AssertionError("OVS computed property or shorthand method was lost:\n" + code);
        }
    }

    private static void assertDirectJavaViewConversion() {
        SubhutiCst cst = parseOvs("view Card(props) { #{ const local = props.title; } div(class = props.className) { props.title } }");
        Program program = new QinOvsCstToAst().toOvsFileAst(cst);
        if (program.body().size() != 2 || !(program.body().getFirst() instanceof ImportDeclaration)
                || !(program.body().getLast() instanceof VariableDeclaration view)
                || view.declarations().size() != 1
                || !(view.declarations().getFirst().init() instanceof CallExpression componentFactory)
                || !(componentFactory.callee() instanceof com.slime.ast.nodes.expressions.Identifier callee)
                || !"defineOvsComponent".equals(callee.name())) {
            throw new AssertionError("Java OVS view lowering produced an unexpected AST: " + program);
        }
        ArrowFunctionExpression factory = (ArrowFunctionExpression) componentFactory.arguments().getFirst();
        BlockStatement body = (BlockStatement) factory.body();
        if (body.body().size() != 2
                || !(body.body().getFirst() instanceof BlockStatement noRenderBody)
                || noRenderBody.body().size() != 1
                || !(noRenderBody.body().getFirst() instanceof VariableDeclaration)
                || !(body.body().getLast() instanceof ReturnStatement returnStatement)
                || !(returnStatement.argument() instanceof CallExpression renderCall)
                || !(renderCall.callee() instanceof MemberExpression htmlTag)
                || !"div".equals(((com.slime.ast.nodes.expressions.Identifier) htmlTag.property()).name())) {
            throw new AssertionError("Java OVS view must return its rendered value: " + program);
        }
    }

    private static void assertDirectJavaViewParametersAndReturnConversion() {
        Program program = new QinOvsCstToAst().toOvsFileAst(parseOvs("""
                view Card({ title }, enabled = true, ...rest) {
                  const label = title
                  #{ console.log(enabled, rest) }
                  return div(class = label) { title }
                }
                """));
        if (!(program.body().getLast() instanceof VariableDeclaration declaration)
                || declaration.declarations().size() != 1
                || !(declaration.declarations().getFirst().init() instanceof CallExpression definition)
                || !(definition.arguments().getFirst() instanceof ArrowFunctionExpression factory)
                || factory.params().size() != 3
                || !(factory.body() instanceof BlockStatement body)
                || body.body().size() != 3
                || !(body.body().get(0) instanceof VariableDeclaration)
                || !(body.body().get(1) instanceof BlockStatement)
                || !(body.body().getLast() instanceof ReturnStatement returned)
                || !(returned.argument() instanceof CallExpression render)) {
            throw new AssertionError("Java OVS view must preserve destructured/default/rest parameters and explicit return: " + program);
        }
        if (!(render.arguments().get(1) instanceof ArrayExpression children) || children.elements().size() != 1) {
            throw new AssertionError("Explicit view return lost its OVS render children: " + program);
        }
    }

    private static void assertDirectJavaCssConversion() {
        SubhutiCst cst = parseOvs("const style = css { displayFlex, colorRed, customValue }");
        Program program = new QinOvsCstToAst().toOvsFileAst(cst);
        if (program.body().size() != 4
                || !(program.body().get(2) instanceof ImportDeclaration atomImport)
                || !(atomImport.source() instanceof com.slime.ast.nodes.expressions.Literal module)
                || !"virtual:csstsAtom".equals(module.value())
                || !(program.body().getLast() instanceof VariableDeclaration declaration)) {
            throw new AssertionError("Java OVS CSS conversion did not add its atom import: " + program);
        }
        CallExpression merge = (CallExpression) declaration.declarations().getFirst().init();
        if (!(merge.callee() instanceof MemberExpression mergeMember)
                || !(mergeMember.object() instanceof com.slime.ast.nodes.expressions.Identifier cssts)
                || !"cssts".equals(cssts.name())
                || !(mergeMember.property() instanceof com.slime.ast.nodes.expressions.Identifier mergeName)
                || !"merge".equals(mergeName.name())
                || merge.arguments().size() != 3) {
            throw new AssertionError("Java OVS CSS conversion did not lower css atom list to cssts.merge: " + program);
        }
        for (int i = 0; i < 2; i++) {
            if (!(merge.arguments().get(i) instanceof MemberExpression atom)
                    || !(atom.object() instanceof com.slime.ast.nodes.expressions.Identifier atomNamespace)
                    || !"csstsAtom".equals(atomNamespace.name())) {
                throw new AssertionError("Java OVS CSS conversion lost a cssts atom reference: " + program);
            }
        }
        if (!(merge.arguments().get(2) instanceof com.slime.ast.nodes.expressions.Identifier variable)
                || !"customValue".equals(variable.name())) {
            throw new AssertionError("Java OVS CSS conversion must preserve non-atom variables: " + program);
        }
    }

    private static void assertCsstsBindingScopes() {
        assertCsstsExpressions("""
                const colorBlue = palette;
                export const style = css { <string>colorRed, colorBlue as string, (colorGreen as string) };
                """, "cssts.merge((<string>csstsAtom.colorRed), colorBlue as string, (csstsAtom.colorGreen as string))");

        assertCsstsExpressions("""
                import { palette as colorRed } from "palette";
                import type { Swatch as colorBlue } from "types";
                export const style = css { colorRed, (colorBlue), colorGreen, displayFlex };
                const { custom: colorGreen } = palette;
                """, "cssts.merge(colorRed, (csstsAtom.colorBlue), colorGreen, csstsAtom.displayFlex)");

        assertCsstsExpressions("""
                const block = () => {
                  { let colorGreen = palette; const inside = css { colorGreen, colorBlue }; }
                  { var colorRed = palette; }
                  return css { colorGreen, colorRed };
                };
                const paint = ({ value: colorBlue }, colorBlack = css { colorGray }, ...colorPink) => {
                  var colorGray;
                  return css { colorBlue, colorBlack, colorPink, colorGray };
                };
                const outside = css { colorBlue, colorRed, colorPink };
                """, "cssts.merge(colorGreen, csstsAtom.colorBlue)",
                "cssts.merge(csstsAtom.colorGreen, colorRed)",
                "colorBlack = cssts.merge(csstsAtom.colorGray)",
                "cssts.merge(colorBlue, colorBlack, colorPink, colorGray)",
                "cssts.merge(csstsAtom.colorBlue, csstsAtom.colorRed, csstsAtom.colorPink)");

        assertCsstsExpressions("""
                for (const colorOrange of palette) { css { colorOrange }; }
                try {} catch ({ colorBrown }) { css { colorBrown }; }
                function colorIvory() {}
                class colorYellow {}
                const fn = function colorGray() { return css { colorGray }; };
                const outside = css { colorOrange, colorBrown, colorIvory, colorYellow, colorGray };
                """, "cssts.merge(colorOrange)", "cssts.merge(colorBrown)", "cssts.merge(colorGray)",
                "cssts.merge(csstsAtom.colorOrange, csstsAtom.colorBrown, colorIvory, colorYellow, csstsAtom.colorGray)");

        assertCsstsExpressions("""
                const paint = async colorRed => css { colorRed, (colorBlue) };
                """, "async colorRed => cssts.merge(colorRed, (csstsAtom.colorBlue))");

        assertCsstsExpressions("""
                view Panel({ colorRed }) {
                  div(class = css { colorRed }) {
                    #{ const colorBlue = palette; const local = css { colorBlue }; }
                    span(class = css { colorBlue }) { "content" }
                  }
                }
                """, "cssts.merge(colorRed)", "cssts.merge(colorBlue)", "cssts.merge(csstsAtom.colorBlue)");
    }

    private static void assertTypeImportClassification() {
        var cst = parseOvs("""
                import type { Swatch } from "./types";
                import { type as colorRed, type Shade as colorBlue, type as as colorGreen } from "./palette";
                """);
        var program = new QinOvsCstToAst().toOvsProgram(cst);
        var typeImport = (ImportDeclaration) program.body().get(0);
        var mixedImport = (ImportDeclaration) program.body().get(1);
        var value = (ImportSpecifier) mixedImport.specifiers().get(0);
        var alias = (ImportSpecifier) mixedImport.specifiers().get(1);
        var contextualAlias = (ImportSpecifier) mixedImport.specifiers().get(2);
        if (!typeImport.typeOnly() || mixedImport.typeOnly()
                || value.typeOnly() || !"type".equals(value.imported().name()) || !"colorRed".equals(value.local().name())
                || !alias.typeOnly() || !"Shade".equals(alias.imported().name())
                || !contextualAlias.typeOnly() || !"as".equals(contextualAlias.imported().name())) {
            throw new AssertionError("Type import classification changed: " + program);
        }
        assertParses("a type named as", "import { type as } from './types';");
        assertParses("a runtime binding named as", "import { type as as } from './types';");
    }

    private static void assertTypeExportClassification() {
        var cst = parseOvs("""
                export type * from "./types";
                export type * as Shapes from "./shapes";
                export { type as runtimeType, type Swatch, type as, type as as Shade } from "./palette";
                export * from "./values";
                """);
        var program = new QinOvsCstToAst().toOvsProgram(cst);
        var star = (ExportAllDeclaration) program.body().get(0);
        var namespace = (ExportAllDeclaration) program.body().get(1);
        var named = (ExportNamedDeclaration) program.body().get(2);
        var valueStar = (ExportAllDeclaration) program.body().get(3);
        if (!star.typeOnly() || !namespace.typeOnly() || valueStar.typeOnly()
                || !"Shapes".equals(namespace.exported().name())
                || !named.specifiers().stream().map(specifier -> specifier.typeOnly()).toList()
                        .equals(java.util.List.of(false, true, true, true))) {
            throw new AssertionError("Type export classification changed: " + program);
        }
        if (!new SlimeGenerator().generate(program).contains("export type * as Shapes from \"./shapes\"")) {
            throw new AssertionError("TypeScript source generation lost the type namespace export");
        }
    }

    private static void assertCsstsExpressions(String source, String... expected) {
        var program = new QinOvsCstToAst().toOvsFileAst(parseOvs(source));
        String code = new SlimeGenerator().generate(program);
        for (String expression : expected) {
            if (!code.contains(expression)) {
                throw new AssertionError("CSSTS binding resolution lost " + expression + ":\n" + code);
            }
        }
    }

    private static void assertRuntimeImportsAreDeduplicated() {
        Program program = new QinOvsCstToAst().toOvsFileAst(parseOvs("""
                import { $OvsHtmlTag, defineOvsComponent, defineReactiveExpression } from "ovsjs"
                import { cssts } from "cssts-ts"
                import "virtual:cssts.css"
                import { csstsAtom } from "virtual:csstsAtom"
                div(class = css { colorRed }) { "content" }
                """));
        java.util.Map<String, java.util.Set<String>> importedNames = new java.util.HashMap<>();
        for (AstNode node : program.body()) {
            if (!(node instanceof ImportDeclaration declaration)) continue;
            String source = ((com.slime.ast.nodes.expressions.Literal) declaration.source()).value().toString();
            for (AstNode specifier : declaration.specifiers()) {
                if (specifier instanceof com.slime.ast.nodes.misc.ImportSpecifier named) {
                    if (!importedNames.computeIfAbsent(source, ignored -> new java.util.HashSet<>()).add(named.imported().name())) {
                        throw new AssertionError("Java OVS visitor duplicated runtime import " + named.imported().name()
                                + " from " + source + ": " + program);
                    }
                }
            }
        }
        if (program.body().stream().filter(ImportDeclaration.class::isInstance).count() != 4) {
            throw new AssertionError("Existing OVS/CSSTS runtime imports should be reused: " + program);
        }
    }

    private static void assertDirectJavaNoRenderInsideRender() {
        SubhutiCst cst = parseOvs("""
            div {
              "before"
              #{
                const local = 1;
                span { "render remains active inside no-render" }
              }
              "after"
            }
            """);
        Program program = new QinOvsCstToAst().toOvsProgram(cst);
        if (!(program.body().getFirst() instanceof ExpressionStatement statement)
                || !(complexRenderBody(statement) instanceof BlockStatement componentBody)
                || componentBody.body().size() != 5
                || !(componentBody.body().getFirst() instanceof VariableDeclaration)
                || !(componentBody.body().get(1) instanceof ExpressionStatement firstAppend)
                || !(componentBody.body().get(2) instanceof BlockStatement noRender)
                || !(componentBody.body().get(3) instanceof ExpressionStatement append)
                || !(append.expression() instanceof CallExpression appendCall)
                || !(componentBody.body().getLast() instanceof ReturnStatement)) {
            throw new AssertionError("NoRender inside an OVS render must execute in the render body and leave only rendered values in children: " + program);
        }
        if (!(firstAppend.expression() instanceof CallExpression firstAppendCall)
                || !"push".equals(((com.slime.ast.nodes.expressions.Identifier)
                ((MemberExpression) firstAppendCall.callee()).property()).name())
                || !"push".equals(((com.slime.ast.nodes.expressions.Identifier)
                ((MemberExpression) appendCall.callee()).property()).name())
                || noRender.body().size() != 2
                || !(noRender.body().getFirst() instanceof VariableDeclaration)) {
            throw new AssertionError("NoRender statements were not preserved in the complex OVS render body: " + program);
        }
        if (!(noRender.body().get(1) instanceof ExpressionStatement nestedRender)
                || !(nestedRender.expression() instanceof CallExpression nestedPush)
                || !(nestedPush.callee() instanceof MemberExpression nestedMember)
                || !(nestedMember.object() instanceof com.slime.ast.nodes.expressions.Identifier object)
                || !"children".equals(object.name())
                || !(nestedMember.property() instanceof com.slime.ast.nodes.expressions.Identifier property)
                || !"push".equals(property.name())) {
            throw new AssertionError("OVS render statements inside NoRender must still append rendered children: " + program);
        }
    }

    private static void assertNoRenderReactiveControlFlow() {
        String source = """
                div {
                  #{
                    if (ready) {
                      span { "conditional render" }
                      console.log("conditional logic")
                    }
                    for (const item of items) {
                      span { item }
                      console.log(item)
                    }
                    switch (kind) {
                      case "ready": span { "switch render" }; break
                      default: console.log("switch logic")
                    }
                    try {
                      span { "try render" }
                    } catch (error) {
                      span { "catch render" }
                    }
                  }
                }
                """;
        SubhutiCst cst = parseOvs(source);
        Program program = new QinOvsCstToAst().toOvsProgram(cst);
        String ast = program.toString();
        boolean ifHasRender = containsControlWithChildrenPush(program, IfStatement.class);
        boolean loopHasRender = containsControlWithChildrenPush(program, com.slime.ast.nodes.statements.ForOfStatement.class);
        boolean switchHasRender = containsControlWithChildrenPush(program, com.slime.ast.nodes.statements.SwitchStatement.class);
        boolean tryHasRender = containsControlWithChildrenPush(program, com.slime.ast.nodes.statements.TryStatement.class);
        if (!ifHasRender || !loopHasRender || !switchHasRender || !tryHasRender || !ast.contains("name=console")) {
            throw new AssertionError("NoRender control flow must reactively append nested OVS renders while retaining ordinary logic"
                    + " (if=" + ifHasRender + ", loop=" + loopHasRender + ", switch=" + switchHasRender
                    + ", try=" + tryHasRender + ", logic=" + ast.contains("name=console") + "): " + program);
        }
    }

    private static void assertDirectJavaReactiveChildConversion() {
        Program program = new QinOvsCstToAst().toOvsProgram(parseOvs("div { props.title }"));
        if (!(program.body().getFirst() instanceof ExpressionStatement statement)
                || !(statement.expression() instanceof CallExpression render)
                || !(render.arguments().get(1) instanceof ArrayExpression children)
                || children.elements().size() != 1
                || !(children.elements().getFirst() instanceof CallExpression reactive)
                || !(reactive.callee() instanceof com.slime.ast.nodes.expressions.Identifier helper)
                || !"defineReactiveExpression".equals(helper.name())
                || !(reactive.arguments().getFirst() instanceof ArrowFunctionExpression getter)
                || !(getter.body() instanceof MemberExpression)) {
            throw new AssertionError("Java OVS render did not wrap a dynamic child expression reactively: " + program);
        }
    }

    private static void assertDirectJavaSideEffectRenderConversion() {
        Program program = new QinOvsCstToAst().toOvsProgram(parseOvs("div { count.value++ }") );
        if (!(program.body().getFirst() instanceof ExpressionStatement statement)
                || !(complexRenderBody(statement) instanceof BlockStatement componentBody)
                || componentBody.body().size() != 3
                || !(componentBody.body().get(1) instanceof ExpressionStatement sideEffect)
                || !(sideEffect.expression() instanceof com.slime.ast.nodes.expressions.UpdateExpression)) {
            throw new AssertionError("OVS side-effect expressions must execute without becoming rendered children: " + program);
        }
    }

    private static void assertDirectJavaReactiveIfConversion() {
        Program program = new QinOvsCstToAst().toOvsProgram(parseOvs("""
                div {
                  if (ready) { span { "yes" } }
                  else { span { "no" } }
                }
                """));
        if (!(program.body().getFirst() instanceof ExpressionStatement statement)
                || !(complexRenderBody(statement) instanceof BlockStatement componentBody)
                || !(componentBody.body().get(1) instanceof ExpressionStatement append)
                || !(append.expression() instanceof CallExpression appendCall)
                || !(appendCall.arguments().getFirst() instanceof CallExpression reactive)
                || !(reactive.callee() instanceof com.slime.ast.nodes.expressions.Identifier helper)
                || !"defineReactiveExpression".equals(helper.name())
                || !(reactive.arguments().getFirst() instanceof ArrowFunctionExpression effect)
                || !(effect.body() instanceof BlockStatement effectBody)
                || !(effectBody.body().get(1) instanceof IfStatement conditional)
                || !containsChildrenPush(conditional.consequent())
                || !containsChildrenPush(conditional.alternate())) {
            throw new AssertionError("Java OVS control flow must produce a reactive child computation: " + program);
        }
    }

    private static com.slime.ast.AstNode complexRenderBody(ExpressionStatement statement) {
        if (statement.expression() instanceof CallExpression call
                && call.callee() instanceof com.slime.ast.nodes.expressions.Identifier helper
                && "defineReactiveExpression".equals(helper.name())
                && call.arguments().getFirst() instanceof ArrowFunctionExpression getter) {
            return getter.body();
        }
        return null;
    }

    private static boolean containsChildrenPush(com.slime.ast.Statement statement) {
        if (statement instanceof BlockStatement block) {
            return block.body().stream().anyMatch(OvsParserJavaSmokeTestMain::containsChildrenPush);
        }
        if (statement instanceof IfStatement conditional) {
            return containsChildrenPush(conditional.consequent())
                    || (conditional.alternate() != null && containsChildrenPush(conditional.alternate()));
        }
        if (statement instanceof com.slime.ast.nodes.statements.WhileStatement loop) return containsChildrenPush(loop.body());
        if (statement instanceof com.slime.ast.nodes.statements.DoWhileStatement loop) return containsChildrenPush(loop.body());
        if (statement instanceof com.slime.ast.nodes.statements.ForStatement loop) return containsChildrenPush(loop.body());
        if (statement instanceof com.slime.ast.nodes.statements.ForInStatement loop) return containsChildrenPush(loop.body());
        if (statement instanceof com.slime.ast.nodes.statements.ForOfStatement loop) return containsChildrenPush(loop.body());
        if (statement instanceof com.slime.ast.nodes.statements.SwitchStatement switchStatement) {
            return switchStatement.cases().stream()
                    .flatMap(switchCase -> switchCase.consequent().stream())
                    .anyMatch(OvsParserJavaSmokeTestMain::containsChildrenPush);
        }
        if (statement instanceof com.slime.ast.nodes.statements.TryStatement tryStatement) {
            return containsChildrenPush(tryStatement.block())
                    || (tryStatement.handler() != null && containsChildrenPush(tryStatement.handler().body()))
                    || (tryStatement.finalizer() != null && containsChildrenPush(tryStatement.finalizer()));
        }
        if (!(statement instanceof ExpressionStatement expressionStatement)
                || !(expressionStatement.expression() instanceof CallExpression call)
                || !(call.callee() instanceof MemberExpression member)
                || !(member.object() instanceof com.slime.ast.nodes.expressions.Identifier object)
                || !"children".equals(object.name())
                || !(member.property() instanceof com.slime.ast.nodes.expressions.Identifier property)) {
            return false;
        }
        return "push".equals(property.name());
    }

    private static void assertDirectJavaReactiveLoopConversion() {
        Program program = new QinOvsCstToAst().toOvsProgram(parseOvs("""
                div {
                  while (ready) {
                    let label = 1;
                    { const label = 2; span { label } }
                    label += 1;
                    span { label }
                  }
                }
                """));
        if (!(program.body().getFirst() instanceof ExpressionStatement root)
                || !(complexRenderBody(root) instanceof BlockStatement body)
                || !(body.body().get(1) instanceof ExpressionStatement append)
                || !(append.expression() instanceof CallExpression appendCall)
                || !(appendCall.arguments().getFirst() instanceof CallExpression reactive)
                || !(reactive.callee() instanceof com.slime.ast.nodes.expressions.Identifier helper)
                || !"defineReactiveExpression".equals(helper.name())
                || !(reactive.arguments().getFirst() instanceof ArrowFunctionExpression getter)
                || !(getter.body() instanceof BlockStatement effect)
                || !(effect.body().get(1) instanceof com.slime.ast.nodes.statements.WhileStatement loop)
                || !(loop.body() instanceof BlockStatement loopBody)
                || loopBody.body().size() != 4
                || !(loopBody.body().getFirst() instanceof VariableDeclaration)
                || !(loopBody.body().get(1) instanceof BlockStatement nested)
                || !(nested.body().getFirst() instanceof VariableDeclaration)
                || !(loopBody.body().get(2) instanceof ExpressionStatement)
                || !containsChildrenPush(loop)) {
            throw new AssertionError("OVS loop must preserve its bindings, explicit nested block, and render children: " + program);
        }
    }

    private static void assertDirectJavaReactiveSwitchAndTryConversion() {
        Program switchProgram = new QinOvsCstToAst().toOvsProgram(parseOvs("""
                div {
                  switch (kind) {
                    case "a": span { "A" }; break
                    default: span { "other" }
                  }
                }
                """));
        if (!containsControlWithChildrenPush(switchProgram, com.slime.ast.nodes.statements.SwitchStatement.class)
                || !containsChildrenPush(switchProgram.body().getFirst() instanceof ExpressionStatement root
                        && complexRenderBody(root) instanceof BlockStatement body
                        && body.body().get(1) instanceof ExpressionStatement append
                        && append.expression() instanceof CallExpression push
                        && push.arguments().getFirst() instanceof CallExpression reactive
                        && reactive.arguments().getFirst() instanceof ArrowFunctionExpression getter
                        && getter.body() instanceof BlockStatement effect
                        && effect.body().get(1) instanceof com.slime.ast.Statement statement
                                ? statement : null)) {
            throw new AssertionError("OVS switch should be reactively lowered with render children in its clauses: " + switchProgram);
        }

        Program tryProgram = new QinOvsCstToAst().toOvsProgram(parseOvs("""
                div {
                  try { span { "try" } }
                  catch (error) { span { "catch" } }
                  finally { span { "finally" } }
                }
                """));
        if (!containsControlWithChildrenPush(tryProgram, com.slime.ast.nodes.statements.TryStatement.class)
                || !containsChildrenPush(tryProgram)) {
            throw new AssertionError("OVS try/catch/finally should be reactively lowered with render children: " + tryProgram);
        }
    }

    private static void assertNestedRenderControlFlowBoundary() {
        Program program = new QinOvsCstToAst().toOvsProgram(parseOvs("""
                const ListItem = (props) => { return li { props.text } }
                const items = ["Apple", "Banana"]
                div {
                  ul {
                    for (const item of items) {
                      ListItem({ text: item })
                    }
                  }
                }
                """));
        String ast = program.toString();
        if (!ast.contains("property=Identifier[name=div")
                || !ast.contains("property=Identifier[name=ul")
                || !ast.contains("ForOfStatement")
                || !ast.contains("ListItem")) {
            throw new AssertionError("A nested OVS render must own and preserve its own loop subtree: " + program);
        }
    }

    private static boolean containsChildrenPush(com.slime.ast.AstNode node) {
        if (node instanceof Program program) {
            return program.body().stream().anyMatch(OvsParserJavaSmokeTestMain::containsChildrenPush);
        }
        if (node instanceof ExpressionStatement expression) return containsChildrenPush(expression.expression());
        if (node instanceof CallExpression call) {
            if (call.callee() instanceof MemberExpression member
                    && member.object() instanceof com.slime.ast.nodes.expressions.Identifier object
                    && "children".equals(object.name())
                    && member.property() instanceof com.slime.ast.nodes.expressions.Identifier property
                    && "push".equals(property.name())) return true;
            return containsChildrenPush(call.callee())
                    || call.arguments().stream().anyMatch(OvsParserJavaSmokeTestMain::containsChildrenPush);
        }
        if (node instanceof ArrowFunctionExpression arrow) return containsChildrenPush(arrow.body());
        if (node instanceof com.slime.ast.nodes.expressions.ArrayExpression array) {
            return array.elements().stream().anyMatch(OvsParserJavaSmokeTestMain::containsChildrenPush);
        }
        if (!(node instanceof com.slime.ast.Statement statement)) return false;
        return containsChildrenPush(statement);
    }

    private static boolean containsControlWithChildrenPush(com.slime.ast.AstNode node, Class<?> type) {
        if (node == null) return false;
        if (type.isInstance(node)) {
            return node instanceof com.slime.ast.Statement statement && containsChildrenPush(statement);
        }
        if (node instanceof Program program) return program.body().stream()
                .anyMatch(child -> containsControlWithChildrenPush(child, type));
        if (node instanceof BlockStatement block) return block.body().stream()
                .anyMatch(child -> containsControlWithChildrenPush(child, type));
        if (node instanceof IfStatement conditional) return containsControlWithChildrenPush(conditional.test(), type)
                || containsControlWithChildrenPush(conditional.consequent(), type)
                || containsControlWithChildrenPush(conditional.alternate(), type);
        if (node instanceof ExpressionStatement expression) return containsControlWithChildrenPush(expression.expression(), type);
        if (node instanceof CallExpression call) return containsControlWithChildrenPush(call.callee(), type)
                || call.arguments().stream().anyMatch(child -> containsControlWithChildrenPush(child, type));
        if (node instanceof ArrowFunctionExpression arrow) return containsControlWithChildrenPush(arrow.body(), type);
        if (node instanceof com.slime.ast.nodes.expressions.ArrayExpression array) return array.elements().stream()
                .anyMatch(child -> containsControlWithChildrenPush(child, type));
        if (node instanceof com.slime.ast.nodes.statements.SwitchStatement value) return value.cases().stream()
                .anyMatch(item -> item.consequent().stream().anyMatch(child -> containsControlWithChildrenPush(child, type)));
        if (node instanceof com.slime.ast.nodes.statements.TryStatement value) {
            return containsControlWithChildrenPush(value.block(), type)
                    || (value.handler() != null && containsControlWithChildrenPush(value.handler().body(), type))
                    || containsControlWithChildrenPush(value.finalizer(), type);
        }
        return false;
    }

}
