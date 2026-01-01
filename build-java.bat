@echo off
REM Qin Java Build Script for Windows
REM This script compiles all Java source files

echo ========================================
echo Qin Java Build Tool
echo ========================================

REM Create output directories
if not exist ".qin\classes" mkdir ".qin\classes"
if not exist "lib" mkdir "lib"

REM Bootstrap dependencies using coursier
REM First ensure coursier.jar exists
if not exist "lib\coursier.jar" (
    echo Downloading Coursier...
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/coursier/coursier/releases/download/v2.1.10/coursier.jar' -OutFile 'lib\coursier.jar'"
)

REM Use coursier to resolve all dependencies from qin.config.json
echo Resolving dependencies...
java -jar lib\coursier.jar fetch ^
    com.google.code.gson:gson:2.10.1 ^
    io.get-coursier:coursier_2.13:2.1.10 ^
    --classpath > .qin\deps-classpath.txt

if errorlevel 1 (
    echo Failed to resolve dependencies
    exit /b 1
)

REM Read classpath from file
set /p DEPS_CP=<.qin\deps-classpath.txt
SET CP=.qin\classes;%DEPS_CP%

echo.
echo [1/7] Compiling types...
javac -d .qin\classes src\java-rewrite\com\qin\types\*.java
if errorlevel 1 goto :error

echo [2/7] Compiling core modules...
javac -d .qin\classes -cp "%CP%" src\java-rewrite\com\qin\core\*.java
if errorlevel 1 goto :error

echo [3/7] Compiling java utilities...
javac -d .qin\classes -cp "%CP%" src\java-rewrite\com\qin\java\*.java
if errorlevel 1 goto :error

echo [4/7] Compiling commands...
javac -d .qin\classes -cp "%CP%" src\java-rewrite\com\qin\commands\*.java
if errorlevel 1 goto :error

echo [5/7] Compiling core plugins...
javac -d .qin\classes -cp "%CP%" src\java-rewrite\com\qin\plugins\*.java
if errorlevel 1 goto :error

echo [6/7] Compiling CLI...
javac -d .qin\classes -cp "%CP%" src\java-rewrite\com\qin\cli\*.java
if errorlevel 1 goto :error

echo [7/7] Compiling extra plugins...
if errorlevel 1 goto :error

javac -d .qin\classes -cp ".qin\classes" packages\qin-plugin-graalvm\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

javac -d .qin\classes -cp ".qin\classes" packages\qin-plugin-graalvm-js\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

echo [8/8] Compiling create-qin...
javac -d .qin\classes -cp ".qin\classes" packages\create-qin\src\java\com\qin\create\*.java
if errorlevel 1 goto :error

REM Copy dependencies into .qin/classes for packaging
echo.
echo Packaging dependencies...
if not exist ".qin\classes\lib" mkdir ".qin\classes\lib"
copy lib\gson-2.10.1.jar .qin\classes\lib\ >nul
copy lib\coursier.jar .qin\classes\lib\ >nul

echo.
echo ========================================
echo Build successful!
echo ========================================
echo.
echo Run with: java -cp ".qin\classes;lib\gson-2.10.1.jar" com.qin.cli.QinCli help
echo.
goto :end

:error
echo.
echo ========================================
echo Build failed!
echo ========================================
exit /b 1

:end
