/**
 * qin env command
 * Display environment status including GraalVM detection
 */

import chalk from "chalk";
import { detectGraalVM, type GraalVMDetectionResult } from "../../packages/qin-plugin-graalvm/src/index";

/**
 * 显示 GraalVM 状态
 */
export async function displayGraalVMStatus(): Promise<void> {
  console.log(chalk.blue("\n📦 GraalVM Status"));
  console.log(chalk.gray("─".repeat(40)));

  const detection = await detectGraalVM();

  if (detection.found && detection.info) {
    console.log(chalk.green("✓") + " GraalVM detected");
    console.log(chalk.gray(`  Path: ${detection.info.home}`));
    console.log(chalk.gray(`  Version: ${detection.info.version}`));
    console.log(chalk.gray(`  Detected by: ${detection.detectedBy}`));

    // 显示已安装组件
    console.log(chalk.blue("\n  Installed Components:"));
    if (detection.info.components.length > 0) {
      for (const component of detection.info.components) {
        console.log(chalk.green("    ✓") + ` ${component}`);
      }
    } else {
      console.log(chalk.yellow("    No additional components installed"));
    }

    // 检查关键组件
    console.log(chalk.blue("\n  Key Components:"));
    
    // Node.js
    if (detection.info.nodePath) {
      console.log(chalk.green("    ✓") + " Node.js: " + chalk.gray(detection.info.nodePath));
    } else {
      console.log(chalk.red("    ✗") + " Node.js: " + chalk.yellow("Not installed"));
      console.log(chalk.gray("      Run: gu install nodejs"));
    }

    // Java
    if (detection.info.javaPath) {
      console.log(chalk.green("    ✓") + " Java: " + chalk.gray(detection.info.javaPath));
    } else {
      console.log(chalk.yellow("    ⚠") + " Java: Not found in GraalVM bin");
    }
  } else {
    console.log(chalk.red("✗") + " GraalVM not detected");
    console.log(chalk.gray(`  ${detection.error}`));
    console.log();
    console.log(chalk.blue("  Installation Guide:"));
    console.log(chalk.gray("  1. Download GraalVM from https://www.graalvm.org/downloads/"));
    console.log(chalk.gray("  2. Set GRAALVM_HOME environment variable"));
    console.log(chalk.gray("  3. Add $GRAALVM_HOME/bin to PATH"));
    console.log();
    console.log(chalk.blue("  Install Node.js component:"));
    console.log(chalk.gray("  gu install nodejs"));
  }
}

/**
 * 格式化环境状态输出
 */
export function formatEnvStatus(detection: GraalVMDetectionResult): string {
  const lines: string[] = [];

  lines.push("GraalVM Status");
  lines.push("─".repeat(40));

  if (detection.found && detection.info) {
    lines.push(`✓ GraalVM detected`);
    lines.push(`  Path: ${detection.info.home}`);
    lines.push(`  Version: ${detection.info.version}`);
    lines.push(`  Components: ${detection.info.components.join(", ") || "none"}`);
    
    if (detection.info.nodePath) {
      lines.push(`  Node.js: ${detection.info.nodePath}`);
    } else {
      lines.push(`  Node.js: Not installed (run: gu install nodejs)`);
    }
  } else {
    lines.push(`✗ GraalVM not detected`);
    lines.push(`  ${detection.error}`);
  }

  return lines.join("\n");
}
