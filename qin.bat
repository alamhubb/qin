@echo off
REM Qin Build Tool Launcher
REM 设置控制台编码为 UTF-8，避免中文乱码
chcp 65001 >nul 2>&1
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "d:\project\qkyproject\slime-java\qin\build\classes;d:\project\qkyproject\slime-java\qin\lib\gson-2.10.1.jar;d:\project\qkyproject\slime-java\qin\lib\coursier.jar" com.qin.cli.QinCli %*
