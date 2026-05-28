# Lab Requirement Checklist

Mapped from `Project_III_Microservices_Architecture_Splitting_the_Monolith_en.txt` and the Week 13 Docker Compose lecture.

| Requirement | Implemented as |
| --- | --- |
| 3+ isolated services | `student-service`, `room-service`, `booking-service`, `notification-service`, plus `frontend-service` |
| Database-per-service | `student-db`, `room-db`, `booking-db`, `notification-db` with separate Postgres volumes |
| Hexagonal Architecture | Each service has `core/domain`, `core/port/in`, `core/port/out`, `core/service`, `adapter/in`, `adapter/out` |
| Inter-service communication | `booking-service` calls `student-service`, `room-service`, and `notification-service` via REST |
| Dockerfile per service | Each service folder has its own `Dockerfile` |
| One command startup | `docker compose up --build` from this folder |
| Internal Docker network | Compose uses service names such as `http://student-service:8080` |
| Postman/curl test artifact | `postman/room-booking-microservices.postman_collection.json` and `scripts/demo-requests.ps1` |
| Single integrated system link | `frontend-service` exposes `http://localhost:8080` |

## Domain Mapping

The lab text uses a thesis-management example. This project applies the same architecture to room booking:

| Lab example | This project |
| --- | --- |
| thesis-service | booking-service |
| student-service | student-service |
| notification-service | notification-service |
| thesis_db | booking_db |
| student_db | student_db |
| email log | notification_db |

## Demo Flow

1. Get students from `student-service`.
2. Get rooms from `room-service`.
3. Create a booking in `booking-service`.
4. Confirm that `booking-service` created a notification through `notification-service`.
5. Approve/reject/cancel/delete the booking through `booking-service`.
