package common;

/**
 * 共享工具类
 */
public class Utils {
    /**
     * 格式化问候语
     */
    public static String greet(String name) {
        return "Hello, " + name + "!";
    }

    /**
     * 获取当前时间戳
     */
    public static long timestamp() {
        return System.currentTimeMillis();
    }
}
