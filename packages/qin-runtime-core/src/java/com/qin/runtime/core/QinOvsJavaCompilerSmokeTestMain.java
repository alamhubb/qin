package com.qin.runtime.core;

public final class QinOvsJavaCompilerSmokeTestMain {
    private QinOvsJavaCompilerSmokeTestMain() {
    }

    public static void main(String[] args) {
        QinOvsCompiler.QinOvsCompileResult result = QinOvsJavaCompiler.compile("""
                div(class = css { displayFlex, colorRed }) {
                  "Java compiler output"
                }
                """);
        if (!result.code().contains("from \"cssts-ts\"")
                || !result.code().contains("virtual:cssts.css")
                || !result.code().contains("virtual:csstsAtom")
                || !result.code().contains("cssts.merge(csstsAtom.displayFlex, csstsAtom.colorRed)")) {
            throw new AssertionError("Java OVS compiler emitted an incomplete module: " + result.code());
        }
        if (!result.hasStyles()
                || !result.css().contains(".cssts_display_flex { display: flex; }")
                || !result.css().contains(".cssts_color_red { color: red; }")
                || !result.atomModule().contains("displayFlex: { 'cssts_display_flex': 'display' }")
                || !result.atomModule().contains("colorRed: { 'cssts_color_red': 'color' }")) {
            throw new AssertionError("Java OVS compiler emitted incomplete CSSTS artifacts: " + result);
        }
        QinOvsCompiler.QinOvsCompileResult noRender = QinOvsJavaCompiler.compile("""
                div {
                  "before"
                  #{
                    const local = 1;
                    span { "render remains active inside no-render" }
                    if (ready) {
                      span { "conditional render inside no-render" }
                      console.log("conditional setup")
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
                  props.title
                }
                """);
        if (!noRender.code().contains("defineOvsComponent")
                || !noRender.code().contains("const local = 1")
                || !noRender.code().contains("children.push($OvsHtmlTag.span")
                || !noRender.code().contains("if (ready)")
                || !noRender.code().contains("for (const item of items)")
                || !noRender.code().contains("switch (kind)")
                || !noRender.code().contains("try {")
                || !noRender.code().contains("console.log")
                || !noRender.code().contains("children.push")
                || !noRender.code().contains("defineReactiveExpression")) {
            throw new AssertionError("Java OVS compiler did not preserve non-render statements inside render blocks: " + noRender.code());
        }
        QinOvsCompiler.QinOvsCompileResult conditional = QinOvsJavaCompiler.compile("""
                div {
                  if (ready) { span { "yes" } }
                  else { span { "no" } }
                }
                """);
        if (!conditional.code().contains("defineReactiveExpression")
                || !conditional.code().contains("children.push")
                || !conditional.code().contains("if (ready)")) {
            throw new AssertionError("Java OVS compiler did not preserve reactive render control flow: " + conditional.code());
        }
        var svg = QinOvsJavaCompiler.compile("""
                import { Polyline } from "./Polyline.ovs";
                svg {
                  polyline(points = "0,0 10,10") {}
                  Polyline { }
                }
                """);
        if (!svg.code().contains("$OvsHtmlTag.svg(")
                || !svg.code().contains("$OvsHtmlTag.polyline(")
                || !svg.code().contains("() => Polyline(")) {
            throw new AssertionError("OVS must distinguish native SVG polyline from a component: " + svg.code());
        }
        var complexView = QinOvsJavaCompiler.compile("""
                view Stable(props) {
                  div {
                    const label = props.label;
                    button { label }
                  }
                }
                export { Stable };
                """);
        if (!complexView.code().contains("return defineReactiveExpression(() =>")
                || complexView.code().contains("return defineOvsComponent(() =>")) {
            throw new AssertionError("Complex render must keep its Vue component type across updates: " + complexView.code());
        }
        assertNestedNoRenderControlFlow();
        assertNestedRenderLoopControlFlow();
        assertComplexRenderStaysInsideTry();
        assertLabeledRenderLoop();
        assertRenderChildrenBindingIsIsolated();
        assertImplicitRenderSourceOrder();
        assertAliasedRuntimeImports();
        assertRuntimeBindingCollisions();
        assertEmbeddedRenderExpressions();
        QinOvsCompiler.QinOvsCompileResult view = QinOvsJavaCompiler.compile("""
                view Card({ title }, enabled = true, ...rest) {
                  const label = title
                  #{ console.log(enabled, rest) }
                  return div(class = label) { title }
                }
                """);
        if (!view.code().contains("defineOvsComponent")
                || !view.code().contains("enabled = true")
                || !view.code().contains("...rest")
                || !view.code().contains("console.log")
                || !view.code().contains("return $OvsHtmlTag.div")) {
            throw new AssertionError("Java OVS compiler did not preserve view parameters and function body: " + view.code());
        }
        QinOvsCompiler.QinOvsCompileResult loop = QinOvsJavaCompiler.compile("""
                div {
                  while (ready) { span { "item" } }
                }
                """);
        if (!loop.code().contains("defineReactiveExpression")
                || !loop.code().contains("while (ready)")
                || !loop.code().contains("children.push")) {
            throw new AssertionError("Java OVS compiler did not preserve reactive loop rendering: " + loop.code());
        }
        QinOvsCompiler.QinOvsCompileResult pseudo = QinOvsJavaCompiler.compile(
                "div(class = css { hover, click }) { \"interactive\" }");
        if (!pseudo.atomModule().contains("hover: { 'cssts_hover': ':hover' }")
                || !pseudo.atomModule().contains("click: { 'cssts_click': true }")
                || !pseudo.css().contains(".cssts_hover:hover { filter: brightness(1.15); }")
                || !pseudo.css().contains(".cssts_click { cursor: pointer; }")) {
            throw new AssertionError("Java OVS compiler did not preserve pseudo/class-group atom semantics: " + pseudo);
        }
        var implicitAfterTypeExports = QinOvsJavaCompiler.compile("""
                export type Label = string;
                export type * from "./type-helpers.js";
                div { "card" }
                """);
        if (!implicitAfterTypeExports.code().contains("export default defineOvsComponent(")
                || implicitAfterTypeExports.code().contains("type-helpers")
                || implicitAfterTypeExports.code().contains("type Label")) {
            throw new AssertionError("Type exports prevented the implicit OVS component: " + implicitAfterTypeExports.code());
        }
        var typedClass = QinOvsJavaCompiler.compile("""
                interface Named { name(): string }
                export abstract class Base implements Named {
                  declare ghost: string;
                  abstract name(): string;
                  protected value(text?: string): string { return text || "default"; }
                }
                export class Derived extends Base {
                  public override name(): string { return this.value(); }
                  render(): unknown { return div { this.name() } }
                }
                """);
        if (!typedClass.code().contains("export class Base")
                || !typedClass.code().contains("value(text)")
                || !typedClass.code().contains("export class Derived extends Base")
                || !typedClass.code().contains("$OvsHtmlTag.div")
                || typedClass.code().contains("abstract ") || typedClass.code().contains(" implements ")
                || typedClass.code().contains("ghost") || typedClass.code().contains("protected ")
                || typedClass.code().contains(" override ") || typedClass.code().contains(": string")) {
            throw new AssertionError("OVS emitted TypeScript class annotations: " + typedClass.code());
        }
        assertParameterProperties();
        System.out.println("QinOvsJavaCompilerSmokeTestMain passed.");
    }

