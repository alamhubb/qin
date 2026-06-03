package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJavaAstJsBackendEnumSmokeTestMain {
    private QinJavaAstJsBackendEnumSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                package com.example;
                import java.lang.String;
                enum SourceType {
                    SCRIPT, MODULE
                }
                class ParserMode {
                    boolean isModule(SourceType sourceType) {
                        return sourceType == SourceType.MODULE;
                    }
                }
                """);

        String generated = new QinJsBackend().compileProgram(program);
        require(generated.contains("class __QinJavaLangEnum"), "Enum runtime shim");
        require(generated.contains("class SourceType extends Enum"), "enum class inheritance");
        require(generated.contains("SourceType.SCRIPT = __qin_init_enum_value(new SourceType(), \"SCRIPT\", 0);"),
                "SCRIPT static enum value");
        require(generated.contains("SourceType.MODULE = __qin_init_enum_value(new SourceType(), \"MODULE\", 1);"),
                "MODULE static enum value");

        Path root = Files.createTempDirectory("qin-java-ast-js-backend-enum-");
        Files.writeString(root.resolve("qin.config.js"), "export default { name: \"qin-java-ast-js-backend-enum\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated
                        + "\nconst mode = new ParserMode();\n"
                        + "mode.isModule(SourceType.MODULE) + \":\""
                        + " + mode.isModule(SourceType.SCRIPT) + \":\""
                        + " + SourceType.MODULE.name() + \":\""
                        + " + SourceType.MODULE.ordinal();\n",
                "java_ast_js_backend_enum");
        if (!"true:false:MODULE:1".equals(result)) {
            throw new IllegalStateException("Expected generated Java enum runtime value, got: " + result);
        }
        System.out.println("QinJavaAstJsBackendEnumSmokeTestMain OK");
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
