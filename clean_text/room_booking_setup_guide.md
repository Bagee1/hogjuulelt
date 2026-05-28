# Room Booking Web App - Maven, Eclipse, Setup Guide

Энэ guide нь `Room Booking Web App`-ийг багшид сайн харагдахаар хийхэд хэрэгтэй stack, Maven dependency, Eclipse/Tomcat/PostgreSQL тохиргоо, project хийх дарааллыг нэгтгэсэн.

## 1. Recommended Stack

Course болон lecture/lab шаардлагатай нийцүүлэхийн тулд энэ stack ашигла.

| Tool / Library | Recommended Version | Яагаад |
|---|---:|---|
| Java JDK | 17 | Lecture/lab дээр Java 17+ гэж байгаа, Tomcat 10.1-тэй тохирно |
| Maven | 3.9.x эсвэл Eclipse embedded Maven | Java web app build/package хийхэд хангалттай |
| Apache Tomcat | 10.1.x | Jakarta Servlet 6.0, JSP 3.1 support-той |
| Jakarta Servlet API | 6.0.0 | Tomcat 10.1-тэй match хийнэ |
| JSP/JSTL | JSTL API 3.0.2, GlassFish impl 3.0.1 | JSP дээр `<c:out>`, `<c:forEach>` ашиглахад |
| PostgreSQL JDBC | 42.7.8 | PostgreSQL-тэй JDBC-р холбогдоно |
| Jackson Databind | 2.18.6 | Optional REST API JSON serialization |
| JUnit Jupiter | 5.13.4 | Unit test |
| Mockito | 5.23.0 | Port/interface mock test |
| Maven Compiler Plugin | 3.14.1 | Java 17 compile |
| Maven WAR Plugin | 3.4.0 | `.war` package үүсгэнэ |
| Maven Surefire Plugin | 3.5.4 | JUnit 5 test ажиллуулна |

Version reference:

- Tomcat 10.1 documentation: https://tomcat.apache.org/tomcat-10.1-doc/introduction.html
- Tomcat 10.1 migration/spec note: https://tomcat.apache.org/migration-10.1.html
- PostgreSQL JDBC: https://pgjdbc.github.io/pgjdbc/
- PostgreSQL JDBC Maven Central: https://central.sonatype.com/artifact/org.postgresql/postgresql/42.7.8
- JSTL API Maven Central: https://central.sonatype.com/artifact/jakarta.servlet.jsp.jstl/jakarta.servlet.jsp.jstl-api/3.0.2/jar
- JSTL implementation Maven Central: https://central.sonatype.com/artifact/org.glassfish.web/jakarta.servlet.jsp.jstl
- JUnit Maven Central: https://central.sonatype.com/artifact/org.junit.jupiter/junit-jupiter/5.13.4
- Mockito Maven Central: https://central.sonatype.com/artifact/org.mockito/mockito-core/5.23.0
- Maven Compiler Plugin: https://maven.apache.org/plugins-archives/maven-compiler-plugin-3.14.1/plugin-info.html
- Maven WAR Plugin: https://maven.apache.org/plugins-archives/maven-war-plugin-3.4.0/plugin-info.html
- Maven Surefire Plugin: https://maven.apache.org/surefire-archives/surefire-3.5.4/maven-surefire-plugin/dependency-info.html

## 2. Project Creation Option

Хамгийн цэвэр арга:

- Eclipse дээр Maven project үүсгэнэ.
- Packaging нь `war` байна.
- Servlet/JSP project боловч Maven dependency-г `pom.xml`-оор удирдана.

Recommended Maven coordinates:

```text
groupId: mn.edu.room
artifactId: room-booking-web
version: 1.0-SNAPSHOT
packaging: war
```