    private static void assertParameterProperties() {
        String source = """
                export class Base {
                  constructor(public name: string = "base", private readonly count: number = 3) {
                    this.seen = this.name + this.count;
                  }
                }
                export class Child extends Base {
                  constructor(readonly id: string, flag: boolean) {
                    const __qinInitializeParameterProperties = "user";
                    if (flag) {
                      const id = "shadow";
                      super(id);
                    } else {
                      super("alternate");
                    }
                    this.local = __qinInitializeParameterProperties;
                  }
                }
                """;
        String code = QinOvsJavaCompiler.compile(source).code();
        if (!code.contains("name;") || !code.contains("count;")
                || !code.contains("constructor(name = \"base\", count = 3)")
                || !code.contains("this.name = name;") || !code.contains("this.count = count;")
                || !code.contains("this.id = id;") || !code.contains("const __qinInitializeParameterProperties = \"user\";")
                || !code.contains("(super(id), __qinInitializeParameterProperties$(), this)")
                || !code.contains("(super(\"alternate\"), __qinInitializeParameterProperties$(), this)")) {
            throw new AssertionError("OVS lost parameter properties or initialized them before super: " + code);
        }
        String typescript = new com.slime.generator.SlimeGenerator().generate(new com.qin.parser.QinParserFacade().parseProgram(source));
        if (!typescript.contains("public name: string = \"base\"") || !typescript.contains("private readonly count: number = 3")
                || !typescript.contains("readonly id: string")) {
            throw new AssertionError("TypeScript regeneration lost parameter property modifiers: " + typescript);
        }
    }

