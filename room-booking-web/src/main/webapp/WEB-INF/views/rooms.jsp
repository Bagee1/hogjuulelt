<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Өрөөнүүд | Өрөө захиалга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body class="${currentUser.admin ? 'admin-shell' : 'student-shell'}">
<jsp:include page="partials/nav.jsp"/>
<main class="page">
    <header class="page-header">
        <div>
            <p class="page-kicker">Өрөөнүүд</p>
            <h1>Өрөөнүүд</h1>
            <p>Захиалах боломжтой сургалтын өрөө, танхимууд.</p>
        </div>
        <c:if test="${not currentUser.admin}">
            <a class="button" href="${pageContext.request.contextPath}/bookings">Захиалга үүсгэх</a>
        </c:if>
    </header>

    <c:if test="${param.success == 'created'}">
        <div class="alert alert-success">Шинэ өрөө нэмэгдлээ.</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <c:if test="${currentUser.admin}">
        <section class="panel">
            <div class="booking-form-header">
                <div>
                    <p class="page-kicker">Админ үйлдэл</p>
                    <h2>Өрөө нэмэх</h2>
                </div>
            </div>
            <form method="post" action="${pageContext.request.contextPath}/rooms" class="room-create-form">
                <label>
                    Өрөөний нэр
                    <input name="name" placeholder="Жишээ: Lab 303" required>
                </label>
                <label>
                    Багтаамж
                    <input type="number" name="capacity" min="1" placeholder="30" required>
                </label>
                <label>
                    Байршил
                    <input name="location" placeholder="Main Building" required>
                </label>
                <button type="submit">Өрөө нэмэх</button>
            </form>
        </section>
    </c:if>

    <section class="room-list">
        <c:forEach var="room" items="${rooms}">
            <article class="card room-card">
                <div>
                    <h2><c:out value="${room.name}"/></h2>
                    <p><c:out value="${room.location}"/></p>
                </div>
                <div class="room-meta">
                    <span class="chip"><c:out value="${room.capacity}"/> суудал</span>
                    <span class="chip">Захиалах боломжтой</span>
                </div>
            </article>
        </c:forEach>
    </section>

    <div class="table-header">
        <h2>Өрөөний жагсаалт</h2>
    </div>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Нэр</th>
                <th>Багтаамж</th>
                <th>Байршил</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="room" items="${rooms}">
                <tr>
                    <td><c:out value="${room.name}"/></td>
                    <td><c:out value="${room.capacity}"/></td>
                    <td><c:out value="${room.location}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
