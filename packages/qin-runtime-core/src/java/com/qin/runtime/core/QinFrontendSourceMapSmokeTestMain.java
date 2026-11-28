package com.qin.runtime.core;

import com.qin.lang.sema.esm.QinEsmSemanticException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Verifies mappings on the ESM delivered by the frontend service. */
public final class QinFrontendSourceMapSmokeTestMain {
    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-source-map-");
        Path app = Files.createDirectories(root.resolve("app"));
        Path entry = app.resolve("main.js");
        Files.writeString(entry, """
                import Panel from "./Panel.ovs";
                import Implicit from "./Implicit.ovs";
                import { item } from "./components.js";
                import { color, forwarded, nestedValue } from "./theme.cssts";
                console.log(Panel, Implicit, item, color, forwarded, nestedValue);
                """);
        Files.writeString(app.resolve("helper.js"), "export const value = 'hello';");
        Files.writeString(app.resolve("components.js"), "export * from './Named.ovs';");
        Path panel = app.resolve("Panel.ovs");
        String source = "import { value } from \"./helper.js\";\r\n"
                + "const marker = \"export default \";\r\n"
                + "function text(input: string): string { return input; }\r\n"
                + "export default div { \"😀\"; span { text(value) } }\r\n";
        Files.writeString(panel, source);
        Files.writeString(app.resolve("Implicit.ovs"), "const label = \"😀\";\r\ndiv { label }\r\n");
        Files.writeString(app.resolve("Named.ovs"), "export const item = span(class = css { colorRed }) { \"label\" };\r\n"
                + "export class Settings { constructor(public label: string) {} }\r\n"
                + "export const cssts = 'source';\r\n"
                + "export const $OvsHtmlTag = 'source tag';\r\n");
        Path theme = app.resolve("theme.cssts");
        String themeSource = "const text = \"😀\"; export const color = css { colorRed };\r\n"
                + "export { value as forwarded, type Swatch } from \"./helper.js\";\r\n"
                + "export const { palette: [nestedValue] } = { palette: [42] };\r\n"
                + "export type * from \"./unshipped-types.js\";\r\n";
        Files.writeString(theme, themeSource);

        var service = QinFrontendEsmService.create(root, entry);
        var repeatService = QinFrontendEsmService.create(root, entry);
        Path output = Files.createDirectory(root.resolve("verification"));
        for (String file : List.of("Panel.ovs", "Implicit.ovs", "Named.ovs", "theme.cssts")) {
            String path = "/@qin-mod/app/" + file + ".js";
            String served = service.transpileByRequestPath(path);
            if (!served.equals(service.transpileByPublicRequestPath("/app/" + file + ".js"))
                    || !served.equals(repeatService.transpileByRequestPath(path))) {
                throw new AssertionError("Inconsistent served or cached source map for " + file);
            }
            Files.writeString(output.resolve(file + ".dev.js"), served);
        }
        var mapped = QinOvsJavaCompiler.compileWithSourceMap(source, "cssts_", "", "/app/Panel.ovs");
        if (!mapped.output().equals(QinOvsJavaCompiler.compile(source, "cssts_", ""))) {
            throw new AssertionError("Source mapping changed raw ESM or style output");
        }
        service.emitProduction(output.resolve("production"));

        Files.writeString(panel, "\r\n" + source);
        if (!service.refreshChangedFrontendModules(List.of(panel))) {
            throw new AssertionError("Expected source-only edit to refresh in place");
        }
        Files.writeString(output.resolve("Panel.ovs.refreshed.js"),
                service.transpileByRequestPath("/@qin-mod/app/Panel.ovs.js"));

