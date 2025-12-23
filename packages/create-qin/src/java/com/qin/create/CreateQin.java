package com.qin.create;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * create-qin - 项目脚手架工具
 * 
 * 功能：
 * - 交互式创建新项目
 * - 支持多种语言模板 (Java, Bun, Node)
 * - 支持多种项目类型 (fullstack, monorepo)
 */
public class CreateQin {
    private static final String VERSION = "0.1.0";
    
    // 支持的语言
    private static final Map<String, String> LANGUAGES = new LinkedHashMap<>();
    static {
        LANGUAGES.put("java", "Java (Spring Boot)");
        LANGUAGES.put("bun", "Bun (Hono/Elysia)");
        LANGUAGES.put("node", "Node.js (Express/Fastify)");
    }
    
    // 各语言的项目类型
    private static final Map<String, Map<String, String>> TEMPLATES = new HashMap<>();
    static {
        Map<String, String> javaTemplates = new LinkedHashMap<>();
        javaTemplates.put("fullstack", "全栈项目 (Spring Boot + Vite)");
        javaTemplates.put("monorepo", "Monorepo 多包项目");
        javaTemplates.put("mono-fullstack", "Monorepo 全栈项目");
        TEMPLATES.put("java", javaTemplates);
        
        Map<String, String> bunTemplates = new LinkedHashMap<>();
        bunTemplates.put("fullstack", "全栈项目 (Hono + Vite)");
        bunTemplates.put("monorepo", "Monorepo 多包项目");
        bunTemplates.put("mono-fullstack", "Monorepo 全栈项目");
        TEMPLATES.put("bun", bunTemplates);
        
        Map<String, String> nodeTemplates = new LinkedHashMap<>();
        nodeTemplates.put("fullstack", "全栈项目 (Express + Vite)");
        nodeTemplates.put("monorepo", "Monorepo 多包项目");
        nodeTemplates.put("mono-fullstack", "Monorepo 全栈项目");
        TEMPLATES.put("node", nodeTemplates);
    }

    // ANSI 颜色
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
        // 解析参数
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

        // 1. 获取项目名称
        if (projectName == null && !skipPrompts) {
            System.out.print(CYAN + "?" + RESET + " 项目名称: ");
            projectName = scanner.nextLine().trim();
        }
        if (projectName == null || projectName.isEmpty()) {
            projectName = "my-qin-app";
        }

        // 2. 选择语言
        if (language == null && !skipPrompts) {
            language = selectLanguage(scanner);
        }
        if (language == null) {
            language = "java";
        }

        // 3. 选择项目类型
        if (template == null && !skipPrompts) {
            template = selectTemplate(scanner, language);
        }
        if (template == null) {
            template = "fullstack";
        }

        System.out.println();
        info("创建项目: " + projectName);
        info("语言: " + LANGUAGES.get(language));
        info("类型: " + TEMPLATES.get(language).get(template));
        System.out.println();

