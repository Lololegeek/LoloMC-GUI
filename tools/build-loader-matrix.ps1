param([switch]$ContinueOnError)

$projects = @(
    'examples/fabric-mod',
    'examples/forge-mod',
    'examples/forge-mod-fg7',
    'examples/neoforge-mod',
    'examples/fabric-mod-26',
    'examples/forge-mod-26',
    'examples/neoforge-mod-26'
)
$failures = @()

foreach ($project in $projects) {
    Write-Host "==> Building $project" -ForegroundColor Cyan
    Push-Location $project
    try {
        & .\gradlew.bat build --no-daemon
        if ($LASTEXITCODE -ne 0) { $failures += $project }
    } finally {
        Pop-Location
    }
    if ($failures.Count -gt 0 -and -not $ContinueOnError) { break }
}

if ($failures.Count -gt 0) {
    Write-Error ("Loader matrix failures: " + ($failures -join ', '))
    exit 1
}

Write-Host 'Loader matrix build completed.' -ForegroundColor Green
