package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public final class QinGeneratedTsSlimeOvsTransformSmokeTestMain {
    private QinGeneratedTsSlimeOvsTransformSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath().getParent().resolve("qin-ovs-cssts-generated-ts-slime-demo");
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { ovsTransformBase, ovsTransformFile, vitePluginOvsTransform, OvsParser } from "ovs-compiler";

                const source = `
                import { ref } from 'vue'

                let count = ref(0)

                div(class="ovs-card") {
                  h2 { "Rendered from .ovs" }
                  p {
                    "OVS count: "
                    count.value
                  }
                  button(
                    class="ovs-button",
                    onClick() {
                      count.value++
                    }
                  ) {
                    "Increment OVS"
                  }
                }
                `;

                function valueOfProperty(target, name) {
                  const value = target ? target[name] : null;
                  return typeof value === "function" ? value.call(target) : value;
                }

                function childList(cst) {
                  if (!cst) return [];
                  const children = typeof cst.getChildren === "function" ? cst.getChildren() : cst.children;
                  return children || [];
                }

                function cstSummary(cst, depth = 0) {
                  if (!cst || depth > 3) return null;
                  const children = childList(cst);
                  const name = typeof cst.getName === "function" ? cst.getName() : cst.name;
                  const value = typeof cst.getValue === "function" ? cst.getValue() : cst.value;
                  return {
                    name,
                    value: value == null ? null : String(value),
                    childCount: children.length,
                    children: children.slice(0, 8).map(child => cstSummary(child, depth + 1))
                  };
                }

                const parser = new OvsParser(source);
                const cst = parser.Program();
                const curToken = valueOfProperty(parser, "curToken");
                const base = ovsTransformBase(source);
                const file = ovsTransformFile(source);
                const plugin = vitePluginOvsTransform(source, { globalStyles: new Set() });
                ({
                  cst: cstSummary(cst),
                  consumedTokens: valueOfProperty(parser, "currentTokenIndex"),
                  currentTokenName: curToken ? (typeof curToken.tokenName === "function" ? curToken.tokenName() : curToken.tokenName) : "EOF",
                  currentTokenValue: curToken ? (typeof curToken.value === "function" ? curToken.value() : curToken.tokenValue || curToken.value) : "",
                  tokenCount: base.tokens ? base.tokens.length : -1,
                  hasAst: !!base.ast,
                  baseAstType: base.ast ? base.ast.type : null,
                  baseBodyLength: base.ast && base.ast.body ? base.ast.body.length : -1,
                  fileHasAst: !!file.ast,
                  fileAstType: file.ast ? file.ast.type : null,
                  fileBodyLength: file.ast && file.ast.body ? file.ast.body.length : -1,
                  fileFirstType: file.ast && file.ast.body && file.ast.body[0] ? file.ast.body[0].type : null,
                  pluginKeys: plugin ? Object.keys(plugin).join(",") : "",
                  codeLength: plugin && plugin.code ? plugin.code.length : 0,
                  codePreview: plugin && plugin.code ? plugin.code.slice(0, 120) : "",
                  hasIncrement: !!(plugin && plugin.code && plugin.code.includes("Increment OVS"))
                });
                """, "generated_ts_slime_ovs_transform");

        if (!(result instanceof Map<?, ?> map)) {
            throw new IllegalStateException("Expected object result, got: " + result);
        }
        Object tokenCount = map.get("tokenCount");
        if (!(tokenCount instanceof Number count) || count.intValue() <= 0
                || !Boolean.TRUE.equals(map.get("hasAst"))
                || !(map.get("codeLength") instanceof Number codeLength)
                || codeLength.intValue() <= 0
                || !Boolean.TRUE.equals(map.get("hasIncrement"))) {
            throw new IllegalStateException("Expected generated TS Slime OVS transform output, got: "
                    + QinObjectJsonEncoder.toJson(map));
        }
        System.out.println("QinGeneratedTsSlimeOvsTransformSmokeTestMain OK " + QinObjectJsonEncoder.toJson(map));
    }
}
