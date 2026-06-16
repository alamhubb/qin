package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinCsstsCompilerPanelSmokeTestMain {
    private QinCsstsCompilerPanelSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-cssts-panel-");
        Files.writeString(root.resolve("qin.config.js"), """
                export default {
                  name: "qin-cssts-panel",
                  dependencies: {
                    "cssts-compiler": "0.2.87",
                    "cssts-ts": "0.2.87"
                  }
                }
                """, StandardCharsets.UTF_8);
        String source = """
                const cardStyle = css {
                  displayFlex,
                  flexDirectionColumn,
                  gap12px,
                  padding20px,
                  borderStyleSolid,
                  borderWidth1px,
                  borderColorLightgray,
                  borderRadius8px,
                  backgroundColorWhite
                }
                const headingStyle = css { fontSize20px, fontWeight700, colorBlue }
                const textStyle = css { colorGray }
                """;
        QinCsstsCompiler.QinCsstsCompileResult result = new QinCsstsCompiler().compile(root, source);
        if (!result.code().contains("cssts.merge(")
                || !result.atomModule().contains("displayFlex")
                || !result.atomModule().contains("colorBlue")) {
            throw new IllegalStateException("Expected panel CSSTS atoms, got:\n" + result);
        }
        if (!result.atomModule().contains("'cssts_padding_20px'")
                || !result.atomModule().contains("'cssts_background-color_white'")
                || result.atomModule().contains("'cssts_padding20px'")
                || result.atomModule().contains("'cssts_background_color_white'")) {
            throw new IllegalStateException("Expected atom classes to match generated CSS selectors:\n"
                    + result.atomModule() + "\nCSS:\n" + result.css());
        }
        System.out.println("QinCsstsCompilerPanelSmokeTestMain OK");
    }
}
