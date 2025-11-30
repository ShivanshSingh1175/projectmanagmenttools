# run-docker.ps1 — Build WAR, build Docker image, and run Tomcat container (PowerShell)
# Usage: Start PowerShell as Administrator if Docker Desktop installation/privileges are required.

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

# Project helper values
$MvnCmd = "C:\\Users\\Shivansh Singh\\.maven\\maven-3.9.11\\bin\\mvn.cmd"
$WarPath = Join-Path -Path (Get-Location) -ChildPath "target\\ProjectManagementTool-1.0.0.war"

# 1) Build WAR if missing
if (-not (Test-Path $WarPath)) {
    Write-Host 'WAR not found, building...'
    & $MvnCmd clean package -DskipTests
} else {
    Write-Host 'WAR found at' $WarPath
}

# 2) Build Docker image
Write-Host 'Building Docker image pmt-tomcat:latest'
docker build -t pmt-tomcat:latest -f Dockerfile .

# 3) Remove existing container if present
$existing = docker ps -a --filter "name=pmt-tomcat" --format "{{.ID}}"
if ($existing) {
    Write-Host 'Stopping and removing existing container...'
    docker rm -f pmt-tomcat | Out-Null
}

# 4) Run container
Write-Host 'Starting container pmt-tomcat (port 8080)'
try {
    $cid = docker run --name pmt-tomcat -p 8080:8080 -d pmt-tomcat:latest
} catch {
    Write-Host 'Failed to start container:' -ForegroundColor Red
    Write-Host $_.Exception.Message
    exit 1
}

Write-Host 'Waiting for Tomcat to initialize (10 seconds)'
Start-Sleep -Seconds 10

Write-Host "--- Container Status ---"
docker ps -a --filter "name=pmt-tomcat" --format "table {{.ID}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}\t{{.Names}}"

Write-Host "--- docker inspect (NetworkSettings.Ports) ---"
docker inspect --format '{{json .NetworkSettings.Ports}}' pmt-tomcat | ConvertFrom-Json | ConvertTo-Json -Depth 5

Write-Host "--- Recent Tomcat logs (tail 200) ---"
docker logs --tail 200 pmt-tomcat

Write-Host "--- Host connectivity checks ---"
$tc = Test-NetConnection -ComputerName localhost -Port 8080 -WarningAction SilentlyContinue
Write-Host "TCP Test: TcpTestSucceeded="$($tc.TcpTestSucceeded)", RemoteAddress=$($tc.RemoteAddress), RemotePort=$($tc.RemotePort)")

try {
    $resp = Invoke-WebRequest -Uri http://localhost:8080/pmt-health -UseBasicParsing -ErrorAction Stop -TimeoutSec 5
    Write-Host "HTTP /pmt-health status: $($resp.StatusCode) $($resp.StatusDescription)"
    $body = $resp.Content
    if ($body) { Write-Host "Response body (truncated 400 chars):"; Write-Host ($body.Substring(0,[Math]::Min(400,$body.Length))) }
} catch {
    Write-Host "Invoke-WebRequest failed:" -ForegroundColor Yellow
    Write-Host $_.Exception.Message
}

Write-Host "If the container exited, run 'docker ps -a' and 'docker logs pmt-tomcat' to see why."
