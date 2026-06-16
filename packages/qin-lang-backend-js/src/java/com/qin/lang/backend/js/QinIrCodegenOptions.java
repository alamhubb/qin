package com.qin.lang.backend.js;

import java.util.Objects;

public record QinIrCodegenOptions(QinIrCodegenTarget target) {
    public QinIrCodegenOptions {
        Objects.requireNonNull(target, "target cannot be null");
    }

    public static QinIrCodegenOptions javaScript() {
        return new QinIrCodegenOptions(QinIrCodegenTarget.JAVASCRIPT);
    }

    public static QinIrCodegenOptions typeScript() {
        return new QinIrCodegenOptions(QinIrCodegenTarget.TYPESCRIPT);
    }

    public boolean emitTypeAnnotations() {
        return target.typeAnnotations();
    }

    public String fileExtension() {
        return target.fileExtension();
    }
}
