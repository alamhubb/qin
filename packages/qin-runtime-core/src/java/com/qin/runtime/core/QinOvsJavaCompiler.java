package com.qin.runtime.core;

import com.qin.parser.OvsParser;
import com.qin.parser.QinOvsCstToAst;
import com.slime.ast.nodes.misc.Program;
import com.slime.generator.SlimeGenerator;
import com.slime.parser.SlimeParser;
import com.subhuti.struct.SubhutiCst;

import java.util.List;

/** Java-native OVS/CSSTS transformation pipeline. */
public final class QinOvsJavaCompiler {
    private static final String DEFAULT_CLASS_PREFIX = "cssts_";

    private QinOvsJavaCompiler() {
    }

    public static QinOvsCompiler.QinOvsCompileResult compile(String source) {
        return compile(source, DEFAULT_CLASS_PREFIX, "");
    }

    public static QinOvsCompiler.QinOvsCompileResult compile(String source, String classPrefix) {
        return compile(source, classPrefix, "");
    }

    public static QinOvsCompiler.QinOvsCompileResult compile(String source, String classPrefix, String configSource) {
        String input = source == null ? "" : source;
        QinPhaseTimer profile = QinPhaseTimer.start("java-ovs-transform");
        OvsParser parser = new OvsParser(input);
        SubhutiCst cst = parser.Program(SlimeParser.SourceType.MODULE);
        profile.checkpoint("parse source");
        if (parser.isParserFail() || cst == null || !parser.isAtEnd()) {
            throw new IllegalArgumentException("OVS Java parse failed: " + parser.getDiagnostics());
        }

        QinOvsCstToAst converter = new QinOvsCstToAst();
        QinCsstsJavaCompiler.CsstsOptions options = QinCsstsJavaCompiler.parseOptions(configSource);
        converter.configureCsstsAtoms(options.pseudoNames(), options.groupNames());
        Program ast = converter.toOvsFileAst(cst);
        profile.checkpoint("cst to ast");
        String code = new SlimeGenerator().generate(ast);
        profile.checkpoint("generate esm");
        List<String> usedAtoms = converter.usedCsstsAtoms().stream().sorted().toList();
        String normalizedClassPrefix = normalizeClassPrefix(classPrefix);
        String css = QinCsstsJavaCompiler.generateCss(usedAtoms, normalizedClassPrefix,
                options.pseudoStyles(), options.classGroups());
        String atomModule = QinCsstsJavaCompiler.generateAtomModule(usedAtoms, normalizedClassPrefix,
                options.pseudoStyles(), options.classGroups());
        profile.done("atoms=" + usedAtoms.size());
        return new QinOvsCompiler.QinOvsCompileResult(code, !usedAtoms.isEmpty(), css, atomModule);
    }

    private static String normalizeClassPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) return "";
        return prefix.endsWith("_") || prefix.endsWith("-") ? prefix : prefix + "_";
    }

}
