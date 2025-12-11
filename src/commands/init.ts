/**
 * Init Command for Qin
 * Creates a new Qin project with default configuration
 */

import { join } from "path";
import { mkdir, writeFile, access } from "fs/promises";
import chalk from "chalk";

const CONFIG_TEMPLATE = `import type { QinConfig } from "qin";

export default {
  entry: "src/Main.java",
  dependencies: [
    // Add Maven dependencies here, e.g.:
    // "com.google.guava:guava:32.1.3-jre",
  ],
  output: {
    dir: "dist",
    jarName: "app.jar",
  },
} satisfies QinConfig;
`;

const MAIN_JAVA_TEMPLATE = `public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from Qin!");
        System.out.println("Edit src/Main.java to get started.");
    }
}
`;

/**
 * Initialize a new Qin project
 */
export async function initProject(cwd?: string): Promise<void> {
  const projectDir = cwd || process.cwd();
  const configPath = join(projectDir, "qin.config.ts");
  const srcDir = join(projectDir, "src");
  const mainJavaPath = join(srcDir, "Main.java");

  // Check if config already exists
  if (await fileExists(configPath)) {
    console.log(chalk.yellow("⚠ qin.config.ts already exists. Skipping initialization."));
    return;
  }

  // Create src directory
  await mkdir(srcDir, { recursive: true });

  // Create qin.config.ts
  await writeFile(configPath, CONFIG_TEMPLATE);
  console.log(chalk.green("✓ Created qin.config.ts"));

  // Create Main.java (only if it doesn't exist)
  if (!(await fileExists(mainJavaPath))) {
    await writeFile(mainJavaPath, MAIN_JAVA_TEMPLATE);
    console.log(chalk.green("✓ Created src/Main.java"));
  }

  console.log();
  console.log(chalk.blue("Project initialized! Next steps:"));
  console.log(chalk.gray("  1. Edit qin.config.ts to add dependencies"));
  console.log(chalk.gray("  2. Run 'qin run' to compile and run"));
  console.log(chalk.gray("  3. Run 'qin build' to create a Fat Jar"));
}

/**
 * Check if a file exists
 */
async function fileExists(path: string): Promise<boolean> {
  try {
    await access(path);
    return true;
  } catch {
    return false;
  }
}
