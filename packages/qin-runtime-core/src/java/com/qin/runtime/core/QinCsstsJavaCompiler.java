package com.qin.runtime.core;

import com.qin.parser.CsstsAtomNames;
import com.qin.parser.OvsParser;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

/** Java-native CSSTS source transformation for .cssts modules. */
public final class QinCsstsJavaCompiler {
    private QinCsstsJavaCompiler() {
    }

    public static QinCsstsCompiler.QinCsstsCompileResult compile(String source, String configSource) {
        String input = source == null ? "" : source;
        QinPhaseTimer profile = QinPhaseTimer.start("java-cssts-transform");
        OvsParser parser = new OvsParser(input);
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
        String code = new SlimeGenerator().generate(ast);
        profile.checkpoint("generate esm");
        String prefix = options.prefix();
        List<String> atoms = converter.usedCsstsAtoms().stream().sorted().toList();
        String css = generateCss(atoms, prefix, options.pseudoStyles(), options.classGroups());
        String atomModule = generateAtomModule(atoms, prefix, options.pseudoStyles(), options.classGroups());
        profile.done("atoms=" + atoms.size());
        boolean hasStyles = !atoms.isEmpty();
        return new QinCsstsCompiler.QinCsstsCompileResult(code, code, hasStyles, css, atomModule);
    }

    static CsstsOptions parseOptions(String configSource) {
        Map<String, Map<String, String>> pseudoStyles = new LinkedHashMap<>(CsstsAtomNames.pseudoAtoms());
        Map<String, List<String>> classGroups = new LinkedHashMap<>(CsstsAtomNames.classGroups());
        if (configSource == null) return new CsstsOptions("cssts_", pseudoStyles, classGroups);
        QinConfigStaticValueReader config = QinConfigStaticValueReader.parse(configSource);
        String configuredPrefix = QinConfigStaticValueReader.stringValue(config.firstProperty("classPrefix"));
        String prefix = configuredPrefix == null ? "cssts" : configuredPrefix;
        prefix = prefix.isEmpty() ? "" : prefix.endsWith("_") || prefix.endsWith("-") ? prefix : prefix + "_";
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

    record CsstsOptions(String prefix, Map<String, Map<String, String>> pseudoStyles, Map<String, List<String>> classGroups) {
        CsstsOptions {
            pseudoStyles = Collections.unmodifiableMap(new LinkedHashMap<>(pseudoStyles));
            classGroups = Collections.unmodifiableMap(new LinkedHashMap<>(classGroups));
        }
        Set<String> pseudoNames() { return pseudoStyles.keySet(); }
        Set<String> groupNames() { return classGroups.keySet(); }
    }

    static String generateCss(List<String> atoms, String prefix) {
        return generateCss(atoms, prefix, CsstsAtomNames.pseudoAtoms(), CsstsAtomNames.classGroups());
    }

    static String generateCss(List<String> atoms, String prefix, Map<String, Map<String, String>> pseudoStyles,
            Map<String, List<String>> classGroups) {
        Map<String, List<String>> groupedRules = new LinkedHashMap<>();
        for (String name : atoms) {
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
            if (atom != null) {
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