    private static void assertNestedNoRenderControlFlow() {
        String code = QinOvsJavaCompiler.compile("""
                div {
                  if (ready) {
                    #{ trace("selected") }
                    span { "yes" }
                  } else {
                    span { "no" }
                  }
                  #{
                    for (const item of items) {
                      if (item === 2) continue;
                      trace(item)
                      span { item }
                    }
                  }
                }
                """).code();
        int branch = code.indexOf("if (ready)");
        int trace = code.indexOf("trace(\"selected\")");
        int alternate = code.indexOf("else", trace);
        int loop = code.indexOf("for (const item of items)");
        int jump = code.indexOf("continue;", loop);
        if (branch < 0 || trace < branch || alternate < trace || loop < alternate || jump < loop
                || !code.substring(trace, alternate).contains("children.push($OvsHtmlTag.span")
                || !code.substring(alternate, loop).contains("children.push($OvsHtmlTag.span")
                || code.substring(loop, jump).contains("=>")) {
            throw new AssertionError("Java OVS compiler moved nested NoRender code out of its control flow:\n" + code);
        }
    }

    private static void assertNestedRenderLoopControlFlow() {
        String code = QinOvsJavaCompiler.compile("""
                export default div {
                  for (const item of [1, 2, 3]) {
                    try {
                      switch (item) {
                        case 2: continue;
                        default: span { item }
                      }
                    } finally {
                      #{ trace(item) }
                    }
                  }
                }
                """).code();
        int loop = code.indexOf("for (const item of [1, 2, 3])");
        int tryBlock = code.indexOf("try {", loop);
        int switchBlock = code.indexOf("switch (item)", tryBlock);
        int jump = code.indexOf("continue;", switchBlock);
        int render = code.indexOf("children.push($OvsHtmlTag.span", jump);
        int finallyBlock = code.indexOf("finally {", render);
        int trace = code.indexOf("trace(item);", finallyBlock);
        if (loop < 0 || tryBlock < loop || switchBlock < tryBlock || jump < switchBlock
                || render < jump || finallyBlock < render || trace < finallyBlock
                || code.substring(loop, jump).contains("=>")) {
            throw new AssertionError("OVS nested loop branches lost render or moved continue into a callback:\n" + code);
        }
    }

    private static void assertComplexRenderStaysInsideTry() {
        String code = QinOvsJavaCompiler.compile("""
                export default div {
                  try {
                    button(title = fail()) { const label = "unexpected"; label; }
                  } catch (error) {
                    p { "caught" }
                  } finally {
                    #{ trace("finally") }
                  }
                }
                """).code();
        int start = code.indexOf("try {");
        int end = code.indexOf("} catch", start);
        if (start < 0 || end < start
                || !code.substring(start, end).contains("children.push((() =>")
                || !code.substring(start, end).contains("title: fail()")) {
            throw new AssertionError("OVS must evaluate the protected render before leaving try:\n" + code);
        }
    }

    private static void assertLabeledRenderLoop() {
        String code = QinOvsJavaCompiler.compile("""
                div {
                  outer: for (const item of [1, 2, 3, 4]) {
                    for (const sub of [0, 1]) {
                      if (item === 1) continue outer;
                      if (item === 4) break outer;
                      if (sub === 1) continue;
                      p { item }
                    }
                  }
                }
                """).code();
        int label = code.indexOf("outer:");
        int loop = code.indexOf("for (const item", label);
        int jump = code.indexOf("continue outer;", loop);
        int stop = code.indexOf("break outer;", jump);
        int render = code.indexOf("children.push($OvsHtmlTag.p", stop);
        if (label < 0 || loop < label || jump < loop || stop < jump || render < stop
                || code.substring(label, stop).contains("=>")) {
            throw new AssertionError("OVS labeled loop lost a render child or changed a jump target:\n" + code);
        }
    }

