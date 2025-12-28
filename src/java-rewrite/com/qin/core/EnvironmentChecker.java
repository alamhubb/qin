package com.qin.core;

import com.qin.types.EnvironmentStatus;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.zip.*;

/**
 * Environment Checker for Qin
 * Verifies that required tools are installed
 */
public class EnvironmentChecker {
    private final String qinHome;
    private final String csPath;
    private final boolean isWindows;

    public EnvironmentChecker() {
        String home = System.getProperty("user.home");
        this.qinHome = Paths.get(home, ".qin").toString();
        this.isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        this.csPath = isWindows
                ? Paths.get(qinHome, "bin", "cs.exe").toString()
                : Paths.get(qinHome, "bin", "cs").toString();
    }

    public String getCoursierCommand() {
        return csPath;
    }

    /**
     * Check if Coursier is installed
     */
    public boolean checkCoursier() {
        // Check global installation
        if (runCommand("cs", "--version")) {
            return true;
        }

        // Check local installation
        if (Files.exists(Paths.get(csPath))) {
            return runCommand(csPath, "--version");
        }

        return false;
    }

    /**
     * Auto-install Coursier
     */
    public boolean installCoursier() {
        System.out.println("→ 正在安装 Coursier...");

        try {
            Path binDir = Paths.get(qinHome, "bin");
            Files.createDirectories(binDir);

            if (isWindows) {
                return installCoursierWindows(binDir);
            } else {
                return installCoursierUnix(binDir);
            }
        } catch (Exception e) {
            System.err.println("✗ Coursier 安装失败: " + e.getMessage());
            System.out.println("\n手动安装方法:");
            System.out.println(getInstallGuide("coursier"));
            return false;
        }
    }

    private boolean installCoursierWindows(Path binDir) throws Exception {
        String url = "https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-win32.zip";
        Path zipPath = Paths.get(qinHome, "cs.zip");

        System.out.println("  下载 Coursier...");
        downloadFile(url, zipPath);

        System.out.println("  解压...");
        unzip(zipPath, binDir);
        Files.deleteIfExists(zipPath);

        // Rename if needed
        Path oldName = binDir.resolve("cs-x86_64-pc-win32.exe");
        Path newName = binDir.resolve("cs.exe");
        if (Files.exists(oldName)) {
            Files.move(oldName, newName, StandardCopyOption.REPLACE_EXISTING);
        }

        return verifyCoursier();
    }

    private boolean installCoursierUnix(Path binDir) throws Exception {
        String arch = System.getProperty("os.arch").contains("aarch64") ? "aarch64" : "x86_64";
        String os = System.getProperty("os.name").toLowerCase().contains("mac")
                ? "apple-darwin"
                : "pc-linux";
        String url = String.format(
                "https://github.com/coursier/launchers/raw/master/cs-%s-%s.gz",
                arch, os);

        System.out.println("  下载 Coursier...");
        Path gzPath = Paths.get(qinHome, "cs.gz");
        downloadFile(url, gzPath);

        // Decompress
        Path csFile = Paths.get(csPath);
        try (GZIPInputStream gis = new GZIPInputStream(Files.newInputStream(gzPath));
                OutputStream os2 = Files.newOutputStream(csFile)) {
            gis.transferTo(os2);
        }
        Files.deleteIfExists(gzPath);

        // Make executable
        csFile.toFile().setExecutable(true);

        return verifyCoursier();
    }

    private boolean verifyCoursier() {
        if (runCommand(csPath, "--version")) {
            System.out.println("✓ Coursier 安装成功");
            return true;
        }
        return false;
    }

    /**
     * Check if javac is installed
     */
    public boolean checkJavac() {
        return runCommand("javac", "-version");
    }

    /**
     * Check if java is installed
     */
    public boolean checkJava() {
        return runCommand("java", "-version");
    }

    /**
     * Run all environment checks
     */
    public EnvironmentStatus checkAll() {
        boolean coursier = checkCoursier();
        boolean javac = checkJavac();
        boolean java = checkJava();
        return new EnvironmentStatus(coursier, javac, java);
    }

    /**
     * Get installation guide
     */
    public String getInstallGuide(String tool) {
        boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");

        if ("coursier".equals(tool)) {
            if (isWindows) {
                return """
                        To install Coursier on Windows:
                          1. Download from: https://get-coursier.io/docs/cli-installation
                          2. Or use Scoop: scoop install coursier
                          3. Or use Chocolatey: choco install coursier
                        """;
            } else if (isMac) {
                return """
                        To install Coursier on macOS:
                          brew install coursier/formulas/coursier
                        """;
            } else {
                return """
                        To install Coursier on Linux:
                          curl -fL https://github.com/coursier/launchers/raw/master/cs-x86_64-pc-linux.gz | gzip -d > cs
                          chmod +x cs
                          ./cs setup
                        """;
            }
        }

        if ("javac".equals(tool)) {
            if (isWindows) {
                return """
                        To install JDK on Windows:
                          1. Download from: https://adoptium.net/
                          2. Or use Scoop: scoop install temurin17-jdk
                        """;
            } else if (isMac) {
                return """
                        To install JDK on macOS:
                          brew install openjdk@17
                        """;
            } else {
                return """
                        To install JDK on Linux:
                          sudo apt install openjdk-17-jdk  # Debian/Ubuntu
                          sudo dnf install java-17-openjdk-devel  # Fedora
                        """;
            }
        }

        return "Unknown tool";
    }

    private boolean runCommand(String... args) {
        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            proc.getInputStream().transferTo(OutputStream.nullOutputStream());
            return proc.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void downloadFile(String urlStr, Path dest) throws IOException {
        URL url = new URL(urlStr);
        try (InputStream in = url.openStream()) {
            Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void unzip(Path zipFile, Path destDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path destPath = destDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(destPath);
                } else {
                    Files.createDirectories(destPath.getParent());
                    Files.copy(zis, destPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
