@echo off
setlocal EnableDelayedExpansion

echo.
echo ========================================
echo   Qin Installer
echo ========================================
echo.

REM Get current script directory
set "QIN_HOME=%~dp0"
REM Remove trailing backslash
set "QIN_HOME=%QIN_HOME:~0,-1%"

echo [1/3] Qin location: %QIN_HOME%

REM Get current user PATH
for /f "tokens=2*" %%a in ('reg query "HKCU\Environment" /v Path 2^>nul') do set "USER_PATH=%%b"

echo [2/3] Adding to PATH...

REM Check if already in PATH
echo !USER_PATH! | findstr /i /c:"%QIN_HOME%" >nul
if %errorlevel%==0 (
    echo   OK Qin is already in PATH
) else (
    REM Add to user PATH (no admin required)
    setx PATH "%QIN_HOME%;!USER_PATH!" >nul 2>&1
    if %errorlevel%==0 (
        echo   OK Qin added to PATH
        echo   ! Please restart your terminal for changes to take effect
    ) else (
        echo   X Failed to add Qin to PATH
        pause
        exit /b 1
    )
)

echo [3/3] Testing qin command...
call "%QIN_HOME%\qin.bat" version

echo.
echo ========================================
echo   Installation Complete!
echo ========================================
echo.
echo You can now use: qin compile, qin run, etc.
echo.
pause
