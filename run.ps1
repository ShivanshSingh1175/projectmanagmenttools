param(
    [int]$Port = $env:APP_PORT -as [int] -or 8080
)

Write-Host "Building project and copying runtime dependencies..."
# Try to find mvn on PATH, otherwise fall back to the user's maven install if present
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    $mvnCmd = 'mvn'
} elseif (Test-Path "$env:USERPROFILE\.maven\maven-3.9.11\bin\mvn.cmd") {
    $mvnCmd = Join-Path $env:USERPROFILE ".maven\maven-3.9.11\bin\mvn.cmd"
} else {
    Write-Error "Maven not found in PATH and no fallback detected. Please install Maven or add it to PATH."
    exit 1
}

& $mvnCmd -DskipTests=true clean package

Write-Host "Copying dependencies to target/dependency (if not already present)..."
& $mvnCmd dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency

$root = Resolve-Path .
$jars = Get-ChildItem -Path "$root\target\dependency" -Filter '*.jar' -File | ForEach-Object { $_.FullName }
$classes = "$root\target\classes"
$cp = ($jars + $classes) -join ';'

Write-Host "Starting embedded runner on port $Port..."
Write-Host "Classpath contains $($jars.Count) jars"

Start-Process -NoNewWindow -FilePath 'java' -ArgumentList "-Dapp.port=$Port -cp `"$cp`" com.pmt.runner.EmbeddedJettyRunner" -WorkingDirectory $root
Write-Host "Runner started (check logs). Use Ctrl-C to stop if running in foreground." 
