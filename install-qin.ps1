# 将qin添加到系统PATH
# 运行: .\install-qin.ps1

$QIN_HOME = Split-Path -Parent $MyInvocation.MyCommand.Path

Write-Host "→ Installing Qin to PATH..." -ForegroundColor Cyan
Write-Host "  Qin location: $QIN_HOME"

# 获取当前用户的PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")

# 检查是否已经在PATH中
if ($currentPath -like "*$QIN_HOME*") {
    Write-Host "  ✓ Qin is already in PATH" -ForegroundColor Green
}
else {
    # 添加到PATH
    $newPath = "$QIN_HOME;$currentPath"
    [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    Write-Host "  ✓ Qin added to PATH" -ForegroundColor Green
    Write-Host "  ℹ Please restart your terminal for changes to take effect" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "→ Testing qin command..." -ForegroundColor Cyan
& "$QIN_HOME\qin.bat" --help

Write-Host ""
Write-Host "✓ Installation complete!" -ForegroundColor Green
Write-Host "  You can now use: qin compile, qin run, etc." -ForegroundColor Cyan
