package com.qin.debug.run;

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
 * Editor for Qin run configurations.
 */
public class QinRunConfigurationEditor extends SettingsEditor<QinRunConfiguration> {

    private final JPanel mainPanel;
    private final TextFieldWithBrowseButton projectPathField;
    private final JBTextField mainClassField;
    private final JBTextField programArgsField;
    private final JBTextField jvmArgsField;
    private final JBTextField debugPortField;

    public QinRunConfigurationEditor(Project project) {
        projectPathField = new TextFieldWithBrowseButton();
        var descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle("Select Qin Project");
        descriptor.setDescription("Select the directory containing qin.config.js");
        projectPathField.addBrowseFolderListener(new TextBrowseFolderListener(descriptor, project));
        projectPathField.getTextField().setToolTipText("Auto-detect nearest Qin project");

        mainClassField = new JBTextField();
        mainClassField.getEmptyText().setText("Optional, defaults to qin.config.js entry");

        programArgsField = new JBTextField();
        programArgsField.getEmptyText().setText("Optional");

        jvmArgsField = new JBTextField();
        jvmArgsField.getEmptyText().setText("Optional");

        debugPortField = new JBTextField();
        debugPortField.setText("5005");

        mainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Project path (optional):"), projectPathField, 1, false)
                .addLabeledComponent(new JBLabel("Main class (optional):"), mainClassField, 1, false)
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

