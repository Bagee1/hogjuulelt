# Lecture-ээс Гаргасан Бие Даалтын Web Project Шаардлагууд

Энэ файл нь `clean_text/lectures` доторх lecture text-үүдээс DAO, Hexagonal Architecture, DTO, Clean Architecture, Observer, Factory Pattern, PostgreSQL DB холбоотой шаардлагуудыг web project-д хэрэгжүүлэх checklist хэлбэрээр нэгтгэсэн.

## 1. DAO / JDBC / Repository Adapter

Lecture source:
- `Lecture_04_Persistence_JDBC.txt`
- `Lecture_06_CRUD_Exception_Handling.txt`
- `Lecture_11_Web_App_best_practices.txt`

Шаардлага:
- Business logic буюу Service layer SQL, JDBC, database connection мэдэхгүй байна.
- Persistence logic-ийг DAO/Repository adapter дотор тусгаарлана.
- Core дотор repository interface буюу port тодорхойлно. Жишээ: `SubmissionRepository`, `LabRepository`, `UserRepository`.
- PostgreSQL/JDBC implementation нь adapter layer-д байна. Жишээ: `JdbcSubmissionRepository`.
- DAO/Repository implementation нь `PreparedStatement` ашиглана.
- SQL query-г string concatenation-аар user input-тэй нийлүүлэхгүй.
- `try-with-resources` ашиглаж `Connection`, `PreparedStatement`, `ResultSet`-ийг зөв хаана.
- Database row-г domain object руу map хийх mapper method ашиглана. Жишээ: `mapRowToSubmission(rs)`.
- UI/Servlet class-аас DAO-г шууд дуудахгүй, зөвхөн service/use case дуудна.

## 2. Hexagonal Architecture / Ports & Adapters

Lecture source:
- `Lecture_03_Architecture_Testing.txt`
- `Lecture_04_Persistence_JDBC.txt`
- `Lecture_07_Web_Adapters_Servlets_JSP.txt`
- `Lecture_08_Web_Crud.txt`
- `Lecture_10_Restful_APIs.txt`
- `Lecture_11_Web_App_best_practices.txt`

Шаардлага:
- Domain/Core нь project-ийн төвд байна.
- Core нь framework болон infrastructure-аас independent байна.
- Core дотор `jakarta.servlet.*`, `java.sql.*`, `com.fasterxml.jackson.*` import байхгүй.
- Ports нь interface байна.
- Primary/inbound adapters: Servlet, JSP controller, REST API controller.
- Secondary/outbound adapters: JDBC/PostgreSQL repository, email/notification adapter.
- Adapter class-ууд port interface implement хийнэ.
- Service/use case нь concrete adapter биш, port interface дээр depend хийнэ.
- Dependency Injection буюу constructor injection ашиглана.
- Web adapter солигдсон ч core code өөрчлөгдөхгүй байх ёстой.
- Test хийхдээ real DB-ийн оронд fake/in-memory repository ашиглаж болдог байна.

Санал болгох package structure:

```text
src/main/java/your/project/
  core/
    domain/
    port/in/
    port/out/
    service/
    dto/
  adapter/
    in/web/
    in/api/
    out/jdbc/
  config/
  factory/
```

## 3. Clean Architecture / Dependency Inversion

Lecture source:
- `Lecture_03_Architecture_Testing.txt`
- `Lecture_11_Web_App_best_practices.txt`

Шаардлага:
- High-level module буюу business policy нь low-level detail буюу DB/UI дээр depend хийхгүй.
- Both high-level болон low-level layer interface дээр depend хийнэ.
- Domain rule-ууд UI эсвэл DB adapter дотор биш core/service дотор байна.
- External failure, validation error, database error зэргийг boundary дээр зөв handle хийнэ.
- Unit test нь implementation detail биш behavior шалгана.
- Test нь fast, independent, repeatable, self-validating байна.

## 4. DTO

Lecture source:
- `Lecture_04_Persistence_JDBC.txt`
- `Lecture_05_Gui_Construction_Patterns.txt`
- `Lecture_07_Web_Adapters_Servlets_JSP.txt`
- `Lecture_10_Restful_APIs.txt`
- `Lecture_11_Web_App_best_practices.txt`

Шаардлага:
- DTO нь data transfer-д зориулсан object байна, business logic агуулахгүй.
- DTO-г UI/API boundary дээр ашиглана.
- Domain Entity-г шууд JSP/API response руу гаргахгүй.
- Entity -> DTO mapping заавал хийнэ.
- Input request-д зориулж тусдаа DTO ашиглаж болно. Жишээ: `SubmissionCreateDto`.
- Output response-д зориулж тусдаа DTO ашиглаж болно. Жишээ: `SubmissionResponseDto`.
- Sensitive data, жишээ нь password, internal ID, DB-only fields, DTO дээр гарахгүй.
- Java record ашиглаж болно. Жишээ:

```java
public record SubmissionResponseDto(
    Long id,
    String labTitle,
    String studentName,
    String status,
    String feedback
) {}
```

## 5. Observer Pattern

Lecture source:
- `Lecture_05_Gui_Construction_Patterns.txt`
- `Lecture_06_CRUD_Exception_Handling.txt`
- `Lecture_Design_Pattern_and_Frameworks.txt`

Lecture дээр Observer нь Swing `ActionListener`, `ListSelectionListener` маягаар тайлбарлагдсан. Web project дээр Swing байхгүй учраас Observer санааг domain event/listener хэлбэрээр хэрэгжүүлж болно.

Шаардлага:
- Subject/Event source нь state өөрчлөгдөх үед observer/listener-үүдэд мэдэгддэг байна.
- Observer interface тодорхойлно. Жишээ: `SubmissionObserver`.
- Service нь submission approve/reject болох үед observer-үүдэд notify хийнэ.
- Notification/logging logic нь core workflow-оос салангид байна.
- Шинэ observer нэмэхэд service-ийн core rule эвдэхгүй.

Web project-д тохирох жишээ:

```java
public interface SubmissionObserver {
    void onStatusChanged(Submission submission);
}
```

Жишээ observer-ууд:
- `AuditLogObserver`
- `TeacherNotificationObserver`
- `StudentNotificationObserver`

## 6. Factory Pattern

Lecture source:
- `Lecture_02_Advanced_Design_Patterns.txt`
- `Lecture_03_Architecture_Testing.txt`
- `Lecture_04_Persistence_JDBC.txt`
- `Lecture_05_Gui_Construction_Patterns.txt`
- `Lecture_07_Web_Adapters_Servlets_JSP.txt`

Шаардлага:
- Business logic дотор concrete class-уудыг шууд `new` хийхгүй.
- Object creation logic-ийг Factory class дотор төвлөрүүлнэ.
- Factory нь interface type буцаана.
- String-based factory input-оос зайлсхийж enum/config ашиглана.
- `RepositoryFactory` нь config-оос хамаарч `InMemoryRepository` эсвэл `JdbcRepository` буцааж чадна.
- `ServiceFactory` нь бүрэн configured service object буцаана.
- Servlet-ийн `init()` method дотор service-г factory-аар авна.

Жишээ:

```java
public final class ServiceFactory {
    public static SubmissionService createSubmissionService() {
        SubmissionRepository repository = RepositoryFactory.createSubmissionRepository();
        return new SubmissionService(repository);
    }
}
```

## 7. PostgreSQL Database

Lecture source:
- `Lecture_04_Persistence_JDBC.txt`
- `Lecture_12_Containerization_Docker.txt`
- `Lecture_13_Microservices_Docker_Compose.txt`

Шаардлага:
- PostgreSQL-г JDBC-р холбож ашиглана.
- JDBC URL нь `jdbc:postgresql://host:port/database` format-тай байна.
- DB credential-ийг source code дотор hardcode хийхгүй.
- `DB_URL`, `DB_USER`, `DB_PASSWORD` environment variable эсвэл config file ашиглана.
- Docker Compose ашиглаж PostgreSQL container ажиллуулж болно.
- `postgres` official image ашиглана.
- `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB` environment variables тохируулна.
- PostgreSQL data persistence-д volume ашиглана.
- Schema/table үүсгэхэд `init.sql` ашиглаж болно.
- Application container нь PostgreSQL service рүү internal Docker network-оор холбогдоно.

Жишээ compose хэсэг:

```yaml
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secretpassword
      POSTGRES_DB: lab_tracker
    volumes:
      - pg-data:/var/lib/postgresql/data

  app:
    build: .
    environment:
      DB_URL: jdbc:postgresql://db:5432/lab_tracker
      DB_USER: admin
      DB_PASSWORD: secretpassword
    depends_on:
      - db

volumes:
  pg-data:
```

## 8. Багшид Үзүүлэхэд Хангалттай Minimum Checklist

Web project-д дараах зүйлс байвал дээрх lecture requirement-үүдийг сайн хамарна:

- Servlet + JSP web UI.
- Login/session хамгаалалт.
- CRUD functionality.
- Core/domain package framework-гүй, SQL-гүй.
- Port interface-үүдтэй.
- JDBC/PostgreSQL repository adapter-тэй.
- DTO ашигласан.
- Factory ашиглаж service/repository үүсгэсэн.
- Observer/event listener ашиглаж status change log/notification хийсэн.
- PostgreSQL database Docker Compose-оор ажилладаг.
- `PreparedStatement`, `try-with-resources`, validation, error handling ашигласан.
- README дээр architecture, run instruction, DB config, demo flow бичсэн.
