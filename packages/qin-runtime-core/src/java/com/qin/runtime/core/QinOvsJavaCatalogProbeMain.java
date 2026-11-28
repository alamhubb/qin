package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Runs the Java OVS compiler over the repository's checked-in OVS examples. */
public final class QinOvsJavaCatalogProbeMain {
    private static final Set<Path> EXPECTED_INVALID_EXAMPLES = Set.of(Path.of("combined", "01-export-class.ovs"));
    private static final Set<String> HTML_TAGS = Set.of(
            "a", "article", "aside", "b", "blockquote", "body", "button", "canvas", "code", "div",
            "em", "fieldset", "footer", "form", "h1", "h2", "h3", "header", "hr", "i", "img",
            "input", "label", "li", "main", "nav", "ol", "option", "p", "pre", "section", "select",
            "small", "span", "strong", "svg", "table", "tbody", "td", "textarea", "th", "thead", "tr", "ul");
    private static final Pattern RENDER_TAG = Pattern.compile("\\b([A-Za-z_$][\\w$]*)\\s*(?:\\([^\\n{}]*\\)\\s*)?\\{");
    private static final Pattern NAMED_EXPORT = Pattern.compile("\\bexport\\s+(?:const|let|var|function|class)\\s+([A-Za-z_$][\\w$]*)");

    private QinOvsJavaCatalogProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: QinOvsJavaCatalogProbeMain <ovs-example-directory>");
        }

        Path catalog = Path.of(args[0]).toAbsolutePath().normalize();
        List<Path> sources;
        try (var paths = Files.walk(catalog)) {
            sources = paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".ovs"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        }

        List<String> failures = new ArrayList<>();
        int expectedRejections = 0;
        for (Path sourcePath : sources) {
            Path relativePath = catalog.relativize(sourcePath);
            String source = Files.readString(sourcePath, StandardCharsets.UTF_8);
            try {
                QinOvsCompiler.QinOvsCompileResult result = QinOvsJavaCompiler.compile(source);
                if (result.code() == null || result.code().isBlank()) {
                    failures.add(relativePath + " emitted empty JavaScript");
                } else if (EXPECTED_INVALID_EXAMPLES.contains(relativePath)) {
                    failures.add(relativePath + " unexpectedly compiled although OVS props reject object-literal syntax");
                } else {
                    String semanticFailure = semanticFailure(source, result);
                    if (semanticFailure != null) failures.add(relativePath + " " + semanticFailure
                            + "\n  output: " + result.code().replaceAll("\\s+", " "));
                }
            } catch (RuntimeException exception) {
                if (EXPECTED_INVALID_EXAMPLES.contains(relativePath)
                        && exception instanceof IllegalArgumentException
                        && exception.getMessage() != null
                        && exception.getMessage().startsWith("OVS Java parse failed:")) {
                    expectedRejections++;
                    continue;
                }
                String message = exception.getMessage() == null
                        ? exception.getClass().getSimpleName()
                        : exception.getMessage().replaceAll("\\s+", " ");
                failures.add(relativePath + " failed: " + message);
            }
        }

        if (expectedRejections != EXPECTED_INVALID_EXAMPLES.size()) {
            failures.add("Expected " + EXPECTED_INVALID_EXAMPLES.size() + " documented invalid sample(s) to be rejected, got "
                    + expectedRejections);
        }
        System.out.println("OVS catalog cases=" + sources.size() + ", compiled="
                + (sources.size() - expectedRejections - failures.size()) + ", expected rejections="
                + expectedRejections + ", failed=" + failures.size());
        failures.forEach(System.out::println);
        if (!failures.isEmpty()) throw new AssertionError("Java OVS compiler failed catalog cases");
    }

    private static String semanticFailure(String source, QinOvsCompiler.QinOvsCompileResult result) {
        String code = result.code();
        Matcher tags = RENDER_TAG.matcher(source);
        while (tags.find()) {
            String tag = tags.group(1);
            if (HTML_TAGS.contains(tag) && !code.contains("$OvsHtmlTag." + tag)) {
                return "lost HTML render call for " + tag;
            }
        }
        Matcher exports = NAMED_EXPORT.matcher(source);
        while (exports.find()) {
            if (!code.contains("export const " + exports.group(1))) {
                return "lost named export " + exports.group(1);
            }
        }
        if (source.contains("export default") && !code.contains("export default")) {
            return "lost default export";
        }
        if (!source.contains("export") && source.matches("(?s).*\\b(?:div|span|button|p|h[1-6])\\s*\\{.*")) {
            if (!code.contains("export default defineOvsComponent")) return "lost implicit top-level component export";
        }
        if (source.contains("css {") && result.hasStyles() && !code.contains("cssts.merge(")) {
            return "lost CSS atom merge expression";
        }
        if (source.contains("const ListItem") && source.contains("ul {")
                && (!code.contains("$OvsHtmlTag.ul")
                || !code.contains("for (const item of items)")
                || !code.contains("ListItem({ text: item })"))) {
            return "lost nested render ownership or loop component invocation";
        }
        return null;
    }
}
