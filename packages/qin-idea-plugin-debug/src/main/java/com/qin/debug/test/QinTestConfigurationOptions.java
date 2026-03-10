package com.qin.debug.test;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;

/**
 * Qin 测试配置选项
 */
public class QinTestConfigurationOptions extends RunConfigurationOptions {

    private final StoredProperty<String> projectPath = string("").provideDelegate(this, "projectPath");
    private final StoredProperty<String> testClass = string("").provideDelegate(this, "testClass");
    private final StoredProperty<String> testMethod = string("").provideDelegate(this, "testMethod");

    public String getProjectPath() {
        return projectPath.getValue(this);
    }

    public void setProjectPath(String path) {
        projectPath.setValue(this, path);
    }

    public String getTestClass() {
        return testClass.getValue(this);
    }

    public void setTestClass(String className) {
        testClass.setValue(this, className);
    }

    public String getTestMethod() {
        return testMethod.getValue(this);
    }

    public void setTestMethod(String method) {
        testMethod.setValue(this, method);
    }
}
