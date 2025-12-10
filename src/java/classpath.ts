/**
 * Cross-platform classpath utilities
 */

/**
 * Get the platform-specific classpath separator
 * Windows: semicolon (;)
 * Unix/Mac: colon (:)
 */
export function getClasspathSeparator(): string {
  return process.platform === "win32" ? ";" : ":";
}

/**
 * Check if current platform is Windows
 */
export function isWindows(): boolean {
  return process.platform === "win32";
}

/**
 * Build a classpath string from an array of paths
 * Automatically uses the correct separator for the current platform
 */
export function buildClasspath(paths: string[]): string {
  const separator = getClasspathSeparator();
  return paths.filter(p => p.length > 0).join(separator);
}

/**
 * Build a complete classpath including output directory and all dependencies
 */
export function buildFullClasspath(outDir: string, dependencies: string[]): string {
  const allPaths = [outDir, ...dependencies];
  return buildClasspath(allPaths);
}

/**
 * Parse a classpath string into an array of paths
 * Handles both Windows (;) and Unix (:) separators
 */
export function parseClasspath(classpath: string): string[] {
  if (!classpath || classpath.length === 0) {
    return [];
  }
  
  // Try to detect the separator used
  // On Windows, paths may contain colons (C:\...), so we need to be careful
  if (isWindows() || classpath.includes(";")) {
    return classpath.split(";").filter(p => p.length > 0);
  }
  
  return classpath.split(":").filter(p => p.length > 0);
}

/**
 * Validate that all paths in the classpath exist
 * Returns an object with valid and invalid paths
 */
export function validateClasspath(paths: string[]): { valid: string[]; invalid: string[] } {
  const valid: string[] = [];
  const invalid: string[] = [];
  
  for (const path of paths) {
    if (Bun.file(path).size > 0 || path.endsWith("/") || path.endsWith("\\")) {
      valid.push(path);
    } else {
      // Check if it's a directory
      try {
        const glob = new Bun.Glob("*");
        const files = [...glob.scanSync({ cwd: path })];
        if (files.length >= 0) {
          valid.push(path);
        } else {
          invalid.push(path);
        }
      } catch {
        invalid.push(path);
      }
    }
  }
  
  return { valid, invalid };
}
