package com.qin.lang.backend.js;

public enum QinIrCodegenTarget {
    JAVASCRIPT("js", false),
    TYPESCRIPT("ts", true);

    private final String fileExtension;
    private final boolean typeAnnotations;

    QinIrCodegenTarget(String fileExtension, boolean typeAnnotations) {
        this.fileExtension = fileExtension;
        this.typeAnnotations = typeAnnotations;
    }

    public String fileExtension() {
        return fileExtension;
    }

    public boolean typeAnnotations() {
        return typeAnnotations;
    }
}
