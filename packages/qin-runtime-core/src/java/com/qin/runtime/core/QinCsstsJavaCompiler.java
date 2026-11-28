package com.qin.runtime.core;

import com.qin.lang.sema.esm.QinEsmModuleBindings;
import com.qin.parser.CsstsAtomNames;
import com.qin.parser.CssTsParser;
import com.qin.parser.QinOvsCstToAst;
import com.slime.ast.Expression;
import com.slime.ast.nodes.misc.Program;
import com.slime.ast.nodes.expressions.ObjectExpression;
import com.slime.generator.SlimeGenerator;
import com.slime.parser.SlimeParser;
import com.subhuti.struct.SubhutiCst;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

/** Java-native CSSTS source transformation for .cssts modules. */
public final class QinCsstsJavaCompiler {
    private QinCsstsJavaCompiler() {
    }

    public static QinCsstsCompiler.QinCsstsCompileResult compile(String source, String configSource) {
        return compile(source, configSource, false);
    }

    public static QinEsmSourceMap.Result<QinCsstsCompiler.QinCsstsCompileResult> compileWithSourceMap(
            String source, String configSource, String sourceName) {
        var result = compile(source, configSource, true);
        return new QinEsmSourceMap.Result<>(
                new QinCsstsCompiler.QinCsstsCompileResult(result.code(), result.rawCode(), result.hasStyles(),
                        result.css(), result.atomModule(), List.of(), result.moduleBindings()),
                QinEsmSourceMap.encode(result.mappedCode(), sourceName, source == null ? "" : source));
    }

    static QinCsstsCompiler.QinCsstsCompileResult compile(String source, String configSource, boolean sourceMap) {
        String input = source == null ? "" : source;
        QinPhaseTimer profile = QinPhaseTimer.start("java-cssts-transform");
        CssTsParser parser = new CssTsParser(input);
        SubhutiCst cst = parser.Program(SlimeParser.SourceType.MODULE);
        profile.checkpoint("parse source");
        if (parser.isParserFail() || cst == null || !parser.isAtEnd()) {
            throw new IllegalArgumentException("CSSTS Java parse failed: " + parser.getDiagnostics());
        }

        QinOvsCstToAst converter = new QinOvsCstToAst();
        CsstsOptions options = parseOptions(configSource);
        converter.configureCsstsAtoms(options.pseudoStyles().keySet(), options.classGroups().keySet());
        Program ast = converter.toOvsFileAst(cst);
        profile.checkpoint("cst to ast");
        var generator = new SlimeGenerator(SlimeGenerator.OutputLanguage.JAVASCRIPT);
        var mapped = sourceMap ? generator.generateWithMappings(ast) : null;
        String code = mapped == null ? generator.generate(ast) : mapped.code();
        profile.checkpoint("generate esm");
        String prefix = options.prefix();
        List<String> atoms = converter.usedCsstsAtoms().stream().sorted().toList();
        String css = generateCss(atoms, prefix, options.pseudoStyles(), options.classGroups());
        String atomModule = generateAtomModule(atoms, prefix, options.pseudoStyles(), options.classGroups());
        QinEsmModuleBindings moduleBindings = QinEsmModuleBindings.fromProgram(ast);
        profile.done("atoms=" + atoms.size());
        boolean hasStyles = !atoms.isEmpty();
        return new QinCsstsCompiler.QinCsstsCompileResult(code, code, hasStyles, css, atomModule,
                mapped == null ? List.of() : mapped.mappings(), moduleBindings);
    }

    static CsstsOptions parseOptions(String configSource) {
        return parseOptions(configSource, DefaultOptions.CSSTS);
    }

    static CsstsOptions parseOvsProjectOptions(String configSource) {
        return parseOptions(configSource, DefaultOptions.OVS);
    }

