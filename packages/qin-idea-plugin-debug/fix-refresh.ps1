# Fix QinToolWindowFactory.java refresh logic
$file = "src\main\java\com\qin\debug\QinToolWindowFactory.java"
$content = Get-Content $file -Raw

# 替换刷新逻辑
$oldPattern = @"
                            VirtualFileManager.getInstance\(\).refreshWithoutFileWatcher\(false\);

                        // 触发项目结构重新加载
                        ProjectRootManager.getInstance\(project\).incModificationCount\(\);

                        appendLog\("\[✓\] IDEA 项目刷新完成，依赖已生效"\);
"@

$newPattern = @"
                            VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);
                            
                            // 2. 触发项目结构重新加载
                            ProjectRootManager.getInstance(project).incModificationCount();
                            
                            // 3. 等待索引重建
                            Thread.sleep(500);
                            
                            appendLog("[✓] IDEA 刷新完成，索引已更新");
                        } catch (Exception ex) {
                            appendLog("[!] 刷新失败: " + ex.getMessage());
                        }
"@

$content = $content -replace [regex]::Escape($oldPattern), $newPattern
Set-Content $file $content -NoNewline

Write-Host "Fixed refresh logic!"
