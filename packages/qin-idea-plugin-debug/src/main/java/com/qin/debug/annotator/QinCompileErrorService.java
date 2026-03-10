package com.qin.debug.annotator;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编译错误存储服务
 * 管理当前项目的所有编译错误，供 ExternalAnnotator 使用
 */
@Service(Service.Level.PROJECT)
public final class QinCompileErrorService {

    // 按文件路径存储错误列表
    private final Map<String, List<QinCompileError>> errorsByFile = new ConcurrentHashMap<>();

    // 解析 javac 错误的正则表达式
    private static final Pattern ERROR_PATTERN = Pattern.compile(
        "^([A-Za-z]:)?([^:]+\\.java):(\\d+)(?::(\\d+))?:\\s*(error|warning|错误|警告):\\s*(.+)$",
        Pattern.MULTILINE
    );

    /**
     * 解析编译输出并存储错误
     */
    public void parseAndStoreErrors(String compileOutput) {
        clearErrors();

        Matcher matcher = ERROR_PATTERN.matcher(compileOutput);
        while (matcher.find()) {
            String driveLetter = matcher.group(1);
            String filePath = matcher.group(2);
            int line = Integer.parseInt(matcher.group(3));
            int column = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 1;
            String type = matcher.group(5);
            String message = matcher.group(6);

            String fullPath = (driveLetter != null ? driveLetter : "") + filePath;
            fullPath = fullPath.replace('\\', '/');

            boolean isError = type.equals("error") || type.equals("错误");

            QinCompileError error = new QinCompileError(fullPath, line, column, message, isError);
            addError(fullPath, error);
        }
    }

    /**
     * 添加单个错误
     */
    public void addError(String filePath, QinCompileError error) {
        errorsByFile.computeIfAbsent(filePath, k -> new ArrayList<>()).add(error);
    }

    /**
     * 设置某文件的错误列表
     */
    public void setErrors(String filePath, List<QinCompileError> errors) {
        if (errors == null || errors.isEmpty()) {
            errorsByFile.remove(filePath);
        } else {
            errorsByFile.put(filePath, new ArrayList<>(errors));
        }
    }

    /**
     * 获取某文件的错误列表
     */
    public List<QinCompileError> getErrors(String filePath) {
        return errorsByFile.getOrDefault(filePath, Collections.emptyList());
    }

    /**
     * 获取所有错误
     */
    public Map<String, List<QinCompileError>> getAllErrors() {
        return new HashMap<>(errorsByFile);
    }

    /**
     * 清除所有错误
     */
    public void clearErrors() {
        errorsByFile.clear();
    }

    /**
     * 清除某文件的错误
     */
    public void clearErrors(String filePath) {
        errorsByFile.remove(filePath);
    }

    /**
     * 是否有错误
     */
    public boolean hasErrors() {
        return !errorsByFile.isEmpty();
    }

    /**
     * 获取错误总数
     */
    public int getErrorCount() {
        return errorsByFile.values().stream()
            .mapToInt(List::size)
            .sum();
    }
}
