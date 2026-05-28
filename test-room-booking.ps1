Set-StrictMode -Version Latest

$projectPath = Join-Path $PSScriptRoot "room-booking-microservices"
Set-Location $projectPath

powershell -ExecutionPolicy Bypass -File .\scripts\demo-requests.ps1
