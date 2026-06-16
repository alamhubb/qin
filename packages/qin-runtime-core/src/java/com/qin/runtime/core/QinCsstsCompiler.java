package com.qin.runtime.core;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QinCsstsCompiler {
    private static final Pattern CSSTS_MERGE_PATTERN = Pattern.compile("cssts\\.merge\\(([^)]*)\\)");
    private static final Pattern CSS_CLASS_RULE_PATTERN = Pattern.compile("\\.cssts_([A-Za-z0-9_-]+)\\s*\\{\\s*([^:}]+):");

    private final QinJsPackageRunner packageRunner = new QinJsPackageRunner();

    public QinCsstsCompileResult compile(Path projectRoot, String source) throws Exception {
        Object result = packageRunner.runModuleSource(
                projectRoot,
                buildWrapperSource(source),
                "cssts_compiler");
        QinCsstsCompileResult decoded = decodeResult(result);
        return decoded;
    }

    private String buildWrapperSource(String source) {
        String sourceLiteral = QinJsPackageRunner.renderJsLiteral(source);
        return """
                import { CsstsInit, RuntimeStore, transformCssTs, generateStylesCss, generateCsstsAtomModule } from "cssts-compiler";
                const __qin_context_styles__ = new Set();
                const __qin_context__ = { styles: __qin_context_styles__ };
                CsstsInit.init({ dts: false });
                const __qin_result__ = transformCssTs(%s, __qin_context__);
                const __qin_extract_atoms__ = (code) => {
                  const atoms = new Set();
                  const mergePattern = /cssts\\.merge\\(([^)]*)\\)/g;
                  let match;
                  while ((match = mergePattern.exec(code)) !== null) {
                    for (const raw of match[1].split(",")) {
                      const name = raw.trim();
                      if (/^[A-Za-z_$][\\w$]*$/.test(name)) {
                        atoms.add(name);
                      }
                    }
                  }
                  return atoms;
                };
                const __qin_fallback_atoms__ = __qin_extract_atoms__(__qin_result__.code);
                for (const atom of __qin_fallback_atoms__) __qin_context_styles__.add(atom);
                if (RuntimeStore && RuntimeStore.addUsedStyles) {
                  RuntimeStore.addUsedStyles(__qin_context_styles__);
                }
                const __qin_css__ = generateStylesCss.length > 0
                  ? generateStylesCss(__qin_context_styles__)
                  : generateStylesCss();
                const __qin_atom__ = generateCsstsAtomModule.length > 0
                  ? generateCsstsAtomModule(__qin_context_styles__)
                  : generateCsstsAtomModule();
                ({
                  code: __qin_result__.code,
                  hasStyles: __qin_result__.hasStyles,
                  css: __qin_css__,
                  atomModule: __qin_atom__
                });
                """.formatted(sourceLiteral);
    }

    @SuppressWarnings("unchecked")
    private QinCsstsCompileResult decodeResult(Object result) {
        if (!(result instanceof Map<?, ?> rawMap)) {
            throw new IllegalStateException("cssts-compiler did not return an object payload: " + result);
        }
        Map<String, Object> map = (Map<String, Object>) rawMap;
        Object code = map.get("code");
        Object hasStyles = map.get("hasStyles");
        Object css = map.get("css");
        Object atomModule = map.get("atomModule");
        if (!(code instanceof String codeText)) {
            throw new IllegalStateException("cssts-compiler result missing code string: " + result);
        }
        boolean styles = Boolean.TRUE.equals(hasStyles);
        String cssText = css instanceof String text ? text : "";
        String atomText = atomModule instanceof String text ? text : "";
        String normalizedAtomText = ensureAtomModule(codeText, cssText, atomText);
        String normalizedCode = normalizeCsstsAtomReferences(codeText, extractAtomNames(codeText));
        return new QinCsstsCompileResult(
                normalizedCode,
                codeText,
                styles,
                cssText,
                normalizedAtomText);
    }

    private String normalizeCsstsAtomReferences(String code, Set<String> atomNames) {
        if (code == null || code.isBlank() || atomNames == null || atomNames.isEmpty()) {
            return code == null ? "" : code;
        }
        Matcher matcher = CSSTS_MERGE_PATTERN.matcher(code);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String[] parts = matcher.group(1).split(",");
            StringBuilder args = new StringBuilder();
            for (String part : parts) {
                String trimmed = part.trim();
                if (!args.isEmpty()) {
                    args.append(',');
                }
                if (atomNames.contains(trimmed)) {
                    args.append("csstsAtom.").append(trimmed);
                } else {
                    args.append(part);
                }
            }
            matcher.appendReplacement(builder, Matcher.quoteReplacement("cssts.merge(" + args + ")"));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    private String ensureAtomModule(String code, String css, String atomModule) {
        Set<String> atomNames = extractAtomNames(code);
        if (atomNames.isEmpty()) {
            return atomModule == null ? "" : atomModule;
        }
        if (atomModule != null && containsAllAtoms(atomModule, atomNames)) {
            return atomModule;
        }
        Map<String, String> cssPropertiesByClass = extractCssProperties(css);
        Map<String, String> cssClassByNormalizedName = extractCssClassesByNormalizedName(css);
        StringBuilder builder = new StringBuilder();
        builder.append("// Auto-generated by Qin from cssts-compiler output").append(System.lineSeparator());
        builder.append("export const csstsAtom = {").append(System.lineSeparator());
        int index = 0;
        for (String atomName : atomNames) {
            String cssClassName = cssClassNameForAtom(atomName, cssClassByNormalizedName);
            String property = cssPropertiesByClass.getOrDefault(cssClassName, "null");
            builder.append("  ")
                    .append(atomName)
                    .append(": { '")
                    .append(cssClassName)
                    .append("': ")
                    .append("null".equals(property) ? "null" : "'" + escapeJsString(property) + "'")
                    .append(" }");
            if (++index < atomNames.size()) {
                builder.append(',');
            }
            builder.append(System.lineSeparator());
        }
        builder.append("}").append(System.lineSeparator());
        builder.append("export default csstsAtom").append(System.lineSeparator());
        return builder.toString();
    }

    private boolean containsAllAtoms(String atomModule, Set<String> atomNames) {
        for (String atomName : atomNames) {
            if (!atomModule.contains(atomName + ":")) {
                return false;
            }
        }
        return true;
    }

    private Set<String> extractAtomNames(String code) {
        LinkedHashSet<String> names = new LinkedHashSet<>();
        if (code == null || code.isBlank()) {
            return names;
        }
        Matcher matcher = CSSTS_MERGE_PATTERN.matcher(code);
        while (matcher.find()) {
            String[] parts = matcher.group(1).split(",");
            for (String part : parts) {
                String name = part.trim();
                if (name.matches("[A-Za-z_$][\\w$]*")) {
                    names.add(name);
                }
            }
        }
        return names;
    }

    private Map<String, String> extractCssProperties(String css) {
        LinkedHashMap<String, String> properties = new LinkedHashMap<>();
        if (css == null || css.isBlank()) {
            return properties;
        }
        Matcher matcher = CSS_CLASS_RULE_PATTERN.matcher(css);
        while (matcher.find()) {
            properties.put("cssts_" + matcher.group(1), matcher.group(2).trim());
        }
        return properties;
    }

    private Map<String, String> extractCssClassesByNormalizedName(String css) {
        LinkedHashMap<String, String> classes = new LinkedHashMap<>();
        if (css == null || css.isBlank()) {
            return classes;
        }
        Matcher matcher = CSS_CLASS_RULE_PATTERN.matcher(css);
        while (matcher.find()) {
            String cssClassName = "cssts_" + matcher.group(1);
            classes.putIfAbsent(normalizeAtomClassName(matcher.group(1)), cssClassName);
        }
        return classes;
    }

    private String cssClassNameForAtom(String atomName, Map<String, String> cssClassByNormalizedName) {
        String direct = cssClassByNormalizedName.get(normalizeAtomClassName(atomName));
        if (direct != null) {
            return direct;
        }
        int pseudoIndex = atomName.indexOf("$$");
        if (pseudoIndex > 0) {
            String withoutPseudo = atomName.substring(0, pseudoIndex);
            String pseudoClass = cssClassByNormalizedName.get(normalizeAtomClassName(withoutPseudo));
            if (pseudoClass != null) {
                return pseudoClass;
            }
        }
        return "cssts_" + atomClassSuffix(atomName);
    }

    private String normalizeAtomClassName(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                builder.append(Character.toLowerCase(ch));
            }
        }
        return builder.toString();
    }

    private String atomClassSuffix(String atomName) {
        String kebab = camelToKebab(atomName);
        for (Map.Entry<String, String> entry : Map.of(
                "_px", "px",
                "_rem", "rem",
                "_em", "em",
                "_vh", "vh",
                "_vw", "vw").entrySet()) {
            if (kebab.endsWith(entry.getKey())) {
                kebab = kebab.substring(0, kebab.length() - entry.getKey().length()) + entry.getValue();
            }
        }
        return kebab.replace('-', '_');
    }

    private String camelToKebab(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                builder.append('-');
            }
            builder.append(Character.toLowerCase(ch));
        }
        return builder.toString();
    }

    private String escapeJsString(String text) {
        return text.replace("\\", "\\\\").replace("'", "\\'");
    }

    public record QinCsstsCompileResult(
            String code,
            String rawCode,
            boolean hasStyles,
            String css,
            String atomModule) {
    }
}
