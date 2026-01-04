package com.qin.debug;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Qin 工具窗口工厂 - 树形界面
 */
public class QinToolWindowFactory implements ToolWindowFactory {

    // 使用常量类
    private static final String CONFIG_FILE = QinConstants.CONFIG_FILE;
    private static final Set<String> EXCLUDED_DIRS = QinConstants.EXCLUDED_DIRS;

    private Project project;
    private Tree tree;
    private DefaultMutableTreeNode rootNode;
    private JTextArea logArea;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建工具栏
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new RefreshAction());
        actionGroup.addSeparator();

        ActionToolbar toolbar = ActionManager.getInstance()
                .createActionToolbar("QinToolbar", actionGroup, true);
        toolbar.setTargetComponent(mainPanel);

        // 创建树形结构
        rootNode = new DefaultMutableTreeNode("Qin Projects");
        tree = new Tree(rootNode);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new QinTreeCellRenderer());

        // 双击监听器
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (node != null && node.getUserObject() instanceof TaskNode) {
                        TaskNode task = (TaskNode) node.getUserObject();
                        executeTask(task);
                    }
                }
            }
        });

        // 创建分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(tree));

        // 日志区域
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        splitPane.setBottomComponent(new JScrollPane(logArea));
        splitPane.setDividerLocation(400);

        mainPanel.add(toolbar.getComponent(), BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // 加载项目
        loadProjects();

        Content content = ContentFactory.getInstance()
                .createContent(mainPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * 加载所有 Qin 项目
     */
    private void loadProjects() {
        rootNode.removeAllChildren();
        String basePath = project.getBasePath();
        if (basePath == null)
            return;

        java.util.List<Path> projects = new ArrayList<>();

        // 检查根目录
        if (Files.exists(Paths.get(basePath, CONFIG_FILE))) {
            projects.add(Paths.get(basePath));
        }

        // 扫描子目录
        scanQinProjects(Paths.get(basePath), projects, 0, QinConstants.MAX_SCAN_DEPTH);

        if (projects.isEmpty()) {
            DefaultMutableTreeNode emptyNode = new DefaultMutableTreeNode(
                    "No Qin projects found");
            rootNode.add(emptyNode);
        } else {
            for (Path projectPath : projects) {
                addProjectNode(projectPath, basePath);
            }
        }

        ((DefaultTreeModel) tree.getModel()).reload();
        expandAllNodes();
    }

    /**
     * 添加项目节点
     */
    private void addProjectNode(Path projectPath, String workspaceRoot) {
        String relativePath = Paths.get(workspaceRoot).relativize(projectPath).toString();
        if (relativePath.isEmpty())
            relativePath = project.getName();

        // 加载配置
        QinConfig config = QinConfig.load(projectPath.toString());
        String projectName = config != null ? config.name : relativePath;

        ProjectNode projectNode = new ProjectNode(projectName, projectPath.toString());
        DefaultMutableTreeNode projectTreeNode = new DefaultMutableTreeNode(projectNode);

        // Tasks 节点
        DefaultMutableTreeNode tasksNode = new DefaultMutableTreeNode(QinConstants.NODE_TASKS);
        tasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("sync", "Sync dependencies", projectPath.toString())));
        tasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("compile", "Compile project", projectPath.toString())));
        tasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("run", "Run project", projectPath.toString())));
        tasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("build", "Build JAR", projectPath.toString())));
        tasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("clean", "Clean output", projectPath.toString())));
        projectTreeNode.add(tasksNode);

        // Dependencies 节点
        if (config != null && config.dependencies != null && !config.dependencies.isEmpty()) {
            DefaultMutableTreeNode depsNode = new DefaultMutableTreeNode(QinConstants.NODE_DEPENDENCIES);
            config.dependencies.forEach((name, version) -> {
                depsNode.add(new DefaultMutableTreeNode(name + ":" + version));
            });
            projectTreeNode.add(depsNode);
        }

        rootNode.add(projectTreeNode);
    }

    /**
     * 执行任务
     */
    private void executeTask(TaskNode task) {
        appendLog("\n> qin " + task.command + " (" + task.projectPath + ")");
        appendLog("Running...\n");

        new Thread(() -> {
            try {
                ProcessBuilder pb = new ProcessBuilder(QinConstants.CMD_PREFIX, QinConstants.CMD_FLAG,
                        QinConstants.QIN_CMD, task.command);
                pb.directory(new File(task.projectPath));
                pb.redirectErrorStream(true);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), QinConstants.CHARSET_UTF8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String output = line;
                        appendLog(output);
                    }
                }

                int exitCode = process.waitFor();
                appendLog("\n[Exit: " + exitCode + "]\n");

            } catch (Exception e) {
                appendLog("Error: " + e.getMessage());
            }
        }).start();
    }

    /**
     * 扫描 Qin 项目
     */
    private void scanQinProjects(Path dir, java.util.List<Path> projects, int depth, int maxDepth) {
        if (depth >= maxDepth || !Files.exists(dir)) {
            return;
        }

        // 先检查当前目录是否有配置文件（优先级最高）
        if (Files.exists(dir.resolve(CONFIG_FILE)) && !projects.contains(dir)) {
            projects.add(dir);
        }

        // 扫描子目录（即使当前目录有 .git 等也继续扫描子目录）
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();

                // 只排除子目录，不影响当前目录的检测
                if (EXCLUDED_DIRS.contains(dirName) || dirName.startsWith(QinConstants.HIDDEN_PREFIX)) {
                    continue;
                }

                scanQinProjects(subDir, projects, depth + 1, maxDepth);
            }
        } catch (IOException e) {
            // 忽略
        }
    }

    /**
     * 展开所有节点
     */
    private void expandAllNodes() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    // ==================== 刷新动作 ====================

    private class RefreshAction extends AnAction {
        RefreshAction() {
            super("Refresh", "Reload Qin projects", AllIcons.Actions.Refresh);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            appendLog("\n=== Refreshing projects ===\n");
            loadProjects();
            appendLog("Projects reloaded\n");
        }
    }

    // ==================== 树节点类型 ====================

    /**
     * 项目节点
     */
    static class ProjectNode {
        String name;
        String path;

        ProjectNode(String name, String path) {
            this.name = name;
            this.path = path;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 任务节点
     */
    static class TaskNode {
        String command;
        String description;
        String projectPath;

        TaskNode(String command, String description, String projectPath) {
            this.command = command;
            this.description = description;
            this.projectPath = projectPath;
        }

        @Override
        public String toString() {
            return command;
        }
    }

    // ==================== 树渲染器 ====================

    private static class QinTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object userObject = node.getUserObject();

            if (userObject instanceof ProjectNode) {
                setIcon(AllIcons.Nodes.Module);
            } else if (userObject instanceof TaskNode) {
                setIcon(AllIcons.Actions.Execute);
            } else if (QinConstants.NODE_TASKS.equals(userObject)) {
                setIcon(AllIcons.Nodes.Folder);
            } else if (QinConstants.NODE_DEPENDENCIES.equals(userObject)) {
                setIcon(AllIcons.Nodes.PpLib);
            } else if (userObject instanceof String && ((String) userObject).contains(":")) {
                // 依赖项
                setIcon(AllIcons.Nodes.PpJar);
            }

            return this;
        }
    }
}
