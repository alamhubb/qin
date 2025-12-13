# Qin 安装脚本 (Windows PowerShell)
# 用法: irm https://qinjs.dev/install.ps1 | iex

$ErrorActionPreference = "Stop"

function Write-Step { param($msg) Write-Host "→ " -ForegroundColor Blue -NoNewline; Write-Host $msg }
function Write-Success { param($msg) Write-Host "✓ " -ForegroundColor Green -NoNewline; Write-Host $msg }
function Write-Warning { param($msg) Write-Host "! " -ForegroundColor Yellow -NoNewline; Write-Host $msg }
function Write-Error { param($msg) Write-Host "✗ " -ForegroundColor Red -NoNewline; Write-Host $msg }

Write-Host ""
Write-Host "╔═══════════════════════════════════════╗" -ForegroundColor Blue
Write-Host "║         Qin 安装程序 v1.0.0          ║" -ForegroundColor Blue
Write-Host "╚═══════════════════════════════════════╝" -ForegroundColor Blue
Write-Host ""

# 检测并安装 Bun
Write-Step "检测 Bun..."

$bunPath = Get-Command bun -ErrorAction SilentlyContinue
if ($bunPath) {
    $bunVersion = & bun --version
    Write-Success "Bun 已安装 (v$bunVersion)"
} else {
    Write-Warning "Bun 未安装，正在安装..."
    
    # 安装 Bun
    irm bun.sh/install.ps1 | iex
    
    # 刷新环境变量
    $env:BUN_INSTALL = "$env:USERPROFILE\.bun"
    $env:PATH = "$env:BUN_INSTALL\bin;$env:PATH"
    
    $bunPath = Get-Command bun -ErrorAction SilentlyContinue
    if ($bunPath) {
        Write-Success "Bun 安装成功"
    } else {
        Write-Error "Bun 安装失败，请手动安装: https://bun.sh"
        exit 1
    }
}

# 检测 Java
Write-Step "检测 Java..."

$javaPath = Get-Command java -ErrorAction SilentlyContinue
if ($javaPath) {
    $javaVersion = & java -version 2>&1 | Select-Object -First 1
    Write-Success "Java 已安装"
} else {
    Write-Warning "Java 未安装（Java 项目需要 JDK 17+）"
    Write-Warning "下载地址: https://adoptium.net/"
}

# 安装 Qin
Write-Step "安装 Qin..."

& bun install -g qin

$qinPath = Get-Command qin -ErrorAction SilentlyContinue
if ($qinPath) {
    Write-Success "Qin 安装成功"
} else {
    Write-Error "Qin 安装失败"
    exit 1
}

# 完成
Write-Host ""
Write-Host "═══════════════════════════════════════" -ForegroundColor Green
Write-Host "        安装完成！" -ForegroundColor Green
Write-Host "═══════════════════════════════════════" -ForegroundColor Green
Write-Host ""
Write-Host "开始使用："
Write-Host ""
Write-Host "  qin create my-app   # 创建项目"
Write-Host "  cd my-app"
Write-Host "  qin dev             # 启动开发"
Write-Host ""
Write-Host "文档: https://qinjs.dev"
Write-Host ""

# 提示重新打开终端
Write-Host "提示: 请重新打开 PowerShell 以使用 qin 命令" -ForegroundColor Yellow
Write-Host ""
