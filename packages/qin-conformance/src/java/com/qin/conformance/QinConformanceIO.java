package com.qin.conformance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * JSON I/O for conformance models.
 */
public final class QinConformanceIO {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    private QinConformanceIO() {
    }

    public static QinConformanceBaseline loadBaseline(Path baselineFile) throws IOException {
        String json = Files.readString(baselineFile, StandardCharsets.UTF_8);
        QinConformanceBaseline baseline = GSON.fromJson(json, QinConformanceBaseline.class);
        if (baseline == null) {
            throw new IllegalArgumentException("Invalid baseline json: " + baselineFile.toAbsolutePath());
        }
        return baseline;
    }

    public static QinConformanceExclusions loadExclusions(Path exclusionsFile) throws IOException {
        String json = Files.readString(exclusionsFile, StandardCharsets.UTF_8);
        QinConformanceExclusions exclusions = GSON.fromJson(json, QinConformanceExclusions.class);
        if (exclusions == null) {
            return new QinConformanceExclusions(java.util.List.of());
        }
        return exclusions;
    }

    public static void writeReport(Path reportFile, QinConformanceModels.Report report) throws IOException {
        Files.createDirectories(reportFile.getParent());
        Files.writeString(reportFile, GSON.toJson(report), StandardCharsets.UTF_8);
    }

    public static String toJson(QinConformanceModels.Report report) {
        return GSON.toJson(report);
    }
}
