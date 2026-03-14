package com.qin.conformance;

import java.util.List;
import java.util.Objects;

/**
 * Immutable conformance baseline model.
 */
public record QinConformanceBaseline(
        String suite,
        ChromeBaseline chrome,
        List<ConformanceCase> cases) {
    public QinConformanceBaseline {
        Objects.requireNonNull(suite, "suite cannot be null");
        Objects.requireNonNull(chrome, "chrome cannot be null");
        Objects.requireNonNull(cases, "cases cannot be null");
        cases = List.copyOf(cases);
    }

    public record ChromeBaseline(
            String channel,
            String frozenDate,
            List<String> binaryCandidates) {
        public ChromeBaseline {
            Objects.requireNonNull(channel, "channel cannot be null");
            Objects.requireNonNull(frozenDate, "frozenDate cannot be null");
            Objects.requireNonNull(binaryCandidates, "binaryCandidates cannot be null");
            binaryCandidates = List.copyOf(binaryCandidates);
        }
    }

    public record ConformanceCase(
            String id,
            String entry) {
        public ConformanceCase {
            Objects.requireNonNull(id, "id cannot be null");
            Objects.requireNonNull(entry, "entry cannot be null");
        }
    }
}
