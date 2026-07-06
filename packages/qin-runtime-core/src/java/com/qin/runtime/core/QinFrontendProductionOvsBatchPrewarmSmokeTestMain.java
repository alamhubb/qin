package com.qin.runtime.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class QinFrontendProductionOvsBatchPrewarmSmokeTestMain {
    private QinFrontendProductionOvsBatchPrewarmSmokeTestMain() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Files.createTempDirectory("qin-frontend-production-ovs-batch-");
        Path app = root.resolve("app");
        Path out = root.resolve("dist");
        Files.createDirectories(app);
        Files.writeString(root.resolve("qin.config.js"), "{ \"name\": \"qin-frontend-production-ovs-batch\" }\n",
                StandardCharsets.UTF_8);

        String firstMarker = "production-batch-first-" + System.nanoTime();
        String secondMarker = "production-batch-second-" + System.nanoTime();
        Path entry = app.resolve("main.js");
        Files.writeString(entry, """
                import First from "./First.ovs";
                import Second from "./Second.ovs";

                console.log(First, Second);
                """, StandardCharsets.UTF_8);
        Files.writeString(app.resolve("First.ovs"), source(firstMarker), StandardCharsets.UTF_8);
        Files.writeString(app.resolve("Second.ovs"), source(secondMarker), StandardCharsets.UTF_8);

        QinFrontendEsmService service = QinFrontendEsmService.create(root, entry);
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            service.emitProduction(out);
        } finally {
            System.setOut(originalOut);
        }

        requireContains(Files.readString(out.resolve("@qin-mod/app/First.ovs.js"), StandardCharsets.UTF_8),
                firstMarker,
                "first production OVS output");
        requireContains(Files.readString(out.resolve("@qin-mod/app/Second.ovs.js"), StandardCharsets.UTF_8),
                secondMarker,
                "second production OVS output");
        String log = captured.toString(StandardCharsets.UTF_8);
        int wrapperRuns = countOccurrences(log, "compile and run wrapper");
        if (wrapperRuns != 1) {
            throw new IllegalStateException("Production OVS emit should batch prewarm graph modules once; wrapper runs="
                    + wrapperRuns + "\n" + log);
        }
        if (!log.contains("vite_plugin_ovs_transform_batch")) {
            throw new IllegalStateException("Production OVS emit should use the batch transform wrapper:\n" + log);
        }

        System.out.println("QinFrontendProductionOvsBatchPrewarmSmokeTestMain OK");
    }

    private static String source(String marker) {
        return """
                export default div {
                  "%s"
                }
                """.formatted(marker);
    }

    private static void requireContains(String text, String expected, String label) {
        if (text == null || !text.contains(expected)) {
            throw new IllegalStateException("Expected " + label + " to contain " + expected + ", got:\n" + text);
        }
    }

    private static int countOccurrences(String text, String needle) {
        int count = 0;
        int offset = 0;
        while (text != null && needle != null && !needle.isEmpty()) {
            int index = text.indexOf(needle, offset);
            if (index < 0) {
                return count;
            }
            count++;
            offset = index + needle.length();
        }
        return count;
    }
}
