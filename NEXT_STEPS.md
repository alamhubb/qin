# Qin Java 25 重写 - 下一步行动清单

## 📋 立即执行（修复编译）

### 1. 修复剩余 3 个 BOM 文件（5分钟）
需要重新保存为 UTF-8 无 BOM：
```
- src/java-rewrite/com/qin/core/FatJarBuilder.java  ❌
- src/java-rewrite/com/qin/core/JavaRunner.java      ❌ 
- src/java-rewrite/com/qin/core/WorkspaceLoader.java ❌
```

✅ 已修复: ConfigLoader.java

### 2. 测试编译（2分钟）
```bash
cd d:\project\qkyproject\slime-java\qin
.\build-java.bat
```

预期结果：types 层编译通过，core 层有访问器错误

---

## 🔄 Phase 2: 批量修改访问器（2小时）

### 需要修改的模式

**查找并替换**：
```
.getName()        → .name()
.getVersion()     → .version()
.getEntry()       → .entry()
.getJava()        → .java()
.getOutput()      → .output()
.getDependencies()→ .dependencies()
.getRepositories()→ .repositories()
.getPackages()    → .packages()
.isLocalRep()     → .localRep()
.getUrl()         → .url()
.getDir()         → .dir()
.getJarName()     → .jarName()
.getSourceDir()   → .sourceDir()
```

### 影响的文件（约30个）
```
core/:
  - WorkspaceLoader.java
  - JavaRunner.java
  - FatJarBuilder.java
  - PluginManager.java
  - EnvironmentChecker.java
  
cli/:
  - QinCli.java
  
java/:
  - JavaBuilder.java
  - PackageManager.java
  - ClasspathUtils.java
  
npm/:
  - NpmPackageManager.java
  
... (其他)
```

---

## 📝 手动批量修改脚本（PowerShell）

创建 `fix-getters.ps1`：

```powershell
$replacements = @{
    '\.getName\(\)'         = '.name()'
    '\.getVersion\(\)'      = '.version()'
    '\.getEntry\(\)'        = '.entry()'
    '\.getJava\(\)'         = '.java()'
    '\.getOutput\(\)'       = '.output()'
    '\.getDependencies\(\)' = '.dependencies()'
    '\.getRepositories\(\)' = '.repositories()'
    '\.getPackages\(\)'     = '.packages()'
    '\.isLocalRep\(\)'      = '.localRep()'
    '\.getUrl\(\)'          = '.url()'
    '\.getDir\(\)'          = '.dir()'
    '\.getJarName\(\)'      = '.jarName()'
    '\.getSourceDir\(\)'    = '.sourceDir()'
}

Get-ChildItem "src\java-rewrite" -Recurse -Filter "*.java" | ForEach-Object {
    $content = Get-Content $_.FullName -Raw -Encoding UTF8
    $modified = $false
    
    foreach ($old in $replacements.Keys) {
        $new = $replacements[$old]
        if ($content -match $old) {
            $content = $content -replace $old, $new
            $modified = $true
        }
    }
    
    if ($modified) {
        [System.IO.File]::WriteAllText($_.FullName, $content, [System.Text.UTF8Encoding]::new($false))
        Write-Host "✅ Fixed: $($_.Name)"
    }
}

Write-Host "`n完成！"
```

运行：
```powershell
cd d:\project\qkyproject\slime-java\qin
.\fix-getters.ps1
```

---

## ✅ 验证步骤

### 1. 编译测试
```bash
.\build-java.bat
```

### 2. 运行 subhuti-java
```bash
cd ..\slime\slime-java\subhuti-java
java -cp "..\..\..\..\qin\.qin\classes;..\..\..\..\qin\lib\gson-2.10.1.jar" com.qin.cli.QinCli compile
```

### 3. 运行 Main.java
```bash
java -cp "..\..\..\..\qin\.qin\classes;..\..\..\..\qin\lib\gson-2.10.1.jar" com.qin.cli.QinCli run
```

---

## 📊 预计时间

| 任务 | 时间 | 优先级 |
|------|------|--------|
| 修复 BOM 字符 | 5分钟 | 🔥 高 |
| 创建批量脚本 | 10分钟 | 🔥 高 |
| 运行批量修改 | 5分钟 | 🔥 高 |
| 编译测试 | 10分钟 | 🔥 高 |
| 手动修复剩余错误 | 30分钟 | 中 |
| 测试 subhuti-java | 15分钟 | 中 |

**总计**: 约 1.5 小时

---

## 🎯 完成标准

- [  ] 所有文件编译通过
- [  ] Qin CLI 可以运行
- [  ] subhuti-java 可以编译
- [  ] subhuti-java Main.java 可以运行
- [  ] 无编译警告

---

## 💡 备选方案

如果批量修改太复杂，可以：
1. 在 Records 中添加临时的 getter 方法
2. 等项目稳定后再逐步移除

但这违背了 "完全拥抱 Java 25" 的原则，不推荐。

---

**创建时间**: 2025-12-29 05:02  
**下次会话**: 继续执行此清单

🚀 继续加油！
