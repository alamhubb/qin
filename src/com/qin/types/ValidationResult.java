package com.qin.types;

import java.util.List;
import java.util.ArrayList;

/**
 * Validation result for configuration
 */
public class ValidationResult {
    private final boolean valid;
    private final List<String> errors;

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors != null ? errors : new ArrayList<>();
    }

    public static ValidationResult success() {
        return new ValidationResult(true, new ArrayList<>());
    }

    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, errors);
    }

    public boolean isValid() { return valid; }
    public List<String> getErrors() { return errors; }
}
