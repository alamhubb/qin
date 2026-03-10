package com.qin.debug.console;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.HyperlinkInfo;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 控制台过滤器：将编译错误转换为可点击链接
 *
 * 匹配格式示例:
 * - D:\project\src\Main.java:15: error: cannot find symbol
 * - /home/user/project/src/Main.java:15:10: error: ';' expected
 * - D:\project\src\Main.java:15: 错误: 找不到符号
 */
public class QinConsoleFilter implements Filter {

    // 匹配 javac 错误格式: 文件路径:行号: error/warning/错误/警告: 消息
    private static final Pattern ERROR_PATTERN = Pattern.compile(
        "^([A-Za-z]:)?([^:]+\\.java):(\\d+)(?::(\\d+))?:\\s*(error|warning|错误|警告|注意):\\s*(.+)$"
    );

    // 匹配堆栈跟踪格式: at com.example.Main.method(Main.java:15)
    private static final Pattern STACKTRACE_PATTERN = Pattern.compile(
        "\\s*at\\s+([\\w.$]+)\\.([\\w$]+)\\(([\\w$]+\\.java):(\\d+)\\)"
    );

    private final Project project;

    public QinConsoleFilter(Project project) {
        this.project = project;
    }

    @Nullable
    @Override
    public Result applyFilter(@NotNull String line, int entireLength) {
        // 先尝试匹配编译错误
        Result result = matchCompileError(line, entireLength);
        if (result != null) {
            return result;
        }

        // 再尝试匹配堆栈跟踪
        return matchStackTrace(line, entireLength);
    }

    /**
     * 匹配编译错误格式
     */
    @Nullable
    private Result matchCompileError(String line, int entireLength) {
        Matcher matcher = ERROR_PATTERN.matcher(line.trim());
        if (!matcher.find()) {
            return null;
        }

        String driveLetter = matcher.group(1); // Windows 盘符，可能为 null
        String filePath = matcher.group(2);
        int lineNumber = Integer.parseInt(matcher.group(3));
        Integer column = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0;

        // 构建完整路径
        String fullPath = (driveLetter != null ? driveLetter : "") + filePath;
        fullPath = fullPath.replace('\\', '/');

        // 查找文件
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(fullPath);
        if (file == null) {
            // 尝试在项目中查找
            file = findFileInProject(filePath);
        }

        if (file == null) {
            return null;
        }

        // 计算链接的起止位置
        int lineStart = entireLength - line.length();
        String trimmedLine = line.trim();
        int pathStartInTrimmed = 0;
        int pathEndInTrimmed = trimmedLine.indexOf(":", pathStartInTrimmed + fullPath.length());

        // 找到文件路径在原始行中的位置
        int pathStartInLine = line.indexOf(filePath);
        if (pathStartInLine < 0) {
            pathStartInLine = 0;
        }

        // 计算结束位置（包含行号）
        String lineNumStr = String.valueOf(lineNumber);
        int pathEndInLine = line.indexOf(":", pathStartInLine + filePath.length());
        if (pathEndInLine > 0) {
            pathEndInLine = line.indexOf(":", pathEndInLine + 1);
            if (pathEndInLine < 0) {
                pathEndInLine = pathStartInLine + filePath.length() + lineNumStr.length() + 1;
            }
        }

        // 创建超链接
        HyperlinkInfo hyperlink = new OpenFileHyperlinkInfo(
            project, file, lineNumber - 1, column
        );

        return new Result(
            lineStart + pathStartInLine,
            lineStart + pathEndInLine,
            hyperlink
        );
    }

    /**
     * 匹配堆栈跟踪格式
     */
    @Nullable
    private Result matchStackTrace(String line, int entireLength) {
        Matcher matcher = STACKTRACE_PATTERN.matcher(line);
        if (!matcher.find()) {
            return null;
        }

        String className = matcher.group(1);
        String methodName = matcher.group(2);
        String fileName = matcher.group(3);
        int lineNumber = Integer.parseInt(matcher.group(4));

        // 将类名转换为文件路径
        String packagePath = className.substring(0, className.lastIndexOf('.') + 1)
                                       .replace('.', '/');

        // 在项目中查找文件
        VirtualFile file = findFileInProject(packagePath + fileName);
        if (file == null) {
            file = findFileInProject(fileName);
        }

        if (file == null) {
            return null;
        }

        // 计算链接位置 - 匹配 (FileName.java:123) 部分
        int lineStart = entireLength - line.length();
        int linkStart = line.indexOf("(" + fileName);
        int linkEnd = line.indexOf(")", linkStart) + 1;

        if (linkStart < 0 || linkEnd <= linkStart) {
            return null;
        }

        HyperlinkInfo hyperlink = new OpenFileHyperlinkInfo(
            project, file, lineNumber - 1, 0
        );

        return new Result(
            lineStart + linkStart,
            lineStart + linkEnd,
            hyperlink
        );
    }

    /**
     * 在项目中查找文件
     */
    @Nullable
    private VirtualFile findFileInProject(String relativePath) {
        if (project.getBasePath() == null) {
            return null;
        }

        // 常见的源码目录
        String[] sourceDirs = {
            "src/main/java/",
            "src/test/java/",
            "src/",
            ""
        };

        for (String srcDir : sourceDirs) {
            String fullPath = project.getBasePath() + "/" + srcDir + relativePath;
            fullPath = fullPath.replace('\\', '/');
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(fullPath);
            if (file != null && file.exists()) {
                return file;
            }
        }

        return null;
    }
}