    private static CsstsOptions parseOptions(String configSource, CsstsOptions defaults) {
        if (configSource == null || configSource.isBlank()) return defaults;
        Map<String, Map<String, String>> pseudoStyles = new LinkedHashMap<>(CsstsAtomNames.pseudoAtoms());
        Map<String, List<String>> classGroups = new LinkedHashMap<>(CsstsAtomNames.classGroups());
        QinConfigStaticValueReader config = QinConfigStaticValueReader.parse(configSource);
        String configuredPrefix = QinConfigStaticValueReader.stringValue(config.firstProperty("classPrefix"));
        String prefix = configuredPrefix == null ? defaults.prefix() : normalizeClassPrefix(configuredPrefix);
        Expression pseudoValue = config.firstProperty("pseudoClassConfig");
        if (pseudoValue instanceof ObjectExpression pseudoObject) {
            QinConfigStaticValueReader.objectProperties(pseudoObject).forEach((name, stylesValue) -> {
                if (!(stylesValue instanceof ObjectExpression stylesObject)) return;
                Map<String, String> styles = new LinkedHashMap<>();
                QinConfigStaticValueReader.objectProperties(stylesObject).forEach((property, value) -> {
                    String cssValue = QinConfigStaticValueReader.stringValue(value);
                    if (cssValue != null) styles.put(property, cssValue);
                });
                if (!styles.isEmpty()) pseudoStyles.put(name, styles);
            });
        }
        Expression groupValue = config.firstProperty("classGroup");
        if (groupValue instanceof ObjectExpression groupObject) {
            QinConfigStaticValueReader.objectProperties(groupObject).forEach((name, values) -> {
                classGroups.put(name, QinConfigStaticValueReader.stringArray(values));
            });
        }
        return new CsstsOptions(prefix, pseudoStyles, classGroups);
    }

    private static final class DefaultOptions {
        private static final CsstsOptions CSSTS =
                new CsstsOptions("cssts_", CsstsAtomNames.pseudoAtoms(), CsstsAtomNames.classGroups());
        private static final CsstsOptions OVS = CSSTS.withPrefix("cmp-");
    }

