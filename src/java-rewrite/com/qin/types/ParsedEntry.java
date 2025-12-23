package com.qin.types;

/**
 * Parsed entry point information
 */
public class ParsedEntry {
    /** Source directory path */
    private final String srcDir;
    
    /** Class name without .java extension */
    private final String className;
    
    /** Full file path */
    private final String filePath;

    public ParsedEntry(String srcDir, String className, String filePath) {
        this.srcDir = srcDir;
        this.className = className;
        this.filePath = filePath;
    }

    public String getSrcDir() { return srcDir; }
    public String getClassName() { return className; }
    public String getFilePath() { return filePath; }
}
