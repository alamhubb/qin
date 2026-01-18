#!/bin/bash
# Build script for Qin Java version

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}→ Building Qin Java version...${NC}"

# Create directories
mkdir -p build/classes
mkdir -p lib

# Download Gson if not exists
if [ ! -f "lib/gson-2.10.1.jar" ]; then
    echo -e "${BLUE}→ Downloading Gson...${NC}"
    curl -L -o lib/gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar
fi

# Find all Java files
JAVA_FILES=$(find src/java-rewrite -name "*.java")

# Compile
echo -e "${BLUE}→ Compiling...${NC}"
javac -d build/classes -cp lib/gson-2.10.1.jar $JAVA_FILES

echo -e "${GREEN}✓ Build successful!${NC}"
echo ""
echo "Run with:"
echo "  java -cp build/classes:lib/gson-2.10.1.jar com.qin.cli.QinCli help"
