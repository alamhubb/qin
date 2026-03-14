package com.qin.conformance;

import com.qin.conformance.QinConformanceModels.CaseExecution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Executes one ESM entry under Chrome headless and returns pass/fail classification.
 */
public final class QinChromeRunner {
    private static final Pattern PRE_RESULT_PATTERN = Pattern.compile("<pre id=\"q\">([^<]*)</pre>");
    private static final int[] BUDGET_MS_STEPS = new int[] {4000, 10000, 20000};

    private QinChromeRunner() {
    }

    public static String resolveChromeBinary(List<String> candidates, String overrideBinary) {
        if (overrideBinary != null && !overrideBinary.isBlank()) {
            return overrideBinary.trim();
        }
        if (candidates == null) {
            return "";
        }
        for (String candidate : candidates) {
            if (candidate == null || candidate.isBlank()) {
                continue;
            }
            if (isExecutableAvailable(candidate.trim())) {
                return candidate.trim();
            }
        }
        return "";
    }

    public static CaseExecution run(Path entryFile, String chromeBinary) {
        if (chromeBinary == null || chromeBinary.isBlank()) {
            return new CaseExecution("SKIP", "", "Chrome binary not found", "");
        }
        try {
            Path html = createHarnessHtml(entryFile);
            CaseExecution lastFailure = null;
            for (int budgetMs : BUDGET_MS_STEPS) {
                CommandResult commandResult = runChrome(chromeBinary, html, budgetMs);
                if (commandResult.exitCode() != 0) {
                    lastFailure = new CaseExecution(
                            "FAIL",
                            "ChromeProcessError",
                            "Chrome exited with code " + commandResult.exitCode(),
                            trim(commandResult.output()));
                    continue;
                }

                String result = extractHarnessResult(commandResult.output());
                if (result.startsWith("PASS")) {
                    return new CaseExecution("PASS", "", "", trim(commandResult.output()));
                }
                if (result.startsWith("FAIL:")) {
                    String error = result.substring("FAIL:".length()).trim();
                    return new CaseExecution(
                            "FAIL",
                            error.isBlank() ? "Error" : error,
                            result,
                            trim(commandResult.output()));
                }
                lastFailure = new CaseExecution(
                        "FAIL",
                        "UnknownHarnessResult",
                        result,
                        trim(commandResult.output()));
            }

            if (lastFailure != null) {
                return lastFailure;
            }
            return new CaseExecution("FAIL", "UnknownHarnessResult", "No harness result", "");
        } catch (Exception ex) {
            String type = ex.getClass().getSimpleName();
            String message = ex.getMessage() == null ? "" : ex.getMessage();
            return new CaseExecution("FAIL", type, message, "");
        }
    }

    private static List<String> buildCommand(String chromeBinary, Path html, int budgetMs) {
        List<String> command = new ArrayList<>();
        command.add(chromeBinary);
        command.add("--headless=new");
        command.add("--disable-gpu");
        command.add("--allow-file-access-from-files");
        command.add("--virtual-time-budget=" + budgetMs);
        command.add("--dump-dom");
        command.add(html.toUri().toString());
        return command;
    }

    private static CommandResult runChrome(String chromeBinary, Path html, int budgetMs) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(buildCommand(chromeBinary, html, budgetMs));
        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            output = sb.toString();
        }
        int exit = process.waitFor();
        return new CommandResult(exit, output);
    }

    private static Path createHarnessHtml(Path entryFile) throws IOException {
        Path dir = Files.createTempDirectory("qin-conformance-chrome");
        Path html = dir.resolve("runner.html");
        String moduleUrl = entryFile.toAbsolutePath().normalize().toUri().toString();
        String content = """
                <!doctype html>
                <html lang="en">
                <head><meta charset="UTF-8"><title>Qin Conformance</title></head>
                <body>
                <pre id="q">INIT</pre>
                <script type="module">
                const out = document.getElementById("q");
                const done = (value) => { out.textContent = value; };
                try {
                  await import("%s");
                  done("PASS");
                } catch (e) {
                  const name = e && e.name ? e.name : "Error";
                  done("FAIL:" + name);
                }
                </script>
                </body>
                </html>
                """.formatted(moduleUrl.replace("\\", "\\\\").replace("\"", "\\\""));
        Files.writeString(html, content, StandardCharsets.UTF_8);
        return html;
    }

    private static String extractHarnessResult(String htmlDump) {
        if (htmlDump == null || htmlDump.isBlank()) {
            return "FAIL:EmptyChromeOutput";
        }
        Matcher matcher = PRE_RESULT_PATTERN.matcher(htmlDump);
        if (!matcher.find()) {
            return "FAIL:MissingHarnessMarker";
        }
        return matcher.group(1).trim();
    }

    private static boolean isExecutableAvailable(String candidate) {
        Path path = Path.of(candidate);
        if (Files.isRegularFile(path)) {
            return true;
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(candidate, "--version");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            int exit = process.waitFor();
            return exit == 0;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static String trim(String text) {
        if (text == null) {
            return "";
        }
        String normalized = text.trim();
        return normalized.length() > 1600 ? normalized.substring(0, 1600) : normalized;
    }

    private record CommandResult(int exitCode, String output) {
    }
}
