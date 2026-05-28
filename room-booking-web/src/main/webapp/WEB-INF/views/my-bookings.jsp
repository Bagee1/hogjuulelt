<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Миний захиалга | Өрөө захиалга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body class="${currentUser.admin ? 'admin-shell' : 'student-shell'}">
<jsp:include page="partials/nav.jsp"/>
<main class="page">
    <header class="page-header">
        <div>
            <p class="page-kicker">Оюутны хэсэг</p>
            <h1>Миний захиалга</h1>
            <p>Өрөө захиалах хүсэлт үүсгээд хяналтын төлөвөө дагана.</p>
        </div>
    </header>

    <c:if test="${param.success == 'created'}">
        <div class="alert alert-success">Захиалгын хүсэлт илгээгдлээ.</div>
    </c:if>
    <c:if test="${param.success == 'updated'}">
        <div class="alert alert-success">Хүсэлт шинэчлэгдэж дахин батлуулахаар илгээгдлээ.</div>
    </c:if>
    <c:if test="${param.success == 'cancelled'}">
        <div class="alert alert-success">Хүсэлт цуцлагдлаа.</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <section class="panel booking-panel">
        <div class="booking-form-header">
            <div>
                <p class="page-kicker">Шинэ хүсэлт</p>
                <h2>Өрөө захиалах</h2>
            </div>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/bookings" class="booking-form-easy">
            <label class="booking-room-field">
                Өрөө сонгох
                <select name="roomId" required>
                    <c:forEach var="room" items="${rooms}">
                        <option value="${room.id}">
                            <c:out value="${room.name}"/> - <c:out value="${room.location}"/> / <c:out value="${room.capacity}"/> суудал
                        </option>
                    </c:forEach>
                </select>
            </label>
            <div class="booking-time-row">
                <label>
                    Огноо
                    <input type="date" name="bookingDate" min="${minBookingDate}" value="${defaultBookingDate}" required>
                </label>
                <label>
                    Эхлэх
                    <input type="time" name="startTime" value="${defaultStartTime}" required>
                </label>
                <label>
                    Дуусах
                    <input type="time" name="endTime" value="${defaultEndTime}" required>
                </label>
            </div>
            <label class="booking-purpose-field">
                Зорилго
                <input id="purposeInput" name="purpose" maxlength="255" placeholder="Жишээ: Багийн уулзалт" required>
            </label>
            <div class="quick-purpose">
                <button type="button" class="chip-button" data-purpose="Багийн уулзалт">Багийн уулзалт</button>
                <button type="button" class="chip-button" data-purpose="Давтлага">Давтлага</button>
                <button type="button" class="chip-button" data-purpose="Төслийн хэлэлцүүлэг">Төсөл</button>
            </div>
            <button type="submit" class="booking-submit">Хүсэлт илгээх</button>
        </form>
    </section>

    <div class="table-header">
        <h2>Миний хүсэлтүүд</h2>
    </div>
    <div class="table-wrap">
        <table>
            <thead>
            <tr>
                <th>Өрөө</th>
                <th>Огноо</th>
                <th>Цаг</th>
                <th>Зорилго</th>
                <th>Төлөв</th>
                <th>Татгалзсан шалтгаан</th>
                <th>Үйлдэл</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="booking" items="${bookings}">
                <tr>
                    <td class="strong-cell"><c:out value="${booking.roomName}"/></td>
                    <td><c:out value="${booking.bookingDate}"/></td>
                    <td><c:out value="${booking.startTime}"/> - <c:out value="${booking.endTime}"/></td>
                    <td><c:out value="${booking.purpose}"/></td>
                    <td><span class="status status-${booking.status}"><c:out value="${booking.statusLabel}"/></span></td>
                    <td><c:out value="${booking.rejectReasonText}"/></td>
                    <td>
                        <div class="actions student-actions">
                            <c:if test="${booking.status != 'APPROVED'}">
                                <form method="post" action="${pageContext.request.contextPath}/bookings" class="row-edit-form">
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
                                    <button type="submit" class="button-secondary">Дахин илгээх</button>
                                </form>
                            </c:if>
                            <c:if test="${booking.status != 'CANCELLED'}">
                                <form method="post" action="${pageContext.request.contextPath}/bookings" class="inline-form">
                                    <input type="hidden" name="bookingId" value="${booking.id}">
                                    <input type="hidden" name="action" value="cancel">
                                    <input name="cancelReason" placeholder="Цуцлах шалтгаан">
                                    <button type="submit" class="button-secondary">Цуцлах</button>
                                </form>
                            </c:if>
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
<script>
    document.querySelectorAll("[data-purpose]").forEach(function (button) {
        button.addEventListener("click", function () {
            document.getElementById("purposeInput").value = button.dataset.purpose;
        });
    });
</script>
</body>
</html>
