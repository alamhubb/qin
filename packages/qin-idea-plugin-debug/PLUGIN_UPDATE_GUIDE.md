# Qin IDEA 插件更新指南

## 🎯 目标

为 IDEA 插件的 Tasks 树添加 3 个新命令：`test`, `jar`, `fatjar`

## 📝 更新步骤

### 步骤 1: 更新 QinToolWindowFactory.java

**文件路径：** `src/main/java/com/qin/debug/QinToolWindowFactory.java`

**定位到第 145-155 行** (在 `addProjectNode()` 方法中的 Tasks 节点部分)

**替换为以下代码：**

```java
// Tasks 节点
DefaultMutableTreeNode tasksNode = new DefaultMutableTreeNode(com.qin.debug.QinConstants.NODE_TASKS);
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("sync", "Sync dependencies", projectPath.toString())));
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("compile", "Compile project", projectPath.toString())));
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("run", "Run project", projectPath.toString())));
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("test", "Run tests", projectPath.toString())));        // 🆕 新增
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("jar", "Build JAR (no deps)", projectPath.toString())));  // 🆕 新增
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("fatjar", "Build Fat JAR", projectPath.toString())));     // 🆕 新增
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("build", "Build (full)", projectPath.toString())));
tasksNode.add(new DefaultMutableTreeNode(
        new TaskNode("clean", "Clean output", projectPath.toString())));
projectTreeNode.add(tasksNode);
```

### 步骤 2: 重新编译插件

```bash
cd D:/project/qkyproject/slime-java/qin/packages/qin-idea-plugin-debug
./gradlew buildPlugin
```

### 步骤 3: 安装并测试

1. 在 IDEA 中：`File > Settings > Plugins > ⚙️ > Install Plugin from Disk...`
2. 选择生成的插件文件：`build/distributions/qin-idea-plugin-debug-*.zip`
3. 重启 IDEA
4. 打开一个 Qin 项目
5. 查看右侧的 "Qin" 工具窗口
6. 验证新命令是否可见并可点击

## 🎨 预期效果

### 更新前：
```
📦 my-project
  📁 Tasks
    ⚙️ sync
    🔨 compile
    ▶️ run
    🏗️ build
    🧹 clean
```

### 更新后：
```
📦 my-project
  📁 Tasks
    ⚙️ sync
    🔨 compile
    ▶️ run
    🧪 test          ← 🆕 新增
    📦 jar           ← 🆕 新增
    📦 fatjar        ← 🆕 新增
    🏗️ build
    🧹 clean
```

## ✅ 测试验证

更新后测试每个新命令：

1. **test** - 点击后应该执行 `qin test`（如果项目有测试）
2. **jar** - 点击后应该生成普通 JAR 文件（不含依赖）
3. **fatjar** - 点击后应该生成 Fat JAR 文件（包含所有依赖）

查看插件底部的日志输出，确认命令执行成功。

## 📌 注意事项

1. **无需修改 `executeTask()` 方法** - 该方法已经是通用的，可以执行任何命令
2. **命令执行逻辑** - 由 CLI (`QinCli.java`) 处理，插件只负责调用
3. **图标** - 所有任务节点使用统一的 `AllIcons.Actions.Execute` 图标

## 🚀 可选优化

### 为不同命令添加不同图标

如果想为不同命令使用不同的图标，可以在 `QinTreeCellRenderer` 类中添加：

```java
} else if (userObject instanceof TaskNode) {
    TaskNode task = (TaskNode) userObject;
    switch (task.command) {
        case "run" -> setIcon(AllIcons.Actions.Execute);
        case "build", "jar", "fatjar" -> setIcon(AllIcons.Actions.Compile);
        case "test" -> setIcon(AllIcons.RunConfigurations.TestState.Run);
        case "clean" -> setIcon(AllIcons.Actions.GC);
        default -> setIcon(AllIcons.Actions.Execute);
    }
}
```

### 添加右键菜单

可以在树节点上添加右键菜单，提供更多操作选项：
- 在终端中运行
- 带参数运行
- 查看输出目录
- 等等...

## 📊 完成度

- ✅ CLI 集成完成（jar, fatjar, deps 命令）
- ⏳ IDEA 插件更新（待你完成，预计 5 分钟）
- ⏳ 测试验证（更新后测试）

**这是整个构建系统现代化的最后一步！** 🎉
