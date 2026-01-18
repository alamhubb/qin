@echo off
chcp 65001 >nul 2>&1

REM ============================================
REM Qin Distribution Build Script
REM ============================================

echo.
echo ========================================
echo   Qin Distribution Builder
echo ========================================
echo.

REM Set directory variables
set "QIN_DIR=%~dp0"
if "%QIN_DIR:~-1%"=="\" set "QIN_DIR=%QIN_DIR:~0,-1%"
set "BUILD_DIR=%QIN_DIR%\build\classes"
set "DIST_DIR=%QIN_DIR%\dist"
set "LIB_DIR=%QIN_DIR%\lib"
set "TEMP_JAR_DIR=%QIN_DIR%\build\fat-jar-temp"

REM Step 1: Create directories
echo [1/5] Creating directories...
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
if not exist "%DIST_DIR%" mkdir "%DIST_DIR%"
if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"

REM Step 2: Download dependencies
echo [2/5] Checking dependencies...
if not exist "%LIB_DIR%\gson-2.10.1.jar" (
    echo   - Downloading Gson...
    curl -L -o "%LIB_DIR%\gson-2.10.1.jar" "https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar"
    if errorlevel 1 (
        echo   X Failed to download Gson!
        exit /b 1
    )
)
echo   OK Dependencies ready

REM Step 3: Compile Java sources
echo [3/5] Compiling Java sources...
dir /s /b "%QIN_DIR%\src\*.java" > "%QIN_DIR%\sources.txt"
javac -d "%BUILD_DIR%" -cp "%LIB_DIR%\gson-2.10.1.jar" -encoding UTF-8 "@%QIN_DIR%\sources.txt"
if errorlevel 1 (
    echo   X Compilation failed!
    del "%QIN_DIR%\sources.txt" 2>nul
    exit /b 1
)
del "%QIN_DIR%\sources.txt" 2>nul
echo   OK Compilation successful

REM Step 4: Build Fat JAR
echo [4/5] Building Fat JAR...

REM Clean and create temp directory
if exist "%TEMP_JAR_DIR%" rmdir /s /q "%TEMP_JAR_DIR%"
mkdir "%TEMP_JAR_DIR%"
mkdir "%TEMP_JAR_DIR%\META-INF"

REM Copy compiled classes
xcopy /s /e /q /y "%BUILD_DIR%\*" "%TEMP_JAR_DIR%\" >nul

REM Extract Gson to temp directory
pushd "%TEMP_JAR_DIR%"
jar -xf "%LIB_DIR%\gson-2.10.1.jar"
REM Delete signature files
if exist "META-INF\*.SF" del /q "META-INF\*.SF"
if exist "META-INF\*.RSA" del /q "META-INF\*.RSA"
if exist "META-INF\*.DSA" del /q "META-INF\*.DSA"
popd

REM Create MANIFEST.MF
echo Manifest-Version: 1.0> "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF"
echo Main-Class: com.qin.cli.QinCli>> "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF"
echo.>> "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF"

REM Package Fat JAR
jar -cfm "%DIST_DIR%\qin.jar" "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF" -C "%TEMP_JAR_DIR%" .
if errorlevel 1 (
    echo   X Failed to create JAR!
    exit /b 1
)

REM Clean temp directory
rmdir /s /q "%TEMP_JAR_DIR%"
echo   OK Fat JAR created: dist\qin.jar

REM Step 5: Copy script files
echo [5/5] Creating scripts...
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