    static String normalizeClassPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) return "";
        return prefix.endsWith("_") || prefix.endsWith("-") ? prefix : prefix + "_";
    }

    record CsstsOptions(String prefix, Map<String, Map<String, String>> pseudoStyles, Map<String, List<String>> classGroups) {
        CsstsOptions {
            pseudoStyles = Collections.unmodifiableMap(new LinkedHashMap<>(pseudoStyles));
            classGroups = Collections.unmodifiableMap(new LinkedHashMap<>(classGroups));
        }
        Set<String> pseudoNames() { return pseudoStyles.keySet(); }
        Set<String> groupNames() { return classGroups.keySet(); }

        CsstsOptions withPrefix(String prefix) {
            return this.prefix.equals(prefix) ? this : new CsstsOptions(prefix, pseudoStyles, classGroups);
        }
    }

    static String generateCss(List<String> atoms, String prefix) {
        return generateCss(atoms, prefix, CsstsAtomNames.pseudoAtoms(), CsstsAtomNames.classGroups());
    }

    static String generateCss(List<String> atoms, String prefix, Map<String, Map<String, String>> pseudoStyles,
            Map<String, List<String>> classGroups) {
        Map<String, List<String>> groupedRules = new LinkedHashMap<>();
        Set<String> variants = new LinkedHashSet<>();
        for (String name : atoms) {
            var variant = CsstsAtomNames.pseudoVariant(name);
            if (variant != null) {
                String selector = camelToKebab(variant.base()).replace("$", "\\$");
                for (String pseudo : variant.pseudos()) {
                    var styles = pseudoStyles.get(pseudo);
                    if (styles != null) {
                        variants.add("." + prefix + selector + ":" + pseudo + " { " + renderDeclarations(styles) + "; }");
                    }
                }
                continue;
            }
            CsstsAtomNames.AtomData atom = CsstsAtomNames.parseTsAtom(name);
            if (atom == null) continue;
            String rule = "." + prefix + atom.property() + "_" + escapeCssClassValue(atom.value())
                    + " { " + atom.property() + ": " + atom.value() + "; }";
            groupedRules.computeIfAbsent(atom.property(), ignored -> new ArrayList<>()).add(rule);
        }
        StringBuilder css = new StringBuilder("/* Auto-generated by cssts-compiler */\n\n");
        groupedRules.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            entry.getValue().sort(Comparator.naturalOrder());
            css.append("/* ").append(entry.getKey()).append(" */\n");
            for (String rule : entry.getValue()) css.append(rule).append('\n');
            css.append('\n');
        });
        if (!variants.isEmpty()) {
            css.append("/* $$ Pseudo-class styles */\n");
            for (String rule : variants) css.append(rule).append('\n');
            css.append('\n');
        }
        css.append("/* CSSTS Pseudo-class atoms */\n");
        pseudoStyles.forEach((pseudo, styles) -> css.append('.')
                .append(prefix).append(pseudo).append(':').append(pseudo).append(" { ")
                .append(renderDeclarations(styles)).append("; }\n"));
        css.append('\n');
        css.append(generateClassGroupCss(prefix, pseudoStyles, classGroups));
        return css.toString();
    }

    static String generateAtomModule(List<String> atoms, String prefix) {
        return generateAtomModule(atoms, prefix, CsstsAtomNames.pseudoAtoms(), CsstsAtomNames.classGroups());
    }

    static String generateAtomModule(List<String> atoms, String prefix,
            Map<String, Map<String, String>> pseudoStyles, Map<String, List<String>> classGroups) {
        StringBuilder module = new StringBuilder("// Auto-generated by cssts-compiler\n\nexport const csstsAtom = {\n");
        for (int index = 0; index < atoms.size(); index++) {
            String name = atoms.get(index);
            CsstsAtomNames.AtomData atom = CsstsAtomNames.parseTsAtom(name);
            var variant = CsstsAtomNames.pseudoVariant(name);
            if (variant != null) {
                module.append("  ").append(name).append(": { '").append(prefix)
                        .append(camelToKebab(variant.base())).append("': null }");
            } else if (atom != null) {
                module.append("  ").append(name).append(": { '")
                        .append(prefix).append(atom.property()).append('_').append(atom.value())
                        .append("': '").append(atom.property()).append("' }");
            } else if (pseudoStyles.containsKey(name)) {
                module.append("  ").append(name).append(": { '").append(prefix).append(name)
                        .append("': ':").append(name).append("' }");
            } else if (classGroups.containsKey(name)) {
                module.append("  ").append(name).append(": { '").append(prefix).append(name).append("': true }");
            } else {
                continue;
            }
            if (index + 1 < atoms.size()) module.append(',');
            module.append('\n');
        }
        module.append("}\n\nexport default csstsAtom\n");
        return module.toString();
    }

    private static String escapeCssClassValue(String value) {
        return value.replace(".", "\\.").replace("%", "\\%").replace("/", "\\/");
    }

    private static String generateClassGroupCss(String prefix, Map<String, Map<String, String>> pseudoStyles,
            Map<String, List<String>> classGroups) {
        StringBuilder output = new StringBuilder("/* CSSTS Class Group atoms */\n");
        classGroups.forEach((group, items) -> {
            List<String> expanded = new ArrayList<>();
            expandGroup(group, new java.util.LinkedHashSet<>(), expanded, classGroups);
            List<String> normalProperties = new ArrayList<>();
            for (String item : expanded) {
                Map<String, String> pseudo = pseudoStyles.get(item);
                if (pseudo != null) {
                    output.append('.').append(prefix).append(group).append(':').append(item).append(" { ")
                            .append(renderDeclarations(pseudo)).append("; }\n");
                    continue;
                }
                CsstsAtomNames.AtomData atom = CsstsAtomNames.parseTsAtom(item);
                if (atom != null) normalProperties.add(atom.property() + ": " + atom.value());
            }
            if (!normalProperties.isEmpty()) {
                output.append('.').append(prefix).append(group).append(" { ")
                        .append(String.join("; ", normalProperties)).append("; }\n");
            }
        });
        return output.toString();
    }

    private static void expandGroup(String name, java.util.Set<String> visiting, List<String> output,
            Map<String, List<String>> classGroups) {
        if (!visiting.add(name)) return;
        List<String> items = classGroups.get(name);
        if (items == null) {
            output.add(name);
        } else {
            for (String item : items) expandGroup(item, visiting, output, classGroups);
        }
        visiting.remove(name);
    }

    private static String renderDeclarations(Map<String, String> styles) {
        List<String> declarations = new ArrayList<>();
        styles.forEach((property, value) -> declarations.add(camelToKebab(property) + ": " + value));
        return String.join("; ", declarations);
    }

    private static String camelToKebab(String value) {
        return value.replaceAll("([a-z])([A-Z])", "$1-$2")
                .replaceAll("([a-zA-Z])(\\d)", "$1-$2")
                .toLowerCase(java.util.Locale.ROOT);
    }
}
