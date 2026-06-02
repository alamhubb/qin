package com.qin.create;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * create-qin - 椤圭洰鑴氭墜鏋跺伐鍏?
 * 
 * 鍔熻兘锛?
 * - 浜や簰寮忓垱寤烘柊椤圭洰
 * - 鏀寔澶氱璇█妯℃澘 (Java, Bun, Node)
 * - 鏀寔澶氱椤圭洰绫诲瀷 (fullstack, monorepo)
 */
public class CreateQin {
    private static final String VERSION = "0.1.0";
    
    // 鏀寔鐨勮瑷€
    private static final Map<String, String> LANGUAGES = new LinkedHashMap<>();
    static {
        LANGUAGES.put("java", "Java (Spring Boot)");
        LANGUAGES.put("bun", "Bun (Hono/Elysia)");
        LANGUAGES.put("node", "Node.js (Express/Fastify)");
    }
    
    // 鍚勮瑷€鐨勯」鐩被鍨?
    private static final Map<String, Map<String, String>> TEMPLATES = new HashMap<>();
    static {
        Map<String, String> javaTemplates = new LinkedHashMap<>();
        javaTemplates.put("fullstack", "鍏ㄦ爤椤圭洰 (Spring Boot + Vite)");
        javaTemplates.put("monorepo", "Monorepo 澶氬寘椤圭洰");
        javaTemplates.put("mono-fullstack", "Monorepo 鍏ㄦ爤椤圭洰");
        TEMPLATES.put("java", javaTemplates);
        
        Map<String, String> bunTemplates = new LinkedHashMap<>();
        bunTemplates.put("fullstack", "鍏ㄦ爤椤圭洰 (Hono + Vite)");
        bunTemplates.put("monorepo", "Monorepo 澶氬寘椤圭洰");
        bunTemplates.put("mono-fullstack", "Monorepo 鍏ㄦ爤椤圭洰");
        TEMPLATES.put("bun", bunTemplates);
        
        Map<String, String> nodeTemplates = new LinkedHashMap<>();
        nodeTemplates.put("fullstack", "鍏ㄦ爤椤圭洰 (Express + Vite)");
        nodeTemplates.put("monorepo", "Monorepo 澶氬寘椤圭洰");
        nodeTemplates.put("mono-fullstack", "Monorepo 鍏ㄦ爤椤圭洰");
        TEMPLATES.put("node", nodeTemplates);
    }

    // ANSI 棰滆壊
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String DIM = "\u001B[2m";
    private static final String BRIGHT = "\u001B[1m";

    public static void main(String[] args) {
        try {
            new CreateQin().run(args);
        } catch (Exception e) {
            System.err.println(RED + "Error: " + e.getMessage() + RESET);
            System.exit(1);
        }
    }

