# Qin Bootstrap Script
# 用于在build目录为空时编译qin本身
# 使用javac直接编译，不依赖已编译的qin

$ErrorActionPreference = "Stop"

$QIN_HOME = Split-Path -Parent $MyInvocation.MyCommand.Path
$SRC_DIR = Join-Path $QIN_HOME "src\java-rewrite"
$BUILD_DIR = Join-Path $QIN_HOME "build\classes"

Write-Host "→ Qin Bootstrap Compiler" -ForegroundColor Cyan
Write-Host "  Source: $SRC_DIR"
Write-Host "  Output: $BUILD_DIR"

# 创建输出目录
if (-not (Test-Path $BUILD_DIR)) {
    New-Item -ItemType Directory -Path $BUILD_DIR -Force | Out-Null
}

# 收集所有Java文件
$javaFiles = Get-ChildItem -Path $SRC_DIR -Filter "*.java" -Recurse -File | Select-Object -ExpandProperty FullName

Write-Host "  → Found $($javaFiles.Count) Java files"

# 编译所有文件
Write-Host "  → Compiling..." -ForegroundColor Yellow

# 使用ASCII编码写入临时文件，避免BOM问题
$tempFile = [System.IO.Path]::GetTempFileName()
[System.IO.File]::WriteAllLines($tempFile, $javaFiles, [System.Text.Encoding]::ASCII)

try {
    & javac -d $BUILD_DIR -encoding UTF-8 "@$tempFile" 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        $classCount = (Get-ChildItem -Path $BUILD_DIR -Filter "*.class" -Recurse -File).Count
        Write-Host "  ✓ Bootstrap complete! Compiled $classCount classes" -ForegroundColor Green
    }
    else {
        Write-Host "  ✗ Compilation failed with exit code $LASTEXITCODE" -ForegroundColor Red
        exit 1
    }
}
finally {
    Remove-Item $tempFile -ErrorAction SilentlyContinue
}
