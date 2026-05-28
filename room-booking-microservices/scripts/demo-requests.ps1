$ErrorActionPreference = "Stop"

Write-Host "1. Student service"
Invoke-RestMethod "http://localhost:8081/students" | ConvertTo-Json -Depth 5

Write-Host "`n2. Room service"
Invoke-RestMethod "http://localhost:8082/rooms" | ConvertTo-Json -Depth 5

Write-Host "`n3. Create booking through booking-service"
$bookingDate = (Get-Date).AddDays((Get-Random -Minimum 30 -Maximum 365)).ToString("yyyy-MM-dd")
$startHour = Get-Random -Minimum 8 -Maximum 16
$startTime = "{0:D2}:00:00" -f $startHour
$endTime = "{0:D2}:00:00" -f ($startHour + 1)

$bookingBody = @{
    studentId = 1
    roomId = 1
    bookingDate = $bookingDate
    startTime = $startTime
    endTime = $endTime
    purpose = "Microservice demo"
} | ConvertTo-Json

$booking = Invoke-RestMethod "http://localhost:8083/bookings" -Method Post -ContentType "application/json" -Body $bookingBody
$booking | ConvertTo-Json -Depth 5

Write-Host "`n4. Approve booking through booking-service"
$decisionBody = @{
    adminId = 2
    rejectReason = $null
} | ConvertTo-Json

Invoke-RestMethod "http://localhost:8083/bookings/$($booking.id)/approve" -Method Post -ContentType "application/json" -Body $decisionBody | ConvertTo-Json -Depth 5

Write-Host "`n5. Notification service log"
Invoke-RestMethod "http://localhost:8084/notifications" | ConvertTo-Json -Depth 5
