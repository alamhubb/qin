package com.qin.runtime.core;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinSubhutiNativePositionInstanceofSmokeTestMain {
    private QinSubhutiNativePositionInstanceofSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        if (!Files.isRegularFile(root.resolve("qin.config.js"))) {
            throw new IllegalStateException("Expected generated TS Slime demo qin.config.js at " + root);
        }

        Object result = new QinJsPackageRunner().runModuleSource(root, """
                import { OvsParser } from "ovs-compiler/src/parser/OvsParser.ts";
                import { __qin_binary__, __qin_instanceof__, __qin_java_class_info__ } from "@qin/java-sdk-js";
                import { com_subhuti_struct_SubhutiPosition as SubhutiPosition } from "@qin/generated-qin-parser-ts/com/subhuti/struct/SubhutiPosition.ts";
                import { com_subhuti_struct_SubhutiSourceLocation as SubhutiSourceLocation } from "@qin/generated-qin-parser-ts/com/subhuti/struct/SubhutiSourceLocation.ts";

                const parser = new OvsParser("span { \\"ok\\" }");
                const token = parser.LA(1);
                const start = token.startPosition();
                const end = token.endPosition();
                const location = SubhutiSourceLocation.of(start, end);
                const positionClassInfo = __qin_java_class_info__(SubhutiPosition, { name: "com.subhuti.struct.SubhutiPosition" });
                ({
                  token: token.tokenName(),
                  binaryInstanceOf: __qin_binary__("instanceof", start, SubhutiPosition),
                  binaryClassInfoInstanceOf: __qin_binary__("instanceof", start, positionClassInfo),
                  plainObjectClassInfoInstanceOf: __qin_binary__("instanceof", {}, positionClassInfo),
                  objectBuiltinInstanceOf: __qin_instanceof__({}, Object),
                  nullObjectBuiltinInstanceOf: __qin_instanceof__(null, Object),
                  startLine: start.getLine(),
                  endIndex: end.getIndex(),
                  locationText: String(location)
                });
                """, "subhuti_native_position_instanceof_smoke");

        String resultText = String.valueOf(result);
        if (!resultText.contains("binaryClassInfoInstanceOf=true")
                || !resultText.contains("plainObjectClassInfoInstanceOf=false")
                || !resultText.contains("objectBuiltinInstanceOf=true")
                || !resultText.contains("nullObjectBuiltinInstanceOf=false")) {
            throw new IllegalStateException("Qin class-info instanceof bridge failed: " + resultText);
        }
        System.out.println("QinSubhutiNativePositionInstanceofSmokeTestMain " + result);
    }
}