        createProject(projectName, language, template);
    }

    private void banner() {
        System.out.println(CYAN + BRIGHT + """
            
               ██████╗ ██╗███╗   ██╗
              ██╔═══██╗██║████╗  ██║
              ██║   ██║██║██╔██╗ ██║
              ██║▄▄ ██║██║██║╚██╗██║
              ╚██████╔╝██║██║ ╚████║
               ╚══▀▀═╝ ╚═╝╚═╝  ╚═══╝
            """ + RESET + "\n  " + DIM + "新一代跨语言构建工具" + RESET + "\n");
    }

    private String selectLanguage(Scanner scanner) {
        System.out.println("\n选择语言:\n");
        List<String> keys = new ArrayList<>(LANGUAGES.keySet());
        for (int i = 0; i < keys.size(); i++) {
            System.out.println("  " + CYAN + (i + 1) + RESET + ") " + LANGUAGES.get(keys.get(i)));
        }
        System.out.println();

        System.out.print("请选择 (1-" + keys.size() + ") [1]: ");
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

        System.out.println("\n选择项目类型:\n");
        for (int i = 0; i < keys.size(); i++) {
            System.out.println("  " + CYAN + (i + 1) + RESET + ") " + templates.get(keys.get(i)));
        }
        System.out.println();

        System.out.print("请选择 (1-" + keys.size() + ") [1]: ");
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
            throw new IOException("目录 " + name + " 已存在");
        }

        // 创建项目目录
        Files.createDirectories(targetDir);

        // 根据语言和模板创建项目结构
        switch (language) {
            case "java" -> createJavaProject(targetDir, name, template);
            case "bun" -> createBunProject(targetDir, name, template);
            case "node" -> createNodeProject(targetDir, name, template);
        }

        success("项目 " + name + " 创建成功!");
        System.out.println();
        info("下一步:");
        System.out.println("  " + CYAN + "cd " + name + RESET);
        System.out.println("  " + CYAN + "qin run" + RESET);
        System.out.println();
    }

    private void createJavaProject(Path targetDir, String name, String template) throws IOException {
        // 创建 src 目录
        Files.createDirectories(targetDir.resolve("src"));

        // 创建 Main.java
        Files.writeString(targetDir.resolve("src/Main.java"), """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello from %s!");
                }
            }
            """.formatted(name));

        // 创建 qin.config.json
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
        Files.writeString(targetDir.resolve("qin.config.json"), config);

        // 创建 .gitignore
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
        // 创建 src 目录
        Files.createDirectories(targetDir.resolve("src"));

        // 创建 index.ts
        Files.writeString(targetDir.resolve("src/index.ts"), """
            import { Hono } from 'hono'
            
            const app = new Hono()
            
            app.get('/', (c) => c.text('Hello from %s!'))
            
            export default app
            """.formatted(name));

        // 创建 package.json
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

        // 创建 .gitignore
        Files.writeString(targetDir.resolve(".gitignore"), """
            node_modules/
            dist/
            .DS_Store
            """);
    }

    private void createNodeProject(Path targetDir, String name, String template) throws IOException {
        // 创建 src 目录
        Files.createDirectories(targetDir.resolve("src"));

        // 创建 index.js
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

        // 创建 package.json
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

        // 创建 .gitignore
        Files.writeString(targetDir.resolve(".gitignore"), """
            node_modules/
            dist/
            .DS_Store
            """);
    }

    private void createFrontendDir(Path targetDir) throws IOException {
        Path clientDir = targetDir.resolve("src/client");
        Files.createDirectories(clientDir);

        // 创建 index.html
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

        // 创建 main.js
        Files.writeString(clientDir.resolve("main.js"), """
            document.getElementById('app').innerHTML = '<h1>Hello, Qin!</h1>'
            """);
    }

    private void createMonorepoStructure(Path targetDir, String name) throws IOException {
        // 创建 packages 目录
        Files.createDirectories(targetDir.resolve("packages"));
        Files.createDirectories(targetDir.resolve("apps"));

        // 更新根配置为 workspace
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
        Files.writeString(targetDir.resolve("qin.config.json"), config);
    }

    private void showHelp() {
        System.out.println("""
            
            Usage: create-qin [project-name] [options]
            
            Options:
              -java               使用 Java (Spring Boot)
              -bun                使用 Bun (Hono/Elysia)
              -node               使用 Node.js (Express/Fastify)
              -t, --template <t>  项目类型: fullstack, monorepo, mono-fullstack
              -y, --yes           跳过交互，使用默认值
              -h, --help          显示帮助
              -v, --version       显示版本
            
            Examples:
              # 交互式创建
              create-qin
            
              # 创建 Java 全栈项目
              create-qin my-app -java
            
              # 创建 Java Monorepo 项目
              create-qin my-app -java -t monorepo
            """);
    }

    private void success(String msg) {
        System.out.println(GREEN + "✓" + RESET + " " + msg);
    }

    private void info(String msg) {
        System.out.println(CYAN + "ℹ" + RESET + " " + msg);
    }
}
