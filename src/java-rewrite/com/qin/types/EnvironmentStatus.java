package com.qin.types;

/**
 * Environment check status
 */
public class EnvironmentStatus {
    private final boolean coursier;
    private final boolean javac;
    private final boolean java;
    private final boolean ready;

    public EnvironmentStatus(boolean coursier, boolean javac, boolean java) {
        this.coursier = coursier;
        this.javac = javac;
        this.java = java;
        this.ready = coursier && javac && java;
    }

    public boolean hasCoursier() { return coursier; }
    public boolean hasJavac() { return javac; }
    public boolean hasJava() { return java; }
    public boolean isReady() { return ready; }
}
