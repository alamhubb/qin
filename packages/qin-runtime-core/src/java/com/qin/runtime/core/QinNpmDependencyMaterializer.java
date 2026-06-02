package com.qin.runtime.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Qin-owned npm dependency materializer for the current dev-server subset.
 *
 * <p>This is intentionally not a full npm client. It supports the packages
 * declared in {@code qin.config.js} plus their ordinary {@code dependencies}
 * so Qin can run the Vue/CSSTS Stage-1 pipeline without invoking npm, Node, or
 * Vite.
 */
final class QinNpmDependencyMaterializer {
    private static final List<String> REGISTRIES = List.of(
            "https://registry.npmmirror.com",
            "https://registry.npmjs.org");
    private static final Pattern MANIFEST_DEPENDENCIES_BLOCK = Pattern.compile(
            "(?:\"(dependencies|devDependencies)\"|(dependencies|devDependencies))\\s*:\\s*\\{([^}]*)\\}",
            Pattern.DOTALL);
    private static final Pattern PACKAGE_DEPENDENCIES_BLOCK = Pattern.compile(
            "\"dependencies\"\\s*:\\s*\\{([^}]*)\\}",
            Pattern.DOTALL);
    private static final Pattern JSON_STRING_FIELD = Pattern.compile(
            "\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern JSON_VERSION_FIELD = Pattern.compile("\"version\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern JSON_TARBALL_FIELD = Pattern.compile("\"tarball\"\\s*:\\s*\"([^\"]+)\"");

    void materializeProjectDependencies(Path projectRoot, Path nodeModulesRoot) throws IOException {
        Path manifest = findProjectManifest(projectRoot);
        if (!Files.isRegularFile(manifest)) {
            return;
        }
        Map<String, String> dependencies = readProjectDependencyVersions(manifest);
        if (dependencies.isEmpty()) {
            return;
        }
        Files.createDirectories(nodeModulesRoot);
        Set<String> installed = new LinkedHashSet<>();
        for (Map.Entry<String, String> dependency : dependencies.entrySet()) {
            if (isMavenCoordinate(dependency.getKey())) {
                continue;
            }
            installPackage(dependency.getKey(), dependency.getValue(), nodeModulesRoot, installed);
        }
    }

    void materializePackageDependency(String packageName, String versionRange, Path nodeModulesRoot) throws IOException {
        if (packageName == null || packageName.isBlank()) {
            return;
        }
        Files.createDirectories(nodeModulesRoot);
        installPackage(packageName, versionRange, nodeModulesRoot, new LinkedHashSet<>());
    }

    private void installPackage(
            String packageName,
            String versionRange,
            Path nodeModulesRoot,
            Set<String> installed) throws IOException {
        if (packageName == null || packageName.isBlank() || !installed.add(packageName)) {
            return;
        }
        Path packageDir = nodeModulesRoot.resolve(packageName.replace('/', java.io.File.separatorChar)).normalize();
        if (Files.isRegularFile(packageDir.resolve("package.json"))
                && packageName.equals(readPackageName(packageDir.resolve("package.json")))) {
            for (String dependencyName : readDependencyVersions(packageDir.resolve("package.json")).keySet()) {
                installPackage(dependencyName, readDependencyVersions(packageDir.resolve("package.json")).get(dependencyName), nodeModulesRoot, installed);
            }
            return;
        }

        PackageMetadata metadata = resolvePackage(packageName, versionRange);
        if (metadata == null) {
            throw new IllegalStateException("Cannot resolve npm package " + packageName + "@" + versionRange);
        }
        deleteRecursively(packageDir);
        Files.createDirectories(packageDir.getParent());
        Path tarball = downloadTarball(metadata);
        extractTgz(tarball, packageDir);

        Map<String, String> childDependencies = readDependencyVersions(packageDir.resolve("package.json"));
        for (Map.Entry<String, String> child : childDependencies.entrySet()) {
            installPackage(child.getKey(), child.getValue(), nodeModulesRoot, installed);
        }
    }

