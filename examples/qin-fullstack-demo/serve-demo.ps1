param(
  [switch]$BuildOnly,
  [int]$Port = 8080
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)
$packagesRoot = Join-Path $repoRoot "packages"
$workspaceRoot = Split-Path -Parent $repoRoot
$slimeRoot = Join-Path $workspaceRoot "slime\java-slime"

function Compile-Package([string]$srcDir, [string]$outDir, [string]$classpath) {
  New-Item -ItemType Directory -Force -Path $outDir | Out-Null
  $files = Get-ChildItem -Path $srcDir -Recurse -Filter *.java | ForEach-Object { $_.FullName }
  if ($files.Count -eq 0) { return }
  if ([string]::IsNullOrWhiteSpace($classpath)) {
    javac -encoding UTF-8 -d $outDir $files
  } else {
    javac -encoding UTF-8 -cp $classpath -d $outDir $files
  }
}

$irOut = Join-Path $packagesRoot "qin-lang-ir/build/classes"
$faOut = Join-Path $packagesRoot "qin-lang-frontend-adapter/build/classes"
$rtOut = Join-Path $packagesRoot "qin-lang-runtime/build/classes"
$beJvmOut = Join-Path $packagesRoot "qin-lang-backend-jvm/build/classes"
$beJsOut = Join-Path $packagesRoot "qin-lang-backend-js/build/classes"
$cliOut = Join-Path $packagesRoot "qin-lang-cli/build/classes"

Compile-Package (Join-Path $packagesRoot "qin-lang-ir/src/java") $irOut ""
Compile-Package (Join-Path $packagesRoot "qin-lang-frontend-adapter/src/java") $faOut $irOut
Compile-Package (Join-Path $packagesRoot "qin-lang-runtime/src/java") $rtOut ""
Compile-Package (Join-Path $packagesRoot "qin-lang-backend-jvm/src/java") $beJvmOut (@($irOut, $rtOut) -join ";")
Compile-Package (Join-Path $packagesRoot "qin-lang-backend-js/src/java") $beJsOut $irOut
Compile-Package (Join-Path $packagesRoot "qin-lang-cli/src/java") $cliOut (@($irOut, $faOut, $rtOut, $beJvmOut, $beJsOut) -join ";")

# Slime Java runtime classpath (required by QinSlimeFrontendAdapter)
$slimeParserOut = Join-Path $slimeRoot "slime-parser\build\classes"
$slimeAstOut = Join-Path $slimeRoot "slime-ast\build\classes"
$slimeTokenOut = Join-Path $slimeRoot "slime-token\build\classes"
$subhutiOut = Join-Path $slimeRoot "subhuti-java\build\classes"
$subhutiJarDir = Join-Path $slimeRoot "subhuti-java\libs"

$requiredDirs = @($slimeParserOut, $slimeAstOut, $slimeTokenOut, $subhutiOut)
foreach ($dir in $requiredDirs) {
  if (-not (Test-Path $dir)) {
    throw "Missing Slime build output: $dir`nPlease compile slime/java-slime first."
  }
}

$runtimeJarDirs = @(
  $subhutiJarDir,
  (Join-Path $env:USERPROFILE ".qin\libs")
)

$runtimeJars = @()
foreach ($jarDir in $runtimeJarDirs) {
  if (Test-Path $jarDir) {
    $runtimeJars += Get-ChildItem -Path $jarDir -Recurse -Filter *.jar | ForEach-Object { $_.FullName }
  }
}
$runtimeJars = $runtimeJars | Sort-Object -Unique

$cpItems = @(
  $rtOut, $irOut, $faOut, $beJvmOut, $beJsOut, $cliOut,
  $slimeParserOut, $slimeAstOut, $slimeTokenOut, $subhutiOut
) + $runtimeJars

$cp = ($cpItems -join ";")
Set-Location $repoRoot
$cmdArgs = @(
  "--demo-root", (Resolve-Path $PSScriptRoot),
  "--port", $Port
)
if ($BuildOnly) {
  $cmdArgs += "--build-only"
}
java -cp $cp com.qin.lang.cli.QinFullstackServeMain $cmdArgs
