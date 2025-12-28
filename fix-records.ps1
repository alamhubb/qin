# Qin Java 25 批量修复脚本（PowerShell）
# 将所有 JavaBean getter 改为 Record 访问器

$files = @(
    "d:\project\qkyproject\slime-java\qin\src\java-rewrite\com\qin\core\WorkspaceLoader.java",
    "d:\project\qkyproject\slime-java\qin\src\java-rewrite\com\qin\core\JavaRunner.java",
    "d:\project\qkyproject\slime-java\qin\src\java-rewrite\com\qin\core\FatJarBuilder.java",
    "d:\project\qkyproject\slime-java\qin\src\java-rewrite\com\qin\core\ConfigLoader.java",
    "d:\project\qkyproject\slime-java\qin\src\java-rewrite\com\qin\cli\QinCli.java"
)

$replacements = @{
    ".getVersion()" = ".version()"
    ".getName()" = ".name()"
    ".getEntry()" = ".entry()"
    ".getJava()" = ".java()"
    ".getOutput()" = ".output()"
    ".getDependencies()" = ".dependencies()"
    ".getRepositories()" = ".repositories()"
    ".getPackages()" = ".packages()"
    ".getUrl()" = ".url()"
    ".isLocalRep()" = ".localRep()"
    ".getDir()" = ".dir()"
    ".getJarName()" = ".jarName()"
    ".getSourceDir()" = ".sourceDir()"
}

foreach ($file in $files) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw -Encoding UTF8
        
        foreach ($key in $replacements.Keys) {
            $content = $content.Replace($key, $replacements[$key])
        }
        
        $content | Set-Content $file -NoNewline -Encoding UTF8
        Write-Host "✅ 已修复: $(Split-Path $file -Leaf)"
    } else {
        Write-Host "⚠️  文件不存在: $(Split-Path $file -Leaf)"
    }
}

Write-Host ""
Write-Host "==================================" 
Write-Host "批量修复完成！"
Write-Host "==================================" 
