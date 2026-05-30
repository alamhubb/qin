@echo off
REM Build script for Qin Java version (Windows)

echo Building Qin Java version...

if not exist "build\classes" mkdir build\classes
if not exist "lib" mkdir lib

if not exist "lib\gson-2.10.1.jar" (
    echo Downloading Gson...
    curl -L -o lib\gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
)

echo Compiling...
dir /s /b src\*.java > sources.txt
javac -d build\classes -encoding UTF-8 -cp lib\gson-2.10.1.jar @sources.txt
del sources.txt

if errorlevel 1 (
    echo Build failed.
    exit /b 1
)

echo Build successful!
echo.
echo Run with:
echo   java -cp build\classes;lib\gson-2.10.1.jar com.qin.cli.QinCli help
