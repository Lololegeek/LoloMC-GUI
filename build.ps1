param(
    [switch]$SkipStudio,
    [switch]$TestOnly
)

$ErrorActionPreference = 'Stop'
$projectRoot = $PSScriptRoot
$runtimeRoot = Join-Path $projectRoot 'runtime-java'
$buildRoot = Join-Path $runtimeRoot 'build'
$classesDir = Join-Path $buildRoot 'classes'
$testClassesDir = Join-Path $buildRoot 'test-classes'

New-Item -ItemType Directory -Force $classesDir, $testClassesDir | Out-Null
$mainSources = @(Get-ChildItem (Join-Path $runtimeRoot 'src/main/java') -Recurse -Filter '*.java' | ForEach-Object FullName)
$testSources = @(Get-ChildItem (Join-Path $runtimeRoot 'src/test/java') -Recurse -Filter '*.java' | ForEach-Object FullName)

& javac --release 8 -Xlint:-options -encoding UTF-8 -d $classesDir @mainSources
if ($LASTEXITCODE -ne 0) { throw 'Runtime compilation failed' }
& javac --release 8 -Xlint:-options -encoding UTF-8 -cp $classesDir -d $testClassesDir @testSources
if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed' }
& java -ea -cp "$classesDir;$testClassesDir" dev.lolomc.gui.RuntimeTest
if ($LASTEXITCODE -ne 0) { throw 'Runtime tests failed' }

if (-not $TestOnly) {
    $jarPath = Join-Path $buildRoot 'lolomc-gui-0.2.1.jar'
    & jar --create --file $jarPath -C $classesDir .
    if ($LASTEXITCODE -ne 0) { throw 'JAR packaging failed' }
    Write-Host "Built $jarPath"
}

if (-not $SkipStudio -and -not $TestOnly) {
    Push-Location (Join-Path $projectRoot 'studio')
    try { & npm run build; if ($LASTEXITCODE -ne 0) { throw 'Studio build failed' } }
    finally { Pop-Location }
}
