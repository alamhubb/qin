package com.qin.types;

/**
 * 安装结果 (Java 25 Record)
 *
 * @param success     是否成功
 * @param installPath 安装路径
 * @param error       错误信息
 */
public record InstallResult(
        boolean success,
        String installPath,
        String error) {

    /**
     * 成功结果
     */
    public static InstallResult success(String installPath) {
        return new InstallResult(true, installPath, null);
    }

    /**
     * 失败结果
     */
    public static InstallResult failure(String error) {
        return new InstallResult(false, null, error);
    }

    /**
     * Record accessor aliases
     */
    public boolean isSuccess() {
        return success;
    }

    public String getInstallPath() {
        return installPath;
    }

    public String getError() {
        return error;
    }
}