    private PackageMetadata resolvePackage(String packageName, String versionRange) throws IOException {
        IOException lastError = null;
        for (String registry : REGISTRIES) {
            try {
                String packageJson = fetchText(registry + "/" + encodePackageName(packageName));
                String version = resolveVersion(packageJson, versionRange);
                if (version == null) {
                    continue;
                }
                String versionJson = fetchText(registry + "/" + encodePackageName(packageName) + "/" + version);
                Matcher tarballMatcher = JSON_TARBALL_FIELD.matcher(versionJson);
                if (!tarballMatcher.find()) {
                    continue;
                }
                return new PackageMetadata(packageName, version, tarballMatcher.group(1));
            } catch (IOException error) {
                lastError = error;
            }
        }
        if (lastError != null) {
            throw lastError;
        }
        return null;
    }

    private String resolveVersion(String packageJson, String versionRange) {
        String range = versionRange == null || versionRange.isBlank() ? "latest" : versionRange.trim();
        if (range.startsWith("=") && range.length() > 1) {
            range = range.substring(1).trim();
        }
        if ("latest".equals(range) || "*".equals(range)) {
            Matcher latest = Pattern.compile("\"latest\"\\s*:\\s*\"([^\"]+)\"").matcher(packageJson);
            if (latest.find()) {
                return latest.group(1);
            }
        }
        Set<String> versions = new LinkedHashSet<>();
        Matcher matcher = Pattern.compile("\"([0-9]+\\.[0-9]+\\.[^\"]+)\"\\s*:\\s*\\{").matcher(packageJson);
        while (matcher.find()) {
            String version = matcher.group(1);
            if (!version.contains("-")) {
                versions.add(version);
            }
        }
        if (range.startsWith("^")) {
            String[] parts = range.substring(1).split("\\.");
            String major = parts.length > 0 ? parts[0] : "";
            return versions.stream()
                    .filter(version -> version.startsWith(major + "."))
                    .max(this::compareVersions)
                    .orElse(null);
        }
        if (range.startsWith("~")) {
            String[] parts = range.substring(1).split("\\.");
            String prefix = parts.length >= 2 ? parts[0] + "." + parts[1] + "." : "";
            return versions.stream()
                    .filter(version -> version.startsWith(prefix))
                    .max(this::compareVersions)
                    .orElse(null);
        }
        if (versions.contains(range)) {
            return range;
        }
        Matcher exact = Pattern.compile("\"" + Pattern.quote(range) + "\"\\s*:\\s*\\{").matcher(packageJson);
        return exact.find() ? range : null;
    }

