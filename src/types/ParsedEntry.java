package com.qin.types;

/**
 * Parsed entry point information (Java 25 Record)
 * 
 * @param srcDir    Source directory path
 * @param className Class name (fully qualified)
 * @param filePath  Full file path
 */
public record ParsedEntry(
        String srcDir,
        String className,
        String filePath) {

    /**
     * Compact Constructor with validation
     */
    public ParsedEntry {
        if (srcDir == null || srcDir.isBlank()) {
            srcDir = "src/main/java";
        }
        if (className == null || className.isBlank()) {
            className = "Main";
        }
    }

    // Compatibility getters
    public String getSrcDir() {
        return srcDir;
    }

    public String getClassName() {
        return className;
    }

    public String getFilePath() {
        return filePath;
    }
}
