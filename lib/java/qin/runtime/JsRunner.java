package qin.runtime;

import org.graalvm.polyglot.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * JavaScript 执行桥接器
 * 使用 GraalVM Polyglot API 执行 JavaScript 文件
 * 
 * 用法: java qin.runtime.JsRunner <script.js> [args...]
 */
public class JsRunner {
    
    private static final String LANGUAGE_JS = "js";
    
    /**
     * 主入口点
     * @param args [0] = JavaScript 文件路径, [1...] = 脚本参数
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java qin.runtime.JsRunner <script.js> [args...]");
            System.exit(1);
            return;
        }
        
        String scriptPath = args[0];
        String[] scriptArgs = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];
        
        File scriptFile = new File(scriptPath);
        if (!scriptFile.exists()) {
            System.err.println("Error: File not found: " + scriptPath);
            System.exit(1);
            return;
        }
        
        try (Context context = createContext(scriptFile.getParentFile())) {
            // 设置全局 args 数组
            setupArgs(context, scriptArgs);
            
            // 创建并执行 Source
            Source source = createSource(scriptFile);
            context.eval(source);
            
        } catch (PolyglotException e) {
            handleError(e, scriptPath);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error: Cannot read file: " + scriptPath);
            System.err.println("  " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * 创建配置好的 Polyglot Context
     * - 启用 JavaScript 语言
     * - 启用 ES 模块支持
     * - 启用 Java 互操作
     */
    private static Context createContext(File workingDir) {
        return Context.newBuilder(LANGUAGE_JS)
            // 允许所有访问（文件系统、环境变量等）
            .allowAllAccess(true)
            // 启用 Java 互操作
            .allowHostAccess(HostAccess.ALL)
            .allowHostClassLookup(className -> true)
            // 设置当前目录
            .currentWorkingDirectory(workingDir != null ? workingDir.toPath() : Path.of("."))
            // 启用 ES 模块
            .option("js.esm-eval-returns-exports", "true")
            // 允许实验性选项
            .allowExperimentalOptions(true)
            // 标准 IO
            .out(System.out)
            .err(System.err)
            .in(System.in)
            .build();
    }
    
    /**
     * 从文件创建 Source 对象
     */
    private static Source createSource(File file) throws IOException {
        String content = Files.readString(file.toPath());
        String fileName = file.getName();
        
        // 检测是否是 ES 模块
        boolean isModule = fileName.endsWith(".mjs") || 
                          content.contains("import ") || 
                          content.contains("export ");
        
        Source.Builder builder = Source.newBuilder(LANGUAGE_JS, file)
            .name(fileName);
        
        if (isModule) {
            builder.mimeType("application/javascript+module");
        }
        
        return builder.build();
    }
    
    /**
     * 设置全局 args 数组
     */
    private static void setupArgs(Context context, String[] args) {
        // 创建 JavaScript 数组
        StringBuilder jsArray = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) jsArray.append(",");
            // 转义字符串
            jsArray.append("\"").append(escapeJs(args[i])).append("\"");
        }
        jsArray.append("]");
        
        // 设置全局变量
        context.eval(LANGUAGE_JS, "globalThis.args = " + jsArray + ";");
        // 也设置 process.argv 风格的变量（兼容 Node.js）
        context.eval(LANGUAGE_JS, "globalThis.process = globalThis.process || {}; globalThis.process.argv = ['java', 'script.js', ..." + jsArray + "];");
    }
    
    /**
     * 转义 JavaScript 字符串
     */
    private static String escapeJs(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    /**
     * 格式化并输出错误信息
     */
    private static void handleError(PolyglotException e, String scriptPath) {
        if (e.isSyntaxError()) {
            System.err.println("SyntaxError in " + scriptPath);
        } else if (e.isGuestException()) {
            System.err.println("Error in " + scriptPath);
        } else {
            System.err.println("Error: " + e.getMessage());
        }
        
        // 获取源位置
        SourceSection sourceSection = e.getSourceLocation();
        if (sourceSection != null) {
            System.err.println("  File: " + sourceSection.getSource().getName());
            System.err.println("  Line: " + sourceSection.getStartLine() + ", Column: " + sourceSection.getStartColumn());
        }
        
        // 打印消息
        System.err.println("  Message: " + e.getMessage());
        
        // 打印堆栈跟踪（仅 guest 部分）
        if (e.isGuestException()) {
            System.err.println("\nStack trace:");
            for (PolyglotException.StackFrame frame : e.getPolyglotStackTrace()) {
                if (frame.isGuestFrame()) {
                    SourceSection loc = frame.getSourceLocation();
                    if (loc != null) {
                        System.err.println("    at " + frame.getRootName() + 
                            " (" + loc.getSource().getName() + ":" + loc.getStartLine() + ")");
                    } else {
                        System.err.println("    at " + frame.getRootName());
                    }
                }
            }
        }
    }
}
