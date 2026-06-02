package com.qin.plugins;

import com.qin.constants.QinConstants;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Workspace 鎵弿鍣紙澧炲己鐗堬級
 * 
 * 鏀硅繘锛?
 * 1. 涓嶄緷璧?npm workspaces 閰嶇疆锛岀洿鎺ラ€掑綊鎵弿鎵€鏈夊寘鍚?package.json 鐨勭洰褰?
 * 2. 榛樿 monorepoEntry 涓?"./src/index.ts"锛堝鏋滄湭閰嶇疆锛?
 * 3. 鑷姩鏌ユ壘椤圭洰鏍圭洰褰曪紙閫氳繃 .git, qin.config.js, package.json 绛夋爣蹇楋級
 */
public class WorkspaceScanner {

    // 榛樿鐨勬簮鐮佸叆鍙?
    private static final String DEFAULT_MONOREPO_ENTRY = "./src/index.ts";

    /**
     * 鍖呬俊鎭?
     */
    public record PackageInfo(
            String name,
            Path dir,
            String monorepoEntry) {
    }

    /**
     * 浠庢寚瀹氱洰褰曞紑濮嬫壂鎻忔墍鏈夊寘
     * 
     * @param startDir 璧峰鐩綍锛堥€氬父鏄懡浠ゆ墽琛岀殑鐩綍锛?
     * @return 鍖呭悕鍒板寘淇℃伅鐨勬槧灏?
     */
    public Map<String, PackageInfo> scan(Path startDir) {
        Map<String, PackageInfo> packages = new LinkedHashMap<>();

        // 1. 鏌ユ壘椤圭洰鏍圭洰褰?
        Path projectRoot = findProjectRoot(startDir);

        // 2. 浠庨」鐩牴鐩綍閫掑綊鎵弿鎵€鏈夊寘
        scanPackagesRecursive(projectRoot, packages);

        return packages;
    }

    /**
     * 鏌ユ壘椤圭洰鏍圭洰褰?
     * 浼樺厛绾э細
     * 1. IDE 鐜鍙橀噺锛圴SCODE_CWD, IDEA_INITIAL_DIRECTORY锛?
     * 2. 鍚戜笂鏌ユ壘锛屽彇鏈€涓婂眰鐨?.vscode / .idea / qin.config.js / package.json
     */
    public Path findProjectRoot(Path startDir) {
        // 1. 浼樺厛浣跨敤 IDE 鐜鍙橀噺
        String vscodeCwd = System.getenv("VSCODE_CWD");
        if (vscodeCwd != null && !vscodeCwd.isEmpty()) {
            Path vscodePath = Path.of(vscodeCwd);
            if (Files.exists(vscodePath)) {
                return vscodePath;
            }
        }

        String ideaDir = System.getenv("IDEA_INITIAL_DIRECTORY");
        if (ideaDir != null && !ideaDir.isEmpty()) {
            Path ideaPath = Path.of(ideaDir);
            if (Files.exists(ideaPath)) {
                return ideaPath;
            }
        }

        // 2. 鍚戜笂鏌ユ壘锛岃褰曟墍鏈夊尮閰嶏紝鏈€鍚庡彇鏈€涓婂眰鐨?
        Path current = startDir.toAbsolutePath().normalize();
        Path topMostMatch = null; // 鏈€涓婂眰鐨勫尮閰嶏紙璺緞鏈€鐭級

        while (current != null && current.getParent() != null) {
            // 妫€鏌ユ槸鍚︽槸椤圭洰鏍囧織
            final Path finalCurrent = current;
            boolean isProjectRoot = QinConstants.WORKSPACE_ROOT_MARKERS.stream()
                    .anyMatch(marker -> Files.exists(finalCurrent.resolve(marker)));

            if (isProjectRoot) {
                topMostMatch = current; // 缁х画鍚戜笂鎵撅紝鍙栨渶涓婂眰鐨?
            }

            current = current.getParent();
        }

        // 杩斿洖鏈€涓婂眰鐨勫尮閰?
        if (topMostMatch != null) {
            return topMostMatch;
        }

        // 閮芥壘涓嶅埌锛岃繑鍥炶捣濮嬬洰褰?
        return startDir.toAbsolutePath().normalize();
    }

    /**
     * 閫掑綊鎵弿鐩綍锛屾敹闆嗘墍鏈夊寘鍚?package.json 涓旀湁 name 瀛楁鐨勫寘
     */
    private void scanPackagesRecursive(Path dir, Map<String, PackageInfo> packages) {
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return;
        }

        Path pkgPath = dir.resolve(QinConstants.PACKAGE_JSON);

        // 濡傛灉褰撳墠鐩綍鏈?package.json锛屾鏌ユ槸鍚︽槸涓€涓寘
        if (Files.exists(pkgPath)) {
            try {
                String content = Files.readString(pkgPath);
                String name = parseJsonField(content, "name");

                if (name != null && !packages.containsKey(name)) {
                    // 鑾峰彇 monorepo 鍏ュ彛锛屽鏋滄病鏈夐厤缃垯浣跨敤榛樿鍊?
                    String monorepo = parseJsonField(content, "monorepo");
                    String entry = (monorepo != null) ? monorepo : DEFAULT_MONOREPO_ENTRY;

                    // 鍙湁褰?src/index.ts 瀛樺湪鏃舵墠娣诲姞锛堟垨鑰呮槑纭厤缃簡 monorepo锛?
                    Path entryPath = dir.resolve(entry.replace("./", ""));
                    if (monorepo != null || Files.exists(entryPath)) {
                        packages.put(name, new PackageInfo(name, dir, entry));
                    }
                }
            } catch (IOException e) {
                // 蹇界暐璇诲彇閿欒
            }
        }

        // 閫掑綊鎵弿瀛愮洰褰?
        try (var stream = Files.list(dir)) {
            stream.filter(Files::isDirectory)
                    .filter(p -> !QinConstants.EXCLUDED_DIRS.contains(p.getFileName().toString()))
                    .forEach(subDir -> scanPackagesRecursive(subDir, packages));
        } catch (IOException e) {
            // 蹇界暐
        }
    }

    /**
     * 瑙ｆ瀽 JSON 瀛楁鍊?
     */
    private String parseJsonField(String json, String field) {
        String search = "\"" + field + "\"";
        int idx = json.indexOf(search);
        if (idx == -1)
            return null;

        int colonIdx = json.indexOf(':', idx);
        if (colonIdx == -1)
            return null;

        // 璺宠繃绌虹櫧
        int valueStart = colonIdx + 1;
        while (valueStart < json.length() && Character.isWhitespace(json.charAt(valueStart))) {
            valueStart++;
        }

        if (valueStart >= json.length())
            return null;

        char c = json.charAt(valueStart);
        if (c == '"') {
            // 瀛楃涓插€?
            int valueEnd = json.indexOf('"', valueStart + 1);
            if (valueEnd == -1)
                return null;
            return json.substring(valueStart + 1, valueEnd);
        }

        return null;
    }
}

