@echo off
REM Build script for Qin Java version (Windows)

echo → Building Qin Java version...

REM Create directories
if not exist "build\classes" mkdir build\classes
if not exist "lib" mkdir lib

REM Download Gson if not exists
if not exist "lib\gson-2.10.1.jar" (
    echo → Downloading Gson...
    curl -L -o lib\gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
)

REM Find all Java files and compile
echo → Compiling...
dir /s /b src\java-rewrite\*.java > sources.txt
javac -d build\classes -cp lib\gson-2.10.1.jar @sources.txt
del sources.txt

echo ✓ Build successful!
echo.
echo Run with:
echo   java -cp build\classes;lib\gson-2.10.1.jar com.qin.cli.QinCli help
