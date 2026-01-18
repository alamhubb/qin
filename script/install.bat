@echo off
chcp 65001 >nul 2>&1
setlocal EnableDelayedExpansion

echo → Installing Qin to PATH...

:: 获取当前脚本所在目录
set "QIN_HOME=%~dp0"
:: 移除末尾的反斜杠
set "QIN_HOME=%QIN_HOME:~0,-1%"

echo   Qin location: %QIN_HOME%

:: 获取当前用户PATH
for /f "tokens=2*" %%a in ('reg query "HKCU\Environment" /v Path 2^>nul') do set "USER_PATH=%%b"

:: 检查是否已在PATH中
echo !USER_PATH! | findstr /i /c:"%QIN_HOME%" >nul
if %errorlevel%==0 (
    echo   √ Qin is already in PATH
) else (
    :: 添加到用户PATH（不需要管理员权限）
    setx PATH "%QIN_HOME%;!USER_PATH!" >nul 2>&1
    if %errorlevel%==0 (
        echo   √ Qin added to PATH
        echo   ! Please restart your terminal for changes to take effect
    ) else (
        echo   × Failed to add Qin to PATH
        exit /b 1
    )
)

echo.
echo → Testing qin command...
call "%QIN_HOME%\qin.bat" version

echo.
echo √ Installation complete!
echo   You can now use: qin compile, qin run, etc.
