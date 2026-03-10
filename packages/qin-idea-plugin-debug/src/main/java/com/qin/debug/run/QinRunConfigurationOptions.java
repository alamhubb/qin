package com.qin.debug.run;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;

/**
 * Qin 运行配置选项
 * 存储运行配置的各种选项，支持持久化
 */
public class QinRunConfigurationOptions extends RunConfigurationOptions {

    private final StoredProperty<String> projectPath = string("").provideDelegate(this, "projectPath");
    private final StoredProperty<String> mainClass = string("").provideDelegate(this, "mainClass");
    private final StoredProperty<String> programArguments = string("").provideDelegate(this, "programArguments");
    private final StoredProperty<String> jvmArguments = string("").provideDelegate(this, "jvmArguments");
    private final StoredProperty<Integer> debugPort = property(5005).provideDelegate(this, "debugPort");

    public String getProjectPath() {
        return projectPath.getValue(this);
    }

    public void setProjectPath(String path) {
        projectPath.setValue(this, path);
    }

    public String getMainClass() {
        return mainClass.getValue(this);
    }

    public void setMainClass(String className) {
        mainClass.setValue(this, className);
    }

    public String getProgramArguments() {
        return programArguments.getValue(this);
    }

    public void setProgramArguments(String args) {
        programArguments.setValue(this, args);
    }

    public String getJvmArguments() {
        return jvmArguments.getValue(this);
    }

    public void setJvmArguments(String args) {
        jvmArguments.setValue(this, args);
    }

    public int getDebugPort() {
        return debugPort.getValue(this);
    }

    public void setDebugPort(int port) {
        debugPort.setValue(this, port);
    }
}