    private static void assertRenderChildrenBindingIsIsolated() {
        String code = QinOvsJavaCompiler.compile("""
                const ch\\u0069ldren = ["source"];
                const __qinOvsChildren = ["reserved"];
                export default div {
                  if (children.length) {
                    span { children[0] }
                    span { __qinOvsChildren[0] }
                  }
                }
                """).code();
        if (code.contains("const children = []") || code.contains("const __qinOvsChildren = []")
                || !code.contains("if (children.length)")
                || !code.contains("() => children[0]") || !code.contains("() => __qinOvsChildren[0]")) {
            throw new AssertionError("An OVS render accumulator shadowed a source binding:\n" + code);
        }
    }

    private static void assertAliasedRuntimeImports() {
        String code = QinOvsJavaCompiler.compile("""
                import { $OvsHtmlTag as tags, defineReactiveExpression as reactive } from "ovsjs";
                import { cssts as styles } from "cssts-ts";
                import { csstsAtom as atoms } from "virtual:csstsAtom";
                export const originalImports = [tags, reactive, styles, atoms];
                export const message = "hello";
                export default div(class = css { colorRed }) { message }
                """).code();
        if (!code.contains("$OvsHtmlTag as tags") || !code.contains("defineReactiveExpression as reactive")
                || !code.contains("cssts as styles") || !code.contains("csstsAtom as atoms")
                || !code.contains("import {$OvsHtmlTag, defineOvsComponent, defineReactiveExpression} from \"ovsjs\"")
                || !code.contains("import {cssts} from \"cssts-ts\"")
                || !code.contains("import {csstsAtom} from \"virtual:csstsAtom\"")) {
            throw new AssertionError("An OVS or CSSTS runtime name was left unbound:\n" + code);
        }
    }

    private static void assertImplicitRenderSourceOrder() {
        String code = QinOvsJavaCompiler.compile("""
                let title = "first";
                button(title = title) { "before" }
                const next = "second";
                title = next;
                button(title = title) { "after" }
                """).code();
        int before = code.indexOf("\"before\"");
        int declaration = code.indexOf("const next");
        int assignment = code.indexOf("title = next;");
        int after = code.indexOf("\"after\"");
        if (before < 0 || declaration <= before || assignment <= declaration || after <= assignment
                || !code.contains("children.push($OvsHtmlTag.button")) {
            throw new AssertionError("Top-level roots must collect rendered values in source order: " + code);
        }
    }

    private static void assertEmbeddedRenderExpressions() {
        String code = QinOvsJavaCompiler.compile("""
                export const choice = ready ? div { "yes" } : span { "no" };
                export const list = [1, div { "middle" }, 3];
                export const call = invoke(1, div { "arg" }, 3);
                export const options = { style: css { colorRed }, value: 2 };
                div {
                  const node = span { "stored" };
                  node
                }
                """).code();
        if (!code.contains("choice = ready ? $OvsHtmlTag.div")
                || !code.contains(": $OvsHtmlTag.span")
                || !code.contains("list = [1, $OvsHtmlTag.div")
                || !code.contains("call = invoke(1, $OvsHtmlTag.div")
                || !code.contains("cssts.merge(csstsAtom.colorRed), value: 2")
                || !code.contains("const node = $OvsHtmlTag.span")
                || !code.contains("defineReactiveExpression(() => node)")) {
            throw new AssertionError("OVS or CSS expression replaced its enclosing JavaScript expression:\n" + code);
        }
    }

    private static void assertRuntimeBindingCollisions() {
        String code = QinOvsJavaCompiler.compile("""
                export const $OvsHtmlTag = "business tag";
                export const defineOvsComponent = "business component";
                export const __qinOvsHtmlTag = "reserved";
                export function row(defineReactiveExpression) {
                    return [div { defineReactiveExpression }, defineReactiveExpression];
                }
                view Display(label) { span { label } }
                export { Display };
                """).code();
        if (!code.contains("$OvsHtmlTag as __qinOvsHtmlTag1")
                || !code.contains("defineOvsComponent as __qinOvsDefineComponent")
                || !code.contains("defineReactiveExpression as __qinOvsReactiveExpression")
                || !code.contains("return [__qinOvsHtmlTag1.div(")
                || !code.contains("__qinOvsReactiveExpression(() => defineReactiveExpression)")) {
            throw new AssertionError("OVS rendering references conflict with source bindings:\n" + code);
        }
    }
}
