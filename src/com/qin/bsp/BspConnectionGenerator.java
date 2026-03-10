package com.qin.bsp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qin.constants.QinConstants;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * BSP 连接配置生成器
 *
 * 根据 BSP 规范，IDE 通过 .bsp/ 目录下的配置文件发现 Build Server
 *
 * 生成的文件结构：
 * project/
 * └── .bsp/
 *     └── qin.json    ← IDE 读取此文件启动 BSP Server
 */
public class BspConnectionGenerator {

    private final String projectDir;
    private final Gson gson;

    public BspConnectionGenerator(String projectDir) {
        this.projectDir = projectDir;
        this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    }

    /**
     * 生成 BSP 连接配置文件
     *
     * @return 生成的配置文件路径
     */
    public Path generate() throws IOException {
        // 创建 .bsp 目录
        Path bspDir = Paths.get(projectDir, ".bsp");
        Files.createDirectories(bspDir);

        // 生成配置
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("name", "Qin");
        config.put("version", "0.3.0");
        config.put("bspVersion", "2.1.0");
        config.put("languages", List.of("java"));

        // 启动命令
        List<String> argv = buildArgv();
        config.put("argv", argv);

        // 写入文件
        Path configPath = bspDir.resolve("qin.json");
        String json = gson.toJson(config);
        Files.writeString(configPath, json);

        return configPath;
    }

    /**
     * 构建启动命令
     */
    private List<String> buildArgv() {
        List<String> argv = new ArrayList<>();

        // 检测操作系统
        boolean isWindows = System.getProperty("os.name")
            .toLowerCase().contains("win");

        if (isWindows) {
            // Windows: 使用 cmd /c qin bsp
            argv.add("cmd");
            argv.add("/c");
            argv.add("qin");
            argv.add("bsp");
        } else {
            // Unix/Linux/Mac: 直接调用
            argv.add("qin");
            argv.add("bsp");
        }

        return argv;
    }

    /**
     * 检查是否已存在 BSP 配置
     */
    public boolean exists() {
        return Files.exists(Paths.get(projectDir, ".bsp", "qin.json"));
    }

    /**
     * 删除 BSP 配置
     */
    public void remove() throws IOException {
        Path configPath = Paths.get(projectDir, ".bsp", "qin.json");
        Files.deleteIfExists(configPath);
    }

    /**
     * 主入口：为当前项目生成 BSP 配置
     */
    public static void main(String[] args) throws IOException {
        String projectDir = args.length > 0 ? args[0] : QinConstants.getCwd();

        BspConnectionGenerator generator = new BspConnectionGenerator(projectDir);
        Path configPath = generator.generate();

        System.out.println("✓ BSP configuration generated: " + configPath);
        System.out.println();
        System.out.println("Generated .bsp/qin.json:");
        System.out.println(Files.readString(configPath));
    }
}
