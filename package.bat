@echo off
chcp 65001 >nul 2>&1

REM ============================================
REM Qin Distribution Build Script (Self-hosting)
REM ============================================

echo.
echo ========================================
echo   Qin Distribution Builder
echo ========================================
echo.

REM Set directory variables
set "QIN_DIR=%~dp0"
if "%QIN_DIR:~-1%"=="\" set "QIN_DIR=%QIN_DIR:~0,-1%"
set "DIST_DIR=%QIN_DIR%\dist"

REM Step 1: Build using qin (self-hosting)
echo [1/2] Building with qin...
pushd "%QIN_DIR%"
call qin build
if errorlevel 1 (
    echo   X Build failed!
    popd
    exit /b 1
)
popd
echo   OK Build successful

REM Step 2: Copy script files
echo [2/2] Creating scripts...
if not exist "%DIST_DIR%" mkdir "%DIST_DIR%"
copy /y "%QIN_DIR%\script\qin.bat" "%DIST_DIR%\qin.bat" >nul
echo   OK Created: dist\qin.bat
copy /y "%QIN_DIR%\script\install.bat" "%DIST_DIR%\install.bat" >nul
echo   OK Created: dist\install.bat
copy /y "%QIN_DIR%\script\uninstall.bat" "%DIST_DIR%\uninstall.bat" >nul
echo   OK Created: dist\uninstall.bat

echo.
echo ========================================
echo   Build Complete!
echo ========================================
echo.
echo Distribution files:
echo   dist\qin.jar        - Executable Fat JAR
echo   dist\qin.bat        - Launcher script
echo   dist\install.bat    - One-click installer
echo   dist\uninstall.bat  - One-click uninstaller
echo.
echo To distribute:
echo   1. Zip the 'dist' folder
echo   2. Send to users
echo   3. Users run install.bat
echo   4. Done!
echo.
