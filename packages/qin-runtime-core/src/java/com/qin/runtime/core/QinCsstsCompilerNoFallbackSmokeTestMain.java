package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Guards the public CSSTS compile entry and Java artifact generation. */
public final class QinCsstsCompilerNoFallbackSmokeTestMain {
    private QinCsstsCompilerNoFallbackSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path packageRoot = Path.of("").toAbsolutePath().normalize();
        if (!Files.isDirectory(packageRoot.resolve("src/java/com/qin/runtime/core"))) {
            packageRoot = packageRoot.resolve("qin/packages/qin-runtime-core");
        }
        Path sources = packageRoot.resolve("src/java/com/qin/runtime/core");
        assertJavaOnlySource(sources.resolve("QinCsstsCompiler.java"));
        assertJavaOnlySource(sources.resolve("QinCsstsJavaCompiler.java"));

        Path project = Files.createTempDirectory("qin-cssts-java-only-");
        var compiler = new QinCsstsCompiler();
        var result = compiler.compile(project, "export const style = css { colorRed };");
        if (!result.hasStyles() || !result.code().contains("csstsAtom.colorRed")
                || !result.css().contains("color: red") || !result.atomModule().contains("colorRed:")) {
            throw new AssertionError("CSSTS Java compile entry did not generate the expected artifacts");
        }

        assertDefaultCssOrder(result.css());
        String incomplete = "const style = css {\r\n  colorRed,";
        assertRejected(compiler, project, incomplete, "offset=" + incomplete.length() + ", line=2, column=12");
        assertRejected(compiler, project, "css colorRed");
        assertRejected(compiler, project, "css buttonBase = { colorRed }");
        assertRejected(compiler, project, "export default div { \"OVS render\" }");
        var ordinary = compiler.compile(project, "const css = 1; export const total = css + 2;");
        if (ordinary.hasStyles() || !ordinary.code().contains("total = css + 2")) {
            throw new AssertionError("CSSTS changed an ordinary JavaScript identifier: " + ordinary.code());
        }
        System.out.println("QinCsstsCompilerNoFallbackSmokeTestMain OK");
    }

    private static void assertDefaultCssOrder(String css) {
        int previous = -1;
        for (String rule : List.of(".cssts_hover:hover", ".cssts_active:active", ".cssts_focus:focus",
                ".cssts_disabled:disabled { opacity: 0.5; cursor: not-allowed; filter: grayscale(0.2); }",
                ".cssts_click:hover", ".cssts_ddClick:hover")) {
            int offset = css.indexOf(rule);
            if (offset <= previous) throw new AssertionError("CSSTS CSS declarations are out of order: " + rule);
            previous = offset;
        }
    }

    private static void assertRejected(QinCsstsCompiler compiler, Path project, String source) throws Exception {
        assertRejected(compiler, project, source, "");
    }

    private static void assertRejected(QinCsstsCompiler compiler, Path project, String source, String location)
            throws Exception {
        try {
            compiler.compile(project, source);
            throw new AssertionError("The CSSTS compile entry accepted invalid source: " + source);
        } catch (IllegalArgumentException error) {
            if (!error.getMessage().startsWith("CSSTS Java parse failed:")) throw error;
            if (!error.getMessage().contains(location)) {
                throw new AssertionError("CSSTS diagnostic has an incorrect source location", error);
            }
        }
    }

    private static void assertJavaOnlySource(Path source) throws Exception {
        String text = Files.readString(source, StandardCharsets.UTF_8);
        for (String forbidden : List.of(
                "QinJsPackageRunner",
                "QinInMemoryJvmRunner",
                "generated-runtime-adapter",
                "InterpretedFunction",
                "org.graalvm.polyglot",
                "ScriptEngine",
                "__qin_extract_atoms__",
                "__qin_fallback_atoms__",
                "mergePattern = /cssts")) {
            if (text.contains(forbidden)) {
                throw new IllegalStateException(source.getFileName() + " uses an alternate compilation path: " + forbidden);
            }
        }
    }
}
