/**
 * Environment Checker for Qin
 * Verifies that required tools (Coursier, JDK) are installed
 * Auto-installs Coursier if missing
 */

import { join } from "path";
import { mkdir, chmod } from "fs/promises";
import type { EnvironmentStatus } from "../types";

export class EnvironmentChecker {
  private qinHome: string;
  private csPath: string;

  constructor() {
    // Qin home directory for storing tools
    this.qinHome = join(process.env.HOME || process.env.USERPROFILE || ".", ".qin");
    this.csPath = process.platform === "win32" 
      ? join(this.qinHome, "bin", "cs.exe")
      : join(this.qinHome, "bin", "cs");
  }

  /**
   * Get the Coursier command (either global or local)
   */
  getCoursierCommand(): string {
    return this.csPath;
  }

  /**
   * Check if Coursier (cs) is installed (globally or locally)
   */
  async checkCoursier(): Promise<boolean> {
    // First check global installation
    try {
      const proc = Bun.spawn(["cs", "--version"], {
        stdout: "pipe",
        stderr: "pipe",
      });
      await proc.exited;
      if (proc.exitCode === 0) return true;
    } catch {
      // Global not found, check local
    }

    // Check local installation
    try {
      const file = Bun.file(this.csPath);
      if (await file.exists()) {
        const proc = Bun.spawn([this.csPath, "--version"], {
          stdout: "pipe",
          stderr: "pipe",
        });
        await proc.exited;
        return proc.exitCode === 0;
      }
    } catch {
      // Local not found either
    }

    return false;
  }

  /**
   * Auto-install Coursier to ~/.qin/bin/
   */
  async installCoursier(): Promise<boolean> {
    const isWindows = process.platform === "win32";
    const binDir = join(this.qinHome, "bin");

    console.log("→ 正在安装 Coursier...");

    try {
      // Create bin directory
      await mkdir(binDir, { recursive: true });

      if (isWindows) {
        // Windows: Use curl (more reliable than PowerShell Invoke-WebRequest)
        console.log("  下载 Coursier (使用 curl)...");
        
        const zipPath = join(this.qinHome, "cs.zip");
        const url = "https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-win32.zip";
        
        // Try curl first (available on Windows 10+)
        const curlProc = Bun.spawn([
          "curl", "-L", "-o", zipPath, url
        ], { stdout: "pipe", stderr: "pipe" });
        
        await curlProc.exited;
        
        if (curlProc.exitCode !== 0) {
          // Fallback to PowerShell
          console.log("  curl 失败，尝试 PowerShell...");
          const psProc = Bun.spawn([
            "powershell", "-Command",
            `$ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri '${url}' -OutFile '${zipPath}' -UseBasicParsing`
          ], { stdout: "pipe", stderr: "pipe" });
          await psProc.exited;
          
          if (psProc.exitCode !== 0) {
            throw new Error("下载失败，请检查网络连接");
          }
        }

        // Extract zip and rename to cs.exe
        console.log("  解压...");
        const extractProc = Bun.spawn([
          "powershell", "-Command",
          `Expand-Archive -Path '${zipPath}' -DestinationPath '${binDir}' -Force; Remove-Item '${zipPath}' -Force; if (Test-Path '${binDir}\\cs-x86_64-pc-win32.exe') { Move-Item '${binDir}\\cs-x86_64-pc-win32.exe' '${binDir}\\cs.exe' -Force }`
        ], { stdout: "pipe", stderr: "pipe" });
        await extractProc.exited;

      } else {
        // Linux/macOS: download binary directly
        const arch = process.arch === "arm64" ? "aarch64" : "x86_64";
        const os = process.platform === "darwin" ? "apple-darwin" : "pc-linux";
        const url = `https://github.com/coursier/launchers/raw/master/cs-${arch}-${os}.gz`;

        console.log("  下载 Coursier...");
        const response = await fetch(url);
        if (!response.ok) throw new Error(`Download failed: ${response.status}`);

        // Decompress gzip
        const compressed = await response.arrayBuffer();
        const decompressed = Bun.gunzipSync(new Uint8Array(compressed));
        await Bun.write(this.csPath, decompressed);

        // Make executable
        await chmod(this.csPath, 0o755);
      }

      // Verify installation
      const proc = Bun.spawn([this.csPath, "--version"], {
        stdout: "pipe",
        stderr: "pipe",
      });
      await proc.exited;

      if (proc.exitCode === 0) {
        console.log("✓ Coursier 安装成功");
        return true;
      } else {
        throw new Error("Coursier 验证失败");
      }
    } catch (error) {
      const msg = error instanceof Error ? error.message : String(error);
      console.error("✗ Coursier 安装失败:", msg);
      console.log("\n手动安装方法:");
      console.log(this.getInstallGuide("coursier"));
    }

    return false;
  }

  /**
   * Ensure Coursier is available (install if needed)
   */
  async ensureCoursier(): Promise<boolean> {
    if (await this.checkCoursier()) {
      return true;
    }
    return await this.installCoursier();
  }

  /**
   * Check if javac is installed
   */
  async checkJavac(): Promise<boolean> {
    try {
      const proc = Bun.spawn(["javac", "-version"], {
        stdout: "pipe",
        stderr: "pipe",
      });
      await proc.exited;
      return proc.exitCode === 0;
    } catch {
      return false;
    }
  }

  /**
   * Check if java is installed
   */
  async checkJava(): Promise<boolean> {
    try {
      const proc = Bun.spawn(["java", "-version"], {
        stdout: "pipe",
        stderr: "pipe",
      });
      await proc.exited;
      return proc.exitCode === 0;
    } catch {
      return false;
    }
  }

  /**
   * Run all environment checks
   */
  async checkAll(): Promise<EnvironmentStatus> {
    const [coursier, javac, java] = await Promise.all([
      this.checkCoursier(),
      this.checkJavac(),
      this.checkJava(),
    ]);

    return {
      coursier,
      javac,
      java,
      ready: coursier && javac && java,
    };
  }

  /**
   * Get installation guide for a specific tool
   */
  getInstallGuide(tool: "coursier" | "javac"): string {
    const isWindows = process.platform === "win32";
    const isMac = process.platform === "darwin";

    if (tool === "coursier") {
      if (isWindows) {
        return `
To install Coursier on Windows:
  1. Download from: https://get-coursier.io/docs/cli-installation
  2. Or use Scoop: scoop install coursier
  3. Or use Chocolatey: choco install coursier
`;
      } else if (isMac) {
        return `
To install Coursier on macOS:
  brew install coursier/formulas/coursier
`;
      } else {
        return `
To install Coursier on Linux:
  curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz | gzip -d > cs
  chmod +x cs
  ./cs setup
`;
      }
    }

    if (tool === "javac") {
      if (isWindows) {
        return `
To install JDK on Windows:
  1. Download from: https://adoptium.net/
  2. Or use Scoop: scoop install temurin17-jdk
  3. Or use Chocolatey: choco install temurin17
`;
      } else if (isMac) {
        return `
To install JDK on macOS:
  brew install openjdk@17
`;
      } else {
        return `
To install JDK on Linux:
  sudo apt install openjdk-17-jdk  # Debian/Ubuntu
  sudo dnf install java-17-openjdk-devel  # Fedora
`;
      }
    }

    return "Unknown tool";
  }
}
