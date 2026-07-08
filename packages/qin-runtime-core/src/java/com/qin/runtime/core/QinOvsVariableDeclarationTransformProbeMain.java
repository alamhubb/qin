package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsVariableDeclarationTransformProbeMain {
    private QinOvsVariableDeclarationTransformProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { OvsCstToSlimeAstUtils } from "ovs-compiler/src/factory/OvsCstToSlimeAstUtils.ts";
                import { CssTsCstToAstUtils } from "cssts-compiler/src/factory/CssTsCstToAstUtils.ts";
                import { SlimeCstToAstUtils } from "@qin/generated-qin-parser-ts/SlimeCstToAstBridge";

                function methodOwner(obj, methodName) {
                  let proto = obj;
                  while (proto) {
                    if (Object.prototype.hasOwnProperty.call(proto, methodName)) {
                      return proto.constructor && proto.constructor.name ? proto.constructor.name : "(anonymous)";
                    }
                    proto = Object.getPrototypeOf(proto);
                  }
                  return "";
                }
                ({
                  ovsStatementOwner: methodOwner(OvsCstToSlimeAstUtils, "createStatementListItemAst"),
                  ovsVariableOwner: methodOwner(OvsCstToSlimeAstUtils, "createVariableDeclarationAst"),
                  cssVariableOwner: methodOwner(CssTsCstToAstUtils, "createVariableDeclarationAst"),
                  generatedVariableOwner: methodOwner(SlimeCstToAstUtils, "createVariableDeclarationAst"),
                  sameVariableFunction: OvsCstToSlimeAstUtils.createVariableDeclarationAst === CssTsCstToAstUtils.createVariableDeclarationAst
                });
                """, "ovs_variable_declaration_transform_probe");

        System.out.println("QinOvsVariableDeclarationTransformProbeMain " + result);
    }
}
