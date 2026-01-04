package com.qin.debug;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Qin 工具窗口工厂
 */
public class QinToolWindowFactory implements ToolWindowFactory {

    private JTextArea outputArea;

    @Override
    public void createToolWindowContent(@NotNull Project project,
            @NotNull ToolWindow toolWindow) {
        JPanel panel = new JPanel(new BorderLayout());

        // 按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton syncBtn = new JButton("Sync");
        syncBtn.addActionListener(e -> runQinCommand(project, "sync"));

        JButton compileBtn = new JButton("Compile");
        compileBtn.addActionListener(e -> runQinCommand(project, "compile"));

        JButton runBtn = new JButton("Run");
        runBtn.addActionListener(e -> runQinCommand(project, "run"));

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> outputArea.setText(""));

        btnPanel.add(syncBtn);
        btnPanel.add(compileBtn);
        btnPanel.add(runBtn);
        btnPanel.add(clearBtn);

        // 项目信息 + 输出区域
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // 加载项目信息
        String basePath = project.getBasePath();
        if (basePath != null) {
            QinConfig config = QinConfig.load(basePath);
            if (config != null) {
                appendOutput("=== Qin Project ===");
                appendOutput("Name: " + config.name);
                appendOutput("Version: " + config.version);
                appendOutput("Entry: " + config.entry);
                if (config.dependencies != null && !config.dependencies.isEmpty()) {
                    appendOutput("\nDependencies:");
                    config.dependencies.forEach((k, v) -> appendOutput("  " + k + ": " + v));
                }
                appendOutput("\n--- Ready ---\n");
            } else {
                appendOutput("Not a Qin project (qin.config.json not found)");
            }
        }

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        Content content = ContentFactory.getInstance()
                .createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    private void runQinCommand(Project project, String cmd) {
        String basePath = project.getBasePath();
        if (basePath == null)
            return;

        appendOutput("\n> qin " + cmd);
        appendOutput("Running...\n");

        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder("qin", cmd);
                pb.directory(new File(basePath));
                pb.redirectErrorStream(true);
                Process process = pb.start();

                // 读取输出
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String output = line;
                        appendOutput(output);
                    }
                }

                int exitCode = process.waitFor();
                appendOutput("\n[Exit: " + exitCode + "]");

            } catch (Exception e) {
                appendOutput("Error: " + e.getMessage());
            }
        }).start();
    }
}
