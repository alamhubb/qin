package com.qin.lang.module.policy;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Enforces zone-based import rules:
 * - app(frontend): allow JS, deny JAVA
 * - main(backend): allow JAVA and JS
 * - shared: deny JAVA and non-local JS
 * - relative local modules (e.g. ./x.js, ../y.js) are treated as LOCAL and allowed
 */
public final class QinImportPolicyChecker {
    public void validate(Path projectRoot, List<QinImportDescriptor> imports) {
        Objects.requireNonNull(imports, "imports cannot be null");
        List<QinImportPolicyViolation> violations = new ArrayList<>();
        for (QinImportDescriptor descriptor : imports) {
            QinSourceZone zone = QinSourceZone.detect(projectRoot, descriptor.sourceFile());
            QinImportPolicyViolation violation = validateOne(zone, descriptor);
            if (violation != null) {
                violations.add(violation);
            }
        }

        if (!violations.isEmpty()) {
            throw new QinImportPolicyException(violations);
        }
    }

    private QinImportPolicyViolation validateOne(QinSourceZone zone, QinImportDescriptor descriptor) {
        QinImportKind kind = descriptor.kind();
        if (kind != QinImportKind.JAVA && kind != QinImportKind.JS) {
            return null;
        }

        return switch (zone) {
            case FRONTEND -> validateFrontend(descriptor);
            case BACKEND -> validateBackend(descriptor);
            case SHARED -> validateShared(descriptor);
            case UNKNOWN -> null;
        };
    }

    private QinImportPolicyViolation validateFrontend(QinImportDescriptor descriptor) {
        if (descriptor.kind() == QinImportKind.JAVA) {
            return violation(
                    descriptor,
                    QinSourceZone.FRONTEND,
                    "QIN1001",
                    "frontend(app/) cannot import java modules: " + descriptor.moduleSpecifier());
        }
        return null;
    }

    private QinImportPolicyViolation validateBackend(QinImportDescriptor descriptor) {
        // Backend accepts both JAVA and JS imports.
        return null;
    }

    private QinImportPolicyViolation validateShared(QinImportDescriptor descriptor) {
        return violation(
                descriptor,
                QinSourceZone.SHARED,
                "QIN1003",
                "shared(shared/) cannot import java/js modules: " + descriptor.moduleSpecifier());
    }

    private QinImportPolicyViolation violation(
            QinImportDescriptor descriptor,
            QinSourceZone zone,
            String ruleCode,
            String message) {
        return new QinImportPolicyViolation(
                descriptor.sourceFile(),
                zone,
                descriptor.moduleSpecifier(),
                descriptor.kind(),
                descriptor.line(),
                descriptor.column(),
                ruleCode,
                message);
    }
}
