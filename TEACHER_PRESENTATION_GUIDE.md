# Багшид тайлбарлах ерөнхий заавар

Энэ файлыг төслөө хамгаалахдаа богино cheat sheet шиг ашиглана. Гол санаа нь эхлээд системийн зорилгыг хэлээд, дараа нь architecture, lab requirement, ажиллаж байгаа demo-г дарааллаар нь харуулах юм.

## 1. Нэг минутын танилцуулга

Энэ төсөл бол их сургуулийн өрөө захиалгын web систем. Оюутан өрөө захиалах хүсэлт илгээнэ, admin хүсэлтийг батлах, татгалзах, цуцлах, устгах боломжтой. Мөн admin шинэ өрөө нэмнэ, систем хүсэлт бүрийн notification бүртгэлийг хадгална.

Эхний хувилбар нь `room-booking-web` дотор Servlet/JSP monolith байсан. Дараа нь lab-ийн microservices requirement-ийн дагуу `room-booking-microservices` дотор хэд хэдэн service болгон салгасан.

## 2. Demo эхлүүлэх

Repo root дээрээс:

```powershell
.\start-room-booking.ps1
```

Эсвэл:

```powershell
cd room-booking-microservices
docker compose up --build
```

Browser дээр:

```text
http://localhost:8080
```

Цэвэр demo эхлүүлэх хэрэгтэй бол:

```powershell
cd room-booking-microservices
docker compose down -v
docker compose up --build
```

## 3. Demo хийх дараалал

1. `http://localhost:8080` нээгээд нэгтгэсэн dashboard-ийг харуулна.
2. Өрөөний жагсаалт, хэрэглэгч, хүлээгдэж буй хүсэлтүүдийн тоог харуулна.
3. Student хэсгээс өрөө захиалах хүсэлт илгээнэ.
4. Admin хүсэлтийн жагсаалтаас тухайн хүсэлтийг харна.
5. Admin хүсэлтийг `Батлах`, `Татгалзах`, `Цуцлах`, `Устгах` үйлдлээр шалгана.
6. Admin шинэ өрөө нэмээд room-service өөрийн DB дээр хадгалж байгааг тайлбарлана.
7. Notification хэсэгт booking үүсэх, батлагдах, татгалзах зэрэг event бүртгэгдэж байгааг харуулна.

## 4. Service бүтэц

| Service | Port | DB | Үүрэг |
| --- | --- | --- | --- |
| `frontend-service` | 8080 | DB байхгүй | Нэгтгэсэн web UI, бусад service рүү REST-ээр хандана |
| `student-service` | 8081 | `student_db` | Оюутан/admin мэдээлэл |
| `room-service` | 8082 | `room_db` | Өрөөний жагсаалт, өрөө нэмэх |
| `booking-service` | 8083 | `booking_db` | Захиалгын workflow, батлах/татгалзах/цуцлах |
| `notification-service` | 8084 | `notification_db` | Event notification log |

## 5. Lab requirement-ийг яаж хангасан бэ?

| Шаардлага | Төсөл дээрх хэрэгжилт |
| --- | --- |
| 3-аас дээш microservice | `student-service`, `room-service`, `booking-service`, `notification-service`, `frontend-service` |
| Database per service | Service бүр тусдаа PostgreSQL DB болон Docker volume-той |
| REST communication | `booking-service` нь student, room, notification service рүү REST call хийдэг |
| Docker Compose | `room-booking-microservices/docker-compose.yml` нэг командаар бүх service болон DB-г асаана |
| Dockerfile per service | Service бүр өөрийн `Dockerfile`-той |
| Hexagonal/Clean Architecture | Service бүр `core`, `port`, `adapter` бүтэцтэй |
| DTO | Request/response DTO классууд `core/dto` дотор байна |
| DAO/Repository | JDBC repository классууд `adapter/out/jdbc` дотор байна |
| PostgreSQL | Compose дотор 4 тусдаа Postgres container ашигласан |
| Test | `BookingServiceTest` болон demo request script байна |

Дэлгэрэнгүй checklist:

```text
room-booking-microservices/REQUIREMENTS_CHECKLIST.md
```

