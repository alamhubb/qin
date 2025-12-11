package base;

/**
 * 通用工具类
 * 提供常用的工具方法
 */
public class Utils {
    
    /**
     * 格式化问候语
     */
    public static String greet(String name) {
        return "你好, " + (name != null ? name : "世界") + "!";
    }
    
    /**
     * 获取当前时间戳
     */
    public static long timestamp() {
        return System.currentTimeMillis();
    }
    
    /**
     * 格式化 JSON 响应
     */
    public static String jsonResponse(String message) {
        return String.format("{\"message\":\"%s\",\"timestamp\":%d}", message, timestamp());
    }
}
