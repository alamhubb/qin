package com.qin.debug.test;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Editor for Qin test configurations.
 */
public class QinTestConfigurationEditor extends SettingsEditor<QinTestConfiguration> {

    private final JPanel mainPanel;
    private final TextFieldWithBrowseButton projectPathField;
    private final JBTextField testClassField;
    private final JBTextField testMethodField;

    public QinTestConfigurationEditor(Project project) {
        projectPathField = new TextFieldWithBrowseButton();
        var descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle("Select Qin Project");
        descriptor.setDescription("Select the directory containing qin.config.js");
        projectPathField.addBrowseFolderListener(new TextBrowseFolderListener(descriptor, project));
        projectPathField.getTextField().setToolTipText("Auto-detect nearest Qin project");

        testClassField = new JBTextField();
        testClassField.getEmptyText().setText("Optional");

        testMethodField = new JBTextField();
        testMethodField.getEmptyText().setText("Optional");

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Project path (optional):"), projectPathField, 1, false)
                .addLabeledComponent(new JBLabel("Test class (optional):"), testClassField, 1, false)
                .addLabeledComponent(new JBLabel("Test method (optional):"), testMethodField, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    @Override
    protected void resetEditorFrom(@NotNull QinTestConfiguration config) {
        projectPathField.setText(config.getProjectPath() != null ? config.getProjectPath() : "");
        testClassField.setText(config.getTestClass() != null ? config.getTestClass() : "");
        testMethodField.setText(config.getTestMethod() != null ? config.getTestMethod() : "");
    }

    @Override
    protected void applyEditorTo(@NotNull QinTestConfiguration config) {
        config.setProjectPath(projectPathField.getText().trim());
        config.setTestClass(testClassField.getText().trim());
        config.setTestMethod(testMethodField.getText().trim());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }
}

