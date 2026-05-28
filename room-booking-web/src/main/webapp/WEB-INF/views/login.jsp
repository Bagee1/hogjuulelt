<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${loginHeading}"/> | Өрөө захиалга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body>
<main class="login-page login-page-${loginMode}">
    <section class="login-aside">
        <div class="login-title">
            <div class="brand"><span class="brand-mark">ӨЗ</span> Өрөө захиалга</div>
            <p class="role-badge"><c:out value="${loginKicker}"/></p>
            <h1><c:out value="${loginTitle}"/></h1>
            <p><c:out value="${loginSubtitle}"/></p>
        </div>
    </section>

    <section class="login-main">
        <div class="login-box">
            <p class="page-kicker">Нууцлалтай нэвтрэх</p>
            <h1><c:out value="${loginHeading}"/></h1>
            <p>Доорх туршилтын эрхээр нэвтэрч болно.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}"/></div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}${loginPath}">
                <label>
                    Нэвтрэх нэр
                    <input name="username" autocomplete="username" placeholder="${usernameHint}" required>
                </label>
                <br>
                <label>
                    Нууц үг
                    <input name="password" type="password" autocomplete="current-password" required>
                </label>
                <br>
                <button type="submit">Нэвтрэх</button>
            </form>

            <div class="credential-row">
                <div class="credential">
                    <strong><c:out value="${demoTitle}"/></strong>
                    <c:out value="${demoCredentials}"/>
                </div>
                <a class="credential credential-link" href="${pageContext.request.contextPath}${alternateLoginPath}">
                    <strong><c:out value="${alternateLoginLabel}"/></strong>
                    Өөр нэвтрэх хэсэг рүү очих
                </a>
                <c:if test="${loginMode == 'student'}">
                    <a class="credential credential-link" href="${pageContext.request.contextPath}/register">
                        <strong>Бүртгүүлэх</strong>
                        Шинэ оюутны эрх үүсгэх
                    </a>
                </c:if>
            </div>
        </div>
    </section>
</main>
</body>
</html>
