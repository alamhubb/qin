package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Files;
import java.nio.file.Path;

public final class QinGeneratedJavaWrapperTailSmokeTestMain {
    private static final String GAP_CLASS = "com_subhuti_struct_SubhutiTokenContextConstraint";

    private QinGeneratedJavaWrapperTailSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path bundle = findQinRoot().resolve(".qin/generated/slime-parser/slime-parser.bundle.js");
        String generated = Files.readString(bundle);
        assertContains("bundle-only", generated);
        assertContains("with-tail-consts", generated + """

                const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                const SourceType = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeJavascriptParser$SourceType"];
                const JavaScriptTokens = globalThis.__qinJavaProjectExports["com.slime.token.JavaScriptTokens"];
                const tokenCount = JavaScriptTokens.getTokens().size();
                """);
        assertContains("with-tail-try", generated + """

                const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                let parser;
                let constructError = null;
                try {
                  parser = new SlimeParser("const answer = 42;");
                } catch (error) {
                  constructError = error;
                }
                """);
        assertContains("with-tail-ternary", generated + """

                let constructError = null;
                let tokenCount = 0;
                constructError != null
                  ? "construct-error;tokens=" + tokenCount + ";error=" + constructError
                  : (() => {
                      const programValue = null;
                      const parseValue = null;
                      return "programNull=" + (programValue == null)
                        + ";parseNull=" + (parseValue == null);
                    })();
                """);
        assertContains("with-exact-parser-tail", generated + """

                const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                const SourceType = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeJavascriptParser$SourceType"];
                const JavaScriptTokens = globalThis.__qinJavaProjectExports["com.slime.token.JavaScriptTokens"];
                if (typeof SlimeParser !== "function") {
                  throw new Error("Generated SlimeParser export is missing");
                }
                const tokenCount = JavaScriptTokens.getTokens().size();
                let parser;
                let constructError = null;
                try {
                  parser = new SlimeParser("const answer = 42;");
                } catch (error) {
                  constructError = error;
                }
                constructError != null
                  ? "construct-error;tokens=" + tokenCount + ";error=" + constructError
                  : (() => {
                      const programValue = parser.Program(SourceType.MODULE);
                      const parseValue = parser.parse();
                      return "slime=" + typeof SlimeParser
                        + ";tokens=" + tokenCount
                        + ";sourceType=" + typeof SourceType
                        + ";module=" + SourceType.MODULE
                        + ";index=" + parser.getCurrentIndex()
                        + ";fail=" + parser.isParserFail()
                        + ";error=" + parser.getErrorInfo()
                        + ";parsed=" + parser.getParsedTokens().size()
                        + ";unparsed=" + parser.getUnparsedTokens().size()
                        + ";programNull=" + (programValue == null)
                        + ";parseNull=" + (parseValue == null);
                    })();
                """);
        System.out.println("QinGeneratedJavaWrapperTailSmokeTestMain OK");
    }

    private static void assertContains(String label, String source) {
        QinIrProgram program = new QinFrontendLowerer().lowerSource(source);
        boolean found = program.declarations().stream()
                .anyMatch(declaration -> GAP_CLASS.equals(declaration.name()));
        if (!found) {
            throw new IllegalStateException("Missing " + GAP_CLASS + " in " + label);
        }
    }

    private static Path findQinRoot() {
        Path current = Path.of("").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("qin.bat"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Unable to locate qin root from " + Path.of("").toAbsolutePath());
    }
}
