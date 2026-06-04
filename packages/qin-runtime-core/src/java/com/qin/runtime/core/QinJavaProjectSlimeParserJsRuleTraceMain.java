package com.qin.runtime.core;

import com.qin.lang.runtime.JavaEsmGlobal;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class QinJavaProjectSlimeParserJsRuleTraceMain {
    private QinJavaProjectSlimeParserJsRuleTraceMain() {
    }

    public static void main(String[] args) throws Exception {
        Path qinRoot = Path.of("").toAbsolutePath();
        Path workspaceRoot = qinRoot.getParent();
        List<Path> sourceRoots = List.of(
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-parser")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("slime-token")
                        .resolve("src").resolve("main").resolve("java"),
                workspaceRoot.resolve("slime").resolve("java-slime").resolve("subhuti-java")
                        .resolve("src").resolve("main").resolve("java"));

        Path outputFile = qinRoot.resolve(".qin")
                .resolve("generated")
                .resolve("slime-parser")
                .resolve("slime-parser.bundle.js");
        String generated = new QinJavaProjectJsCompiler()
                .compileSuperclassClosure(sourceRoots, "com.slime.parser.SlimeParser", outputFile);

        Path root = Files.createTempDirectory("qin-java-slime-parser-js-rule-trace-");
        Files.writeString(root.resolve("qin.config.js"),
                "export default { name: \"qin-java-slime-parser-js-rule-trace\" };\n",
                StandardCharsets.UTF_8);
        JavaEsmGlobal.setInterpretedCallCountLimit(1_000_000);
        try {
            Object result = new QinJsPackageRunner().runModuleSource(
                    root,
                    generated
                            + """

                        const SlimeParser = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeParser"];
                        const SourceType = globalThis.__qinJavaProjectExports["com.slime.parser.SlimeJavascriptParser$SourceType"];
                        const TokenConsumer = globalThis.__qinJavaProjectExports["com.slime.parser.consumer.SlimeJavascriptTokenConsumer"];
                        const parser = new SlimeParser("const answer = 42;");
                        let steps = 0;
                        const recent = [];
                        parser.__qin_field__debugger = {
                          resetForNewParse(value) {},
                          onRuleEnter(ruleName, tokenIndex) {
                            steps++;
                            recent.push(ruleName + "@" + tokenIndex + "#" + parser.getCurrentIndex());
                            if (recent.length > 50) recent.shift();
                            if (steps <= 20 || steps % 10 === 0) {
                              console.log("[rule-trace] steps=" + steps
                                + "; index=" + parser.getCurrentIndex()
                                + "; tokenIndex=" + tokenIndex
                                + "; fail=" + parser.isParserFail()
                                + "; methodCache=" + TokenConsumer.__qin_field_METHOD_CACHE.size()
                                + "; recent=" + recent.join(" > "));
                            }
                            if (steps > 500) {
                              throw new Error("rule trace limit; steps=" + steps
                                + "; index=" + parser.getCurrentIndex()
                                + "; tokenIndex=" + tokenIndex
                                + "; fail=" + parser.isParserFail()
                                + "; methodCache=" + TokenConsumer.__qin_field_METHOD_CACHE.size()
                                + "; recent=" + recent.join(" > "));
                            }
                            return steps;
                          },
                          onRuleExit(ruleName, cacheHit, startTime) {},
                          setCst(cst) {},
                          autoOutput() {}
                        };
                        console.log("[rule-trace] debugger-installed=" + (parser.__qin_field__debugger != null)
                          + "; cacheEnabled=" + parser.__qin_field_enableMemoization
                          + "; index=" + parser.getCurrentIndex()
                          + "; parsed=" + parser.getParsedTokens().size()
                          + "; unparsed=" + parser.getUnparsedTokens().size());
                        const originalTryAndRestore = parser.tryAndRestore;
                        let tries = 0;
                        parser.tryAndRestore = __qin_java_functional((fn) => {
                          tries++;
                          const before = parser.getCurrentIndex();
                          if (tries <= 40 || tries % 20 === 0) {
                            console.log("[try-trace] enter=" + tries
                              + "; before=" + before
                              + "; fail=" + parser.isParserFail()
                              + "; recentRules=" + recent.join(" > "));
                          }
                          const value = originalTryAndRestore(fn);
                          if (tries <= 40 || tries % 20 === 0) {
                            console.log("[try-trace] exit=" + tries
                              + "; before=" + before
                              + "; after=" + parser.getCurrentIndex()
                              + "; result=" + value
                              + "; fail=" + parser.isParserFail());
                          }
                          return value;
                        });
                        parser.Program(SourceType.MODULE);
                        "completed;steps=" + steps + ";tries=" + tries + ";index=" + parser.getCurrentIndex();
                        """,
                    "java_project_slime_parser_js_rule_trace");
            System.out.println(result);
        } finally {
            JavaEsmGlobal.clearInterpretedCallCountLimit();
        }
    }
}
