package com.qin.conformance;

import java.util.List;
import java.util.Objects;

/**
 * Approved exclusions for conformance mismatches.
 */
public record QinConformanceExclusions(List<Exclusion> exclusions) {
    public QinConformanceExclusions {
        Objects.requireNonNull(exclusions, "exclusions cannot be null");
        exclusions = List.copyOf(exclusions);
    }

    public boolean isExcluded(String caseId) {
        if (caseId == null || caseId.isBlank()) {
            return false;
        }
        for (Exclusion exclusion : exclusions) {
            if (caseId.equals(exclusion.caseId())) {
                return true;
            }
        }
        return false;
    }

    public String reasonOf(String caseId) {
        if (caseId == null || caseId.isBlank()) {
            return "";
        }
        for (Exclusion exclusion : exclusions) {
            if (caseId.equals(exclusion.caseId())) {
                return exclusion.reason();
            }
        }
        return "";
    }

    public record Exclusion(String caseId, String reason) {
        public Exclusion {
            Objects.requireNonNull(caseId, "caseId cannot be null");
            Objects.requireNonNull(reason, "reason cannot be null");
        }
    }
}
