package com.qin.core;

import java.util.List;
import java.util.ArrayList;

/**
 * 检测结果
 */
public class DetectionResult {
    /** 检测到的语言 */
    private List<String> languages = new ArrayList<>();
    
    /** 检测到的特性 */
    private List<String> features = new ArrayList<>();
    
    /** 建议的插件 */
    private List<String> suggestedPlugins = new ArrayList<>();
    
    /** 检测到的入口文件 */
    private String entry;
    
    /** 检测到的前端目录 */
    private String clientDir;

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    public void addLanguage(String lang) { this.languages.add(lang); }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
    public void addFeature(String feature) { this.features.add(feature); }

    public List<String> getSuggestedPlugins() { return suggestedPlugins; }
    public void setSuggestedPlugins(List<String> plugins) { this.suggestedPlugins = plugins; }
    public void addSuggestedPlugin(String plugin) { this.suggestedPlugins.add(plugin); }

    public String getEntry() { return entry; }
    public void setEntry(String entry) { this.entry = entry; }

    public String getClientDir() { return clientDir; }
    public void setClientDir(String clientDir) { this.clientDir = clientDir; }

    /**
     * 获取检测结果的友好描述
     */
    public String describe() {
        List<String> parts = new ArrayList<>();

        if (!languages.isEmpty()) {
            parts.add("语言: " + String.join(", ", languages));
        }
        if (!features.isEmpty()) {
            parts.add("特性: " + String.join(", ", features));
        }
        if (entry != null) {
            parts.add("入口: " + entry);
        }
        if (clientDir != null) {
            parts.add("前端: " + clientDir);
        }

        return String.join(" | ", parts);
    }
}
