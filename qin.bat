@echo off
REM Qin Build Tool Launcher
chcp 65001 >nul 2>&1

REM Get script directory
set "QIN_HOME=%~dp0"
set "QIN_HOME=%QIN_HOME:~0,-1%"

java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp "%QIN_HOME%\build\classes;%QIN_HOME%\lib\gson-2.10.1.jar;%QIN_HOME%\lib\coursier.jar" com.qin.cli.QinCli %*