        Path verifier = output.resolve("verify.mjs");
        Files.writeString(verifier, """
                import assert from "node:assert/strict";
                import { readFileSync } from "node:fs";
                import { SourceMap } from "node:module";
                import { SourceTextModule } from "node:vm";

                function verify(path, sourceName, generatedText, sourceText, line, column) {
                  const code = readFileSync(new URL(path, import.meta.url), "utf8");
                  new SourceTextModule(code);
                  const encoded = code.match(/sourceMappingURL=data:application\\/json;base64,([^\\r\\n]+)/)[1];
                  const payload = JSON.parse(Buffer.from(encoded, "base64").toString("utf8"));
                  assert.equal(payload.version, 3);
                  assert.deepEqual(payload.sources, [sourceName]);
                  const sourceLines = payload.sourcesContent[0].split("\\r\\n");
                  assert.equal(sourceLines[line].slice(column, column + sourceText.length), sourceText);
                  const offset = code.indexOf(generatedText);
                  assert.ok(offset >= 0, generatedText);
                  const lines = code.slice(0, offset).split("\\n");
                  const entry = new SourceMap(payload).findEntry(lines.length - 1, lines.at(-1).length);
                  assert.equal(entry.originalSource, sourceName);
                  assert.equal(entry.originalLine, line);
                  assert.equal(entry.originalColumn, column);
                  return code;
                }

                for (const file of ["./Panel.ovs.dev.js", "./production/@qin-mod/app/Panel.ovs.js"]) {
                  const code = verify(file, "/app/Panel.ovs", "$OvsHtmlTag.span", "span", 3, 27);
                  assert.ok(code.includes('from "/@qin-mod/app/helper.js"'));
                  assert.ok(code.includes("const __qinOvsComponent ="));
                  assert.ok(code.includes('const marker = "export default ";'));
                  assert.ok(code.includes("function text(input)"));
                  assert.equal(code.includes("import.meta.hot"), file.endsWith(".dev.js"));
                  verify(file, "/app/Panel.ovs", '"😀"', '"😀"', 3, 21);
                  verify(file, "/app/Panel.ovs", "return input;", "return", 2, 39);
                }
                verify("./Panel.ovs.refreshed.js", "/app/Panel.ovs", "$OvsHtmlTag.span", "span", 4, 27);
                for (const prefix of ["./", "./production/@qin-mod/app/"]) {
                  const suffix = prefix === "./" ? ".dev.js" : ".js";
                  verify(prefix + "Implicit.ovs" + suffix, "/app/Implicit.ovs", "$OvsHtmlTag.div", "div", 1, 0);
                  const named = verify(prefix + "Named.ovs" + suffix, "/app/Named.ovs", "__qinOvsHtmlTag.span", "span", 0, 20);
                  assert.ok(named.includes("$OvsHtmlTag as __qinOvsHtmlTag"));
                  assert.ok(named.includes("cssts as __qinCssts"));
                  assert.ok(named.includes("__qinCssts.merge(csstsAtom.colorRed)"));
                  verify(prefix + "Named.ovs" + suffix, "/app/Named.ovs", "label;", "label", 1, 43);
                  const theme = verify(prefix + "theme.cssts" + suffix, "/app/theme.cssts", "colorRed", "colorRed", 0, 46);
                  assert.ok(theme.includes('export {value as forwarded} from "/@qin-mod/app/helper.js"'));
                  const body = theme.split("//# sourceMappingURL=")[0];
                  assert.ok(!body.includes("unshipped-types"));
                }
                """);
        Process node = new ProcessBuilder("node", "--experimental-vm-modules", "--no-warnings", verifier.toString())
                .inheritIO().start();
        if (node.waitFor() != 0) throw new AssertionError("Source map verification failed in " + output);

        Path otherRoot = Files.createTempDirectory("qin-frontend-binding-cache-");
        Path otherApp = Files.createDirectories(otherRoot.resolve("app"));
        Files.writeString(otherApp.resolve("theme.cssts"), themeSource);
        Files.writeString(otherApp.resolve("helper.js"), "export const other = true;");
        Path otherEntry = Files.writeString(otherApp.resolve("main.js"), "import { forwarded } from './theme.cssts';");
        assertMissingExport(otherRoot, otherEntry, "forwarded");

        Files.writeString(theme, themeSource.replace("export const color", "const color"));
        if (!service.refreshChangedFrontendModules(List.of(theme))) {
            throw new AssertionError("Expected CSSTS export edit to refresh in place");
        }
        try {
            service.transpileByRequestPath("/@qin-mod/app/theme.cssts.js");
            throw new AssertionError("Hot refresh accepted a removed CSSTS export");
        } catch (QinEsmSemanticException expected) {
            if (!hasNamedCsstsDiagnostic(expected, "color")) throw expected;
        }
        assertMissingExport(root, entry, "color");
        Files.writeString(entry, "import nonexistentDefault from './theme.cssts';");
        assertMissingExport(root, entry, "default");
        Files.writeString(entry, "import nonexistentDefault from './Named.ovs';");
        var lazyOvsService = QinFrontendEsmService.create(root, entry);
        try {
            lazyOvsService.transpileByRequestPath("/@qin-mod/app/Named.ovs.js");
            throw new AssertionError("Frontend accepted an OVS default export absent from emitted ESM");
        } catch (IllegalStateException expected) {
            if (!(expected.getCause() instanceof QinEsmSemanticException error)
                    || error.diagnostics().stream().noneMatch(diagnostic -> "ESM2003".equals(diagnostic.code()))) {
                throw expected;
            }
        }
        System.out.println("QinFrontendSourceMapSmokeTestMain passed.");
    }

    private static void assertMissingExport(Path root, Path entry, String name) throws Exception {
        try {
            QinFrontendEsmService.create(root, entry);
            throw new AssertionError("Frontend accepted missing export " + name);
        } catch (QinEsmSemanticException expected) {
            if (!hasNamedCsstsDiagnostic(expected, name)) throw expected;
        }
    }

    private static boolean hasNamedCsstsDiagnostic(QinEsmSemanticException error, String name) {
        return error.diagnostics().stream().anyMatch(diagnostic ->
                "ESM2003".equals(diagnostic.code()) && diagnostic.message().contains(name + " from ./theme.cssts"));
    }
}
