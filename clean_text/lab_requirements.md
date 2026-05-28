# Lab Даалгаврын Шаардлагын Нэгтгэл

Энэ файл нь `clean_text/labs` доторх цэвэрлэсэн PDF текстүүдээс lab хийхэд хэрэгтэй гол шаардлагуудыг нэгтгэсэн хувилбар.

Тайлбар:
- `Starter-Kit`, `Structure`, code skeleton төрлийн PDF-үүдийг туслах материал гэж үзэв.
- `Lectures_09_State_Management_Security.txt` нь lecture материал тул lab requirement хэсэгт оруулаагүй.
- `Lab_02_Tasks.txt` ба `Lab_02_Tasks_V2_0.txt` нь ижил sprint-ийн хоёр хувилбар тул нэгтгэв.

## Lab 02 - Creation Logic

Эх сурвалж: `Lab_02_Tasks.txt`, `Lab_02_Tasks_V2_0.txt`

Гол зорилго:
- Thread-safe database/repository access layer хийх.
- User creation logic-ийг concrete class-аас салгаж Factory + interface ашиглах.

Шаардлага:
- `ThesisRepository`-г Singleton хэлбэрээр хэрэгжүүлэх.
- Constructor нь `private` байна.
- Access зөвхөн `getInstance()`-аар хийгдэнэ.
- Double-Checked Locking ашиглаж, `instance` талбарыг `volatile` болгоно.
- 5+ parallel thread-ээр зөвхөн нэг instance үүсэж байгааг шалгах тест хийнэ.
- `ThesisUser` interface үүсгэж `getRole()` method-той болгоно.
- `Student`, `Professor` class-ууд `ThesisUser` interface implement хийнэ.
- `UserFactory` concrete class биш, `ThesisUser` interface type буцаана.
- Main/business logic дотор `new Student()` эсвэл `new Professor()` шууд дуудахгүй.

## Lab 03 & 04 - Clean Architecture / Ports & Adapters

Эх сурвалж: `Lab_03_04_Tasks_3_and_4.txt`

Гол зорилго:
- Thesis Submission mini application хийх.
- Clean Architecture, Ports & Adapters, Dependency Inversion Principle ашиглах.

Шаардлага:
- Java 17+, Maven, JUnit 5 ашиглана.
- Package structure: `domain`, `ports`, `app`, `adapters`.
- Domain layer framework, DB, adapter dependency-гүй байна.
- `Thesis` entity/model үүсгэнэ.
- `ThesisId` value object optional боловч зөвлөмжтэй.
- `DomainException` эсвэл validation exception үүсгэнэ.
- `title`, `studentId` хоосон байж болохгүй.
- `IdGeneratorPort`, `ThesisRepositoryPort` interface-үүдийг `ports` дотор тодорхойлно.
- `SubmitThesisCommand`, `SubmitThesisResult`, `SubmitThesisService` зэрэг use case/application service үүсгэнэ.
- Service нь ports-оо constructor injection-аар авна.
- Use case test заавал бичнэ.
- Validation test заавал бичнэ.
- Test deterministic байна, uncontrolled real UUID/time шууд ашиглахгүй.
- `UuidIdGeneratorAdapter`, `InMemoryThesisRepositoryAdapter` adapter-уудыг хэрэгжүүлнэ.
- Root package дотор composition root/manual wiring class үүсгэж demo call ажиллуулна.
- Optional: ArchUnit architecture tests.
- `README.md` дотор description, package structure, run/test заавар, extension заавар бичнэ.

Шалгах минимум:
- `mvn test` амжилттай ажиллана.
- `app` layer `adapters` import хийхгүй.
- `domain` layer independent байна.
- Хамгийн багадаа 2 unit test байна.

## Lab 05 / Phase 1 Project - Student Thesis Management System

Эх сурвалж: `Lab_05_Project_Example.txt`

Гол зорилго:
- Java Swing + JDBC/H2 ашигласан thesis management desktop application хийх.
- Hexagonal Architecture + Scrum process ашиглах.

Функциональ шаардлага:
- Thesis list table харуулах.
- Thesis detail form харуулах.
- CRUD: create, update, delete.
- Workflow actions: submit, approve, reject.
- Reject хийхэд reason шаардлагатай.
- Error handling dialog/status байдлаар ойлгомжтой харагдана.
- DB operation-ууд Event Dispatch Thread дээр ажиллахгүй, `SwingWorker` ашиглана.
- Use case эсвэл presenter level basic unit tests байна.