## 3. Recommended `pom.xml`

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>mn.edu.room</groupId>
    <artifactId>room-booking-web</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>17</maven.compiler.release>

        <jakarta.servlet.version>6.0.0</jakarta.servlet.version>
        <jstl.api.version>3.0.2</jstl.api.version>
        <jstl.impl.version>3.0.1</jstl.impl.version>
        <postgresql.version>42.7.8</postgresql.version>
        <jackson.version>2.18.6</jackson.version>
        <junit.version>5.13.4</junit.version>
        <mockito.version>5.23.0</mockito.version>
    </properties>

    <dependencies>
        <!-- Tomcat provides Servlet API at runtime. Do not package it into WAR. -->
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>${jakarta.servlet.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- JSTL for safe JSP rendering: c:out, c:forEach, etc. -->
        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>${jstl.api.version}</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>${jstl.impl.version}</version>
        </dependency>

        <!-- PostgreSQL JDBC Driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>${postgresql.version}</version>
        </dependency>

        <!-- Optional: REST API JSON serialization -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
        </dependency>

        <!-- Unit testing -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
            <version>${mockito.version}</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>room-booking</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.14.1</version>
                <configuration>
                    <release>17</release>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.4</version>
            </plugin>
        </plugins>
    </build>
</project>
```

## 4. Eclipse Setup

### Install / Use Correct Eclipse

Use:

```text
Eclipse IDE for Enterprise Java and Web Developers
```

Хэрэв standard Eclipse байгаа бол:

```text
Help -> Eclipse Marketplace -> Eclipse Web Developer Tools
```

### JDK Setup

1. `Window -> Preferences`
2. `Java -> Installed JREs`
3. Add JDK 17.
4. JDK 17-г default болго.
5. `Java -> Compiler`
6. Compiler compliance level: `17`.

### Maven Setup

1. `Window -> Preferences`
2. `Maven -> Installations`
3. Embedded Maven байж болно.
4. `Maven -> User Settings`
5. Local repository хэвийн байгаа эсэхийг шалга.

Project дээр:

```text
Right click project -> Maven -> Update Project
```

Shortcut:

```text
Alt + F5
```

### Tomcat Setup

1. Apache Tomcat 10.1.x ZIP татаж ав.
2. Жишээ folder:

```text
C:\Tomcat10
```

3. Eclipse:

```text
Window -> Preferences -> Server -> Runtime Environments -> Add
```

4. `Apache Tomcat v10.1` сонго.
5. Tomcat folder-оо зааж өг.
6. JRE нь JDK 17 байгаа эсэхийг шалга.

### Add Project to Tomcat

1. `Window -> Show View -> Servers`
2. `No servers are available...` дээр дарж Tomcat 10.1 server үүсгэнэ.
3. Project-оо Add/Remove дээрээс server рүү нэмнэ.
4. Server дээр right click:

```text
Clean
Publish
Start
```

Expected URL:

```text
http://localhost:8080/room-booking/
```

Хэрэв `<finalName>room-booking</finalName>` ашигласан бол context path ихэвчлэн `room-booking` байна.

## 5. Project Folder Structure

```text
room-booking-web/
  pom.xml
  src/
    main/
      java/
        mn/edu/room/
          core/
            domain/
            dto/
            port/in/
            port/out/
            service/
            observer/
          adapter/
            in/web/
            in/api/
            out/jdbc/
          config/
          factory/
      resources/
        database.properties
      webapp/
        WEB-INF/
          views/
            login.jsp
            dashboard.jsp
            rooms.jsp
            my-bookings.jsp
            admin-bookings.jsp
            audit-logs.jsp
    test/
      java/
        mn/edu/room/
          core/service/
```

## 6. PostgreSQL Setup Option A - Docker Compose

Project root дээр `docker-compose.yml`:

```yaml
services:
  db:
    image: postgres:15
    container_name: room_booking_postgres
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secretpassword
      POSTGRES_DB: room_booking
    ports:
      - "5432:5432"
    volumes:
      - pg-data:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql

volumes:
  pg-data:
```

Run:

```powershell
docker compose up -d
docker ps
```

Stop:

```powershell
docker compose down
```

Reset database fully:

```powershell
docker compose down -v
docker compose up -d
```

## 7. `init.sql`

Project root дээр:

```sql
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
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

CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    booking_id INT REFERENCES bookings(id),
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, role)
SELECT 'student', '123', 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student');

INSERT INTO users (username, password, role)
SELECT 'admin', '123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO rooms (name, capacity, location)
SELECT 'Lab 201', 30, 'Main Building'
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE name = 'Lab 201');

INSERT INTO rooms (name, capacity, location)
SELECT 'Seminar Room 105', 20, 'Library Building'
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE name = 'Seminar Room 105');

