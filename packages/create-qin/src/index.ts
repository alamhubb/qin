#!/usr/bin/env node
import * as fs from "fs";
import * as path from "path";
import * as readline from "readline";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// 支持的语言
const LANGUAGES = {
  java: "Java (Spring Boot)",
  bun: "Bun (Hono/Elysia)",
  node: "Node.js (Express/Fastify)",
} as const;

// 各语言的项目类型
const TEMPLATES: Record<string, Record<string, string>> = {
  java: {
    fullstack: "全栈项目 (Spring Boot + Vite)",
    monorepo: "Monorepo 多包项目",
    "mono-fullstack": "Monorepo 全栈项目",
  },
  bun: {
    fullstack: "全栈项目 (Hono + Vite)",
    monorepo: "Monorepo 多包项目",
    "mono-fullstack": "Monorepo 全栈项目",
  },
  node: {
    fullstack: "全栈项目 (Express + Vite)",
    monorepo: "Monorepo 多包项目",
    "mono-fullstack": "Monorepo 全栈项目",
  },
};

type LanguageType = keyof typeof LANGUAGES;

interface ProjectOptions {
  name: string;
  language: LanguageType;
  template: string;
}

const colors = {
  reset: "\x1b[0m",
  bright: "\x1b[1m",
  cyan: "\x1b[36m",
  green: "\x1b[32m",
  yellow: "\x1b[33m",
  red: "\x1b[31m",
  dim: "\x1b[2m",
};

function log(msg: string) {
  console.log(msg);
}

function success(msg: string) {
  console.log(`${colors.green}✓${colors.reset} ${msg}`);
}

function info(msg: string) {
  console.log(`${colors.cyan}ℹ${colors.reset} ${msg}`);
}

function banner() {
  console.log(`
${colors.cyan}${colors.bright}
   ██████╗ ██╗███╗   ██╗
  ██╔═══██╗██║████╗  ██║
  ██║   ██║██║██╔██╗ ██║
  ██║▄▄ ██║██║██║╚██╗██║
  ╚██████╔╝██║██║ ╚████║
   ╚══▀▀═╝ ╚═╝╚═╝  ╚═══╝
${colors.reset}
  ${colors.dim}新一代跨语言构建工具${colors.reset}
`);
}

async function prompt(question: string): Promise<string> {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  return new Promise((resolve) => {
    rl.question(question, (answer) => {
      rl.close();
      resolve(answer.trim());
    });
  });
}

async function selectLanguage(): Promise<LanguageType> {
  log("\n选择语言:\n");
  const keys = Object.keys(LANGUAGES) as LanguageType[];
  keys.forEach((key, i) => {
    log(`  ${colors.cyan}${i + 1}${colors.reset}) ${LANGUAGES[key]}`);
  });
  log("");

  const answer = await prompt(`请选择 (1-${keys.length}) [1]: `);
  const index = parseInt(answer || "1") - 1;

  if (index >= 0 && index < keys.length) {
    return keys[index];
  }
  return "java";
}

async function selectTemplate(language: LanguageType): Promise<string> {
  const templates = TEMPLATES[language];
  const keys = Object.keys(templates);

  log("\n选择项目类型:\n");
  keys.forEach((key, i) => {
    log(`  ${colors.cyan}${i + 1}${colors.reset}) ${templates[key]}`);
  });
  log("");

  const answer = await prompt(`请选择 (1-${keys.length}) [1]: `);
  const index = parseInt(answer || "1") - 1;

  if (index >= 0 && index < keys.length) {
    return keys[index];
  }
  return "fullstack";
}

function getTemplateDir(language: string, template: string): string {
  // 模板路径: templates/{language}/{template}
  const devPath = path.resolve(__dirname, "..", "templates", language, template);
  const distPath = path.resolve(__dirname, "..", "..", "templates", language, template);

  if (fs.existsSync(devPath)) {
    return devPath;
  }
  return distPath;
}

