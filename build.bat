@echo off
chcp 65001 >nul 2>&1
setlocal EnableDelayedExpansion

REM ============================================
REM Qin Distribution Build Script
REM 构建可分发的 qin.jar + qin.bat + install.bat
REM ============================================

echo.
echo ========================================
echo   Qin Distribution Builder
echo ========================================
echo.

set "QIN_DIR=%~dp0"
set "QIN_DIR=%QIN_DIR:~0,-1%"
set "BUILD_DIR=%QIN_DIR%\build\classes"
set "DIST_DIR=%QIN_DIR%\dist"
set "LIB_DIR=%QIN_DIR%\lib"

REM Step 1: 创建目录
echo [1/5] Creating directories...
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"
if not exist "%DIST_DIR%" mkdir "%DIST_DIR%"
if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"

REM Step 2: 下载依赖（如果不存在）
echo [2/5] Checking dependencies...
if not exist "%LIB_DIR%\gson-2.10.1.jar" (
    echo   - Downloading Gson...
    curl -L -o "%LIB_DIR%\gson-2.10.1.jar" https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
)

REM Step 3: 编译 Java 源文件
echo [3/5] Compiling Java sources...
dir /s /b "%QIN_DIR%\src\*.java" > "%QIN_DIR%\sources.txt"
javac -d "%BUILD_DIR%" -cp "%LIB_DIR%\gson-2.10.1.jar" -encoding UTF-8 @"%QIN_DIR%\sources.txt"
if %errorlevel% neq 0 (
    echo   X Compilation failed!
    del "%QIN_DIR%\sources.txt" 2>nul
    exit /b 1
)
del "%QIN_DIR%\sources.txt" 2>nul
echo   √ Compilation successful

REM Step 4: 创建 Fat JAR
echo [4/5] Building Fat JAR...

REM 创建临时目录用于合并
set "TEMP_JAR_DIR=%QIN_DIR%\build\fat-jar-temp"
if exist "%TEMP_JAR_DIR%" rmdir /s /q "%TEMP_JAR_DIR%"
mkdir "%TEMP_JAR_DIR%"

REM 复制编译后的类
xcopy /s /e /q /y "%BUILD_DIR%\*" "%TEMP_JAR_DIR%\" >nul

REM 解压 Gson 到临时目录
cd /d "%TEMP_JAR_DIR%"
jar -xf "%LIB_DIR%\gson-2.10.1.jar"
REM 删除签名文件（避免冲突）
if exist "META-INF\*.SF" del /q "META-INF\*.SF"
if exist "META-INF\*.RSA" del /q "META-INF\*.RSA"
if exist "META-INF\*.DSA" del /q "META-INF\*.DSA"

REM 创建 MANIFEST.MF
echo Manifest-Version: 1.0> "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF"
echo Main-Class: com.qin.cli.QinCli>> "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF"
echo.>> "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF"

REM 打包 Fat JAR
jar -cfm "%DIST_DIR%\qin.jar" "%TEMP_JAR_DIR%\META-INF\MANIFEST.MF" -C "%TEMP_JAR_DIR%" .
if %errorlevel% neq 0 (
    echo   X Failed to create JAR!
    exit /b 1
)

REM 清理临时目录
cd /d "%QIN_DIR%"
rmdir /s /q "%TEMP_JAR_DIR%"
echo   √ Fat JAR created: dist\qin.jar

REM Step 5: 创建启动脚本和安装脚本
echo [5/5] Creating scripts...

REM 创建 qin.bat 启动器
(
echo @echo off
echo java -jar "%%~dp0qin.jar" %%*
) > "%DIST_DIR%\qin.bat"
echo   √ Created: dist\qin.bat

REM 创建 install.bat 安装器
(
echo @echo off
echo chcp 65001 ^>nul 2^>^&1
echo setlocal EnableDelayedExpansion
echo.
echo echo.
echo echo ========================================
echo echo   Qin Installer
echo echo ========================================
echo echo.
echo.
echo :: 获取当前脚本所在目录
echo set "QIN_HOME=%%~dp0"
echo :: 移除末尾的反斜杠
echo set "QIN_HOME=%%QIN_HOME:~0,-1%%"
echo.
echo echo [1/2] Qin location: %%QIN_HOME%%
echo.
echo :: 获取当前用户PATH
echo for /f "tokens=2*" %%%%a in ^('reg query "HKCU\Environment" /v Path 2^^^>nul'^) do set "USER_PATH=%%%%b"
echo.
echo :: 检查是否已在PATH中
echo echo !USER_PATH! ^| findstr /i /c:"%%QIN_HOME%%" ^>nul
echo if %%errorlevel%%==0 ^(
echo     echo [2/2] Qin is already in PATH
echo ^) else ^(
echo     echo [2/2] Adding Qin to PATH...
echo     :: 添加到用户PATH（不需要管理员权限）
echo     setx PATH "%%QIN_HOME%%;!USER_PATH!" ^>nul 2^>^&1
echo     if %%errorlevel%%==0 ^(
echo         echo   √ Qin added to PATH successfully
echo     ^) else ^(
echo         echo   X Failed to add Qin to PATH
echo         exit /b 1
echo     ^)
echo ^)
echo.
echo echo.
echo echo ========================================
echo echo   Installation Complete!
echo echo ========================================
echo echo.
echo echo Please restart your terminal, then run:
echo echo   qin --version
echo echo   qin help
echo echo.
echo pause
) > "%DIST_DIR%\install.bat"
echo   √ Created: dist\install.bat

echo.
echo ========================================
echo   Build Complete!
echo ========================================
echo.
echo Distribution files:
echo   dist\qin.jar      - Executable Fat JAR
echo   dist\qin.bat      - Launcher script
echo   dist\install.bat  - One-click installer
echo.
echo To distribute:
echo   1. Zip the 'dist' folder
echo   2. Send to users
echo   3. Users run install.bat
echo   4. Done!
echo.
