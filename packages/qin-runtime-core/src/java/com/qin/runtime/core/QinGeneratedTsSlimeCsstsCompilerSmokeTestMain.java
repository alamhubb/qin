package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinGeneratedTsSlimeCsstsCompilerSmokeTestMain {
    private QinGeneratedTsSlimeCsstsCompilerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = QinOvsCsstsDemoPaths.generatedTsSlimeDemoRoot();
        String source = """
                import { ref } from 'vue'

                const count = ref(0)
                const title = 'Rendered from Vue lang=cssts'
                const panelStyle = css { displayFlex, colorBlue }

                function increment() {
                  count.value++
                }
                """;

        QinCsstsCompiler.QinCsstsCompileResult result = new QinCsstsCompiler().compile(root, source);
        if (!result.rawCode().contains("const count = ref(0)")
                || !result.rawCode().contains("const title =")
                || !result.rawCode().contains("const panelStyle = cssts.merge(")
                || !result.rawCode().contains("function increment()")
                || !result.rawCode().contains("count.value++")) {
            throw new IllegalStateException("Generated TS Slime CSSTS output lost declarations:\n" + result.rawCode());
        }
        System.out.println("QinGeneratedTsSlimeCsstsCompilerSmokeTestMain OK");
    }
}
