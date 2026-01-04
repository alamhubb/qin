package com.qin.debug;

import com.google.gson.Gson;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 解析 qin.config.json
 */
public class QinConfig {
    public String name;
    public String version;
    public String entry;
    public Map<String, String> dependencies;
    public JavaConfig java;

    public static class JavaConfig {
        public String version;
        public String sourceDir;
    }

    public static QinConfig load(String projectPath) {
        try {
            Path configPath = Paths.get(projectPath, "qin.config.json");
            if (!Files.exists(configPath))
                return null;
            String json = Files.readString(configPath);
            return new Gson().fromJson(json, QinConfig.class);
        } catch (Exception e) {
            return null;
        }
    }
}
