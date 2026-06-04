package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class QinGeneratedJavaBundleGapSmokeTestMain {
    private static final Pattern CLASS_DECLARATION =
            Pattern.compile("(?m)^class\\s+([A-Za-z_$][A-Za-z0-9_$]*)");

    private QinGeneratedJavaBundleGapSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path bundle = findQinRoot().resolve(".qin/generated/slime-parser/slime-parser.bundle.js");
        String source = Files.readString(bundle);
        String slice = slice(
                source,
                "class com_subhuti_error_ErrorPosition",
                "com_slime_token_JavaScriptTokens.__qin_field_TOKENS =",
                bundle);
        List<String> classNames = CLASS_DECLARATION.matcher(slice)
                .results()
                .map(MatchResult::group)
                .map(text -> text.substring("class ".length()))
                .toList();

        QinIrProgram program = new QinFrontendLowerer().lowerSource(padded(slice) + "\nconst __qin_gap_done = true;\n");
        Set<String> declarationNames = program.declarations().stream()
                .map(declaration -> declaration.name())
                .collect(Collectors.toSet());
        List<String> missing = classNames.stream()
                .filter(name -> !declarationNames.contains(name))
                .toList();
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Missing generated class declarations in gap slice: " + missing);
        }

        System.out.println("QinGeneratedJavaBundleGapSmokeTestMain OK");
    }

    private static String slice(String source, String startMarker, String endMarker, Path bundle) {
        int start = source.indexOf(startMarker);
        int end = source.indexOf(endMarker);
        if (start < 0 || end < 0 || end <= start) {
            throw new IllegalStateException(
                    "Unable to locate generated bundle markers in "
                            + bundle
                            + "; length="
                            + source.length()
                            + "; start="
                            + start
                            + "; end="
                            + end);
        }
        return source.substring(start, end);
    }

    private static String padded(String source) {
        StringBuilder builder = new StringBuilder(520_000 + source.length());
        builder.append("/*");
        builder.append("x".repeat(520_000));
        builder.append("*/\n");
        builder.append(source);
        return builder.toString();
    }

    private static Path findQinRoot() {
        Path current = Path.of("").toAbsolutePath();
        while (current != null) {
            if (Files.exists(current.resolve("qin.bat"))) {
                return current;
            }
            current = current.getParent();
        }
        throw new IllegalStateException("Unable to locate qin root from " + Path.of("").toAbsolutePath());
    }
}
