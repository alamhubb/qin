@echo off
chcp 65001 >nul 2>&1
setlocal EnableDelayedExpansion

echo.
echo ========================================
echo   Qin Uninstaller
echo ========================================
echo.

:: 获取当前脚本所在目录
set "QIN_HOME=%~dp0"
:: 移除末尾的反斜杠
set "QIN_HOME=%QIN_HOME:~0,-1%"

echo [1/2] Qin location: %QIN_HOME%

:: 获取当前用户PATH
for /f "tokens=2*" %%a in ('reg query "HKCU\Environment" /v Path 2^>nul') do set "USER_PATH=%%b"

:: 检查是否在PATH中
echo !USER_PATH! | findstr /i /c:"%QIN_HOME%" >nul
if %errorlevel% neq 0 (
    echo.
    echo   ! Qin is not in PATH, nothing to uninstall
    echo.
    pause
    exit /b 0
)

echo [2/2] Removing Qin from PATH...

:: 从 PATH 中移除 QIN_HOME
:: 处理两种情况: "QIN_HOME;" 或 ";QIN_HOME"
set "NEW_PATH=!USER_PATH!"

:: 移除 "QIN_HOME;" (在开头或中间)
set "NEW_PATH=!NEW_PATH:%QIN_HOME%;=!"

:: 移除 ";QIN_HOME" (在末尾)
set "NEW_PATH=!NEW_PATH:;%QIN_HOME%=!"

:: 如果 PATH 只有 QIN_HOME 一个值
if "!NEW_PATH!"=="%QIN_HOME%" set "NEW_PATH="

:: 更新用户PATH
if "!NEW_PATH!"=="" (
    :: PATH 为空，删除环境变量
    reg delete "HKCU\Environment" /v Path /f >nul 2>&1
) else (
    :: 设置新的 PATH
    setx PATH "!NEW_PATH!" >nul 2>&1
)

if %errorlevel%==0 (
    echo   √ Qin removed from PATH successfully
) else (
    echo   × Failed to remove Qin from PATH
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Uninstall Complete!
echo ========================================
echo.
echo Please restart your terminal.
echo After restart, 'qin' command will no longer be available.
echo.
echo To reinstall, run: install.bat
echo.
pause