Domain шаардлага:
- `Thesis` талбарууд: `id`, `title`, `studentId`, optional `supervisorId`, `status`, `rejectReason`.
- `ThesisStatus`: `DRAFT`, `SUBMITTED`, `APPROVED`, `REJECTED`.
- Workflow rule-ууд core дотор enforced байна:
  - `DRAFT -> SUBMITTED`
  - `SUBMITTED -> APPROVED`
  - `SUBMITTED -> REJECTED`
  - Reject reason хоосон байж болохгүй.

Architecture шаардлага:
- Core нь Swing, JDBC, DAO import хийхгүй.
- Swing нь inbound adapter байна.
- DAO/JDBC нь outbound adapter байна.
- UI class дотор DAO шууд ашиглахгүй.
- Existing DAO-г шинээр rebuild хийхгүй, repository adapter/wrapper-ээр ашиглана.

UI шаардлага:
- Main window: table, form fields, buttons.
- Buttons: Create, Update, Delete, Refresh, Submit, Approve, Reject.
- Workflow button-ууд selected status-аас хамаарч enable/disable болно.

Deliverables:
- Source code / Git repository.
- README: build/run, H2/MySQL config, architecture overview.
- Scrum artifacts: product backlog, sprint backlog, retrospective notes.
- 3-5 минутын demo: list, create, update, delete, submit, approve, reject, responsive UI.

## Lab 06 - Swing GUI Basics

Эх сурвалж: `Lab_06_Acceptance_Criteria_for_Students.txt`, `Lab_06_Swing_GUI_Basics_Read_Create.txt`, `Lab_06_Starter.txt`, `Lab_06_tms_Structure.txt`

Гол зорилго:
- Existing core/H2 project дээр Swing primary adapter холбох.
- Read болон Create workflow ажиллуулах.

Шаардлага:
- `Main.java` дээр service/repository-г `RepositoryFactory` эсвэл `ServiceFactory`-аар авна.
- UI class дотор `new JdbcThesisRepository()` гэх мэт repository instantiation хийхгүй.
- Service-г `MainFrame` рүү constructor injection-аар дамжуулна.
- `ThesisTableModel` нь `AbstractTableModel` extend хийнэ.
- `getValueAt()` нь Java Record accessor syntax ашиглана, жишээ нь `thesis.title()`.
- App start хийхэд H2 database-ийн existing rows `JTable` дээр шууд харагдана.
- Form нь `Title`, `Score` input талбар болон `Save` button-той байна.
- `Save` button listener input уншиж, score-г `Double` болгон safe parse хийнэ.
- Service-ийн create/save method дуудна.
- Save амжилттай бол input fields clear хийгдэж, table reload эсвэл `fireTableDataChanged()` дуудагдана.

Structure зөвлөмж:
- `core/domain`, `core/application`, `core/ports`
- `infrastructure/config`, `infrastructure/persistence`
- `ui`
- `database.properties`-ийг `.gitignore` дотор оруулна.

## Lab 07 - Advanced CRUD & Defensive UI

Эх сурвалж: `Lab_07_Acceptance_Criteria_for_Students.txt`, `Lab_07_Advanced_CRUD_Defensive_UI.txt`

Гол зорилго:
- Swing UI дээр update/delete, master-detail navigation, defensive error handling нэмэх.

Шаардлага:
- `JTable` row click хийхэд form fields автоматаар populate болно.
- `ListSelectionListener`-ийг table selection model дээр холбоно.
- Selected record ID-г class variable-д хадгална, жишээ нь `private Long selectedThesisId`.
- Row сонгосны дараа `Save` button text `Update` болж өөрчлөгдөнө.
- Delete дарахад `JOptionPane.showConfirmDialog` confirmation харуулна.
- User `No` эсвэл `Cancel` дарвал юу ч хийхгүй.
- User `Yes` дарвал service-ээр delete хийж, form clear, `selectedThesisId = null`, table refresh хийнэ.
- Save/Update logic бүхэлдээ `try-catch` дотор байна.
- `selectedThesisId == null` бол create, set байвал update ажиллана.
- Validation error, жишээ нь `IllegalArgumentException`, `WARNING_MESSAGE` dialog-аар харагдана.
- General error, жишээ нь `Exception` эсвэл `SQLException`, `ERROR_MESSAGE` dialog-аар харагдана.