## 6. Code дээр харуулах гол файлууд

```text
room-booking-microservices/docker-compose.yml
```

Service, DB, port, internal network dependency-г эндээс харуулна.

```text
room-booking-microservices/booking-service/src/main/java/mn/edu/room/booking/core/service/BookingService.java
```

Booking business logic: validation, overlap шалгах, approve/reject/cancel workflow.

```text
room-booking-microservices/booking-service/src/main/java/mn/edu/room/booking/core/port/out/
```

Hexagonal architecture-ийн outbound port-ууд.

```text
room-booking-microservices/booking-service/src/main/java/mn/edu/room/booking/adapter/out/rest/
```

Бусад service рүү REST-ээр холбогдож байгаа adapter.

```text
room-booking-microservices/booking-service/src/main/java/mn/edu/room/booking/adapter/out/jdbc/JdbcBookingRepository.java
```

DAO/Repository layer, PostgreSQL-тэй ажиллаж байгаа хэсэг.

```text
room-booking-microservices/frontend-service/src/main/resources/templates/dashboard.html
```

Нэгтгэсэн web UI.

Monolith дээр Factory/Observer pattern харуулах бол:

```text
room-booking-web/src/main/java/mn/edu/room/factory/
room-booking-web/src/main/java/mn/edu/room/core/observer/
```

## 7. Багш асуувал богино хариултууд

**Яагаад service бүр тусдаа database-тэй вэ?**

Microservice бүр өөрийн өгөгдлөө эзэмших ёстой. Ингэснээр service-үүд хоорондоо DB table share хийхгүй, loosely coupled болно.

**Booking service яагаад student болон room мэдээллийг бүтнээр хадгалахгүй байгаа вэ?**

Booking service зөвхөн `student_id`, `room_id` хадгална. Student-ийн дэлгэрэнгүй мэдээллийг student-service, room-ийн мэдээллийг room-service эзэмшинэ.

**Service-үүд хоорондоо яаж холбогддог вэ?**

Docker internal network дээр service name ашиглаж REST call хийдэг. Жишээ нь `http://student-service:8080`.

**Notification нь яаж ажилладаг вэ?**

Booking үүсэх, батлагдах, татгалзах, цуцлагдах үед booking-service notification-service рүү event илгээж log үүсгэнэ.

**Hexagonal Architecture хаана байна вэ?**

`core` нь business logic, `port` нь interface, `adapter` нь web/JDBC/REST implementation. Ингэснээр business logic framework болон database-аас аль болох хамаарал багатай болсон.

**Яаж test хийсэн бэ?**

JUnit unit test болон `scripts/demo-requests.ps1` script ашиглаж API flow-г шалгасан. UI дээр browser-оор хүсэлт үүсгэх, admin action хийх боломжтой.

## 8. 2 минутын ярих script

Сайн байна уу. Миний хийсэн төсөл бол их сургуулийн өрөө захиалгын web систем. Оюутан өрөө захиалах хүсэлт илгээдэг, admin тэр хүсэлтийг батлах, татгалзах, цуцлах, устгах боломжтой. Мөн admin өрөө нэмэх боломжтой.

Энэ төслийг lab-ийн microservices architecture requirement-ийн дагуу monolith санаанаас салгаад `student-service`, `room-service`, `booking-service`, `notification-service`, `frontend-service` гэж хуваасан. Service бүр өөрийн PostgreSQL database-тэй, Docker Compose ашиглаад нэг командаар бүгд асдаг.

Architecture-ийн хувьд service бүрт `core`, `port`, `adapter` гэсэн Hexagonal/Clean Architecture бүтэц ашигласан. DTO классууд request/response өгөгдөл дамжуулна, JDBC repository буюу DAO layer database-тэй ажиллана. Booking service нь бусад service-тэй REST call хийж student, room шалгаад notification event үүсгэдэг.

Одоо demo дээр `http://localhost:8080` нээгээд өрөө захиалах хүсэлт илгээж, admin талаас батлах workflow-г харуулъя.

## 9. Demo дуусгах

```powershell
.\stop-room-booking.ps1
```

Эсвэл:

```powershell
cd room-booking-microservices
docker compose down
```
