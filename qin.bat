@echo off
REM Qin Build Tool Launcher
chcp 65001 >nul 2>&1

REM Get script directory
set "QIN_HOME=%~dp0"
set "QIN_HOME=%QIN_HOME:~0,-1%"

set "QIN_JAVA=java"
if not "%JAVA_HOME%"=="" (
    if exist "%JAVA_HOME%\bin\java.exe" (
        set "QIN_JAVA=%JAVA_HOME%\bin\java.exe"
    )
)

if "%QIN_JAVA_OPTS%"=="" (
    set "QIN_JAVA_OPTS=-Xms64m -Xmx2g -Xshare:off -XX:+UseSerialGC -XX:-UseJVMCICompiler -XX:TieredStopAtLevel=1"
)

set "QIN_ARGS_FILE=%TEMP%\qin-launch-%RANDOM%%RANDOM%.args"
powershell -NoProfile -Command ^
    "$cache = Join-Path $env:QIN_HOME '.qin/classpath.json';" ^
    "$entries = New-Object System.Collections.Generic.List[string];" ^
    "$entries.Add((Join-Path $env:QIN_HOME 'build/classes'));" ^
    "$entries.Add((Join-Path $env:QIN_HOME 'lib/gson-2.10.1.jar'));" ^
    "$entries.Add((Join-Path $env:QIN_HOME 'lib/coursier.jar'));" ^
    "if (Test-Path -LiteralPath $cache) { $json = Get-Content -Raw -Encoding UTF8 -LiteralPath $cache | ConvertFrom-Json; if ($null -ne $json.classpath) { foreach ($entry in $json.classpath) { if ($entry -and -not [string]::IsNullOrWhiteSpace($entry)) { $entries.Add($entry) } } } }" ^
    "$argsFile = $env:QIN_ARGS_FILE;" ^
    "$lines = @('-Dfile.encoding=UTF-8', '-Dstdout.encoding=UTF-8', '-Dstderr.encoding=UTF-8', '-cp', ($entries -join [IO.Path]::PathSeparator), 'com.qin.cli.QinCli');" ^
    "[System.IO.File]::WriteAllText($argsFile, ($lines -join [Environment]::NewLine), (New-Object System.Text.UTF8Encoding($false)))"

"%QIN_JAVA%" %QIN_JAVA_OPTS% @"%QIN_ARGS_FILE%" %*
set "QIN_EXIT_CODE=%ERRORLEVEL%"
del "%QIN_ARGS_FILE%" >nul 2>&1
exit /b %QIN_EXIT_CODE%
