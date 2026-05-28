# Eclipse Setup

## Import

Use Maven import:

```text
File -> Import -> Maven -> Existing Maven Projects
```

Select the `room-booking-web` folder.

## Expected Eclipse Structure

```text
room-booking-web
  Java Resources
    src/main/java
    src/main/resources
    src/test/java
  src/main/webapp
  Maven Dependencies
  JRE System Library [JavaSE-17]
  Apache Tomcat v10.1
  pom.xml
```

This is the Maven version of the old Eclipse `WebContent` layout:

```text
src/main/webapp = WebContent
```

## Required Eclipse Settings

1. Install or use `Eclipse IDE for Enterprise Java and Web Developers`.
2. Add JDK 17:

```text
Window -> Preferences -> Java -> Installed JREs
```

3. Set compiler level:

```text
Window -> Preferences -> Java -> Compiler -> 17
```

4. Configure Tomcat 10.1:

```text
Window -> Preferences -> Server -> Runtime Environments -> Add -> Apache Tomcat v10.1
```

5. Update Maven project:

```text
Right click project -> Maven -> Update Project
```

## Run

1. Start PostgreSQL:

```powershell
docker compose up -d
```

2. Add project to Tomcat from the Servers view.
3. Start Tomcat.
4. Open:

```text
http://localhost:8080/room-booking/login
```
