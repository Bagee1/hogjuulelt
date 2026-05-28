<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Үйлдлийн түүх | Өрөө захиалга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body class="admin-shell">
<jsp:include page="partials/nav.jsp"/>
<main class="page">
    <header class="page-header">
        <div>
            <p class="page-kicker">Observer бүртгэл</p>
            <h1>Үйлдлийн түүх</h1>
            <p>Захиалгын төлөв өөрчлөгдөх бүрд үүссэн бүртгэл.</p>
        </div>
        <a class="button button-secondary" href="${pageContext.request.contextPath}/admin/bookings">Хяналт руу буцах</a>
    </header>

    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Захиалга</th>
                <th>Мэдээлэл</th>
                <th>Үүссэн огноо</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="log" items="${logs}">
                <tr>
                    <td><c:out value="${log.id}"/></td>
                    <td><c:out value="${log.bookingId}"/></td>
                    <td><c:out value="${log.message}"/></td>
                    <td><c:out value="${log.createdAt}"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty logs}">
                <tr>
                    <td colspan="4" class="empty-state">Одоогоор үйлдлийн түүх алга.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