## Lab 08 - Web Adapters: Servlets & JSP

Эх сурвалж: `Lab_08_Sprint_08_Backlog_Web_Adapters_Servlets_JSP.txt`, `Lab_08_Sprint_08_Starter_Kit.txt`

Гол зорилго:
- Swing primary adapter-ийг web adapter-аар сольж, Servlet + JSP ашиглах.
- Hexagonal core огт өөрчлөгдөхгүй.

Шаардлага:
- `pom.xml` дотор `jakarta.servlet-api` dependency нэмнэ.
- Servlet API dependency scope нь `provided` байна.
- Tomcat server IDE дээр configured, project deploy алдаагүй байна.
- `adapter.primary.web` package дотор `ThesisServlet` үүсгэнэ.
- `ThesisServlet` нь `HttpServlet` extend хийнэ.
- `@WebServlet("/theses")` annotation ашиглана.
- `init()` дотор service/repository-г factory-аар авна.
- `doGet()` нь thesis list уншиж request attribute-д хийж JSP рүү forward хийнэ.
- `src/main/webapp/WEB-INF/views/theses.jsp` үүсгэнэ.
- JSP нь HTML table structure-тэй байна.
- JSP request attribute-аас list авч Java loop-оор render хийнэ.
- Java Record accessor ашиглана, жишээ нь `t.title()`.

DoD:
- Tomcat start хийнэ.
- Browser дээр `/theses` нээнэ.
- H2 database-ийн өмнөх thesis data table дээр харагдана.
- Core package дотор нэг ч мөр өөрчлөхгүй.

## Lab 09 - Web Forms & Full CRUD

Эх сурвалж: `Lab_09_Backlog_Web_Forms_Full_CRUD_Sprint_09_Backlog_Starter_Kit.txt`

Гол зорилго:
- Web adapter дээр create/delete mutation хийх.
- HTML form, HTTP POST, PRG pattern ашиглах.

Шаардлага:
- `theses.jsp` дотор `<form action="theses" method="POST">` нэмнэ.
- Input талбар бүр `name` attribute-тэй байна.
- Submit button `<button type="submit">` байна.
- `ThesisServlet` нь `doPost()` override хийнэ.
- Request parameter-үүдийг `request.getParameter(...)`-аар авна.
- Score/string numeric input-ийг safe parse хийж `NumberFormatException` handle хийнэ.
- Service-ийн create/save method дуудна.
- Save амжилттай бол `response.sendRedirect("theses")` ашиглаж PRG pattern хэрэгжүүлнэ.
- Delete хийхдээ GET link ашиглахгүй.
- Table row бүр дээр `method="POST"` бүхий mini-form байна.
- Hidden input-аар `thesisId` дамжуулна.
- `/delete-thesis` mapped servlet үүсгэнэ.

DoD:
- Web form-оор thesis create хийхэд table шууд шинэчлэгдэнэ.
- F5 дарахад duplicate үүсэхгүй.
- Delete button record устгана.
- Swing desktop app дээр web-ээр хийсэн өөрчлөлт харагдаж байвал ports/adapters reuse батлагдана.

## Lab 10 - Secure Authentication

Эх сурвалж: `Lab_10_Sprint_10_Backlog_Secure_Authentication_Starter_Kit.txt`

Гол зорилго:
- Session management ашиглан web app хамгаалах.

Шаардлага:
- `login.jsp` үүсгэж username/password POST form хийнэ.
- `LoginServlet` POST request handle хийнэ.
- Valid credential бол session дотор user хадгална.
- Invalid credential бол login page рүү error flag-тай redirect хийнэ.
- `DashboardServlet` session дээр user/loggedInUser attribute байгаа эсэхийг шалгана.
- User session байхгүй бол `/login` рүү redirect хийж method execution-ийг `return`-ээр зогсооно.
- Authenticated user л dashboard JSP харах боломжтой.
- Navigation дээр logout link нэмнэ.
- `LogoutServlet` session invalidate хийгээд login page рүү redirect хийнэ.

DoD:
- Incognito window-оор dashboard руу шууд ороход login рүү буцна.
- Зөв credential-оор login хийж dashboard харна.
- Logout хийсний дараа browser back/refresh хийсэн ч dashboard харагдахгүй, login рүү буцна.