    private Path downloadTarball(PackageMetadata metadata) throws IOException {
        Path cacheDir = Path.of(System.getProperty("user.home"), ".qin", "npm-cache").toAbsolutePath().normalize();
        Files.createDirectories(cacheDir);
        String filename = sanitizeFilename(metadata.name() + "-" + metadata.version() + ".tgz");
        Path target = cacheDir.resolve(filename);
        if (Files.isRegularFile(target) && Files.size(target) > 0) {
            return target;
        }
        HttpURLConnection connection = (HttpURLConnection) URI.create(metadata.tarballUrl()).toURL().openConnection();
        connection.setRequestProperty("User-Agent", "Qin-Npm-Materializer/0.1");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(120000);
        int status = connection.getResponseCode();
        if (status >= 300 && status < 400 && connection.getHeaderField("Location") != null) {
            connection = (HttpURLConnection) URI.create(connection.getHeaderField("Location")).toURL().openConnection();
            connection.setRequestProperty("User-Agent", "Qin-Npm-Materializer/0.1");
        }
        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to download " + metadata.tarballUrl()
                    + ": HTTP " + connection.getResponseCode());
        }
        try (InputStream input = connection.getInputStream();
                OutputStream output = Files.newOutputStream(target)) {
            input.transferTo(output);
        }
        return target;
    }

    private void extractTgz(Path tarball, Path targetDir) throws IOException {
        Files.createDirectories(targetDir);
        try (InputStream fileInput = Files.newInputStream(tarball);
                GZIPInputStream gzipInput = new GZIPInputStream(fileInput);
                TarReader tarReader = new TarReader(gzipInput)) {
            TarReader.Entry entry;
            while ((entry = tarReader.nextEntry()) != null) {
                String name = entry.name();
                if (name.startsWith("package/")) {
                    name = name.substring("package/".length());
                }
                if (name.isBlank()) {
                    continue;
                }
                Path output = targetDir.resolve(name).normalize();
                if (!output.startsWith(targetDir)) {
                    throw new IOException("Refusing to extract npm tar entry outside target: " + name);
                }
                if (entry.directory()) {
                    Files.createDirectories(output);
                } else {
                    Files.createDirectories(output.getParent());
                    try (OutputStream stream = Files.newOutputStream(output)) {
                        tarReader.copyEntryTo(stream);
                    }
                }
            }
        }
    }

    private String fetchText(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "Qin-Npm-Materializer/0.1");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(120000);
        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to fetch " + url + ": HTTP " + connection.getResponseCode());
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append('\n');
            }
            return text.toString();
        }
    }

    private Path findProjectManifest(Path projectRoot) {
        Path root = projectRoot.toAbsolutePath().normalize();
        return root.resolve("qin.config.js");
    }

    private Map<String, String> readProjectDependencyVersions(Path manifest) throws IOException {
        if (!Files.isRegularFile(manifest)) {
            return Map.of();
        }
        String text = Files.readString(manifest, StandardCharsets.UTF_8);
        Matcher blockMatcher = MANIFEST_DEPENDENCIES_BLOCK.matcher(text);
        Map<String, String> dependencies = new LinkedHashMap<>();
        while (blockMatcher.find()) {
            Matcher fieldMatcher = JSON_STRING_FIELD.matcher(blockMatcher.group(3));
            while (fieldMatcher.find()) {
                dependencies.put(fieldMatcher.group(1), fieldMatcher.group(2));
            }
        }
        return dependencies;
    }

    private Map<String, String> readDependencyVersions(Path manifest) throws IOException {
        if (!Files.isRegularFile(manifest)) {
            return Map.of();
        }
        String json = Files.readString(manifest, StandardCharsets.UTF_8);
        Matcher blockMatcher = PACKAGE_DEPENDENCIES_BLOCK.matcher(json);
        if (!blockMatcher.find()) {
            return Map.of();
        }
        Map<String, String> dependencies = new LinkedHashMap<>();
        Matcher fieldMatcher = JSON_STRING_FIELD.matcher(blockMatcher.group(1));
        while (fieldMatcher.find()) {
            dependencies.put(fieldMatcher.group(1), fieldMatcher.group(2));
        }
        return dependencies;
    }

    private String readPackageName(Path packageJson) throws IOException {
        if (!Files.isRegularFile(packageJson)) {
            return null;
        }
        Matcher matcher = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"")
                .matcher(Files.readString(packageJson, StandardCharsets.UTF_8));
        return matcher.find() ? matcher.group(1) : null;
    }

    private boolean isMavenCoordinate(String dependencyName) {
        return dependencyName != null
                && dependencyName.indexOf(':') > 0
                && !dependencyName.startsWith("@");
    }

    private String encodePackageName(String name) {
        return URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String sanitizeFilename(String value) {
        return value.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    private int compareVersions(String left, String right) {
        List<Integer> leftParts = versionParts(left);
        List<Integer> rightParts = versionParts(right);
        int count = Math.max(leftParts.size(), rightParts.size());
        for (int i = 0; i < count; i++) {
            int l = i < leftParts.size() ? leftParts.get(i) : 0;
            int r = i < rightParts.size() ? rightParts.get(i) : 0;
            if (l != r) {
                return Integer.compare(l, r);
            }
        }
        return 0;
    }

    private List<Integer> versionParts(String version) {
        String[] parts = version.split("\\.");
        List<Integer> values = new ArrayList<>();
        for (String part : parts) {
            Matcher matcher = Pattern.compile("^(\\d+)").matcher(part);
            values.add(matcher.find() ? Integer.parseInt(matcher.group(1)) : 0);
        }
        return values;
    }

    private void deleteRecursively(Path path) throws IOException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        Files.walkFileTree(path, new java.nio.file.SimpleFileVisitor<>() {
            @Override
            public java.nio.file.FileVisitResult visitFile(
                    Path file,
                    java.nio.file.attribute.BasicFileAttributes attrs) throws IOException {
                Files.deleteIfExists(file);
                return java.nio.file.FileVisitResult.CONTINUE;
            }

            @Override
            public java.nio.file.FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.deleteIfExists(dir);
                return java.nio.file.FileVisitResult.CONTINUE;
            }
        });
    }

    private record PackageMetadata(String name, String version, String tarballUrl) {
    }

    private static final class TarReader implements AutoCloseable {
        private static final int BLOCK_SIZE = 512;
        private final InputStream input;
        private Entry currentEntry;
        private long remaining;

        TarReader(InputStream input) {
            this.input = input;
        }

        Entry nextEntry() throws IOException {
            drainCurrentEntry();
            byte[] header = input.readNBytes(BLOCK_SIZE);
            if (header.length < BLOCK_SIZE || isEmptyBlock(header)) {
                currentEntry = null;
                return null;
            }
            String name = parseString(header, 0, 100);
            long size = parseOctal(header, 124, 12);
            boolean directory = header[156] == '5';
            String prefix = parseString(header, 345, 155);
            if (!prefix.isBlank()) {
                name = prefix + "/" + name;
            }
            currentEntry = new Entry(name, size, directory);
            remaining = size;
            return currentEntry;
        }

        void copyEntryTo(OutputStream output) throws IOException {
            if (currentEntry == null) {
                return;
            }
            byte[] buffer = new byte[8192];
            while (remaining > 0) {
                int read = input.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read < 0) {
                    throw new IOException("Unexpected EOF in tar entry: " + currentEntry.name());
                }
                output.write(buffer, 0, read);
                remaining -= read;
            }
            skipPadding(currentEntry.size());
            currentEntry = null;
        }

        private void drainCurrentEntry() throws IOException {
            if (currentEntry == null) {
                return;
            }
            while (remaining > 0) {
                long skipped = input.skip(remaining);
                if (skipped <= 0) {
                    if (input.read() < 0) {
                        break;
                    }
                    skipped = 1;
                }
                remaining -= skipped;
            }
            skipPadding(currentEntry.size());
            currentEntry = null;
        }

        private void skipPadding(long size) throws IOException {
            long padding = (BLOCK_SIZE - (size % BLOCK_SIZE)) % BLOCK_SIZE;
            while (padding > 0) {
                long skipped = input.skip(padding);
                if (skipped <= 0) {
                    if (input.read() < 0) {
                        break;
                    }
                    skipped = 1;
                }
                padding -= skipped;
            }
        }

        private static boolean isEmptyBlock(byte[] block) {
            for (byte b : block) {
                if (b != 0) {
                    return false;
                }
            }
            return true;
        }

        private static String parseString(byte[] data, int offset, int length) {
            int end = offset;
            while (end < offset + length && data[end] != 0) {
                end++;
            }
            return new String(data, offset, end - offset, StandardCharsets.UTF_8).trim();
        }

        private static long parseOctal(byte[] data, int offset, int length) {
            String value = parseString(data, offset, length).trim();
            return value.isBlank() ? 0 : Long.parseLong(value, 8);
        }

        @Override
        public void close() throws IOException {
            input.close();
        }

        private record Entry(String name, long size, boolean directory) {
        }
    }
}
