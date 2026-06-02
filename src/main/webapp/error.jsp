<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Error</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/mini.css/3.0.1/mini-default.min.css"/>
</head>
<body>
<div class="container">
    <header>
        <h1>Something went wrong</h1>
    </header>
    <section>
        <div class="card error">
            <c:choose>
                <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
                <c:otherwise>An unexpected error occurred.</c:otherwise>
            </c:choose>
            <c:if test="${not empty exception}">
                <pre>${exception.message}</pre>
            </c:if>
        </div>
        <a class="button" href="index.jsp">Return to Home</a>
    </section>
</div>
</body>
</html>
