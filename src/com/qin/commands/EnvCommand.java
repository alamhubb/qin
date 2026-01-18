package com.qin.commands;

import com.qin.core.EnvironmentChecker;
import com.qin.types.EnvironmentStatus;

/**
 * Environment check command
 */
public class EnvCommand {
    
    public static void execute() {
        EnvironmentChecker checker = new EnvironmentChecker();
        EnvironmentStatus status = checker.checkAll();

        System.out.println("\nQin Environment Check\n");
        System.out.println("  Java:     " + (status.hasJava() ? "✓" : "✗"));
        System.out.println("  Javac:    " + (status.hasJavac() ? "✓" : "✗"));
        System.out.println("  Coursier: " + (status.hasCoursier() ? "✓" : "✗"));
        System.out.println();

        if (status.isReady()) {
            System.out.println("✓ All dependencies are installed");
        } else {
            System.out.println("✗ Some dependencies are missing\n");

            if (!status.hasJavac()) {
                System.out.println("To install JDK:");
                System.out.println(checker.getInstallGuide("javac"));
            }

            if (!status.hasCoursier()) {
                System.out.println("To install Coursier:");
                System.out.println(checker.getInstallGuide("coursier"));
            }
        }
    }

    /**
     * Show detailed environment info
     */
    public static void showInfo() {
        System.out.println("\nSystem Information\n");
        System.out.println("  OS:        " + System.getProperty("os.name"));
        System.out.println("  Arch:      " + System.getProperty("os.arch"));
        System.out.println("  Java Home: " + System.getProperty("java.home"));
        System.out.println("  Java Ver:  " + System.getProperty("java.version"));
        System.out.println("  User Dir:  " + System.getProperty("user.dir"));
        System.out.println();

        // Check JAVA_HOME
        String javaHome = System.getenv("JAVA_HOME");
        if (javaHome != null) {
            System.out.println("  JAVA_HOME: " + javaHome);
        } else {
            System.out.println("  JAVA_HOME: (not set)");
        }

        // Check PATH for java
        String path = System.getenv("PATH");
        if (path != null && path.toLowerCase().contains("java")) {
            System.out.println("  PATH:      Contains Java");
        }

        System.out.println();
        execute();
    }
}