function copyDir(src: string, dest: string, projectName: string) {
  fs.mkdirSync(dest, { recursive: true });

  const entries = fs.readdirSync(src, { withFileTypes: true });

  for (const entry of entries) {
    const srcPath = path.join(src, entry.name);
    const destPath = path.join(dest, entry.name);

    if (entry.isDirectory()) {
      copyDir(srcPath, destPath, projectName);
    } else {
      let content = fs.readFileSync(srcPath, "utf-8");
      content = content.replace(/\{\{name\}\}/g, projectName);
      fs.writeFileSync(destPath, content);
    }
  }
}

function createProject(options: ProjectOptions) {
  const { name, language, template } = options;
  const targetDir = path.resolve(process.cwd(), name);

  if (fs.existsSync(targetDir)) {
    console.error(`${colors.red}错误: 目录 ${name} 已存在${colors.reset}`);
    process.exit(1);
  }

  const templateDir = getTemplateDir(language, template);

  if (!fs.existsSync(templateDir)) {
    console.error(`${colors.red}错误: 模板 ${language}/${template} 不存在${colors.reset}`);
    console.error(`${colors.dim}(该语言/模板组合尚未实现)${colors.reset}`);
    process.exit(1);
  }

  copyDir(templateDir, targetDir, name);

  success(`项目 ${name} 创建成功!`);
  log("");
  info("下一步:");
  log(`  ${colors.cyan}cd ${name}${colors.reset}`);
  log(`  ${colors.cyan}qin run${colors.reset}`);
  log("");
}

function showHelp() {
  console.log(`
${colors.bright}Usage:${colors.reset} create-qin [project-name] [options]

${colors.bright}Options:${colors.reset}
  -java               使用 Java (Spring Boot)
  -bun                使用 Bun (Hono/Elysia)
  -node               使用 Node.js (Express/Fastify)
  -t, --template <t>  项目类型: fullstack, monorepo, mono-fullstack
  -y, --yes           跳过交互，使用默认值
  -h, --help          显示帮助

${colors.bright}Examples:${colors.reset}
  ${colors.dim}# 交互式创建${colors.reset}
  npm create qin@latest

  ${colors.dim}# 创建 Java 全栈项目${colors.reset}
  npm create qin@latest my-app -java

  ${colors.dim}# 创建 Java Monorepo 项目${colors.reset}
  npm create qin@latest my-app -java -t monorepo

  ${colors.dim}# 创建 Bun 全栈项目${colors.reset}
  npm create qin@latest my-app -bun

  ${colors.dim}# 跳过所有交互${colors.reset}
  npm create qin@latest my-app -java -t fullstack -y
`);
}

async function main() {
  const args = process.argv.slice(2);

  // 解析参数
  let projectName = "";
  let language: LanguageType | null = null;
  let template: string | null = null;
  let skipPrompts = false;

  for (let i = 0; i < args.length; i++) {
    const arg = args[i];
    if (arg === "-java" || arg === "--java") {
      language = "java";
    } else if (arg === "-bun" || arg === "--bun") {
      language = "bun";
    } else if (arg === "-node" || arg === "--node") {
      language = "node";
    } else if (arg === "-t" || arg === "--template") {
      template = args[++i];
    } else if (arg === "-y" || arg === "--yes") {
      skipPrompts = true;
    } else if (arg === "-h" || arg === "--help") {
      showHelp();
      process.exit(0);
    } else if (!arg.startsWith("-")) {
      projectName = arg;
    }
  }

  banner();

  // 1. 获取项目名称
  if (!projectName && !skipPrompts) {
    projectName = await prompt(`${colors.cyan}?${colors.reset} 项目名称: `);
  }
  if (!projectName) {
    projectName = "my-qin-app";
  }

  // 2. 选择语言
  if (!language && !skipPrompts) {
    language = await selectLanguage();
  }
  if (!language) {
    language = "java";
  }

  // 3. 选择项目类型
  if (!template && !skipPrompts) {
    template = await selectTemplate(language);
  }
  if (!template) {
    template = "fullstack";
  }

  log("");
  info(`创建项目: ${projectName}`);
  info(`语言: ${LANGUAGES[language]}`);
  info(`类型: ${TEMPLATES[language][template] || template}`);
  log("");

  createProject({ name: projectName, language, template });
}

main().catch(console.error);
