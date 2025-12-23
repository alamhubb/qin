@echo off
REM Qin Java Build Script for Windows
REM This script compiles all Java source files

echo ========================================
echo Qin Java Build Tool
echo ========================================

REM Create output directories
if not exist ".qin\classes" mkdir ".qin\classes"
if not exist "lib" mkdir "lib"

REM Download Gson if not exists
if not exist "lib\gson-2.10.1.jar" (
    echo Downloading Gson library...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'lib\gson-2.10.1.jar'"
)

echo.
echo [1/7] Compiling types...
javac -d .qin\classes src\java-rewrite\com\qin\types\*.java
if errorlevel 1 goto :error

echo [2/7] Compiling core modules...
javac -d .qin\classes -cp ".qin\classes;lib\gson-2.10.1.jar" src\java-rewrite\com\qin\core\*.java
if errorlevel 1 goto :error

echo [3/7] Compiling java utilities...
javac -d .qin\classes -cp ".qin\classes;lib\gson-2.10.1.jar" src\java-rewrite\com\qin\java\*.java
if errorlevel 1 goto :error

echo [4/7] Compiling commands...
javac -d .qin\classes -cp ".qin\classes;lib\gson-2.10.1.jar" src\java-rewrite\com\qin\commands\*.java
if errorlevel 1 goto :error

echo [5/7] Compiling CLI...
javac -d .qin\classes -cp ".qin\classes;lib\gson-2.10.1.jar" src\java-rewrite\com\qin\cli\*.java
if errorlevel 1 goto :error

echo [6/7] Compiling plugins...
javac -d .qin\classes packages\qin-plugin-java\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

javac -d .qin\classes -cp ".qin\classes" packages\qin-plugin-java-hot-reload\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

javac -d .qin\classes -cp ".qin\classes" packages\qin-plugin-spring\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

javac -d .qin\classes -cp ".qin\classes" packages\qin-plugin-vite\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

javac -d .qin\classes -cp ".qin\classes" packages\qin-plugin-graalvm\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

javac -d .qin\classes -cp ".qin\classes" packages\qin-plugin-graalvm-js\src\java\com\qin\plugins\*.java
if errorlevel 1 goto :error

echo [7/7] Compiling create-qin...
javac -d .qin\classes -cp ".qin\classes" packages\create-qin\src\java\com\qin\create\*.java
if errorlevel 1 goto :error

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
