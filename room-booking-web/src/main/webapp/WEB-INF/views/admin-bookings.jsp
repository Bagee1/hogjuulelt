<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Админ хяналт | Өрөө захиалга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body class="admin-shell">
<jsp:include page="partials/nav.jsp"/>
<main class="page">
    <header class="page-header">
        <div>
            <p class="page-kicker">Админ хэсэг</p>
            <h1>Захиалгын хяналт</h1>
            <p>Хүлээгдэж буй хүсэлтийг шалгаж, зөвшөөрөх эсвэл татгалзана.</p>
        </div>
        <a class="button button-secondary" href="${pageContext.request.contextPath}/admin/audit-logs">Үйлдлийн түүх</a>
    </header>

    <c:if test="${param.success == 'approved'}">
        <div class="alert alert-success">Захиалга зөвшөөрөгдлөө.</div>
    </c:if>
    <c:if test="${param.success == 'rejected'}">
        <div class="alert alert-success">Захиалга татгалзагдлаа.</div>
    </c:if>
    <c:if test="${param.success == 'updated'}">
        <div class="alert alert-success">Захиалгын мэдээлэл шинэчлэгдлээ.</div>
    </c:if>
    <c:if test="${param.success == 'cancelled'}">
        <div class="alert alert-success">Захиалга цуцлагдлаа.</div>
    </c:if>
    <c:if test="${param.success == 'deleted'}">
        <div class="alert alert-success">Захиалга устгагдлаа.</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Оюутан</th>
                <th>Өрөө</th>
                <th>Огноо</th>
                <th>Цаг</th>
                <th>Зорилго</th>
                <th>Төлөв</th>
                <th>Үйлдэл</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="booking" items="${bookings}">
                <tr>
                    <td class="strong-cell"><c:out value="${booking.studentUsername}"/></td>
                    <td><c:out value="${booking.roomName}"/></td>
                    <td><c:out value="${booking.bookingDate}"/></td>
                    <td><c:out value="${booking.startTime}"/> - <c:out value="${booking.endTime}"/></td>
                    <td><c:out value="${booking.purpose}"/></td>
                    <td>
                        <span class="status status-${booking.status}"><c:out value="${booking.statusLabel}"/></span>
                        <c:if test="${not empty booking.rejectReason}">
                            <br><small><c:out value="${booking.rejectReason}"/></small>
                        </c:if>
                    </td>
                    <td>
                        <div class="actions admin-actions">
                            <form method="post" action="${pageContext.request.contextPath}/admin/bookings" class="row-edit-form">
                                <input type="hidden" name="bookingId" value="${booking.id}">
                                <input type="hidden" name="action" value="update">
                                <select name="roomId" aria-label="Өрөө">
                                    <c:forEach var="room" items="${rooms}">
                                        <option value="${room.id}" ${room.id == booking.roomId ? 'selected' : ''}>
                                            <c:out value="${room.name}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                                <input type="date" name="bookingDate" value="${booking.bookingDate}" aria-label="Огноо" required>
                                <input type="time" name="startTime" value="${booking.startTime}" aria-label="Эхлэх цаг" required>
                                <input type="time" name="endTime" value="${booking.endTime}" aria-label="Дуусах цаг" required>
                                <input name="purpose" value="${fn:escapeXml(booking.purpose)}" aria-label="Зорилго" required>
                                <button type="submit" class="button-secondary">Засах</button>
                            </form>
                            <c:if test="${booking.status == 'PENDING'}">
                                <form method="post" action="${pageContext.request.contextPath}/admin/bookings">
                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                    <input type="hidden" name="action" value="approve">
                                    <button type="submit">Зөвшөөрөх</button>
                                </form>
                                <form method="post" action="${pageContext.request.contextPath}/admin/bookings" class="inline-form">
                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                    <input type="hidden" name="action" value="reject">
                                    <input name="rejectReason" placeholder="Татгалзах шалтгаан" required>
                                    <button type="submit" class="button-danger">Татгалзах</button>
                                </form>
                            </c:if>
                            <c:if test="${booking.status != 'CANCELLED'}">
                                <form method="post" action="${pageContext.request.contextPath}/admin/bookings" class="inline-form">
                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                    <input type="hidden" name="action" value="cancel">
                                    <input name="cancelReason" placeholder="Цуцлах шалтгаан">
                                    <button type="submit" class="button-secondary">Цуцлах</button>
                                </form>
                            </c:if>
                            <form method="post" action="${pageContext.request.contextPath}/admin/bookings">
                                <input type="hidden" name="bookingId" value="${booking.id}">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit" class="button-danger">Устгах</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty bookings}">
                <tr>
                    <td colspan="7" class="empty-state">Одоогоор захиалга алга.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
