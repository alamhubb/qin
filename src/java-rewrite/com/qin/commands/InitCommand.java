package com.qin.commands;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Initialize a new Qin project
 */
public class InitCommand {
    
    public static void execute() throws IOException {
        execute(System.getProperty("user.dir"));
    }

    public static void execute(String projectDir) throws IOException {
        Path cwd = Paths.get(projectDir);
        
        // Create src directory
        Files.createDirectories(cwd.resolve("src"));
        
        // Create Main.java if not exists
        Path mainJava = cwd.resolve("src/Main.java");
        if (!Files.exists(mainJava)) {
            Files.writeString(mainJava, """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("Hello, Qin!");
                    }
                }
                """);
            System.out.println("  Created src/Main.java");
        }

        // Create qin.config.json if not exists
        Path configFile = cwd.resolve("qin.config.json");
        if (!Files.exists(configFile)) {
            String projectName = cwd.getFileName().toString();
            Files.writeString(configFile, String.format("""
                {
                  "name": "%s",
                  "version": "1.0.0",
                  "entry": "src/Main.java",
                  "dependencies": {},
                  "repositories": [
                    "https://maven.aliyun.com/repository/public",
                    "https://repo1.maven.org/maven2"
                  ]
                }
                """, projectName));
            System.out.println("  Created qin.config.json");
        }

        // Create .gitignore if not exists
        Path gitignore = cwd.resolve(".gitignore");
        if (!Files.exists(gitignore)) {
            Files.writeString(gitignore, """
                # Build outputs
                build/
                dist/
                
                # Dependencies
                repository/
                
                # IDE
                .idea/
                *.iml
                .vscode/
                
                # OS
                .DS_Store
                Thumbs.db
                """);
            System.out.println("  Created .gitignore");
        }
    }

    /**
     * Interactive project initialization
     */
    public static void executeInteractive() throws IOException {
        Scanner scanner = new Scanner(System.in);
        Path cwd = Paths.get(System.getProperty("user.dir"));
        String defaultName = cwd.getFileName().toString();

        System.out.print("Project name (" + defaultName + "): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = defaultName;
        }

        System.out.print("Version (1.0.0): ");
        String version = scanner.nextLine().trim();
        if (version.isEmpty()) {
            version = "1.0.0";
        }

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        // Create directories
        Files.createDirectories(cwd.resolve("src"));

        // Create Main.java
        Path mainJava = cwd.resolve("src/Main.java");
        if (!Files.exists(mainJava)) {
            Files.writeString(mainJava, String.format("""
                /**
                 * %s
                 */
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("Hello from %s!");
                    }
                }
                """, description.isEmpty() ? name : description, name));
        }

        // Create config
        Path configFile = cwd.resolve("qin.config.json");
        StringBuilder config = new StringBuilder();
        config.append("{\n");
        config.append("  \"name\": \"").append(name).append("\",\n");
        config.append("  \"version\": \"").append(version).append("\",\n");
        if (!description.isEmpty()) {
            config.append("  \"description\": \"").append(description).append("\",\n");
        }
        config.append("  \"entry\": \"src/Main.java\",\n");
        config.append("  \"dependencies\": {},\n");
        config.append("  \"repositories\": [\n");
        config.append("    \"https://maven.aliyun.com/repository/public\"\n");
        config.append("  ]\n");
        config.append("}\n");
        
        Files.writeString(configFile, config.toString());

        System.out.println("\n✓ Project initialized!");
        System.out.println("  Run 'qin run' to start");
    }
}
