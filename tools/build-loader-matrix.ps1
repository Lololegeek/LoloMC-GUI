param([switch]$ContinueOnError)

$projects = @(
    'examples/fabric-mod',
    'examples/forge-mod',
    'examples/neoforge-mod'
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