## Lab 11 - RESTful API Implementation

Эх сурвалж: `Lab_11_Sprint_11_Backlog_RESTful_API_Implementation_Starter_Kit.txt`

Гол зорилго:
- HTML view-ээс тусдаа REST JSON API гаргах.

Шаардлага:
- `jackson-databind` dependency нэмнэ.
- `adapter.primary.api` package үүсгэнэ.
- Postman эсвэл адил API client ашиглана.
- `BookApiServlet` үүсгэж `/api/books/*` дээр map хийнэ.
- `doGet()` override хийнэ.
- Response content type `application/json`, encoding `UTF-8` байна.
- Core repository/service-ээс `List<Book>` уншина.
- `ObjectMapper.writeValueAsString()` ашиглан JSON болгож response-д бичнэ.
- `doPost()` override хийнэ.
- Request input stream-ээс JSON уншиж `ObjectMapper.readValue()`-аар domain object болгоно.
- Core repository/service-ээр save хийнэ.
- Success үед `201 Created` буцаана.
- Not found ID үед `404 Not Found` буцаана.
- Malformed JSON үед `400 Bad Request` буцаана.

DoD:
- `GET /api/books` JSON array буцаана.
- Valid JSON POST хийхэд `201 Created` буцаана.
- Existing биш ID-гаар GET хийхэд `404 Not Found` буцаана.

## Lab 12 / Project 1 - LibraryWeb Monolith

Эх сурвалж: `Lab_12_Sprint_12_Project_1_Backlog_LibraryWeb_Monolith_Starter_Kit.txt`

Гол зорилго:
- Week 1-12 хүртэлх ойлголтуудыг нэг secure, hexagonal monolith web app болгон нэгтгэх.

Architecture & Core:
- Core package нь `jakarta.servlet.*`, `java.sql.*`, `com.fasterxml.jackson.*` import хийхгүй.
- Domain model: `Book(id, title, author, isbn, isAvailable)`, `User(id, username, password, role)`.
- Primary/secondary port interface-үүд тодорхой байна.

Persistence:
- H2 database schema startup дээр SQL script-ээр үүснэ.
- `JdbcBookRepository` нь `BookRepository` implement хийнэ.
- SQL injection-с хамгаалж `PreparedStatement` ашиглана.

Security:
- Login JSP болон LoginServlet байна.
- Session creation `JSESSIONID`-ээр ажиллана.
- Dashboard болон API mutating routes, жишээ нь POST/DELETE, protected байна.
- Unauthenticated web user `/login` рүү redirect болно.
- Unauthenticated API request `401 Unauthorized` буцаана.
- JSP дээр user input render хийхдээ XSS хамгаалалттай, жишээ нь `<c:out>`, байна.

Primary adapters:
- Web UI нь list, add, delete book хийдэг байна.
- PRG pattern ашиглана.
- REST API:
  - `GET /api/books` -> `200 OK` + JSON array
  - `POST /api/books` -> JSON body авч book үүсгээд `201 Created`
- REST API domain object-ийг шууд serialize хийхгүй, `BookDTO` руу map хийнэ.

Quality:
- `web.xml` дотор global `<error-page>` тохируулж unhandled 500 error дээр friendly UI харуулна.
- Server-side validation: blank title/null author байж болохгүй.
- Validation fail үед UI error message эсвэл API `400 Bad Request` буцаана.

Submission:
- GitHub repo link өгнө.
- README дотор Tomcat start хийх, H2 console орох заавар байна.
- Branch strategy ашиглана: `main`, `feature/core-logic`, `feature/jdbc-adapter`, `feature/web-ui`, `feature/rest-api`.

## Sprint 13 - Dockerize the Monolith

Эх сурвалж: `Lab_Sprint_13_Backlog_Starter_Kit.txt`

Гол зорилго:
- LibraryWeb эсвэл өөрийн project-ийг Docker container болгон ажиллуулах.

Шаардлага:
- Docker Desktop/Docker Engine installed and running байна.
- `docker --version` ажиллана.
- `docker run hello-world` амжилттай ажиллана.
- `pom.xml` дотор `<packaging>war</packaging>` байна.
- `mvn clean package` ажиллаж `.war` artifact үүсгэнэ.
- Project root дотор `Dockerfile` үүсгэнэ.
- Base image: `tomcat:10.1-jdk17` эсвэл local JDK-тэй тохирох Tomcat image.
- `target/*.war`-ийг Tomcat `webapps/ROOT.war` рүү copy хийнэ.
- `docker build -t libraryweb-img .` ажиллана.
- `docker run -d -p 8080:8080 --name library-server libraryweb-img` ажиллана.
- Browser дээр `http://localhost:8080` нээгдэнэ.

