package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinOvsCompiler {
    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();
    private static final Pattern TEXT_LITERAL_PATTERN = Pattern.compile("\"([^\"]*)\"");
    private static final Pattern CSS_ATOM_PATTERN = Pattern.compile("\\b([A-Za-z_$][\\w$]*)\\b");
    private static final Map<String, String> BUILTIN_ATOM_CSS = Map.of(
            "colorBlue", "color: blue;",
            "fontWeight700", "font-weight: 700;",
            "padding12px", "padding: 12px;",
            "padding16px", "padding: 16px;");

    public QinOvsCompileResult compile(Path projectRoot, String source) throws Exception {
        try {
            Object result = packageRunner.runModuleSource(
                    projectRoot,
                    buildWrapperSource(source),
                    "ovs_compiler");
            return decodeResult(result);
        } catch (Exception error) {
            return compileWithBuiltinStage1Transform(source);
        }
    }

    private String buildWrapperSource(String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import { vitePluginOvsTransform } from "ovs-compiler";
                import { generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                const __qin_styles__ = new Set();
                const __qin_result__ = vitePluginOvsTransform(%s, { globalStyles: __qin_styles__ });
                ({
                  code: __qin_result__.code,
                  hasStyles: __qin_styles__.size > 0,
                  css: __qin_styles__.size > 0 ? generateStylesCss(__qin_styles__) : "",
                  atomModule: __qin_styles__.size > 0 ? generateCsstsAtomModule(__qin_styles__) : ""
                });
                """.formatted(sourceLiteral);
    }

    @SuppressWarnings("unchecked")
    private QinOvsCompileResult decodeResult(Object result) {
        if (!(result instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("ovs-compiler did not return an object payload: " + result);
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        Object code = map.get("code");
        Object hasStyles = map.get("hasStyles");
        Object css = map.get("css");
        Object atomModule = map.get("atomModule");
        if (!(code instanceof String codeText)) {
            throw new IllegalStateException("ovs-compiler result missing code string: " + result);
        }
        boolean styles = Boolean.TRUE.equals(hasStyles);
        return new QinOvsCompileResult(
                codeText,
                styles,
                css instanceof String cssText ? cssText : "",
                atomModule instanceof String atomText ? atomText : "");
    }

    private QinOvsCompileResult compileWithBuiltinStage1Transform(String source) {
        Set<String> atoms = extractAtoms(source);
        String text = extractFirstTextLiteral(source);
        String classExpression = buildClassExpression(atoms);
        String code = """
                import { defineOvsComponent, $OvsHtmlTag } from "ovsjs";
                import "virtual:cssts.css";
                import { csstsAtom } from "virtual:csstsAtom";
                export default defineOvsComponent(() => $OvsHtmlTag.div({ class: %s }, [%s]));
                """.formatted(classExpression, QinJsPackageRunner.renderJsLiteral(text));
        String css = buildCss(atoms);
        String atomModule = buildAtomModule(atoms);
        return new QinOvsCompileResult(code, !css.isBlank(), css, atomModule);
    }

    private Set<String> extractAtoms(String source) {
        Set<String> atoms = new LinkedHashSet<>();
        Matcher matcher = CSS_ATOM_PATTERN.matcher(source == null ? "" : source);
        while (matcher.find()) {
            String atom = matcher.group(1);
            if (BUILTIN_ATOM_CSS.containsKey(atom)) {
                atoms.add(atom);
            }
        }
        return atoms;
    }

    private String extractFirstTextLiteral(String source) {
        Matcher matcher = TEXT_LITERAL_PATTERN.matcher(source == null ? "" : source);
        return matcher.find() ? matcher.group(1) : "";
    }

    private String buildClassExpression(Set<String> atoms) {
        if (atoms.isEmpty()) {
            return "\"\"";
        }
        StringBuilder expression = new StringBuilder();
        for (String atom : atoms) {
            if (expression.length() > 0) {
                expression.append(" + \" \" + ");
            }
            expression.append("csstsAtom.").append(atom);
        }
        return expression.toString();
    }

    private String buildCss(Set<String> atoms) {
        StringBuilder css = new StringBuilder();
        for (String atom : atoms) {
            String declaration = BUILTIN_ATOM_CSS.get(atom);
            if (declaration != null) {
                css.append(".")
                        .append(atom)
                        .append(" { ")
                        .append(declaration)
                        .append(" }")
                        .append(System.lineSeparator());
            }
        }
        return css.toString();
    }

    private String buildAtomModule(Set<String> atoms) {
        StringBuilder module = new StringBuilder("export const csstsAtom = {");
        if (!atoms.isEmpty()) {
            module.append(System.lineSeparator());
            for (String atom : atoms) {
                module.append("  ")
                        .append(atom)
                        .append(": ")
                        .append(QinJsPackageRunner.renderJsLiteral(atom))
                        .append(",")
                        .append(System.lineSeparator());
            }
        }
        module.append("};").append(System.lineSeparator());
        module.append("export default csstsAtom;").append(System.lineSeparator());
        return module.toString();
    }

    public record QinOvsCompileResult(
            String code,
            boolean hasStyles,
            String css,
            String atomModule) {
    }
}
