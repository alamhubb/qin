package com.qin.constants;

/**
 * Qin 构建系统常量
 */
public class QinConstants {

    // 依赖坐标分隔符
    /**
     * Qin 配置文件中使用的分隔符
     * 例如: com.google.code.gson@gson
     */
    public static final String QIN_COORDINATE_SEPARATOR = "@";

    /**
     * Maven/Coursier 使用的分隔符
     * 例如: com.google.code.gson:gson:2.10.1
     */
    public static final String MAVEN_COORDINATE_SEPARATOR = ":";

    /**
     * 版本分隔符
     * 例如: gson-2.10.1
     */
    public static final String VERSION_SEPARATOR = "-";

    /**
     * 将 Qin 坐标转换为 Maven 坐标
     * 
     * @param qinCoordinate Qin 格式: groupId@artifactId
     * @return Maven 格式: groupId:artifactId
     */
    public static String toMavenCoordinate(String qinCoordinate) {
        return qinCoordinate.replace(QIN_COORDINATE_SEPARATOR, MAVEN_COORDINATE_SEPARATOR);
    }

    /**
     * 将 Maven 坐标转换为 Qin 坐标
     * 
     * @param mavenCoordinate Maven 格式: groupId:artifactId
     * @return Qin 格式: groupId@artifactId
     */
    public static String toQinCoordinate(String mavenCoordinate) {
        return mavenCoordinate.replace(MAVEN_COORDINATE_SEPARATOR, QIN_COORDINATE_SEPARATOR);
    }

    private QinConstants() {
        // 工具类，禁止实例化
    }
}
