#!/bin/bash

echo "→ Installing Qin to PATH..."

# 获取脚本所在目录的绝对路径
QIN_HOME="$(cd "$(dirname "$0")" && pwd)"
echo "  Qin location: $QIN_HOME"

# 检测 shell 配置文件
if [ -n "$ZSH_VERSION" ] || [ -f "$HOME/.zshrc" ]; then
    SHELL_RC="$HOME/.zshrc"
elif [ -f "$HOME/.bashrc" ]; then
    SHELL_RC="$HOME/.bashrc"
elif [ -f "$HOME/.bash_profile" ]; then
    SHELL_RC="$HOME/.bash_profile"
else
    SHELL_RC="$HOME/.profile"
fi

echo "  Shell config: $SHELL_RC"

# 检查是否已在 PATH 中
if grep -q "QIN_HOME" "$SHELL_RC" 2>/dev/null; then
    echo "  ✓ Qin is already in PATH"
else
    # 添加到 shell 配置文件
    echo "" >> "$SHELL_RC"
    echo "# Qin Build Tool" >> "$SHELL_RC"
    echo "export QIN_HOME=\"$QIN_HOME\"" >> "$SHELL_RC"
    echo "export PATH=\"\$QIN_HOME:\$PATH\"" >> "$SHELL_RC"
    echo "  ✓ Qin added to PATH"
    echo "  ℹ Please run: source $SHELL_RC"
fi

# 确保 qin 脚本可执行
chmod +x "$QIN_HOME/qin" 2>/dev/null

echo ""
echo "→ Testing qin command..."
"$QIN_HOME/qin.bat" version 2>/dev/null || "$QIN_HOME/qin" version 2>/dev/null || echo "  (run 'source $SHELL_RC' first)"

echo ""
echo "✓ Installation complete!"
echo "  You can now use: qin compile, qin run, etc."
