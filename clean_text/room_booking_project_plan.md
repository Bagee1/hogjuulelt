# Room Booking Web App - Бие Даалтын Төлөвлөгөө

## Project Idea

Их сургуулийн өрөө/лабораторийн захиалгын жижиг web system.

User нь өрөө захиалах хүсэлт илгээнэ. Admin нь хүсэлтийг approve/reject хийнэ. System нь нэг өрөө нэг цаг дээр давхар захиалагдахаас хамгаална.

## Яагаад Энэ Санаа Шаардлагад Таарч Байна

- DAO/JDBC: Room, Booking, User, AuditLog data-г PostgreSQL-д хадгална.
- Hexagonal Architecture: Core business logic нь Web/DB-ээс тусдаа байна.
- DTO: JSP/API руу Domain Entity шууд гаргахгүй, DTO ашиглана.
- Clean Architecture: Core нь `jakarta.servlet.*`, `java.sql.*` import хийхгүй.
- Observer Pattern: Booking status өөрчлөгдөхөд audit log автоматаар үүснэ.
- Factory Pattern: Repository/Service object creation-ийг factory хариуцна.
- PostgreSQL DB: Real database persistence ашиглана.

## Roles

1. Student / User
   - Login хийнэ.
   - Өрөөний жагсаалт харна.
   - Booking request үүсгэнэ.
   - Өөрийн booking status харна.

2. Admin
   - Login хийнэ.
   - Бүх booking request харна.
   - Approve / Reject хийнэ.
   - Reject хийхдээ reason бичнэ.
   - Audit log харна.

## Main Features

### Authentication

- Login page байна.
- Session ашиглана.
- Login хийгээгүй хэрэглэгч protected page рүү орж болохгүй.
- Demo credential:
  - `student / 123`
  - `admin / 123`

### Room Management

- Room list харуулна.
- Admin room нэмэх боломжтой байж болно.
- Room fields:
  - `id`
  - `name`
  - `capacity`
  - `location`

### Booking Request

- Student room сонгоно.
- Date, start time, end time оруулна.
- Purpose буюу зориулалт бичнэ.
- Booking initial status: `PENDING`.

### Booking Workflow

Status:

- `PENDING`
- `APPROVED`
- `REJECTED`
- `CANCELLED`

Rules:

- `PENDING` booking-г admin approve/reject хийж болно.
- `REJECTED` бол reject reason заавал байна.
- Same room дээр time overlap байвал approve хийхгүй.
- End time нь start time-аас хойш байх ёстой.
- Past date/time дээр booking үүсгэхгүй.

### Observer / Audit Log

Booking status өөрчлөгдөх бүрт audit log үүснэ.

Example:

```text
Booking #5 changed from PENDING to APPROVED by admin
```

Observer idea:

```java
public interface BookingObserver {
    void onStatusChanged(Booking booking, BookingStatus oldStatus, BookingStatus newStatus);
}
```

Implementation:

- `AuditLogObserver`
- Optional: `NotificationObserver`

## Hexagonal Architecture Structure

```text
src/main/java/mn/edu/roombooking/
  core/
    domain/
      User.java
      Room.java
      Booking.java
      BookingStatus.java
      AuditLog.java
    dto/
      BookingCreateDto.java
      BookingResponseDto.java
      RoomResponseDto.java
      LoginDto.java
    port/
      in/
        BookingUseCase.java
        RoomUseCase.java
        AuthUseCase.java
      out/
        BookingRepository.java
        RoomRepository.java
        UserRepository.java
        AuditLogRepository.java
    service/
      BookingService.java
      RoomService.java
      AuthService.java
    observer/
      BookingObserver.java
      AuditLogObserver.java

  adapter/
    in/
      web/
        LoginServlet.java
        LogoutServlet.java
        DashboardServlet.java
        RoomServlet.java
        BookingServlet.java
        AdminBookingServlet.java
      api/
        BookingApiServlet.java
    out/
      jdbc/
        JdbcBookingRepository.java
        JdbcRoomRepository.java
        JdbcUserRepository.java
        JdbcAuditLogRepository.java

  config/
    DatabaseConnection.java

  factory/
    RepositoryFactory.java
    ServiceFactory.java
```

## Clean Architecture Rules

Core package дотор дараах import байхгүй:

```java
jakarta.servlet.*
java.sql.*
com.fasterxml.jackson.*
```

Servlet нь Service дуудна.