Bonus:
- H2 file database container restart-ийн дараа хадгалагдах ёстой.
- JDBC URL `/app/db/...` рүү зааж volume mount ашиглана.

DoD:
- `docker ps` дээр container running харагдана.
- Local IDE Tomcat stop хийсэн ч browser дээр container app ажиллана.
- `docker stop library-server` хиймэгц website offline болно.

## Sprint 14 - Splitting the Monolith & Docker Compose

Эх сурвалж: `Sprint_14_Backlog_Splitting_the_Monolith_Docker_Compose_Starter_Kit.txt`

Гол зорилго:
- LibraryWeb monolith-ийг `book-service` болон `frontend-service` болгон салгаж Docker Compose-оор удирдах.

Шаардлага:
- `book-service` folder/project үүсгэнэ.
- Book domain logic, repository, `BookApiServlet`-ийг backend service рүү migrate хийнэ.
- `book-service` Dockerfile үүсгэнэ.
- `GET /api/books` JSON буцаадаг байна.
- `frontend-service` folder/project үүсгэнэ.
- JSP files болон UI servlets-ийг frontend service рүү migrate хийнэ.
- Frontend servlet core use case шууд дуудахгүй.
- Frontend нь HTTP request-ээр `http://book-service:8080/api/books` API-г дуудна.
- `frontend-service` Dockerfile үүсгэнэ.
- Parent root дотор `docker-compose.yml` үүсгэнэ.
- Compose дотор `book-service`, `frontend-service` хоёр service тодорхойлно.
- Host port `8080` зөвхөн frontend-service рүү map хийнэ.
- `book-service` host-д шууд expose хийхгүй, internal Docker network-аар ашиглана.

Bonus:
- `postgres:15-alpine` image ашигласан `db` service нэмнэ.
- `POSTGRES_USER`, `POSTGRES_PASSWORD` environment variable ашиглана.
- `book-service` нь `db` дээр `depends_on` хийнэ.
- JDBC URL `jdbc:postgresql://db:5432/postgres` рүү шилжинэ.

DoD:
- `docker-compose up -d --build` ажиллана.
- `localhost:8080` дээр frontend UI load болно.
- Frontend нь book-service-тэй internal Docker network-аар холбогдож book list харуулна.
- `docker-compose down` хийж бүх system унтарна.

## Sprint 14 Bonus - Thesis Management App + PostgreSQL Compose

Эх сурвалж: `Sprint_14_Backlog_Bonus_Splitting_the_Monolith_Docker_Compose_Starter_Kit_Thesis_Management.txt`

Гол зорилго:
- Thesis app container болон PostgreSQL database container-ийг Docker Compose-оор хамт ажиллуулах.

Шаардлага:
- `JdbcThesisRepository` эсвэл application properties нь `DB_URL`, `DB_USER`, `DB_PASSWORD` environment variable уншдаг байна.
- Root дотор `docker-compose.yml` үүсгэнэ.
- `thesis-db` Postgres service болон `thesis-app` custom Dockerfile service тодорхойлно.
- Postgres volume mount хийж data persistence хангана.
- `init.sql` script-ийг Postgres container руу map хийж first boot дээр `theses` table үүсгэнэ.
- `docker-compose up -d` хоёр container-ийг амжилттай start хийнэ.
- `thesis-app` нь `thesis-db` ready болохыг хүлээж амжилттай connect хийнэ.
- Thesis үүсгээд containers down/up хийсний дараа data хэвээр байна.

## Sprint 15 - GitLab CI/CD Pipeline Setup

Эх сурвалж: `Sprint_15_Backlog_GitLab_CI_and_CD_Pipeline_Setup_Starter_Kit_01.txt`

Гол зорилго:
- LibraryWeb project-ийн build, test, package, Docker image creation/push процессыг GitLab CI/CD-аар automate хийх.

