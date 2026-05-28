<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="mn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Бүртгүүлэх | Өрөө захиалга</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body>
<main class="login-page login-page-student">
    <section class="login-aside">
        <div class="login-title">
            <div class="brand"><span class="brand-mark">ӨЗ</span> Өрөө захиалга</div>
            <p class="role-badge">Оюутны бүртгэл</p>
            <h1>Шинэ оюутан бүртгэнэ.</h1>
            <p>Бүртгүүлсний дараа шууд өөрийн захиалгын самбар руу орно.</p>
        </div>
    </section>

    <section class="login-main">
        <div class="login-box">
            <p class="page-kicker">Бүртгэл</p>
            <h1>Оюутан бүртгүүлэх</h1>
            <p>Нэвтрэх нэр, нууц үгээ үүсгэнэ.</p>

            <c:if test="${not empty error}">
                <div class="alert alert-error"><c:out value="${error}"/></div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/register">
                <label>
                    Нэвтрэх нэр
                    <input name="username" autocomplete="username" minlength="3" required>
                </label>
                <br>
                <label>
                    Нууц үг
                    <input name="password" type="password" autocomplete="new-password" minlength="3" required>
                </label>
                <br>
                <label>
                    Нууц үг давтах
                    <input name="confirmPassword" type="password" autocomplete="new-password" minlength="3" required>
                </label>
                <br>
                <button type="submit">Бүртгүүлэх</button>
            </form>

            <div class="credential-row single">
                <a class="credential credential-link" href="${pageContext.request.contextPath}/student-login">
                    <strong>Оюутнаар нэвтрэх</strong>
                    Аль хэдийн бүртгэлтэй бол эндээс орно
                </a>
            </div>
        </div>
    </section>
</main>
</body>
</html>
