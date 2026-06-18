package com.qin.runtime.core;

import java.nio.file.Path;

public final class QinGeneratedTsSlimeCsstsCompilerSmokeTestMain {
    private QinGeneratedTsSlimeCsstsCompilerSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path cwd = Path.of("").toAbsolutePath().normalize();
        Path workspace = "qin".equals(cwd.getFileName().toString()) ? cwd.getParent() : cwd;
        Path root = workspace.resolve("qin-ovs-cssts-generated-ts-slime-demo").normalize();
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
                || !result.rawCode().contains("function increment()")) {
            throw new IllegalStateException("Generated TS Slime CSSTS output lost declarations:\n" + result.rawCode());
        }
        System.out.println("QinGeneratedTsSlimeCsstsCompilerSmokeTestMain OK");
    }
}
