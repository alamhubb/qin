package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinOvsImportExportTransformProbeMain {
    private QinOvsImportExportTransformProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        String source = """
                import { ref } from "vue"

                export const ImportedGrid = () => {
                  const count = ref(1)
                  return section(class = "summary-grid") {
                    span { String(count.value) }
                  }
                }
                """;
        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { ovsTransformFile } from "ovs-compiler";

                const source = %s;
                function get(value, key) {
                  if (value == null) return null;
                  const raw = value[key];
                  return typeof raw === "function" ? raw.call(value) : raw;
                }
                function arr(value) {
                  if (value == null) return [];
                  if (Array.isArray(value)) return value;
                  if (typeof value.size === "function" && typeof value.get === "function") {
                    const out = [];
                    for (let i = 0; i < value.size(); i++) out.push(value.get(i));
                    return out;
                  }
                  if (typeof value.length === "number") return Array.from(value);
                  return [];
                }
                function str(value) {
                  if (value == null) return "";
                  if (typeof value === "string") return value;
                  const name = get(value, "name");
                  if (typeof name === "string") return name;
                  const type = get(value, "type");
                  if (typeof type === "string") return type;
                  const enumName = get(type, "name");
                  if (typeof enumName === "string") return enumName;
                  return String(value);
                }
                const transformed = ovsTransformFile(source);
                const ast = transformed.ast;
                const body = arr(get(ast, "body"));
                const exportNode = body.find(item => str(get(item, "type")) === "ExportNamedDeclaration") || body[body.length - 1];
                const declaration = get(exportNode, "declaration");
                const declarations = arr(get(declaration, "declarations"));
                const first = declarations[0];
                const id = get(first, "id");
                const init = get(first, "init");
                const initBody = get(init, "body");
                const blockBody = arr(get(initBody, "body"));
                "bodyCount=" + body.length
                  + ";bodyTypes=" + body.map(item => str(get(item, "type"))).join(",")
                  + ";exportType=" + str(get(exportNode, "type"))
                  + ";exportKeys=" + Object.keys(exportNode || {}).join(",")
                  + ";declarationType=" + str(get(declaration, "type"))
                  + ";declarationKeys=" + Object.keys(declaration || {}).join(",")
                  + ";declarationCount=" + declarations.length
                  + ";idName=" + str(get(id, "name"))
                  + ";initType=" + str(get(init, "type"))
                  + ";initBodyType=" + str(get(initBody, "type"))
                  + ";blockBodyCount=" + blockBody.length
                  + ";blockBodyTypes=" + blockBody.map(item => str(get(item, "type"))).join(",");
                """.formatted(QinJsPackageRunner.renderJsLiteral(source)), "ovs_import_export_transform_probe");

        System.out.println("QinOvsImportExportTransformProbeMain " + result);
    }
}
