package com.qin.runtime.core;

import com.slime.parser.SlimeJavascriptParser;
import com.subhuti.struct.SubhutiCst;

import java.lang.reflect.Method;

public final class QinJsStringRawCstDiagnosticMain {
    private QinJsStringRawCstDiagnosticMain() {
    }

    public static void main(String[] args) throws Exception {
        String source = """
                const raw = String.raw`[\\p{ID_Start}$_]|\\\\u[0-9a-fA-F]{4}`;
                raw;
                """;
        Object parser = createStaticEnhancedParser(source.trim());
        Method program = parser.getClass().getMethod("Program", SlimeJavascriptParser.SourceType.class);
        SubhutiCst cst = (SubhutiCst) program.invoke(parser, SlimeJavascriptParser.SourceType.MODULE);
        if (cst == null) {
            Method getCst = parser.getClass().getMethod("getCst");
            cst = (SubhutiCst) getCst.invoke(parser);
        }
        printTemplateNodes(cst, 0);
    }

    private static Object createStaticEnhancedParser(String source) {
        try {
            Class<?> parserClass = Class.forName("com.slime.parser.SlimeParserStaticEnhanced");
            Method create = parserClass.getMethod("create", String.class);
            return create.invoke(null, source);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "SlimeParserStaticEnhanced is a generated build artifact. "
                            + "Run slime-parser/compile.bat before this diagnostic main.",
                    e);
        }
    }

    private static void printTemplateNodes(SubhutiCst cst, int depth) {
        if (cst == null) {
            return;
        }
        String name = cst.getName();
        if (name != null && name.contains("Template")) {
            String value = cst.getValue();
            System.out.println(" ".repeat(depth * 2) + name + " value=" + printable(value));
        }
        if (cst.getChildren() == null) {
            return;
        }
        for (SubhutiCst child : cst.getChildren()) {
            printTemplateNodes(child, depth + 1);
        }
    }

    private static String printable(String value) {
        if (value == null) {
            return "<null>";
        }
        return "\"" + value.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }
}