Service нь Repository interface дуудна.

JDBC Repository нь PostgreSQL-тэй харьцана.

## DAO / Repository Requirements

- `JdbcBookingRepository` нь `BookingRepository` implement хийнэ.
- `PreparedStatement` ашиглана.
- `try-with-resources` ашиглана.
- Row mapper method ашиглана.

Example:

```java
private Booking mapRow(ResultSet rs) throws SQLException {
    // DB row -> Domain object
}
```

## DTO Requirements

Domain entity-г JSP/API дээр шууд гаргахгүй.

Example DTO:

```java
public record BookingResponseDto(
    Long id,
    String roomName,
    String studentUsername,
    String bookingDate,
    String startTime,
    String endTime,
    String status,
    String purpose,
    String rejectReason
) {}
```

## Factory Pattern

Service болон Repository үүсгэлтийг factory хариуцна.

Example:

```java
public final class ServiceFactory {
    public static BookingUseCase bookingUseCase() {
        BookingRepository bookingRepository = RepositoryFactory.bookingRepository();
        AuditLogRepository auditLogRepository = RepositoryFactory.auditLogRepository();
        BookingObserver observer = new AuditLogObserver(auditLogRepository);
        return new BookingService(bookingRepository, observer);
    }
}
```

Servlet дотор:

```java
public void init() {
    this.bookingUseCase = ServiceFactory.bookingUseCase();
}
```

## PostgreSQL Schema

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(100) NOT NULL
);

CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    room_id INT NOT NULL REFERENCES rooms(id),
    user_id INT NOT NULL REFERENCES users(id),
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reject_reason VARCHAR(255)
);

CREATE TABLE audit_logs (
    id SERIAL PRIMARY KEY,
    booking_id INT REFERENCES bookings(id),
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, role) VALUES
('student', '123', 'STUDENT'),
('admin', '123', 'ADMIN');

INSERT INTO rooms (name, capacity, location) VALUES
('Lab 201', 30, 'Main Building'),
('Seminar Room 105', 20, 'Library Building'),
('Conference Hall', 80, 'Administration Building');
```

## Web Pages

```text
/login
/logout
/dashboard
/rooms
/bookings
/admin/bookings
/admin/audit-logs
```

JSP files:

```text
src/main/webapp/WEB-INF/views/login.jsp
src/main/webapp/WEB-INF/views/dashboard.jsp
src/main/webapp/WEB-INF/views/rooms.jsp
src/main/webapp/WEB-INF/views/my-bookings.jsp
src/main/webapp/WEB-INF/views/admin-bookings.jsp
src/main/webapp/WEB-INF/views/audit-logs.jsp
```

## Optional REST API

Bonus feature:

```text
GET /api/bookings
GET /api/bookings/{id}
POST /api/bookings
```

Requirements:

- JSON response.
- DTO serialize хийнэ.
- `404 Not Found`, `400 Bad Request`, `201 Created` status code ашиглана.

## Demo Flow

1. Browser дээр `/login` нээнэ.
2. `student / 123` хэрэглэгчээр login хийнэ.
3. Room list харна.
4. `Lab 201` дээр booking request үүсгэнэ.
5. My bookings page дээр status `PENDING` гэж харагдана.
6. Logout хийнэ.
7. `admin / 123` хэрэглэгчээр login хийнэ.
8. Admin booking list дээр student-ийн request харагдана.
9. Approve дарна.
10. Давхар цагтай өөр booking approve хийхэд validation error гарч байгааг харуулна.
11. Audit logs page дээр status change log автоматаар үүссэнийг харуулна.
12. Code structure дээр core package SQL/Servlet import-гүйг харуулна.
13. PostgreSQL table дээр data хадгалагдсан байгааг харуулна.

## Minimum Submission Checklist

- [ ] Maven Java web project.
- [ ] Servlet + JSP ажилладаг.
- [ ] PostgreSQL database-тэй.
- [ ] Login/session хамгаалалттай.
- [ ] Room list.
- [ ] Booking create.
- [ ] Admin approve/reject.
- [ ] Time overlap validation.
- [ ] DAO/JDBC repository adapter.
- [ ] DTO ашигласан.
- [ ] Factory ашигласан.
- [ ] Observer ашиглаж audit log үүсгэсэн.
- [ ] Hexagonal package structure.
- [ ] README run instruction.
- [ ] Demo flow бэлэн.
