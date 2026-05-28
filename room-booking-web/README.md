# Room Booking Web App

Small Servlet/JSP web project for booking university rooms. It is structured with Hexagonal Architecture, DAO/JDBC adapters, DTOs, Factory Pattern, Observer Pattern, and PostgreSQL persistence.

## Stack

- Java 17
- Maven WAR project
- Tomcat 10.1
- Jakarta Servlet 6.0 / JSP 3.1
- PostgreSQL JDBC
- JUnit 5 / Mockito

## Run Database

```powershell
docker compose up -d
```

The default database config is:

```text
DB_URL=jdbc:postgresql://localhost:5432/room_booking
DB_USER=admin
DB_PASSWORD=secretpassword
```

You can override these with environment variables or `src/main/resources/database.properties`.

## Build

Use JDK 17:

```powershell
mvn clean test
mvn clean package
```

Deploy `target/room-booking.war` to Tomcat 10.1.

For Eclipse-specific import and Tomcat setup, see `ECLIPSE_SETUP.md`.
For the local verification notes, see `VERIFICATION.md`.

Expected URL:

```text
http://localhost:8080/room-booking/login
```

## Demo Accounts

```text
student / 123
admin / 123
```

## Demo Flow

1. Login as `student`.
2. Open Rooms and create a booking request.
3. Open My Bookings and show `PENDING`.
4. Logout and login as `admin`.
5. Open Admin Bookings and approve/reject the booking.
6. Open Audit Logs and show observer-created status log.
7. Try approving an overlapping booking to show validation.

## Architecture Proof

Show these points to the teacher:

- `core` has no `jakarta.servlet.*` imports.
- `core` has no `java.sql.*` imports.
- JDBC SQL is isolated in `adapter/out/jdbc`.
- Servlets are isolated in `adapter/in/web`.
- DTOs are used at UI/API boundaries.
- Factory classes create services/repositories.
- Observer creates audit logs on booking status changes.
