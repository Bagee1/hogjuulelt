# Verification

Verified on May 27, 2026 with a portable JDK 17 under `.tools/jdk-17`.

## Build

```powershell
mvn clean test
mvn clean package
```

Result:

```text
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
target/room-booking.war created
```

## Architecture Check

The core package was checked for forbidden framework/infrastructure imports:

```powershell
rg -n "jakarta\\.servlet|java\\.sql|com\\.fasterxml" src\main\java\mn\edu\room\core
```

Result: no matches.

## Database

PostgreSQL was started with Docker Compose:

```powershell
docker compose up -d
```

Verified tables:

```text
users
rooms
bookings
audit_logs
```

Verified seed data:

```text
student / 123 / STUDENT
admin / 123 / ADMIN
Lab 201
Seminar Room 105
Conference Hall
```

## Web Smoke Test

Tomcat 10.1.55 was used for local smoke testing.

Verified URL:

```text
http://localhost:8080/room-booking/login
```

Verified flow:

- Student login redirects to dashboard.
- Student can create a booking request.
- Admin can approve a booking.
- Observer creates one audit log for status change.
- Overlapping room/time approval is rejected and booking remains `PENDING`.
- UI redesign smoke test passed for login, dashboard, rooms, my bookings, admin bookings, and audit logs.

After verification, smoke-test bookings and audit logs were truncated so the demo database starts clean.
