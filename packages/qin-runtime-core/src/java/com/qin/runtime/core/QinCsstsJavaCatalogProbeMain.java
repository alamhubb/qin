package com.qin.runtime.core;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Runs the Java CSSTS compiler over the checked-in compiler example catalog. */
public final class QinCsstsJavaCatalogProbeMain {
    private static final Pattern ATOM_PROPERTY = Pattern.compile("(?m)^\\s*([A-Za-z_$][\\w$]*): ");

    private QinCsstsJavaCatalogProbeMain() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: QinCsstsJavaCatalogProbeMain <cssts-example-directory>");
        }

        Path catalog = Path.of(args[0]).toAbsolutePath().normalize();
        List<Path> sources;
        try (var paths = Files.walk(catalog)) {
            sources = paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".cssts"))
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        }

        List<String> failures = new ArrayList<>();
        for (Path sourcePath : sources) {
            String source = Files.readString(sourcePath, StandardCharsets.UTF_8);
            try {
                QinCsstsCompiler.QinCsstsCompileResult result = QinCsstsJavaCompiler.compile(source, "");
                if (result.code() == null || result.code().isBlank()) {
                    failures.add(catalog.relativize(sourcePath) + " emitted empty JavaScript");
                } else {
                    String semanticFailure = semanticFailure(sourcePath, source, result);
                    if (semanticFailure != null) failures.add(catalog.relativize(sourcePath) + " " + semanticFailure
                            + "\n  output: " + result.code().replaceAll("\\s+", " "));
                }
            } catch (RuntimeException exception) {
                String message = exception.getMessage() == null
                        ? exception.getClass().getSimpleName()
                        : exception.getMessage().replaceAll("\\s+", " ");
                failures.add(catalog.relativize(sourcePath) + " failed: " + message);
            }
        }

        System.out.println("CSSTS catalog cases=" + sources.size() + ", compiled="
                + (sources.size() - failures.size()) + ", failed=" + failures.size());
        failures.forEach(System.out::println);
        if (!failures.isEmpty()) throw new AssertionError("Java CSSTS compiler failed catalog cases");
    }

    private static String semanticFailure(Path sourcePath, String source,
            QinCsstsCompiler.QinCsstsCompileResult result) {
        String code = result.code();
        if (!result.hasStyles() || !code.contains("cssts.merge(")) {
            return "did not emit CSSTS merge/style output";
        }
        Matcher atomMatcher = ATOM_PROPERTY.matcher(result.atomModule());
        while (atomMatcher.find()) {
            String atomName = atomMatcher.group(1);
            if (!code.contains("csstsAtom." + atomName)) return "atom module entry is absent from generated code: " + atomName;
        }
        String fileName = sourcePath.getParent().getFileName().toString();
        if (fileName.startsWith("07-") && !code.contains("isActive ? cssts.merge")) {
            return "lost conditional CSS expressions";
        }
        if (fileName.startsWith("08-") && (!code.contains("enabled && cssts.merge") || !code.contains("null || cssts.merge"))) {
            return "lost logical CSS expressions";
        }
        if (fileName.startsWith("09-") && !code.contains("cssts.merge(base, csstsAtom.fontBold)")) {
            return "did not pass the spread style object and static atom to CSSTS merge in source order";
        }
        if (fileName.startsWith("10-") && !code.contains("isError && csstsAtom.colorRed")) {
            return "lost conditional atom expression";
        }
        if (fileName.startsWith("11-") && !code.contains("isActive ? csstsAtom.colorRed : csstsAtom.colorGray")) {
            return "lost ternary atom expression";
        }
        if (fileName.startsWith("13-") && (!code.contains("isActive && csstsAtom.colorRed")
                || !code.contains("csstsAtom.displayFlex"))) {
            return "lost CSS expressions inside arrow functions";
        }
        if (fileName.startsWith("15-") && (!code.contains("export const primaryStyle")
                || !code.contains("export const secondaryStyle"))) {
            return "lost named exports";
        }
        if (fileName.startsWith("16-") && !code.contains("export default buttonStyle")) {
            return "lost the default export";
        }
        if (fileName.startsWith("19-") && (!code.contains("async function loadStyle") || !code.contains("await fetch"))) {
            return "lost async function syntax";
        }
        if (fileName.startsWith("20-") && (!code.contains("isActive && csstsAtom.colorRed")
                || !code.contains("!isDisabled && csstsAtom.cursorPointer")
                || !code.contains("theme === 'dark' ? bgDark : bgLight"))) {
            return "lost combined logical, unary, or ternary atom expressions";
        }
        return null;
    }
}
