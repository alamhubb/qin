#!/bin/bash
# Qin 安装脚本
# 用法: curl -fsSL https://qinjs.dev/install.sh | bash

set -e

# 颜色
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_step() {
    echo -e "${BLUE}→${NC} $1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}!${NC} $1"
}

print_error() {
    echo -e "${RED}✗${NC} $1"
}

echo ""
echo -e "${BLUE}╔═══════════════════════════════════════╗${NC}"
echo -e "${BLUE}║${NC}         Qin 安装程序 v1.0.0          ${BLUE}║${NC}"
echo -e "${BLUE}╚═══════════════════════════════════════╝${NC}"
echo ""

# 检测操作系统
OS="$(uname -s)"
ARCH="$(uname -m)"

case "$OS" in
    Linux*)     OS_TYPE="linux";;
    Darwin*)    OS_TYPE="darwin";;
    *)          print_error "不支持的操作系统: $OS"; exit 1;;
esac

# 检测并安装 Bun
print_step "检测 Bun..."

if command -v bun &> /dev/null; then
    BUN_VERSION=$(bun --version)
    print_success "Bun 已安装 (v$BUN_VERSION)"
else
    print_warning "Bun 未安装，正在安装..."
    curl -fsSL https://bun.sh/install | bash
    
    # 加载 bun 到当前 shell
    export BUN_INSTALL="$HOME/.bun"
    export PATH="$BUN_INSTALL/bin:$PATH"
    
    if command -v bun &> /dev/null; then
        print_success "Bun 安装成功"
    else
        print_error "Bun 安装失败，请手动安装: https://bun.sh"
        exit 1
    fi
fi

# 检测 Java（可选）
print_step "检测 Java..."

if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    print_success "Java 已安装 (v$JAVA_VERSION)"
else
    print_warning "Java 未安装（Java 项目需要 JDK 17+）"
    print_warning "下载地址: https://adoptium.net/"
fi

# 安装 Qin
print_step "安装 Qin..."

bun install -g qin

if command -v qin &> /dev/null; then
    QIN_VERSION=$(qin --version 2>/dev/null || echo "unknown")
    print_success "Qin 安装成功"
else
    print_error "Qin 安装失败"
    exit 1
fi

# 完成
echo ""
echo -e "${GREEN}═══════════════════════════════════════${NC}"
echo -e "${GREEN}        安装完成！${NC}"
echo -e "${GREEN}═══════════════════════════════════════${NC}"
echo ""
echo "开始使用："
echo ""
echo "  qin create my-app   # 创建项目"
echo "  cd my-app"
echo "  qin dev             # 启动开发"
echo ""
echo "文档: https://qinjs.dev"
echo ""

# 提示重新加载 shell（如果安装了 bun）
if [[ ! -z "$BUN_INSTALL" ]]; then
    echo -e "${YELLOW}提示: 请运行以下命令或重新打开终端以使用 qin:${NC}"
    echo ""
    echo "  source ~/.bashrc  # 或 source ~/.zshrc"
    echo ""
fi
