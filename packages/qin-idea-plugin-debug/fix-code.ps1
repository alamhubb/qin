$file = "src\main\java\com\qin\debug\QinToolWindowFactory.java"
$content = Get-Content $file -Raw -Encoding UTF8

# 找到并替换错误的代码块
$pattern = @'
                    // ✨ 触发 IDEA 完整刷新（包括索引重建）
                    ApplicationManager.getApplication\(\).invokeLater\(\(\) -> \{
                        try \{
                            appendLog\("\[开始刷新 IDEA...\]"\);
                            // 1. 刷新虚拟文件系统（启用监听器）
                            VirtualFileManager.getInstance\(\).refreshWithoutFileWatcher\(false\);

                        // 触发项目结构重新加载
                        ProjectRootManager.getInstance\(project\).incModificationCount\(\);

                        appendLog\("\[✓\] IDEA 项目刷新完成，依赖已生效"\);
                    \}\);
'@

$replacement = @'
                    // ✨ 触发 IDEA 完整刷新（包括索引重建）
                    ApplicationManager.getApplication().invokeLater(() -> {
                        try {
                            appendLog("[开始刷新 IDEA...]");
                            
                            // 1. 刷新虚拟文件系统（启用监听器）
                            VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);
                            
                            // 2. 触发项目结构重新加载
                            ProjectRootManager.getInstance(project).incModificationCount();
                            
                            // 3. 等待索引重建
                            Thread.sleep(500);
                            
                            appendLog("[✓] IDEA 刷新完成，索引已更新");
                        } catch (Exception ex) {
                            appendLog("[!] 刷新失败: " + ex.getMessage());
                        }
                    });
'@

if ($content -match $pattern) {
    $content = $content -replace $pattern, $replacement
    Set-Content $file $content -NoNewline -Encoding UTF8
    Write-Host "✓ Fixed refresh logic successfully!"
}
else {
    Write-Host "✗ Pattern not found, trying manual fix..."
    
    # 读取行并手动修复
    $lines = Get-Content $file -Encoding UTF8
    $output = @()
    $inBadBlock = $false
    
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]
        
        # 检测开始位置
        if ($line -match "触发 IDEA 完整刷新") {
            $inBadBlock = $true
            $output += $line
            $output += "                    ApplicationManager.getApplication().invokeLater(() -> {"
            $output += "                        try {"
            $output += "                            appendLog(`"[开始刷新 IDEA...]`");"
            $output += "                            "
            $output += "                            // 1. 刷新虚拟文件系统（启用监听器）"
            $output += "                            VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);"
            $output += "                            "
            $output += "                            // 2. 触发项目结构重新加载"
            $output += "                            ProjectRootManager.getInstance(project).incModificationCount();"
            $output += "                            "
            $output += "                            // 3. 等待索引重建"
            $output += "                            Thread.sleep(500);"
            $output += "                            "
            $output += "                            appendLog(`"[✓] IDEA 刷新完成，索引已更新`");"
            $output += "                        } catch (Exception ex) {"
            $output += "                            appendLog(`"[!] 刷新失败: `" + ex.getMessage());"
            $output += "                        }"
            $output += "                    });"
            
            # 跳过接下来的错误行
            while ($i -lt $lines.Count - 1) {
                $i++
                if ($lines[$i] -match "^\s+\}\)\;") {
                    $inBadBlock = $false
                    break
                }
            }
        }
        else {
            if (-not $inBadBlock) {
                $output += $line
            }
        }
    }
    
    $output | Set-Content $file -Encoding UTF8
    Write-Host "✓ Manual fix completed!"
}
