param(
    [int]$Port = $env:APP_PORT -as [int] -or 8080
)

Write-Host "Building project and copying runtime dependencies..."
& mvn -DskipTests=true clean package

Write-Host "Copying dependencies to target/dependency (if not already present)..."
& mvn dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency

$root = Resolve-Path .
$jars = Get-ChildItem -Path "$root\target\dependency" -Filter '*.jar' -File | ForEach-Object { $_.FullName }
$classes = "$root\target\classes"
$cp = ($jars + $classes) -join ';'

Write-Host "Starting embedded runner on port $Port..."
Write-Host "Classpath contains $($jars.Count) jars"

Start-Process -NoNewWindow -FilePath 'java' -ArgumentList "-Dapp.port=$Port -cp `"$cp`" com.pmt.runner.EmbeddedJettyRunner" -WorkingDirectory $root
Write-Host "Runner started (check logs). Use Ctrl-C to stop if running in foreground." 
