package com.qin.runtime.core;

import com.qin.lang.sema.esm.QinEsmModuleBindings;
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
        return compile(source, classPrefix, configSource, false);
    }

    public static QinEsmSourceMap.Result<QinOvsCompiler.QinOvsCompileResult> compileWithSourceMap(
            String source, String classPrefix, String configSource, String sourceName) {
        var result = compile(source, classPrefix, configSource, true);
        return new QinEsmSourceMap.Result<>(
                new QinOvsCompiler.QinOvsCompileResult(result.code(), result.hasStyles(), result.css(), result.atomModule(),
                        List.of(), result.moduleBindings(), result.moduleLayout()),
                QinEsmSourceMap.encode(result.mappedCode(), sourceName, source == null ? "" : source));
    }

    static QinOvsCompiler.QinOvsCompileResult compile(
            String source, String classPrefix, String configSource, boolean sourceMap) {
        var options = QinCsstsJavaCompiler.parseOptions(configSource)
                .withPrefix(QinCsstsJavaCompiler.normalizeClassPrefix(classPrefix));
        return compile(source, options, sourceMap);
    }

    static QinOvsCompiler.QinOvsCompileResult compile(
            String source, QinCsstsJavaCompiler.CsstsOptions options, boolean sourceMap) {
        String input = source == null ? "" : source;
        QinPhaseTimer profile = QinPhaseTimer.start("java-ovs-transform");
        OvsParser parser = new OvsParser(input);
        SubhutiCst cst = parser.Program(SlimeParser.SourceType.MODULE);
        profile.checkpoint("parse source");
        if (parser.isParserFail() || cst == null || !parser.isAtEnd()) {
            throw new IllegalArgumentException("OVS Java parse failed: " + parser.getDiagnostics());
        }

        QinOvsCstToAst converter = new QinOvsCstToAst();
        converter.configureCsstsAtoms(options.pseudoNames(), options.groupNames());
        Program ast = converter.toOvsFileAst(cst);
        profile.checkpoint("cst to ast");
        var generator = new SlimeGenerator(SlimeGenerator.OutputLanguage.JAVASCRIPT);
        var mapped = sourceMap ? generator.generateWithMappings(ast) : null;
        String code = mapped == null ? generator.generate(ast) : mapped.code();
        profile.checkpoint("generate esm");
        List<String> usedAtoms = converter.usedCsstsAtoms().stream().sorted().toList();
        String css = QinCsstsJavaCompiler.generateCss(usedAtoms, options.prefix(),
                options.pseudoStyles(), options.classGroups());
        String atomModule = QinCsstsJavaCompiler.generateAtomModule(usedAtoms, options.prefix(),
                options.pseudoStyles(), options.classGroups());
        QinEsmModuleBindings moduleBindings = QinEsmModuleBindings.fromProgram(ast);
        profile.done("atoms=" + usedAtoms.size());
        return new QinOvsCompiler.QinOvsCompileResult(code, !usedAtoms.isEmpty(), css, atomModule,
                mapped == null ? List.of() : mapped.mappings(),
                moduleBindings, generator.moduleLayout());
    }

}
