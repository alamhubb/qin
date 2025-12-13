/**
 * Init Command for Qin
 * 交互式创建新项目
 */

import { join, basename } from "path";
import { mkdir, writeFile, access } from "fs/promises";
import chalk from "chalk";
import * as readline from "readline";

/**
 * 项目模板类型
 */
type ProjectTemplate = "java" | "java-fullstack" | "bun" | "empty";

/**
 * 模板配置
 */
const TEMPLATES: Record<ProjectTemplate, {
  name: string;
  description: string;
  files: Record<string, string>;
}> = {
  java: {
    name: "Java",
    description: "纯 Java 后端项目",
    files: {
      "qin.config.ts": `import { defineConfig } from "qin";

export default defineConfig({
  entry: "src/Main.java",
});
`,
      "src/Main.java": `public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Qin!");
    }
}
`,
    },
  },
  "java-fullstack": {
    name: "Java + Vite",
    description: "Java 后端 + Vite 前端全栈项目",
    files: {
      "qin.config.ts": `import { defineConfig } from "qin";

export default defineConfig({
  entry: "src/Main.java",
  client: {
    root: "src/client",
  },
});
`,
      "src/Main.java": `public class Main {
    public static void main(String[] args) {
        System.out.println("Server starting on port 8080...");
        // TODO: Add your web server here (e.g., Javalin, Spring Boot)
    }
}
`,
      "src/client/index.html": `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Qin App</title>
</head>
<body>
  <div id="app">
    <h1>Hello from Qin!</h1>
    <p>Edit src/client/index.html to get started.</p>
  </div>
  <script type="module" src="/main.js"></script>
</body>
</html>
`,
      "src/client/main.js": `console.log("Hello from Vite!");
`,
    },
  },
  bun: {
    name: "Bun/TypeScript",
    description: "纯 Bun/TypeScript 项目",
    files: {
      "qin.config.ts": `import { defineConfig } from "qin";

export default defineConfig({
  // Bun 项目不需要 entry
});
`,
      "src/index.ts": `console.log("Hello from Bun!");
`,
      "package.json": `{
  "name": "{{name}}",
  "type": "module",
  "scripts": {
    "dev": "bun run src/index.ts",
    "build": "bun build src/index.ts --outdir dist"
  }
}
`,
    },
  },
  empty: {
    name: "空项目",
    description: "只创建配置文件",
    files: {
      "qin.config.ts": `import { defineConfig } from "qin";

export default defineConfig({
  // 配置你的项目
});
`,
    },
  },
};

/**
 * 简单的交互式输入
 */
async function prompt(question: string, defaultValue?: string): Promise<string> {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
  });

  return new Promise((resolve) => {
    const q = defaultValue ? `${question} (${defaultValue}): ` : `${question}: `;
    rl.question(q, (answer) => {
      rl.close();
      resolve(answer.trim() || defaultValue || "");
    });
  });
}

/**
 * 选择菜单
 */
async function select(question: string, options: { value: string; label: string }[]): Promise<string> {
  console.log(chalk.blue(question));
  options.forEach((opt, i) => {
    console.log(chalk.gray(`  ${i + 1}. ${opt.label}`));
  });

  const answer = await prompt("请选择", "1");
  const index = parseInt(answer, 10) - 1;

  if (index >= 0 && index < options.length && options[index]) {
    return options[index].value;
  }

  return options[0]?.value || options[0]?.value || "";
}

/**
 * 初始化项目（交互式）
 */
export async function initProject(cwd?: string): Promise<void> {
  const projectDir = cwd || process.cwd();
  const configPath = join(projectDir, "qin.config.ts");

  // 检查是否已存在配置
  if (await fileExists(configPath)) {
    console.log(chalk.yellow("⚠ qin.config.ts 已存在，跳过初始化。"));
    return;
  }

  console.log(chalk.blue.bold("\n🚀 Qin 项目初始化\n"));

  // 获取项目名称
  const defaultName = basename(projectDir);
  const projectName = await prompt("项目名称", defaultName);

  // 选择模板
  const template = await select("\n选择项目模板:", [
    { value: "java", label: `${TEMPLATES.java.name} - ${TEMPLATES.java.description}` },
    { value: "java-fullstack", label: `${TEMPLATES["java-fullstack"].name} - ${TEMPLATES["java-fullstack"].description}` },
    { value: "bun", label: `${TEMPLATES.bun.name} - ${TEMPLATES.bun.description}` },
    { value: "empty", label: `${TEMPLATES.empty.name} - ${TEMPLATES.empty.description}` },
  ]) as ProjectTemplate;

  console.log();

  // 创建文件
  const templateConfig = TEMPLATES[template];
  for (const [filePath, content] of Object.entries(templateConfig.files)) {
    const fullPath = join(projectDir, filePath);
    const dir = join(fullPath, "..");

    // 创建目录
    await mkdir(dir, { recursive: true });

    // 替换占位符
    const finalContent = content.replace(/\{\{name\}\}/g, projectName);

    // 写入文件
    await writeFile(fullPath, finalContent);
    console.log(chalk.green(`✓ 创建 ${filePath}`));
  }

  // 完成提示
  console.log();
  console.log(chalk.green.bold("✓ 项目初始化完成！"));
  console.log();
  console.log(chalk.blue("下一步:"));

  if (template === "java" || template === "java-fullstack") {
    console.log(chalk.gray("  qin dev    # 启动开发服务器（热重载）"));
    console.log(chalk.gray("  qin run    # 编译并运行"));
    console.log(chalk.gray("  qin build  # 打包成 JAR"));
  } else if (template === "bun") {
    console.log(chalk.gray("  bun run dev   # 运行开发"));
    console.log(chalk.gray("  bun run build # 构建"));
  } else {
    console.log(chalk.gray("  编辑 qin.config.ts 配置你的项目"));
  }
}

/**
 * 快速初始化（非交互式）
 */
export async function quickInit(
  template: ProjectTemplate = "java",
  projectName?: string,
  cwd?: string
): Promise<void> {
  const projectDir = cwd || process.cwd();
  const name = projectName || basename(projectDir);

  const templateConfig = TEMPLATES[template];
  if (!templateConfig) {
    throw new Error(`Unknown template: ${template}`);
  }

  for (const [filePath, content] of Object.entries(templateConfig.files)) {
    const fullPath = join(projectDir, filePath);
    const dir = join(fullPath, "..");

    await mkdir(dir, { recursive: true });
    const finalContent = content.replace(/\{\{name\}\}/g, name);
    await writeFile(fullPath, finalContent);
  }
}

/**
 * 检查文件是否存在
 */
async function fileExists(path: string): Promise<boolean> {
  try {
    await access(path);
    return true;
  } catch {
    return false;
  }
}
