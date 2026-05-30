# Qin Bootstrap Script
# Compiles the root Qin CLI from source with javac only.

$ErrorActionPreference = "Stop"

$QIN_HOME = Split-Path -Parent $MyInvocation.MyCommand.Path
$SRC_DIR = Join-Path $QIN_HOME "src"
$BUILD_DIR = Join-Path $QIN_HOME "build\classes"
$GSON_JAR = Join-Path $QIN_HOME "lib\gson-2.10.1.jar"

Write-Host "-> Qin Bootstrap Compiler" -ForegroundColor Cyan
Write-Host "  Source: $SRC_DIR"
Write-Host "  Output: $BUILD_DIR"

if (-not (Test-Path $SRC_DIR)) {
    Write-Host "  ERROR: Source directory does not exist: $SRC_DIR" -ForegroundColor Red
    exit 1
}

if (-not (Test-Path $GSON_JAR)) {
    Write-Host "  ERROR: Missing dependency: $GSON_JAR" -ForegroundColor Red
    Write-Host "  Run script\build.bat or restore lib\gson-2.10.1.jar first." -ForegroundColor Yellow
    exit 1
}

if (-not (Test-Path $BUILD_DIR)) {
    New-Item -ItemType Directory -Path $BUILD_DIR -Force | Out-Null
}

$javaFiles = Get-ChildItem -Path $SRC_DIR -Filter "*.java" -Recurse -File | Select-Object -ExpandProperty FullName
Write-Host "  -> Found $($javaFiles.Count) Java files"

if ($javaFiles.Count -eq 0) {
    Write-Host "  ERROR: No Java sources found under $SRC_DIR" -ForegroundColor Red
    exit 1
}

Write-Host "  -> Compiling..." -ForegroundColor Yellow

# Keep the javac @sources file BOM-free; javac treats a BOM as part of the first path.
$tempFile = [System.IO.Path]::GetTempFileName()
[System.IO.File]::WriteAllLines($tempFile, [string[]]$javaFiles, [System.Text.Encoding]::ASCII)

try {
    & javac -d $BUILD_DIR -encoding UTF-8 -cp $GSON_JAR "@$tempFile"

    if ($LASTEXITCODE -eq 0) {
        $classCount = (Get-ChildItem -Path $BUILD_DIR -Filter "*.class" -Recurse -File).Count
        Write-Host "  OK: Bootstrap complete. Compiled $classCount classes." -ForegroundColor Green
    }
    else {
        Write-Host "  ERROR: Compilation failed with exit code $LASTEXITCODE" -ForegroundColor Red
        exit 1
    }
}
finally {
    Remove-Item $tempFile -ErrorAction SilentlyContinue
}
