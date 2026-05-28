<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="topbar">
    <div class="brand"><span class="brand-mark">ӨЗ</span> Өрөө захиалга</div>
    <nav class="nav" aria-label="Үндсэн цэс">
        <a class="${pageContext.request.servletPath == '/dashboard' ? 'is-active' : ''}" href="${pageContext.request.contextPath}/dashboard">Самбар</a>
        <a class="${pageContext.request.servletPath == '/rooms' ? 'is-active' : ''}" href="${pageContext.request.contextPath}/rooms">Өрөөнүүд</a>
        <a class="${pageContext.request.servletPath == '/bookings' ? 'is-active' : ''}" href="${pageContext.request.contextPath}/bookings">Миний захиалга</a>
        <c:if test="${currentUser.admin}">
            <a class="${pageContext.request.servletPath == '/admin/bookings' ? 'is-active' : ''}" href="${pageContext.request.contextPath}/admin/bookings">Админ хяналт</a>
            <a class="${pageContext.request.servletPath == '/admin/audit-logs' ? 'is-active' : ''}" href="${pageContext.request.contextPath}/admin/audit-logs">Үйлдлийн түүх</a>
        </c:if>
        <span class="user-pill"><c:out value="${currentUser.username}"/> / <c:out value="${currentUser.roleLabel}"/></span>
        <form method="post" action="${pageContext.request.contextPath}/logout">
            <button type="submit">Гарах</button>
        </form>
    </nav>
</header>