INSERT INTO rooms (name, capacity, location)
SELECT 'Conference Hall', 80, 'Administration Building'
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE name = 'Conference Hall');
```

## 8. `database.properties`

`src/main/resources/database.properties`:

```properties
db.url=jdbc:postgresql://localhost:5432/room_booking
db.user=admin
db.password=secretpassword
```

Important:

```text
database.properties
```

гэсэн мөрийг `.gitignore` дээр нэмэх нь зөв. Demo-д бол example config-оо `database.example.properties` гэж хадгалж болно.

## 9. Eclipse Run Checklist

1. Docker PostgreSQL running:

```powershell
docker ps
```

2. Maven dependencies татсан:

```text
Right click project -> Maven -> Update Project
```

3. Build:

```powershell
mvn clean test
mvn clean package
```

4. Tomcat 10.1 server started.
5. Browser:

```text
http://localhost:8080/room-booking/login
```

## 10. How to Make It Look Good to the Teacher

Багшид сайн харагдуулах гол зүйл нь олон feature биш, **architecture clarity**.

Заавал харуулах зүйл:

- `core` package дотор Servlet/JDBC import байхгүй.
- `adapter/out/jdbc` дотор SQL code тусдаа байна.
- `adapter/in/web` дотор Servlet code тусдаа байна.
- Service class constructor injection ашиглаж байна.
- DTO ашиглаж байна.
- Factory class ашиглаж байна.
- Observer ашиглаж audit log үүсгэж байна.
- PostgreSQL data persisted байна.
- `PreparedStatement` ашиглаж SQL injection-аас хамгаалсан байна.
- Reject/Approve validation core service дээр байна.

## 11. Demo Script

1. Login page нээ.
2. `student / 123`-аар login хий.
3. Rooms page дээр өрөөнүүдийг харуул.
4. `Lab 201` дээр booking үүсгэ.
5. My bookings дээр `PENDING` status харуул.
6. Logout хий.
7. `admin / 123`-аар login хий.
8. Admin bookings дээр request харагдаж байгааг харуул.
9. Approve хий.
10. Audit logs дээр автомат log үүссэнийг харуул.
11. Дахиад яг тэр room/time дээр booking үүсгээд approve хийхэд overlap validation error харуул.
12. Code дээр package structure болон core import-уудыг харуул.

## 12. Suggested Build Order

1. Maven project + `pom.xml`.
2. PostgreSQL `docker-compose.yml` + `init.sql`.
3. Domain classes.
4. DTO classes.
5. Port interfaces.
6. JDBC repositories.
7. Services.
8. Factory classes.
9. Observer + audit log.
10. Login/logout servlet.
11. Room list servlet/JSP.
12. Booking create servlet/JSP.
13. Admin approve/reject servlet/JSP.
14. Optional REST API.
15. Unit tests for `BookingService`.
16. README + demo screenshots.

## 13. Minimum Tests

At least 3 JUnit tests:

- Booking create fails when end time is before start time.
- Approve fails when same room/time overlaps existing approved booking.
- Reject fails when reject reason is blank.

Mockito ашиглаж `BookingRepository` port mock хийж болно.

## 14. Common Errors

### `jakarta.servlet` package олдохгүй

Fix:

- `jakarta.servlet-api` dependency байгаа эсэх.
- Scope нь `provided` байгаа эсэх.
- Maven update хийсэн эсэх.

### Tomcat 9 ашиглаад servlet ажиллахгүй

Tomcat 9 нь `javax.servlet.*`, Tomcat 10.1 нь `jakarta.servlet.*`.

Энэ project дээр:

```java
import jakarta.servlet.*;
```

ашиглах тул Tomcat 10.1 хэрэгтэй.

### PostgreSQL connection error

Шалга:

- Docker container running эсэх.
- Port `5432:5432` map хийгдсэн эсэх.
- URL:

```text
jdbc:postgresql://localhost:5432/room_booking
```

- user/password таарч байгаа эсэх.

### JSP дээр JSTL tag ажиллахгүй

Шалга:

- JSTL API + implementation dependency хоёулаа байгаа эсэх.
- JSP дээр taglib:

```jsp
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
```

Tomcat 10/Jakarta stack дээр хуучин `http://java.sun.com/jsp/jstl/core` URI ашиглахаас зайлсхий.
