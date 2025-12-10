import { $ } from "bun";
import { existsSync, mkdirSync } from "fs";
import { join } from "path";
import { buildClasspath, buildFullClasspath } from "./classpath";

export interface JavaBuildConfig {
  srcDir: string;
  outDir: string;
  mainClass?: string;
  classpath?: string[];
  javaVersion?: string;
}

export class JavaBuilder {
  private config: JavaBuildConfig;

  constructor(config: Partial<JavaBuildConfig> = {}) {
    this.config = {
      srcDir: config.srcDir || "src",
      outDir: config.outDir || "build/classes",
      mainClass: config.mainClass,
      classpath: config.classpath || [],
      javaVersion: config.javaVersion || "17",
    };
  }

  /**
   * Get the current configuration
   */
  getConfig(): JavaBuildConfig {
    return { ...this.config };
  }

  /**
   * Build the full classpath string for compilation/execution
   */
  getFullClasspath(): string {
    return buildFullClasspath(this.config.outDir, this.config.classpath || []);
  }

  /**
   * Compile Java source files
   */
  async compile(files?: string[]): Promise<boolean> {
    const outDir = this.config.outDir;
    if (!existsSync(outDir)) {
      mkdirSync(outDir, { recursive: true });
    }

    const javaFiles = files || (await this.findJavaFiles());
    if (javaFiles.length === 0) {
      console.log("No Java files found");
      return false;
    }

    const cpArg = this.config.classpath?.length 
      ? ["-cp", buildClasspath(this.config.classpath)]
      : [];

    try {
      await $`javac -d ${outDir} ${cpArg} ${javaFiles}`.quiet();
      console.log(`✓ Compiled ${javaFiles.length} file(s)`);
      return true;
    } catch (e: any) {
      console.error("Compilation failed:", e.stderr?.toString() || e.message);
      return false;
    }
  }

  /**
   * Run a compiled Java class
   */
  async run(mainClass?: string, args: string[] = []): Promise<void> {
    const main = mainClass || this.config.mainClass;
    if (!main) {
      throw new Error("No main class specified");
    }

    const cpStr = this.getFullClasspath();

    try {
      await $`java -cp ${cpStr} ${main} ${args}`;
    } catch (e: any) {
      console.error("Run failed:", e.stderr?.toString() || e.message);
    }
  }

  /**
   * Compile and run in one step
   */
  async compileAndRun(mainClass?: string, args: string[] = []): Promise<void> {
    const success = await this.compile();
    if (success) {
      await this.run(mainClass, args);
    }
  }

  /**
   * Find all Java files in the source directory
   */
  private async findJavaFiles(): Promise<string[]> {
    const glob = new Bun.Glob("**/*.java");
    const files: string[] = [];
    for await (const file of glob.scan({ cwd: this.config.srcDir })) {
      files.push(join(this.config.srcDir, file));
    }
    return files;
  }
}
