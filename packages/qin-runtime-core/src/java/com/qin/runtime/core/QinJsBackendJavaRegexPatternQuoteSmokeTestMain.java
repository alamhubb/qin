package com.qin.runtime.core;

import com.qin.lang.backend.js.QinJsBackend;
import com.qin.lang.frontend.adapter.QinJavaAstIrLowerer;
import com.qin.lang.ir.QinIrProgram;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinJsBackendJavaRegexPatternQuoteSmokeTestMain {
    private QinJsBackendJavaRegexPatternQuoteSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        QinIrProgram program = new QinJavaAstIrLowerer().lowerSource("""
                import java.util.regex.Pattern;

                class RegexQuoteDemo {
                    boolean ok() {
                        return Pattern.compile(Pattern.quote("(")).matcher("(").lookingAt();
                    }
                }
                """);
        String generated = new QinJsBackend().compileProgram(program);
        if (!generated.contains("replace(/\\\\Q(")) {
            int index = generated.indexOf("replace(/");
            String preview = index < 0 ? generated.substring(0, Math.min(200, generated.length()))
                    : generated.substring(index, Math.min(generated.length(), index + 80));
            throw new IllegalStateException("Expected generated Java Pattern adapter to match Java quote blocks, got: "
                    + preview);
        }
        Path root = Files.createTempDirectory("qin-js-backend-java-regex-pattern-quote-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-js-backend-java-regex-pattern-quote\" };\n",
                StandardCharsets.UTF_8);
        Object result = new QinJsPackageRunner().runModuleSource(
                root,
                generated + "\nnew RegexQuoteDemo().ok();\n",
                "js_backend_java_regex_pattern_quote");
        if (!Boolean.TRUE.equals(result)) {
            throw new IllegalStateException("Expected quoted Pattern to match literal parenthesis, got: " + result);
        }
        System.out.println("QinJsBackendJavaRegexPatternQuoteSmokeTestMain OK");
    }
}
