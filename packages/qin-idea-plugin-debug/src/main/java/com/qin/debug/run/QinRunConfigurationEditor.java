package com.qin.debug.run;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Qin 运行配置编辑器
 * 提供配置界面，让用户编辑运行配置的各项参数
 */
public class QinRunConfigurationEditor extends SettingsEditor<QinRunConfiguration> {

    private final JPanel mainPanel;
    private final TextFieldWithBrowseButton projectPathField;
    private final JBTextField mainClassField;
    private final JBTextField programArgsField;
    private final JBTextField jvmArgsField;
    private final JBTextField debugPortField;

    public QinRunConfigurationEditor(Project project) {
        // 项目路径选择器
        projectPathField = new TextFieldWithBrowseButton();
        projectPathField.addBrowseFolderListener(
            "Select Qin Project",
            "Select the directory containing qin.config.json",
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        );

        // 主类输入框
        mainClassField = new JBTextField();

        // 程序参数输入框
        programArgsField = new JBTextField();

        // JVM 参数输入框
        jvmArgsField = new JBTextField();

        // 调试端口输入框
        debugPortField = new JBTextField();
        debugPortField.setText("5005");

        // 构建表单
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(new JBLabel("Project path:"), projectPathField, 1, false)
            .addLabeledComponent(new JBLabel("Main class:"), mainClassField, 1, false)
            .addLabeledComponent(new JBLabel("Program arguments:"), programArgsField, 1, false)
            .addLabeledComponent(new JBLabel("JVM arguments:"), jvmArgsField, 1, false)
            .addLabeledComponent(new JBLabel("Debug port:"), debugPortField, 1, false)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
    }

    @Override
    protected void resetEditorFrom(@NotNull QinRunConfiguration config) {
        projectPathField.setText(config.getProjectPath() != null ? config.getProjectPath() : "");
        mainClassField.setText(config.getMainClass() != null ? config.getMainClass() : "");
        programArgsField.setText(config.getProgramArguments() != null ? config.getProgramArguments() : "");
        jvmArgsField.setText(config.getJvmArguments() != null ? config.getJvmArguments() : "");
        debugPortField.setText(String.valueOf(config.getDebugPort()));
    }

    @Override
    protected void applyEditorTo(@NotNull QinRunConfiguration config) {
        config.setProjectPath(projectPathField.getText().trim());
        config.setMainClass(mainClassField.getText().trim());
        config.setProgramArguments(programArgsField.getText().trim());
        config.setJvmArguments(jvmArgsField.getText().trim());

        try {
            config.setDebugPort(Integer.parseInt(debugPortField.getText().trim()));
        } catch (NumberFormatException e) {
            config.setDebugPort(5005);
        }
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}
