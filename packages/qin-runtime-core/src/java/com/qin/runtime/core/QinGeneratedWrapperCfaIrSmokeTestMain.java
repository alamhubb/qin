package com.qin.runtime.core;

import com.qin.lang.pipeline.cfa.QinCfaCompileRequest;
import com.qin.lang.pipeline.cfa.QinSlimeCfaCompiler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class QinGeneratedWrapperCfaIrSmokeTestMain {
    private QinGeneratedWrapperCfaIrSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path wrapper = latestWrapper();
        Object result = new QinSlimeCfaCompiler().compile(new QinCfaCompileRequest(
                wrapper,
                wrapper.getParent().getParent().getParent(),
                "com.qin.runtime.generated.npm.WrapperProbe",
                false));
        com.qin.lang.pipeline.cfa.QinCfaCompileResult compileResult =
                (com.qin.lang.pipeline.cfa.QinCfaCompileResult) result;
        Set<String> names = compileResult.irBeforeLowering().declarations().stream()
                .map(declaration -> declaration.name())
                .collect(Collectors.toSet());
        require(names.contains("com_subhuti_struct_SubhutiTokenContextConstraint"), "context constraint declaration");
        require(names.contains("com_subhuti_struct_SubhutiTokenLookahead"), "token lookahead declaration");
        System.out.println("Wrapper source: " + wrapper);
        System.out.println("QinGeneratedWrapperCfaIrSmokeTestMain OK");
    }

    private static Path latestWrapper() throws Exception {
        Path tempRoot = Path.of(System.getProperty("java.io.tmpdir"));
        try (Stream<Path> stream = Files.walk(tempRoot, 6)) {
            return stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().equals("invoke-java_project_slime_parser_js_parse-1.js"))
                    .max(Comparator.comparingLong(path -> path.toFile().lastModified()))
                    .orElseThrow();
        }
    }

    private static void require(boolean condition, String label) {
        if (!condition) {
            throw new IllegalStateException("Expected " + label);
        }
    }
}
