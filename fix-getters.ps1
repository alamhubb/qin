# Qin Java 25 Records - Batch Fix Getters
# Replace old getter calls with Record accessors

$replacements = @{
    '\.getName\(\)'         = '.name()'
    '\.getVersion\(\)'      = '.version()'
    '\.getEntry\(\)'        = '.entry()'
    '\.getJava\(\)'         = '.java()'
    '\.getOutput\(\)'       = '.output()'
    '\.getDependencies\(\)' = '.dependencies()'
    '\.getRepositories\(\)' = '.repositories()'
    '\.getPackages\(\)'     = '.packages()'
    '\.isLocalRep\(\)'      = '.localRep()'
    '\.getUrl\(\)'          = '.url()'
    '\.getDir\(\)'          = '.dir()'
    '\.getJarName\(\)'      = '.jarName()'
    '\.getSourceDir\(\)'    = '.sourceDir()'
    '\.getTestDir\(\)'      = '.testDir()'
    '\.getOutputDir\(\)'    = '.outputDir()'
    '\.getEncoding\(\)'     = '.encoding()'
    '\.isFatJar\(\)'        = '.fatJar()'
    '\.getId\(\)'           = '.id()'
    '\.getSrcDir\(\)'       = '.srcDir()'
    '\.getClassName\(\)'    = '.className()'
    '\.isSuccess\(\)'       = '.isSuccess()'
    '\.getError\(\)'        = '.getError()'
    '\.getJarPaths\(\)'     = '.jarPaths()'
}

$totalFixed = 0
$filesModified = 0

Write-Host "Scanning Java files..." -ForegroundColor Cyan

Get-ChildItem "src\java-rewrite" -Recurse -Filter "*.java" | ForEach-Object {
    $content = Get-Content $_.FullName -Raw -Encoding UTF8
    $modified = $false
    $fileChanges = 0
    
    foreach ($old in $replacements.Keys) {
        $new = $replacements[$old]
        $matches = ([regex]::Matches($content, $old)).Count
        if ($matches -gt 0) {
            $content = $content -replace $old, $new
            $modified = $true
            $fileChanges += $matches
        }
    }
    
    if ($modified) {
        $utf8NoBom = New-Object System.Text.UTF8Encoding $false
        [System.IO.File]::WriteAllText($_.FullName, $content, $utf8NoBom)
        Write-Host "Fixed: $($_.Name) - $fileChanges changes" -ForegroundColor Green
        $filesModified++
        $totalFixed += $fileChanges
    }
}

Write-Host "`nDone!" -ForegroundColor Cyan
Write-Host "Files modified: $filesModified" -ForegroundColor Yellow
Write-Host "Total changes: $totalFixed" -ForegroundColor Yellow
Write-Host "`nNext: Run .\build-java.bat to test compilation" -ForegroundColor Green
