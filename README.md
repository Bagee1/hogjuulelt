# Hogjuulelt Room Booking System

Их сургуулийн өрөө захиалгын жишээ систем. Гол ажиллах хувилбар нь `room-booking-microservices` дотор байгаа microservice систем бөгөөд Docker Compose-оор нэг командаар асна.

## Юу багтсан бэ?

| Хэсэг | Тайлбар |
| --- | --- |
| `room-booking-microservices` | Student, Room, Booking, Notification, Frontend гэсэн service-үүдтэй Docker Compose систем |
| `room-booking-web` | Servlet/JSP monolith хувилбар |
| `clean_text` | Lab/lecture дээр үндэслэсэн шаардлага, setup guide, project plan |

## Шаардлагатай зүйлс

- Docker Desktop
- Git
- Browser

Java, Maven, PostgreSQL-г тусад нь суулгах шаардлагагүй. Docker image дотор build/run хийгдэнэ.

## Асаах

```powershell
cd room-booking-microservices
docker compose up --build
```

Дараа нь browser дээр нээнэ:

```text
http://localhost:8080
```

## Зогсоох

```powershell
cd room-booking-microservices
docker compose down
```

DB volume-уудыг цэвэрлээд шинээр demo эхлүүлэх бол:

```powershell
cd room-booking-microservices
docker compose down -v
```

## Service холбоосууд

| Service | URL |
| --- | --- |
| Frontend UI | http://localhost:8080 |
| Student API | http://localhost:8081 |
| Room API | http://localhost:8082 |
| Booking API | http://localhost:8083 |
| Notification API | http://localhost:8084 |

## Lab requirement

Microservice даалгаврын шаардлагын checklist:

```text
room-booking-microservices/REQUIREMENTS_CHECKLIST.md
```

Багшид тайлбарлах ерөнхий заавар:

```text
TEACHER_PRESENTATION_GUIDE.md
```

Төслийн setup болон Eclipse/Maven тайлбар:

```text
clean_text/room_booking_setup_guide.md
```

## Түргэн тест

```powershell
cd room-booking-microservices
powershell -ExecutionPolicy Bypass -File .\scripts\demo-requests.ps1
```

## GitHub дээр оруулахдаа

`target`, `.tools`, `.m2`, Docker volume, local `.env`, original PDF/PPT lecture files зэрэг том/generated/local файлуудыг `.gitignore`-оор хассан.
