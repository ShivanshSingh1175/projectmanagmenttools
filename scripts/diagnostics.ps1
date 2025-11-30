# diagnostics.ps1 — collect Docker & host network diagnostics to a timestamped file
# Usage: From project root run: .\scripts\diagnostics.ps1

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Continue'

$timestamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$diagFile = Join-Path -Path (Get-Location) -ChildPath "diagnostics-$timestamp.txt"

Write-Host "Starting diagnostics. Output will be saved to: $diagFile"

# Start transcript to capture output and errors
Start-Transcript -Path $diagFile -Force

function RunAndWrite($label, [scriptblock]$body) {
    Write-Host "--- $label ---"
    Write-Output "--- $label ---"
    try {
        & $body
    } catch {
        Write-Output "ERROR running $label : $($_.Exception.Message)"
    }
    Write-Output ""
}

# Basic info
RunAndWrite "Date & Host" { Get-Date; hostname; whoami }

# Docker checks
RunAndWrite "docker --version" { docker --version }
RunAndWrite "docker info" { docker info }
RunAndWrite "docker ps -a (filter pmt-tomcat)" { docker ps -a --filter "name=pmt-tomcat" --format "table {{.ID}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}\t{{.Names}}" }
RunAndWrite "docker images (pmt-tomcat)" { docker images --filter=reference="pmt-tomcat:latest" }

# If container exists, inspect and logs
try {
    $exists = docker ps -a --filter "name=pmt-tomcat" --format "{{.ID}}"
} catch {
    $exists = $null
}
if ($exists) {
    RunAndWrite "docker inspect NetworkSettings.Ports" { docker inspect --format '{{json .NetworkSettings.Ports}}' pmt-tomcat }
    RunAndWrite "docker logs (tail 200)" { docker logs --tail 200 pmt-tomcat }
}

# Host network and HTTP checks
RunAndWrite "Test-NetConnection localhost:8080" { Test-NetConnection -ComputerName localhost -Port 8080 }
RunAndWrite "Invoke-WebRequest / (root)" { try { Invoke-WebRequest -Uri http://localhost:8080/ -UseBasicParsing -TimeoutSec 5 -MaximumRedirection 0 -ErrorAction Stop | Select-Object StatusCode, StatusDescription } catch { Write-Output "Invoke-WebRequest root failed: $($_.Exception.Message)" } }
RunAndWrite "Invoke-WebRequest /pmt-health" { try { Invoke-WebRequest -Uri http://localhost:8080/pmt-health -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop | Select-Object StatusCode, StatusDescription } catch { Write-Output "Invoke-WebRequest /pmt-health failed: $($_.Exception.Message)" } }

# Netstat and firewall rules
RunAndWrite "netstat -ano | findstr :8080" { netstat -ano | findstr ":8080" }
RunAndWrite "Get-NetFirewallRule (docker/tomcat hints)" { Get-NetFirewallRule | Where-Object { $_.DisplayName -like "*tomcat*" -or $_.DisplayName -like "*docker*" -or $_.DisplayName -like "*Docker*" } | Select-Object DisplayName, Enabled }

# docker container health (if present)
if ($exists) {
    RunAndWrite "docker inspect Health" { docker inspect --format '{{json .State.Health}}' pmt-tomcat }
}

Stop-Transcript

Write-Host "Diagnostics complete. File: $diagFile"
Write-Host "You can paste the file contents or the file path here. If it's large, paste the sections for 'docker info', 'docker ps -a', 'docker logs' and the 'Test-NetConnection' results."
