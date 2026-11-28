package com.qin.runtime.core;

import com.qin.parser.QinParserFacade;
import com.slime.generator.SlimeGenerator;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsCompilerSmokeTestMain {
    private QinCsstsCompilerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-compiler-");
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-cssts-compiler-smoke\" }\n", StandardCharsets.UTF_8);
        assertFunctionTypeErasure(root);
        assertRuntimeEnums(root);

        String source = """
                const buttonBase = css { colorRed, fontBold }
                const buttonHover = css { backgroundColorBlue }
                """;

        QinCsstsCompiler.QinCsstsCompileResult result = new QinCsstsCompiler().compile(root, source);
        if (!result.hasStyles()
                || !result.code().contains("cssts.merge(csstsAtom.colorRed, csstsAtom.fontBold)")
                || !result.css().contains(".cssts_color_red { color: red; }")
                || !result.atomModule().contains("colorRed: { 'cssts_color_red': 'color' }")) {
            throw new AssertionError("Java CSSTS compiler did not produce expected module and artifacts: " + result);
        }
        QinCsstsCompiler.QinCsstsCompileResult expressionAtoms = new QinCsstsCompiler().compile(root, """
                const base = { colorRed: true }
                const isError = true
                const active = true
                const style = css { ...base, isError && colorRed, active ? colorBlue : colorGray }
                """);
        if (!expressionAtoms.code().contains("cssts.merge(base, isError")
                || !expressionAtoms.code().contains("isError && csstsAtom.colorRed")
                || !expressionAtoms.code().contains("active ? csstsAtom.colorBlue : csstsAtom.colorGray")
                || !expressionAtoms.atomModule().contains("colorRed:")
                || !expressionAtoms.atomModule().contains("colorBlue:")
                || !expressionAtoms.atomModule().contains("colorGray:")) {
            throw new AssertionError("Java CSSTS compiler did not preserve expression atoms or spread entries: " + expressionAtoms);
        }
        QinCsstsCompiler.QinCsstsCompileResult scopedAtoms = new QinCsstsCompiler().compile(root, """
                import { palette as colorRed } from "./palette.js";
                export const style = css { colorRed, (colorBlue) };
                """);
        if (!scopedAtoms.code().contains("cssts.merge(colorRed, (csstsAtom.colorBlue))")
                || scopedAtoms.css().contains(".cssts_color_red {")
                || scopedAtoms.atomModule().contains("colorRed:")
                || !scopedAtoms.css().contains(".cssts_color_blue { color: blue; }")
                || !scopedAtoms.atomModule().contains("colorBlue:")) {
            throw new AssertionError("CSSTS bindings changed the ESM or generated the wrong atom artifacts: " + scopedAtoms);
        }
        var mixedImports = new QinCsstsCompiler().compile(root, """
                import type { Swatch as colorGreen } from "./type-only.js";
                import { type as colorRed, type Shade as colorBlue } from "./palette.js";
                import palette, { type Theme } from "./default-palette.js";
                import { type Shape } from "./inline-type-only.js";
                import {} from "./empty.js";
                import "./side-effect.js";
                export const style = css { palette, colorRed, colorBlue, colorGreen };
                """);
        if (!mixedImports.code().contains("import {type as colorRed} from \"./palette.js\"")
                || !mixedImports.code().contains("import palette from \"./default-palette.js\"")
                || !mixedImports.code().contains("import {} from \"./empty.js\"")
                || !mixedImports.code().contains("import \"./side-effect.js\"")
                || mixedImports.code().contains("type-only.js") || mixedImports.code().contains("type Shade")
                || !mixedImports.code().contains("cssts.merge(palette, colorRed, csstsAtom.colorBlue, csstsAtom.colorGreen)")
                || mixedImports.atomModule().contains("colorRed:")
                || !mixedImports.atomModule().contains("colorBlue:") || !mixedImports.atomModule().contains("colorGreen:")) {
            throw new AssertionError("CSSTS type imports leaked into runtime output: " + mixedImports.code());
        }
        var typedExports = new QinCsstsCompiler().compile(root, """
                export type * from "./types-star.js";
                export type * as Shapes from "./types-ns.js";
                export type { Shapes as Theme } from "./types-named.js";
                export { type Swatch } from "./types-inline.js";
                export { marker as forwarded, type Swatch } from "./values.js";
                export * as extras from "./extras.js";
                export * from "./star.js";
                export {} from "./side.js";
                export interface Color { red: number }
                export type Shade = string;
                interface LocalShape {}
                type LocalShade = string;
                export const style = css { colorRed };
                """);
        if (!typedExports.code().contains("export {marker as forwarded} from \"./values.js\"")
                || !typedExports.code().contains("export * as extras from \"./extras.js\"")
                || !typedExports.code().contains("export * from \"./star.js\"")
                || !typedExports.code().contains("export {} from \"./side.js\"")
                || !typedExports.code().contains("export const style = cssts.merge(csstsAtom.colorRed)")
                || typedExports.code().contains("./types-") || typedExports.code().contains("type ")
                || typedExports.code().contains("interface ") || typedExports.code().contains("Swatch")) {
            throw new AssertionError("CSSTS type exports leaked into runtime output: " + typedExports.code());
        }
        var plainLoop = new QinCsstsCompiler().compile(root, """
                export function styles(palette) {
                  let output;
                  for (const colorRed of palette) {
                    output = css { colorRed, (colorBlue) };
                  }
                  return output;
                }
                """);
        if (!plainLoop.code().contains("for (const colorRed of palette)")
                || !plainLoop.code().contains("output = cssts.merge(colorRed, (csstsAtom.colorBlue))")
                || plainLoop.code().contains("defineReactiveExpression") || plainLoop.code().contains("children.push")) {
            throw new AssertionError("A standard CSSTS loop was lowered as an OVS render loop: " + plainLoop.code());
        }
        var nestedDeclaration = new QinCsstsCompiler().compile(root, """
                export function run() {
                  function pick() { return css { colorRed }; }
                  return pick();
                }
                """);
        if (!nestedDeclaration.code().contains("function pick()")
                || !nestedDeclaration.code().contains("return cssts.merge(csstsAtom.colorRed)")
                || !nestedDeclaration.code().contains("return pick()")) {
            throw new AssertionError("CSSTS flattened a nested declaration: " + nestedDeclaration.code());
        }
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  cssts: {
                    classPrefix: 'balance-',
                    pseudoClassConfig: { selected: { outline: '2px solid red' } },
                    classGroup: { selectable: ['selected', 'colorGreen'] }
                  }
                };
                """, StandardCharsets.UTF_8);
        QinCsstsCompiler.QinCsstsCompileResult module = new QinCsstsCompiler().compile(root, """
                import "./tokens.cssts"
                const tokenStyle = css { padding12px, borderRadius8px, backgroundColorWhite, colorBlue, fontWeight700 }
                export default tokenStyle
                """);
        if (!module.code().contains("import \"./tokens.cssts\"")
                || !module.code().contains("export default tokenStyle")
                || !module.css().contains(".balance-padding_12px { padding: 12px; }")
                || !module.atomModule().contains("backgroundColorWhite: { 'balance-background-color_white': 'background-color' }")) {
            throw new AssertionError("Java CSSTS compiler did not preserve module syntax and configured atom artifacts: " + module);
        }
        QinCsstsCompiler.QinCsstsCompileResult pseudoAndGroup = new QinCsstsCompiler().compile(root,
                "const interactive = css { hover, click, selected, selectable }");
        if (!pseudoAndGroup.code().contains("csstsAtom.hover")
                || !pseudoAndGroup.code().contains("csstsAtom.click")
                || !pseudoAndGroup.code().contains("csstsAtom.selected")
                || !pseudoAndGroup.code().contains("csstsAtom.selectable")
                || !pseudoAndGroup.atomModule().contains("hover: { 'balance-hover': ':hover' }")
                || !pseudoAndGroup.atomModule().contains("click: { 'balance-click': true }")
                || !pseudoAndGroup.atomModule().contains("selected: { 'balance-selected': ':selected' }")
                || !pseudoAndGroup.atomModule().contains("selectable: { 'balance-selectable': true }")
                || !pseudoAndGroup.css().contains(".balance-hover:hover { filter: brightness(1.15); }")
                || !pseudoAndGroup.css().contains(".balance-click:active { filter: brightness(0.85); }")
                || !pseudoAndGroup.css().contains(".balance-click { cursor: pointer; }")
                || !pseudoAndGroup.css().contains(".balance-selected:selected { outline: 2px solid red; }")
                || !pseudoAndGroup.css().contains(".balance-selectable { color: green; }")) {
            throw new AssertionError("Java CSSTS compiler did not generate pseudo/class-group atoms: " + pseudoAndGroup);
        }
        assertNamedPseudoVariants(root);
        assertRuntimeBindingCollisions(root);
        var nested = new QinCsstsCompiler().compile(root, """
                const colorBlue = { localBlue: true };
                export const style = css { [colorRed, , [colorBlue, colorGreen], ...[fontBold]], displayFlex };
                """);
        if (!nested.code().contains("[csstsAtom.colorRed, , [colorBlue, csstsAtom.colorGreen], ...[csstsAtom.fontBold]]")
                || !nested.css().contains("color: green") || nested.atomModule().contains("colorBlue:")) {
            throw new AssertionError("Java CSSTS did not resolve atoms in nested arrays: " + nested);
        }
        var calls = new QinCsstsCompiler().compile(root, """
                const colorBlue = { localBlue: true };
                export const style = css { service.colorRed?.(colorRed, colorBlue, ...[fontBold]) };
                """);
        if (!calls.code().contains("service.colorRed?.(csstsAtom.colorRed, colorBlue, ...[csstsAtom.fontBold])")
                || calls.atomModule().contains("colorBlue:")) {
            throw new AssertionError("Java CSSTS changed the callee or missed a style argument: " + calls);
        }
        System.out.println("QinCsstsCompilerSmokeTestMain passed.");
    }

    private static void assertRuntimeBindingCollisions(Path root) throws Exception {
        var result = new QinCsstsCompiler().compile(root, """
                export const cssts = "source merge";
                export const __qinCssts = "source reserved";
                export function panel(csstsAtom) {
                    const btn$$hover = css { colorRed };
                    return [btn$$hover, csstsAtom];
                }
                """);
        if (!result.code().contains("import {cssts as __qinCssts1} from \"cssts-ts\"")
                || !result.code().contains("import {csstsAtom as __qinCsstsAtom} from \"virtual:csstsAtom\"")
                || !result.code().contains("__qinCssts1.merge(__qinCsstsAtom.btn$$hover, __qinCsstsAtom.colorRed)")
                || !result.code().contains("return [btn$$hover, csstsAtom];")) {
            throw new AssertionError("Generated CSSTS runtime names conflict with source bindings: " + result.code());
        }
    }

    private static void assertNamedPseudoVariants(Path root) throws Exception {
        var result = new QinCsstsCompiler().compile(root, """
                export const actionButton$$hover = css { colorBlue };
                export const actionButton$$hover$$selected = css { colorRed },
                             ordinary$$hover = cssts.merge(actionButton$$hover);
                export function local() {
                    const localButton$$hover = css {};
                    return localButton$$hover;
                }
                """);
        String hoverRule = ".balance-action-button:hover { filter: brightness(1.15); }";
        if (!result.hasStyles()
                || !result.code().contains("cssts.merge(csstsAtom.actionButton$$hover, csstsAtom.colorBlue)")
                || !result.code().contains("cssts.merge(csstsAtom.actionButton$$hover$$selected, csstsAtom.colorRed)")
                || !result.code().contains("ordinary$$hover = cssts.merge(actionButton$$hover)")
                || !result.code().contains("cssts.merge(csstsAtom.localButton$$hover)")
                || !result.atomModule().contains("actionButton$$hover$$selected: { 'balance-action-button': null }")
                || !result.atomModule().contains("localButton$$hover: { 'balance-local-button': null }")
                || result.atomModule().contains("ordinary$$hover:")
                || !result.css().contains(hoverRule)
                || result.css().indexOf(hoverRule) != result.css().lastIndexOf(hoverRule)
                || !result.css().contains(".balance-action-button:selected { outline: 2px solid red; }")
                || !result.css().contains(".balance-local-button:hover { filter: brightness(1.15); }")) {
            throw new AssertionError("Java CSSTS emitted incorrect named pseudo variants: " + result);
        }
    }

    private static void assertRuntimeEnums(Path root) throws Exception {
        var result = new QinCsstsCompiler().compile(root, """
                function computed(): number { return 8; }
                export enum Mode { Idle, Active = 4, Alias = Active + 1, Next, Label = "mode",
                    LabelAlias = Label, Dynamic = computed(), DynamicAlias = Dynamic + 1 }
                export enum Mode { Merged = Mode.Next + 1 }
                export const enum Flags { One = 1 << 0, Two = 1 << 1, Both = One | Two }
                export enum Linked { Text = Mode["Label"], First = Flags.Both + 1, Next }
                const base = Mode.Active;
                const step = base + 2, label = "step:" + step;
                export enum FromConst { Text = label, Start = step, Next }
                export function shadowConstant(step: number) {
                    enum Local { Start = step }
                    return Local;
                }
                const dynamic = computed();
                export enum FromCall { Start = dynamic }
                export function visible() {
                    enum Inner { Text = Mode.Label, First = Flags.Both + 1, Next }
                    return Inner;
                }
                export function shadow(Flags: { Both: number }) {
                    enum Inner { First = Flags.Both }
                    return Inner;
                }
                export declare enum Ambient { Unavailable }
                export function local() { enum Mode { Inner } return Mode.Inner; }
                """);
        String code = result.code();
        if (!code.contains("export var Mode;") || !code.contains("Mode[Mode[\"Idle\"] = 0.0] = \"Idle\"")
                || !code.contains("Mode[Mode[\"Merged\"] = 7.0] = \"Merged\"")
                || !code.contains("Mode[\"LabelAlias\"] = \"mode\"")
                || !code.contains("Mode.Dynamic + 1") || !code.contains("Flags[Flags[\"Both\"] = 3.0]")
                || !code.contains("Linked[\"Text\"] = \"mode\"")
                || !code.contains("Linked[Linked[\"Next\"] = 5.0] = \"Next\"")
                || !code.contains("FromConst[\"Text\"] = \"step:6\"")
                || !code.contains("FromConst[FromConst[\"Next\"] = 7.0] = \"Next\"")
                || !code.contains("Local[Local[\"Start\"] = step] = \"Start\"")
                || !code.contains("FromCall[FromCall[\"Start\"] = dynamic] = \"Start\"")
                || !code.contains("Inner[\"Text\"] = \"mode\"")
                || !code.contains("Inner[Inner[\"Next\"] = 5.0] = \"Next\"")
                || !code.contains("Inner[Inner[\"First\"] = Flags.Both] = \"First\"")
                || !code.contains("let Mode;") || code.contains("Ambient") || code.contains("enum ")
                || result.moduleBindings().exports().stream().filter(binding -> "Mode".equals(binding.exportName())).count() != 1) {
            throw new AssertionError("CSSTS emitted incorrect runtime enums or bindings: " + code);
        }
    }

    private static void assertFunctionTypeErasure(Path root) throws Exception {
        var result = new QinCsstsCompiler().compile(root, """
                declare const phantom: string;
                export function style<T>(enabled: boolean, sample?: T): unknown {
                  type Local = boolean;
                  interface Theme { color: string }
                  const flag: Local = enabled;
                  return css { flag ? colorRed : colorBlue };
                }
                export function echo(value: number): number;
                export function echo(value: string): string;
                export function echo(value: number | string) { return value; }
                export function join({ name }: { name: string }, ...parts: string[]): string {
                  return name + parts.join(",");
                }
                export function useReceiver(this: { name: string }, suffix: string): string {
                  return this.name + suffix;
                }
                export const add = (value: number, increment: number = 2): number => (value as number) + increment;
                export const result = echo(4);
                """);
        String code = result.code();
        if (!code.contains("function style(enabled, sample)") || !code.contains("function join({name}, ...parts)")
                || !code.contains("function useReceiver(suffix)") || !code.contains("(value, increment = 2) =>")
                || !code.contains("function echo(value)") || code.indexOf("function echo(") != code.lastIndexOf("function echo(")
                || !code.contains("flag ? csstsAtom.colorRed : csstsAtom.colorBlue")
                || code.contains("phantom") || code.contains("type Local") || code.contains("interface Theme")
                || code.contains("as number") || code.contains("Unsupported:")) {
            throw new AssertionError("CSSTS emitted TypeScript or lost function behavior: " + code);
        }
        String regeneratedTypeScript = new SlimeGenerator().generate(new QinParserFacade()
                .parseProgram("export function identity<T>(value: T): T { return value; }"));
        if (!regeneratedTypeScript.contains("function identity<T>(value: T): T")) {
            throw new AssertionError("TypeScript regeneration lost its signature: " + regeneratedTypeScript);
        }
    }

}

