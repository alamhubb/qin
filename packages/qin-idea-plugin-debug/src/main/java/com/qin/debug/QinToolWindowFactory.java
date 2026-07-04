package com.qin.debug;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.*;

// 鍒悕锛氫娇鐢?qin-cli 鐨勯€氱敤甯搁噺
import static com.qin.constants.QinConstants.*;

/**
 * Qin 宸ュ叿绐楀彛宸ュ巶 - 鏍戝舰鐣岄潰
 */
public class QinToolWindowFactory implements ToolWindowFactory {

    private Project project;
    private Tree tree;
    private DefaultMutableTreeNode rootNode;
    private JTextArea logArea;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;

        // 鍒涘缓涓婚潰鏉?
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 鍒涘缓宸ュ叿鏍?
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new RefreshAction());
        actionGroup.addSeparator();

        ActionToolbar toolbar = ActionManager.getInstance()
                .createActionToolbar("QinToolbar", actionGroup, true);
        toolbar.setTargetComponent(mainPanel);

        // 鍒涘缓鏍戝舰缁撴瀯
        rootNode = new DefaultMutableTreeNode("Qin Projects");
        tree = new Tree(rootNode);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new QinTreeCellRenderer());

        // 鍗曞嚮鐩戝惉鍣紙鏀逛负鍗曞嚮瑙﹀彂锛屾彁鍗囦綋楠岋級
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 1) { // 鏀逛负鍗曞嚮
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                    if (node != null && node.getUserObject() instanceof TaskNode) {
                        TaskNode task = (TaskNode) node.getUserObject();
                        executeTask(task);
                    }
                }
            }
        });

        // 鍒涘缓鍒嗗壊闈㈡澘
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(tree));

        // 鏃ュ織鍖哄煙
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        splitPane.setBottomComponent(new JScrollPane(logArea));
        splitPane.setDividerLocation(400);

        mainPanel.add(toolbar.getComponent(), BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // 鍔犺浇椤圭洰
        loadProjects();

        Content content = ContentFactory.getInstance()
                .createContent(mainPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * 鍔犺浇鎵€鏈?Qin 椤圭洰
     */
    private void loadProjects() {
        rootNode.removeAllChildren();
        String basePath = project.getBasePath();
        if (basePath == null)
            return;

        java.util.List<Path> projects = DebugStartup.discoverQinProjects(Paths.get(basePath));

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
     * 娣诲姞椤圭洰鑺傜偣
     */
    private void addProjectNode(Path projectPath, String workspaceRoot) {
        String relativePath = Paths.get(workspaceRoot).relativize(projectPath).toString();
        if (relativePath.isEmpty())
            relativePath = project.getName();

        // 鍔犺浇閰嶇疆
        com.qin.types.QinConfig config = QinConfigSupport.load(projectPath);
        String projectName = QinConfigSupport.projectName(config, relativePath);

        ProjectNode projectNode = new ProjectNode(projectName, projectPath.toString());
        DefaultMutableTreeNode projectTreeNode = new DefaultMutableTreeNode(projectNode);

        DefaultMutableTreeNode tasksNode = new DefaultMutableTreeNode(NODE_TASKS);
        Map<String, String> scripts = QinConfigSupport.scripts(config);
        if (!scripts.isEmpty()) {
            DefaultMutableTreeNode scriptsNode = new DefaultMutableTreeNode("Configured scripts");
            scripts.forEach((name, command) -> scriptsNode.add(new DefaultMutableTreeNode(
                    new TaskNode("script", name, "scripts." + name + " = " + command, projectPath.toString()))));
            tasksNode.add(scriptsNode);
        }

        DefaultMutableTreeNode builtinTasksNode = new DefaultMutableTreeNode("Built-in commands");
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("sync", "Sync dependencies", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("compile", "Compile project", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("run", "Run project", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("test", "Run tests", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("jar", "Build JAR (no deps)", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("fatjar", "Build Fat JAR", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("deps", "Show dependencies", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("build", "Build (full)", projectPath.toString())));
        builtinTasksNode.add(new DefaultMutableTreeNode(
                new TaskNode("clean", "Clean output", projectPath.toString())));
        tasksNode.add(builtinTasksNode);
        projectTreeNode.add(tasksNode);

        Map<String, String> dependencies = QinConfigSupport.dependencies(config);
        Map<String, String> devDependencies = QinConfigSupport.devDependencies(config);
        if (!dependencies.isEmpty() || !devDependencies.isEmpty()) {
            DefaultMutableTreeNode depsNode = new DefaultMutableTreeNode(NODE_DEPENDENCIES);
            addDependencyGroup(depsNode, "dependencies", dependencies);
            addDependencyGroup(depsNode, "devDependencies", devDependencies);
            projectTreeNode.add(depsNode);
        }

        rootNode.add(projectTreeNode);
    }

    private void addDependencyGroup(
            DefaultMutableTreeNode parent,
            String label,
            Map<String, String> dependencies) {
        if (dependencies.isEmpty()) {
            return;
        }
        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(label);
        dependencies.forEach((name, version) -> groupNode.add(new DefaultMutableTreeNode(name + ":" + version)));
        parent.add(groupNode);
    }

    /**
     * 鎵ц浠诲姟
     */
    private void executeTask(TaskNode task) {
        appendLog("\n> qin " + task.command + " (" + task.projectPath + ")");
        appendLog("Running...\n");

        new Thread(() -> {
            try {
                ProcessBuilder pb = QinCliProcessBuilders.toolWindowTask(
                        task.projectPath,
                        task.command,
                        task.scriptName);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), CHARSET_UTF8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        final String output = line;
                        appendLog(output);
                    }
                }

                int exitCode = process.waitFor();
                appendLog("\n[Exit: " + exitCode + "]\n");

                // 濡傛灉鏄?sync 鍛戒护涓旀垚鍔燂紝鐢熸垚 .iml 鏂囦欢
                if ("sync".equals(task.command) && exitCode == 0) {
                    Path ideaDir = Paths.get(project.getBasePath(), ".idea");
                    DebugStartup.generateImlFile(Paths.get(task.projectPath), true, ideaDir); // 鎵嬪姩 sync锛氬己鍒惰鐩栧苟娉ㄥ唽
                    appendLog("[鐢熸垚 .iml 鏂囦欢瀹屾垚]");

                    // 鉁?瑙﹀彂 IDEA 瀹屾暣鍒锋柊锛堝寘鎷储寮曢噸寤猴級
                    ApplicationManager.getApplication().invokeLater(() -> {
                        try {
                            appendLog("[寮€濮嬪埛鏂?IDEA...]");

                            // 1. 鍒锋柊铏氭嫙鏂囦欢绯荤粺锛堝惎鐢ㄧ洃鍚櫒锛?
                            VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);

                            // 2. 瑙﹀彂椤圭洰缁撴瀯閲嶆柊鍔犺浇
                            ProjectRootManager.getInstance(project).incModificationCount();


                            appendLog("[鉁揮 IDEA 鍒锋柊瀹屾垚锛岀储寮曞凡鏇存柊");
                        } catch (Exception ex) {
                            appendLog("[!] 鍒锋柊澶辫触: " + ex.getMessage());
                        }
                    });
                }

            } catch (Exception e) {
                appendLog("Error: " + e.getMessage());
            }
        }).start();
    }


    /**
     * 灞曞紑鎵€鏈夎妭鐐?
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

    // ==================== 鍒锋柊鍔ㄤ綔 ====================

    private class RefreshAction extends AnAction {
        RefreshAction() {
            super("Sync All", "Sync all Qin projects", AllIcons.Actions.Refresh);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            appendLog("\n=== Syncing all projects ===\n");

            // 浣跨敤 qin sync --all --force 鍛戒护杩涜瀹屾暣鍚屾
            ApplicationManager.getApplication().executeOnPooledThread(() -> {
                try {
                    appendLog("[璋冪敤 qin sync --all --force]\n");

                    // 鐩存帴璋冪敤 CLI 鍛戒护锛岀敱 CLI 澶勭悊鎵€鏈夐€昏緫
                    ProcessBuilder pb = QinCliProcessBuilders.syncWorkspaceForce(project.getBasePath());

                    Process process = pb.start();
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream(), CHARSET_UTF8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            appendLog(line);
                        }
                    }

                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        QinLogger.notifySuccess("Qin Sync Complete", "All projects synced successfully");
                    } else {
                        QinLogger.notifyError("Qin Sync Failed", "Sync failed with exit code: " + exitCode);
                    }

                    // 鍚屾瀹屾垚鍚庡埛鏂版爲褰㈠垪琛?
                    SwingUtilities.invokeLater(() -> {
                        loadProjects();
                        appendLog("=== Sync complete ===\n");
                    });
                } catch (Exception ex) {
                    appendLog("Error: " + ex.getMessage() + "\n");
                }
            });
        }
    }

    // ==================== 鏍戣妭鐐圭被鍨?====================

    /**
     * 椤圭洰鑺傜偣
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
     * 浠诲姟鑺傜偣
     */
    static class TaskNode {
        String command;
        String scriptName;
        String description;
        String projectPath;

        TaskNode(String command, String description, String projectPath) {
            this(command, null, description, projectPath);
        }

        TaskNode(String command, String scriptName, String description, String projectPath) {
            this.command = command;
            this.scriptName = scriptName;
            this.description = description;
            this.projectPath = projectPath;
        }

        @Override
        public String toString() {
            return scriptName != null ? scriptName : command;
        }
    }

    // ==================== 鏍戞覆鏌撳櫒 ====================

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
            } else if (NODE_TASKS.equals(userObject)) {
                setIcon(AllIcons.Nodes.Folder);
            } else if (NODE_DEPENDENCIES.equals(userObject)) {
                setIcon(AllIcons.Nodes.PpLib);
            } else if (userObject instanceof String && ((String) userObject).contains(":")) {
                // 渚濊禆椤?
                setIcon(AllIcons.Nodes.PpJar);
            }

            return this;
        }
    }
}
