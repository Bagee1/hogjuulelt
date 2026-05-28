<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Самбар | Өрөө захиалга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body class="${currentUser.admin ? 'admin-shell' : 'student-shell'}">
<jsp:include page="partials/nav.jsp"/>
<main class="page">
    <header class="page-header">
        <div>
            <p class="page-kicker">Самбар</p>
            <h1>Тавтай морил, <c:out value="${currentUser.username}"/></h1>
            <p><c:out value="${currentUser.roleLabel}"/> хэсэг</p>
        </div>
        <c:if test="${not currentUser.admin}">
            <a class="button" href="${pageContext.request.contextPath}/bookings">Шинэ захиалга</a>
        </c:if>
    </header>

    <section class="metric-grid">
        <article class="metric">
            <span>Өрөө</span>
            <strong><c:out value="${roomCount}"/></strong>
        </article>
        <article class="metric">
            <span>${currentUser.admin ? 'Нийт захиалга' : 'Миний захиалга'}</span>
            <strong><c:out value="${bookingCount}"/></strong>
        </article>
        <article class="metric">
            <span>Хүлээгдэж буй</span>
            <strong><c:out value="${pendingCount}"/></strong>
        </article>
    </section>

    <section class="grid">
        <a class="card card-link" href="${pageContext.request.contextPath}/rooms">
            <div class="card-top">
                <h2>Өрөөнүүд</h2>
                <span class="card-arrow">></span>
            </div>
            <p>Багтаамж, байршил, захиалах боломжтой өрөөнүүд.</p>
        </a>
        <a class="card card-link" href="${pageContext.request.contextPath}/bookings">
            <div class="card-top">
                <h2>Миний захиалга</h2>
                <span class="card-arrow">></span>
            </div>
            <p>Хүсэлт, төлөв, татгалзсан шалтгаанаа хянана.</p>
        </a>
        <c:if test="${currentUser.admin}">
            <a class="card card-link" href="${pageContext.request.contextPath}/admin/bookings">
                <div class="card-top">
                    <h2>Админ хяналт</h2>
                    <span class="card-arrow">></span>
                </div>
                <p>Хүсэлтийг зөвшөөрөх, татгалзах, цагийн давхцлыг шалгах.</p>
            </a>
            <a class="card card-link" href="${pageContext.request.contextPath}/admin/audit-logs">
                <div class="card-top">
                    <h2>Үйлдлийн түүх</h2>
                    <span class="card-arrow">></span>
                </div>
                <p>Захиалгын төлөв өөрчлөгдсөн бүртгэлийг харах.</p>
            </a>
        </c:if>
    </section>

    <div class="table-header">
        <div>
            <h2>Сүүлийн үйлдлүүд</h2>
            <p>Энэ хэсэгт харагдах хамгийн сүүлийн захиалгууд.</p>
        </div>
    </div>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Өрөө</th>
                <th>Огноо</th>
                <th>Цаг</th>
                <th>Төлөв</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="booking" items="${recentBookings}">
                <tr>
                    <td class="strong-cell"><c:out value="${booking.roomName}"/></td>
                    <td><c:out value="${booking.bookingDate}"/></td>
                    <td><c:out value="${booking.startTime}"/> - <c:out value="${booking.endTime}"/></td>
                    <td><span class="status status-${booking.status}"><c:out value="${booking.statusLabel}"/></span></td>
                </tr>
            </c:forEach>
            <c:if test="${empty recentBookings}">
                <tr>
                    <td colspan="4" class="empty-state">Одоогоор захиалга алга.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