Шаардлага:
- Project root дотор яг `.gitlab-ci.yml` нэртэй файл үүсгэнэ.
- Stages: `compile`, `test`, `package`, `dockerize`.
- Push хийсний дараа GitLab UI дээр pipeline triggered болохыг шалгана.
- `build-java` job compile stage дээр `mvn clean compile` ажиллуулна.
- `test-java` job test stage дээр `mvn test` ажиллуулна.
- `package-app` job package stage дээр `mvn package` ажиллуулна.
- `package-app` нь `target/*.war` artifact хадгална, expire time тохируулна.
- `build-and-push-docker` job dockerize stage дээр Docker-in-Docker ашиглана.
- GitLab registry рүү predefined variables ашиглан login хийнэ.
- Docker image build/tag хийнэ: `$CI_REGISTRY/$CI_PROJECT_PATH/libraryweb:latest`.
- Image registry рүү push хийнэ.
- Docker push job зөвхөн `main` branch дээр ажиллах `rules`-тэй байна.
- Feature branch дээр intentionally failing JUnit test push хийж pipeline fail болдгийг батална.
- Test fix хийсний дараа pipeline green болохыг батална.

DoD:
- GitLab Build -> Pipelines дээр main branch pipeline бүрэн green байна.
- `package-app` job artifact `.war` download боломжтой байна.
- Deploy -> Container Registry дээр `libraryweb:latest` image upload болсон байна.

## Sprint 15 Bonus - Thesis Management CI/CD

Эх сурвалж: `Sprint_15_Backlog_Bonus_GitLab_CI_CD_Pipeline_Setup_Thesis_Management.txt`

Гол зорилго:
- Thesis Management Docker image-д зориулж compile/test/package pipeline хийх.

Шаардлага:
- `.gitlab-ci.yml` project root дотор үүсгэнэ.
- Stages: `test`, `build`, `dockerize`.
- Test stage: `mvn test`, жишээ нь `CreateThesisServiceTest`.
- Build stage: `mvn package` ажиллуулж `.war` artifact хадгална.
- Dockerize stage: Dockerfile valid эсэхийг шалгаж Docker image build хийнэ.
- Main branch руу push хийхэд pipeline автоматаар trigger болно.
- Failing test оруулахад pipeline test stage дээр fail болно.
- Successful pipeline green checkmark харуулж `.war` artifact хадгална.

## Project III - Microservices Architecture

Эх сурвалж: `Project_III_Microservices_Architecture_Splitting_the_Monolith_en.txt`

Гол зорилго:
- Monolithic Thesis Management System-ийг дор хаяж 3 independent microservice болгон салгах.
- Service бүр өөрийн database-тэй, independently deployable байна.

Thesis Service:
- `thesis-service` project үүсгэнэ.
- Existing hexagonal thesis architecture-ийг service рүү шилжүүлнэ.
- Student name plain text хадгалахгүй, зөвхөн `studentId` reference хадгална.
- Own database, жишээ нь `thesis_db`, тохируулна.
- REST endpoints: `POST /theses`, `GET /theses/{id}`.

Student Service:
- `student-service` project үүсгэнэ.
- Domain: `id`, `firstName`, `lastName`, `email`, `matriculationNumber`.
- Own database, жишээ нь `student_db`, тохируулна.
- REST endpoint: `GET /students/{id}`.
- Thesis үүсгэх үед `thesis-service` нь synchronous HTTP call-аар student exists эсэхийг шалгана.

Notification Service:
- `notification-service` project үүсгэнэ.
- Submitted/approved event дээр notification илгээх logic хийнэ.
- Simple email log database байж болно.
- Communication:
  - synchronous REST option, эсвэл
  - advanced asynchronous event/message broker option.

DevOps:
- Service бүрт тусдаа `Dockerfile` байна.
- Root directory дотор central `docker-compose.yml` байна.
- All services + databases Compose-оор orchestration хийнэ.
- Services internal Docker network-аар бие биетэйгээ харилцана.

Submission acceptance criteria:
- 3+ runnable, isolated microservices байна.
- Central monolithic DB байхгүй, service бүр өөрийн database/storage-тэй байна.
- Hexagonal Architecture service бүрийн дотор хадгалагдана.
- Дор хаяж нэг service өөр service-ээс data авдаг эсвэл action trigger хийдэг байна.
- `docker-compose up --build` нэг command-аар ecosystem бүхэлдээ асна.
- Instructors API test хийх Postman collection эсвэл curl scripts байна.
