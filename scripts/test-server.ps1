<#
Simple server smoke-test script for ProjectManagementTool
Usage: Open PowerShell in project root and run:
  .\scripts\test-server.ps1
#>

param(
    [string]$BaseUrl = 'http://localhost:8080'
)

function Test-URL {
    param(
        [string]$Path,
        [int[]]$ExpectedStatus = @(200)
    )
    $url = "$BaseUrl$Path"
    Write-Host "Testing: $url" -ForegroundColor Cyan
    try {
        $resp = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 10 -ErrorAction Stop
        $status = $resp.StatusCode
        $ok = $ExpectedStatus -contains $status
        if ($ok) {
            Write-Host "  -> OK (status $status)" -ForegroundColor Green
            return @{ path=$Path; ok=$true; status=$status; body=$resp.Content }
        } else {
            Write-Host "  -> FAIL (status $status) expected $ExpectedStatus" -ForegroundColor Red
            return @{ path=$Path; ok=$false; status=$status; body=$resp.Content }
        }
    } catch {
        # show HTTP status code if available
        $err = $_.Exception
        if ($err.Response -ne $null) {
            $code = $err.Response.StatusCode.value__
            Write-Host "  -> FAIL (status $code)" -ForegroundColor Red
            return @{ path=$Path; ok=$false; status=$code; body=$null }
        } else {
            Write-Host "  -> ERROR: $($_.Exception.Message)" -ForegroundColor Red
            return @{ path=$Path; ok=$false; status=0; body=$null }
        }
    }
}

$results = @()

# Tests
$results += Test-URL -Path '/pmt-health' -ExpectedStatus @(200)
$results += Test-URL -Path '/login' -ExpectedStatus @(200)
# JSP may or may not be available; expect either 200 (if JSP engine present) or 404 (if not). Record both as OK.
$jspResult = Test-URL -Path '/jsp/login.jsp' -ExpectedStatus @(200,404)
$results += $jspResult
$results += Test-URL -Path '/css/style.css' -ExpectedStatus @(200)

# Summarize
$failed = $results | Where-Object { -not $_.ok }
Write-Host "`nTest Summary:" -ForegroundColor Yellow
if ($failed.Count -eq 0) {
    Write-Host "  All tests passed." -ForegroundColor Green
    exit 0
} else {
    Write-Host "  ${($results.Count - $failed.Count)} passed, ${($failed.Count)} failed." -ForegroundColor Red
    foreach ($f in $failed) {
        Write-Host "   - $($f.path) -> status $($f.status)" -ForegroundColor Red
    }
    exit 2
}
