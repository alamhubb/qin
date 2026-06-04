package com.qin.lang.frontend.adapter;

import com.qin.lang.ir.QinIrProgram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public final class QinGeneratedJavaBundleGapBisectMain {
    private static final String GAP_CLASS = "com_subhuti_struct_SubhutiTokenContextConstraint";
    private static final Pattern TOP_LEVEL_START =
            Pattern.compile("(?m)^(class|const|function)\\s+([A-Za-z_$][A-Za-z0-9_$]*)");

    private QinGeneratedJavaBundleGapBisectMain() {
    }

    public static void main(String[] args) throws Exception {
        Path bundle = findQinRoot().resolve(".qin/generated/slime-parser/slime-parser.bundle.js");
        String source = Files.readString(bundle);
        int gapStart = source.indexOf("class " + GAP_CLASS);
        int gapEnd = source.indexOf("com_slime_token_JavaScriptTokens.__qin_field_TOKENS =", gapStart);
        if (gapStart < 0 || gapEnd <= gapStart) {
            throw new IllegalStateException("Unable to locate generated gap in " + bundle);
        }

        List<Integer> starts = TOP_LEVEL_START.matcher(source.substring(0, gapStart))
                .results()
                .map(MatchResult::start)
                .toList();
        List<Integer> candidateStarts = new ArrayList<>(starts);
        candidateStarts.add(gapStart);

        int low = 0;
        int high = candidateStarts.size() - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            boolean ok = containsGapClass(source.substring(candidateStarts.get(mid), gapEnd));
            System.out.println("bisect mid=" + mid
                    + " start=" + candidateStarts.get(mid)
                    + " ok=" + ok
                    + " marker=" + markerAt(source, candidateStarts.get(mid)));
            if (ok) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        int firstPassingIndex = low;
        int failingIndex = Math.max(0, firstPassingIndex - 1);
        System.out.println("first passing index=" + firstPassingIndex
                + " start=" + candidateStarts.get(firstPassingIndex)
                + " marker=" + markerAt(source, candidateStarts.get(firstPassingIndex)));
        System.out.println("last failing index=" + failingIndex
                + " start=" + candidateStarts.get(failingIndex)
                + " marker=" + markerAt(source, candidateStarts.get(failingIndex)));
        bisectSuffix(source, candidateStarts.get(0), gapEnd);
        System.out.println("QinGeneratedJavaBundleGapBisectMain OK");
    }

    private static void bisectSuffix(String source, int prefixStart, int gapEnd) {
        List<Integer> suffixStarts = TOP_LEVEL_START.matcher(source.substring(gapEnd))
                .results()
                .map(result -> gapEnd + result.start())
                .toList();
        if (suffixStarts.isEmpty()) {
            System.out.println("suffix bisect skipped: no suffix top-level markers");
            return;
        }

        String prefix = source.substring(prefixStart, gapEnd);
        int low = 0;
        int high = suffixStarts.size();
        while (low < high) {
            int mid = (low + high) / 2;
            String candidate = prefix + "\n" + source.substring(suffixStarts.get(mid));
            boolean ok = containsGapClass(candidate);
            System.out.println("suffix mid=" + mid
                    + " start=" + suffixStarts.get(mid)
                    + " ok=" + ok
                    + " marker=" + markerAt(source, suffixStarts.get(mid)));
            if (ok) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        int firstPassingIndex = low;
        int lastFailingIndex = Math.max(0, firstPassingIndex - 1);
        System.out.println("suffix first passing index=" + firstPassingIndex
                + (firstPassingIndex < suffixStarts.size()
                        ? " start=" + suffixStarts.get(firstPassingIndex)
                                + " marker=" + markerAt(source, suffixStarts.get(firstPassingIndex))
                        : " at EOF"));
        System.out.println("suffix last failing index=" + lastFailingIndex
                + " start=" + suffixStarts.get(lastFailingIndex)
                + " marker=" + markerAt(source, suffixStarts.get(lastFailingIndex)));
    }

    private static boolean containsGapClass(String source) {
        QinIrProgram program = new QinFrontendLowerer().lowerSource(source + "\nconst __qin_gap_bisect_done = true;\n");
        return program.declarations().stream().anyMatch(declaration -> GAP_CLASS.equals(declaration.name()));
    }

    private static String markerAt(String source, int position) {
        int end = source.indexOf('\n', position);
        if (end < 0) {
            end = Math.min(source.length(), position + 120);
        }
        return source.substring(position, Math.min(end, position + 120)).strip();
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
