package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinOvsCompilerCsstsOptionsSmokeTestMain {
    private QinOvsCompilerCsstsOptionsSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-ovs-cssts-options-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  plugins: [
                    {
                      cssts: {
                        classPrefix: "unit",
                        pseudoClassConfig: { selected: { outline: "2px solid red" } },
                        classGroup: { selectable: ["selected", "colorGreen"] }
                      }
                    }
                  ]
                }
                """, StandardCharsets.UTF_8);
        Path appDir = root.resolve("app");
        Files.createDirectories(appDir);
        Path file = appDir.resolve("Options.ovs");
        String source = """
                div(class = css { colorRed }) {
                  "options"
                }
                """;
        Files.writeString(file, source, StandardCharsets.UTF_8);

        QinOvsCompiler compiler = new QinOvsCompiler();
        QinOvsCompiler.QinOvsCompileResult result = compiler.compile(root, file, source);
        String combined = result.code() + "\n" + result.css() + "\n" + result.atomModule();
        if (!combined.contains("unit_color_red")) {
            throw new IllegalStateException("Expected OVS standard transform to honor CSSTS classPrefix, got:\n"
                    + combined);
        }
        if (combined.contains("cssts_color_red")) {
            throw new IllegalStateException("OVS standard transform leaked default CSSTS prefix:\n" + combined);
        }

        String interactive = "div(class = css { selected, selectable }) { \"options\" }";
        QinOvsCompiler.QinOvsCompileResult configured = compiler.compile(root, file, interactive);
        if (!configured.code().contains("csstsAtom.selected")
                || !configured.atomModule().contains("unit_selected")
                || !configured.atomModule().contains("unit_selectable")
                || !configured.css().contains(".unit_selected:selected { outline: 2px solid red; }")
                || !configured.css().contains(".unit_selectable { color: green; }")) {
            throw new IllegalStateException("OVS standard transform lost custom CSSTS pseudo/group config:\n"
                    + configured.code() + "\n" + configured.css() + "\n" + configured.atomModule());
        }

        Path first = appDir.resolve("BatchFirst.ovs");
        Path second = appDir.resolve("BatchSecond.ovs");
        String marker = "\n/* " + root.getFileName() + " */";
        var batchInputs = Map.of(first, interactive + marker, second, source + marker);
        var batch = compiler.compileAll(root, batchInputs);
        if (!batch.get(first).equals(configured) || !batch.get(second).equals(result)) {
            throw new AssertionError("OVS single and batch compilation disagree on configured styles");
        }

        var namedPseudo = compiler.compile(root, file, """
                const actionButton$$hover$$selected = css { colorRed };
                div(class = actionButton$$hover$$selected) { "options" }
                """);
        if (!namedPseudo.code().contains("cssts.merge(csstsAtom.actionButton$$hover$$selected, csstsAtom.colorRed)")
                || !namedPseudo.code().contains("class: actionButton$$hover$$selected")
                || !namedPseudo.atomModule().contains("actionButton$$hover$$selected: { 'unit_action-button': null }")
                || !namedPseudo.css().contains(".unit_action-button:hover { filter: brightness(1.15); }")
                || !namedPseudo.css().contains(".unit_action-button:selected { outline: 2px solid red; }")) {
            throw new AssertionError("OVS did not pass the generated pseudo class to the element: " + namedPseudo);
        }

        Files.writeString(root.resolve("qin.config.js"), "export default { plugins: [{ cssts: { classPrefix: 'changed' } }] };\n",
                StandardCharsets.UTF_8);
        QinOvsCompiler.QinOvsCompileResult afterConfigChange = compiler.compile(root, file, source);
        if (!afterConfigChange.atomModule().contains("changed_color_red")
                || afterConfigChange.atomModule().contains("unit_color_red")) {
            throw new IllegalStateException("OVS memory cache did not invalidate after CSSTS config changes:\n"
                    + afterConfigChange.atomModule());
        }
        var updatedBatch = compiler.compileAll(root, batchInputs);
        if (!updatedBatch.get(second).equals(afterConfigChange)
                || updatedBatch.get(first).atomModule().contains("unit_selected")
                || updatedBatch.get(first).code().contains("csstsAtom.selected")) {
            throw new AssertionError("OVS batch compilation retained the old style configuration");
        }

        Files.writeString(root.resolve("qin.config.js"), """
                // classPrefix: 'comment-'
                const note = "classPrefix: 'string-'"
                export default { plugins: [
                  { otherPlugin: { classPrefix: 'other-' } },
                  { cssts: { classPrefix: 'actual-' } }
                ] }
                """, StandardCharsets.UTF_8);
        QinOvsCompiler.QinOvsCompileResult lexicalConfig = compiler.compile(root, file, source);
        if (!lexicalConfig.atomModule().contains("actual-color_red")
                || lexicalConfig.atomModule().contains("comment-color_red")
                || lexicalConfig.atomModule().contains("string-color_red")
                || lexicalConfig.atomModule().contains("other-color_red")) {
            throw new IllegalStateException("OVS config parsing must read classPrefix from JavaScript properties, not comments or strings:\n"
                    + lexicalConfig.atomModule());
        }

        System.out.println("QinOvsCompilerCsstsOptionsSmokeTestMain OK");
    }
}
