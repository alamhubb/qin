@echo off
setlocal EnableDelayedExpansion

echo.
echo ========================================
echo   Qin Uninstaller
echo ========================================
echo.

REM Get current script directory
set "QIN_HOME=%~dp0"
REM Remove trailing backslash
set "QIN_HOME=%QIN_HOME:~0,-1%"

echo [1/2] Qin location: %QIN_HOME%

REM Get current user PATH
for /f "tokens=2*" %%a in ('reg query "HKCU\Environment" /v Path 2^>nul') do set "USER_PATH=%%b"

REM Check if in PATH
echo !USER_PATH! | findstr /i /c:"%QIN_HOME%" >nul
if %errorlevel% neq 0 (
    echo.
    echo   Qin is not in PATH, nothing to uninstall
    echo.
    pause
    exit /b 0
)

echo [2/2] Removing Qin from PATH...

REM Remove QIN_HOME from PATH
set "NEW_PATH=!USER_PATH!"

REM Remove "QIN_HOME;" (at start or middle)
set "NEW_PATH=!NEW_PATH:%QIN_HOME%;=!"

REM Remove ";QIN_HOME" (at end)
set "NEW_PATH=!NEW_PATH:;%QIN_HOME%=!"

REM If PATH only contains QIN_HOME
if "!NEW_PATH!"=="%QIN_HOME%" set "NEW_PATH="

REM Update user PATH
if "!NEW_PATH!"=="" (
    REM PATH is empty, delete the variable
    reg delete "HKCU\Environment" /v Path /f >nul 2>&1
) else (
    REM Set new PATH
    setx PATH "!NEW_PATH!" >nul 2>&1
)

if %errorlevel%==0 (
    echo   OK Qin removed from PATH successfully
) else (
    echo   X Failed to remove Qin from PATH
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