    public void run(String[] args) throws Exception {
        // 瑙ｆ瀽鍙傛暟
        String projectName = null;
        String language = null;
        String template = null;
        boolean skipPrompts = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-java", "--java" -> language = "java";
                case "-bun", "--bun" -> language = "bun";
                case "-node", "--node" -> language = "node";
                case "-t", "--template" -> {
                    if (i + 1 < args.length) template = args[++i];
                }
                case "-y", "--yes" -> skipPrompts = true;
                case "-h", "--help" -> {
                    showHelp();
                    return;
                }
                case "-v", "--version" -> {
                    System.out.println("create-qin " + VERSION);
                    return;
                }
                default -> {
                    if (!arg.startsWith("-")) {
                        projectName = arg;
                    }
                }
            }
        }

        banner();

        Scanner scanner = new Scanner(System.in);

        // 1. 鑾峰彇椤圭洰鍚嶇О
        if (projectName == null && !skipPrompts) {
            System.out.print(CYAN + "?" + RESET + " 椤圭洰鍚嶇О: ");
            projectName = scanner.nextLine().trim();
        }
        if (projectName == null || projectName.isEmpty()) {
            projectName = "my-qin-app";
        }

        // 2. 閫夋嫨璇█
        if (language == null && !skipPrompts) {
            language = selectLanguage(scanner);
        }
        if (language == null) {
            language = "java";
        }

        // 3. 閫夋嫨椤圭洰绫诲瀷
        if (template == null && !skipPrompts) {
            template = selectTemplate(scanner, language);
        }
        if (template == null) {
            template = "fullstack";
        }

        System.out.println();
        info("鍒涘缓椤圭洰: " + projectName);
        info("璇█: " + LANGUAGES.get(language));
        info("绫诲瀷: " + TEMPLATES.get(language).get(template));
        System.out.println();

        createProject(projectName, language, template);
    }

    private void banner() {
        System.out.println(CYAN + BRIGHT + """
            
               鈻堚枅鈻堚枅鈻堚枅鈺?鈻堚枅鈺椻枅鈻堚枅鈺?  鈻堚枅鈺?
              鈻堚枅鈺斺晲鈺愨晲鈻堚枅鈺椻枅鈻堚晳鈻堚枅鈻堚枅鈺? 鈻堚枅鈺?
              鈻堚枅鈺?  鈻堚枅鈺戔枅鈻堚晳鈻堚枅鈺斺枅鈻堚晽 鈻堚枅鈺?
              鈻堚枅鈺戔杽鈻?鈻堚枅鈺戔枅鈻堚晳鈻堚枅鈺戔暁鈻堚枅鈺椻枅鈻堚晳
              鈺氣枅鈻堚枅鈻堚枅鈻堚晹鈺濃枅鈻堚晳鈻堚枅鈺?鈺氣枅鈻堚枅鈻堚晳
               鈺氣晲鈺愨杸鈻€鈺愨暆 鈺氣晲鈺濃暁鈺愨暆  鈺氣晲鈺愨晲鈺?
            """ + RESET + "\n  " + DIM + "鏂颁竴浠ｈ法璇█鏋勫缓宸ュ叿" + RESET + "\n");
    }

    private String selectLanguage(Scanner scanner) {
        System.out.println("\n閫夋嫨璇█:\n");
        List<String> keys = new ArrayList<>(LANGUAGES.keySet());
        for (int i = 0; i < keys.size(); i++) {
            System.out.println("  " + CYAN + (i + 1) + RESET + ") " + LANGUAGES.get(keys.get(i)));
        }
        System.out.println();

        System.out.print("璇烽€夋嫨 (1-" + keys.size() + ") [1]: ");
        String answer = scanner.nextLine().trim();
        int index = answer.isEmpty() ? 0 : Integer.parseInt(answer) - 1;

        if (index >= 0 && index < keys.size()) {
            return keys.get(index);
        }
        return "java";
    }

    private String selectTemplate(Scanner scanner, String language) {
        Map<String, String> templates = TEMPLATES.get(language);
        List<String> keys = new ArrayList<>(templates.keySet());

        System.out.println("\n閫夋嫨椤圭洰绫诲瀷:\n");
        for (int i = 0; i < keys.size(); i++) {
            System.out.println("  " + CYAN + (i + 1) + RESET + ") " + templates.get(keys.get(i)));
        }
        System.out.println();

        System.out.print("璇烽€夋嫨 (1-" + keys.size() + ") [1]: ");
        String answer = scanner.nextLine().trim();
        int index = answer.isEmpty() ? 0 : Integer.parseInt(answer) - 1;

        if (index >= 0 && index < keys.size()) {
            return keys.get(index);
        }
        return "fullstack";
    }

    private void createProject(String name, String language, String template) throws IOException {
        Path targetDir = Paths.get(System.getProperty("user.dir"), name);

        if (Files.exists(targetDir)) {
            throw new IOException("鐩綍 " + name + " 宸插瓨鍦?);
        }

        // 鍒涘缓椤圭洰鐩綍
        Files.createDirectories(targetDir);

        // 鏍规嵁璇█鍜屾ā鏉垮垱寤洪」鐩粨鏋?
        switch (language) {
            case "java" -> createJavaProject(targetDir, name, template);
            case "bun" -> createBunProject(targetDir, name, template);
            case "node" -> createNodeProject(targetDir, name, template);
        }

        success("椤圭洰 " + name + " 鍒涘缓鎴愬姛!");
        System.out.println();
        info("涓嬩竴姝?");
        System.out.println("  " + CYAN + "cd " + name + RESET);
        System.out.println("  " + CYAN + "qin run" + RESET);
        System.out.println();
    }

    private void createJavaProject(Path targetDir, String name, String template) throws IOException {
        // 鍒涘缓 src 鐩綍
        Files.createDirectories(targetDir.resolve("src"));

        // 鍒涘缓 Main.java
        Files.writeString(targetDir.resolve("src/Main.java"), """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello from %s!");
                }
            }
            """.formatted(name));

        // 鍒涘缓 qin.config.js
        String config = """
            {
              "name": "%s",
              "version": "1.0.0",
              "entry": "src/Main.java",
              "dependencies": {},
              "repositories": [
                "https://maven.aliyun.com/repository/public"
              ]
            }
            """.formatted(name);
        Files.writeString(targetDir.resolve("qin.config.js"), config);

        // 鍒涘缓 .gitignore
        Files.writeString(targetDir.resolve(".gitignore"), """
            build/
            dist/
            repository/
            .idea/
            *.iml
            """);

        if ("fullstack".equals(template) || "mono-fullstack".equals(template)) {
            createFrontendDir(targetDir);
        }

        if ("monorepo".equals(template) || "mono-fullstack".equals(template)) {
            createMonorepoStructure(targetDir, name);
        }
    }

    private void createBunProject(Path targetDir, String name, String template) throws IOException {
        // 鍒涘缓 src 鐩綍
        Files.createDirectories(targetDir.resolve("src"));

        // 鍒涘缓 index.ts
        Files.writeString(targetDir.resolve("src/index.ts"), """
            import { Hono } from 'hono'
            
            const app = new Hono()
            
            app.get('/', (c) => c.text('Hello from %s!'))
            
            export default app
            """.formatted(name));

        // 鍒涘缓 package.json
        String packageJson = """
            {
              "name": "%s",
              "version": "1.0.0",
              "scripts": {
                "dev": "bun run --hot src/index.ts",
                "start": "bun run src/index.ts"
              },
              "dependencies": {
                "hono": "^4.0.0"
              }
            }
            """.formatted(name);
        Files.writeString(targetDir.resolve("package.json"), packageJson);

        // 鍒涘缓 .gitignore
        Files.writeString(targetDir.resolve(".gitignore"), """
            node_modules/
            dist/
            .DS_Store
            """);
    }

    private void createNodeProject(Path targetDir, String name, String template) throws IOException {
        // 鍒涘缓 src 鐩綍
        Files.createDirectories(targetDir.resolve("src"));

        // 鍒涘缓 index.js
        Files.writeString(targetDir.resolve("src/index.js"), """
            const express = require('express')
            const app = express()
            
            app.get('/', (req, res) => {
              res.send('Hello from %s!')
            })
            
            app.listen(3000, () => {
              console.log('Server running on http://localhost:3000')
            })
            """.formatted(name));

        // 鍒涘缓 package.json
        String packageJson = """
            {
              "name": "%s",
              "version": "1.0.0",
              "scripts": {
                "dev": "node --watch src/index.js",
                "start": "node src/index.js"
              },
              "dependencies": {
                "express": "^4.18.0"
              }
            }
            """.formatted(name);
        Files.writeString(targetDir.resolve("package.json"), packageJson);

        // 鍒涘缓 .gitignore
        Files.writeString(targetDir.resolve(".gitignore"), """
            node_modules/
            dist/
            .DS_Store
            """);
    }

    private void createFrontendDir(Path targetDir) throws IOException {
        Path clientDir = targetDir.resolve("src/client");
        Files.createDirectories(clientDir);

        // 鍒涘缓 index.html
        Files.writeString(clientDir.resolve("index.html"), """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Qin App</title>
            </head>
            <body>
              <div id="app"></div>
              <script type="module" src="/main.js"></script>
            </body>
            </html>
            """);

        // 鍒涘缓 main.js
        Files.writeString(clientDir.resolve("main.js"), """
            document.getElementById('app').innerHTML = '<h1>Hello, Qin!</h1>'
            """);
    }

    private void createMonorepoStructure(Path targetDir, String name) throws IOException {
        // 鍒涘缓 packages 鐩綍
        Files.createDirectories(targetDir.resolve("packages"));
        Files.createDirectories(targetDir.resolve("apps"));

        // 鏇存柊鏍归厤缃负 workspace
        String config = """
            {
              "name": "%s",
              "version": "1.0.0",
              "packages": [
                "packages/*",
                "apps/*"
              ]
            }
            """.formatted(name);
        Files.writeString(targetDir.resolve("qin.config.js"), config);
    }

    private void showHelp() {
        System.out.println("""
            
            Usage: create-qin [project-name] [options]
            
            Options:
              -java               浣跨敤 Java (Spring Boot)
              -bun                浣跨敤 Bun (Hono/Elysia)
              -node               浣跨敤 Node.js (Express/Fastify)
              -t, --template <t>  椤圭洰绫诲瀷: fullstack, monorepo, mono-fullstack
              -y, --yes           璺宠繃浜や簰锛屼娇鐢ㄩ粯璁ゅ€?
              -h, --help          鏄剧ず甯姪
              -v, --version       鏄剧ず鐗堟湰
            
            Examples:
              # 浜や簰寮忓垱寤?
              create-qin
            
              # 鍒涘缓 Java 鍏ㄦ爤椤圭洰
              create-qin my-app -java
            
              # 鍒涘缓 Java Monorepo 椤圭洰
              create-qin my-app -java -t monorepo
            """);
    }

    private void success(String msg) {
        System.out.println(GREEN + "鉁? + RESET + " " + msg);
    }

    private void info(String msg) {
        System.out.println(CYAN + "鈩? + RESET + " " + msg);
    }
}

