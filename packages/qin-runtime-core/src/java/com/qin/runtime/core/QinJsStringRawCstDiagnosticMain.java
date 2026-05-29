package com.qin.runtime.core;

import com.slime.parser.SlimeJavascriptParser;
import com.subhuti.parser.SubhutiParser;
import com.subhuti.struct.SubhutiCst;

public final class QinJsStringRawCstDiagnosticMain {
    private QinJsStringRawCstDiagnosticMain() {
    }

    public static void main(String[] args) {
        String source = """
                const raw = String.raw`[\\p{ID_Start}$_]|\\\\u[0-9a-fA-F]{4}`;
                raw;
                """;
        SlimeJavascriptParser parser = SubhutiParser.create(SlimeJavascriptParser.class, source.trim());
        SubhutiCst cst = parser.Program(SlimeJavascriptParser.SourceType.MODULE);
        if (cst == null) {
            cst = parser.getCst();
        }
        printTemplateNodes(cst, 0);
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
