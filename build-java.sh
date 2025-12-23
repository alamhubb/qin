#!/bin/bash
# Qin Java Build Script for Unix/Linux/macOS
# This script compiles all Java source files

echo "========================================"
echo "Qin Java Build Tool"
echo "========================================"

# Create output directories
mkdir -p .qin/classes
mkdir -p lib

# Download Gson if not exists
if [ ! -f "lib/gson-2.10.1.jar" ]; then
    echo "Downloading Gson library..."
    curl -L -o lib/gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
fi

echo ""
echo "[1/7] Compiling types..."
javac -d .qin/classes src/java-rewrite/com/qin/types/*.java || exit 1

echo "[2/7] Compiling core modules..."
javac -d .qin/classes -cp ".qin/classes:lib/gson-2.10.1.jar" src/java-rewrite/com/qin/core/*.java || exit 1

echo "[3/7] Compiling java utilities..."
javac -d .qin/classes -cp ".qin/classes:lib/gson-2.10.1.jar" src/java-rewrite/com/qin/java/*.java || exit 1

echo "[4/7] Compiling commands..."
javac -d .qin/classes -cp ".qin/classes:lib/gson-2.10.1.jar" src/java-rewrite/com/qin/commands/*.java || exit 1

echo "[5/7] Compiling CLI..."
javac -d .qin/classes -cp ".qin/classes:lib/gson-2.10.1.jar" src/java-rewrite/com/qin/cli/*.java || exit 1

echo "[6/7] Compiling plugins..."
javac -d .qin/classes packages/qin-plugin-java/src/java/com/qin/plugins/*.java || exit 1
javac -d .qin/classes -cp ".qin/classes" packages/qin-plugin-java-hot-reload/src/java/com/qin/plugins/*.java || exit 1
javac -d .qin/classes -cp ".qin/classes" packages/qin-plugin-spring/src/java/com/qin/plugins/*.java || exit 1
javac -d .qin/classes -cp ".qin/classes" packages/qin-plugin-vite/src/java/com/qin/plugins/*.java || exit 1
javac -d .qin/classes -cp ".qin/classes" packages/qin-plugin-graalvm/src/java/com/qin/plugins/*.java || exit 1
javac -d .qin/classes -cp ".qin/classes" packages/qin-plugin-graalvm-js/src/java/com/qin/plugins/*.java || exit 1

echo "[7/7] Compiling create-qin..."
javac -d .qin/classes -cp ".qin/classes" packages/create-qin/src/java/com/qin/create/*.java || exit 1

echo ""
echo "========================================"
echo "Build successful!"
echo "========================================"
echo ""
echo "Run with: java -cp '.qin/classes:lib/gson-2.10.1.jar' com.qin.cli.QinCli help"
echo ""
