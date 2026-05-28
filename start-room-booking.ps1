Set-StrictMode -Version Latest

$projectPath = Join-Path $PSScriptRoot "room-booking-microservices"
Set-Location $projectPath

docker compose up --build
