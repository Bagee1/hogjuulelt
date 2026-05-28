# Room Booking Microservices

This folder contains the main runnable system. It is split into isolated services and can be started with Docker Compose.

If you are at the repository root, you can also run:

```powershell
.\start-room-booking.ps1
```

## Services

| Service | Host URL | Own database | Responsibility |
| --- | --- | --- | --- |
| student-service | http://localhost:8081 | student_db | Student/admin profile and login data |
| room-service | http://localhost:8082 | room_db | Room catalog |
| booking-service | http://localhost:8083 | booking_db | Booking workflow, approve/reject/cancel |
| notification-service | http://localhost:8084 | notification_db | Notification log |
| frontend-service | http://localhost:8080 | none | One web UI that consumes all services |

## Start

```powershell
cd room-booking-microservices
docker compose up --build
```

Open the integrated web system:

```text
http://localhost:8080
```

Stop:

```powershell
docker compose down
```

From the repository root:

```powershell
.\stop-room-booking.ps1
```

Delete DB volumes for a clean demo:

```powershell
docker compose down -v
```

## Quick Test

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\demo-requests.ps1
```

From the repository root:

```powershell
.\test-room-booking.ps1
```

The script picks a random future date/time so it can be run more than once without hitting the overlap rule.

Or test manually:

```powershell
Invoke-RestMethod http://localhost:8081/students
Invoke-RestMethod http://localhost:8082/rooms

$body = @{
  studentId = 1
  roomId = 1
  bookingDate = "2026-06-01"
  startTime = "09:00:00"
  endTime = "10:00:00"
  purpose = "Microservice demo"
} | ConvertTo-Json

Invoke-RestMethod http://localhost:8083/bookings -Method Post -ContentType "application/json" -Body $body
Invoke-RestMethod http://localhost:8084/notifications
```

Approve a booking:

```powershell
$decision = @{ adminId = 2; rejectReason = $null } | ConvertTo-Json
Invoke-RestMethod http://localhost:8083/bookings/1/approve -Method Post -ContentType "application/json" -Body $decision
```

## Architecture Notes

- Each service keeps its own Hexagonal Architecture structure:
  - `core/domain`
  - `core/dto`
  - `core/port/in`
  - `core/port/out`
  - `core/service`
  - `adapter/in/web`
  - `adapter/out/jdbc` or `adapter/out/rest`
- `booking-service` stores only `student_id` and `room_id`. It does not share tables with the other services.
- `booking-service` calls:
  - `student-service` to validate `studentId` and `adminId`
  - `room-service` to validate `roomId`
  - `notification-service` to create notification logs
- `frontend-service` renders the single web UI and consumes the API services through the Docker internal network.
- Docker Compose service names are used for internal communication:
  - `http://student-service:8080`
  - `http://room-service:8080`
  - `http://booking-service:8080`
  - `http://notification-service:8080`
