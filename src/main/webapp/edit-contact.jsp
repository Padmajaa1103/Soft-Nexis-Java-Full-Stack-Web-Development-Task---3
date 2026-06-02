<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Edit Contact</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/mini.css/3.0.1/mini-default.min.css"/>
</head>
<body>
<div class="container">
    <header>
        <h1>Edit Contact</h1>
    </header>
    <section>
        <c:if test="${not empty errorMessage}">
            <div class="card error">
                <strong>Error:</strong> ${errorMessage}
            </div>
        </c:if>
        <form action="contacts" method="post">
            <input type="hidden" name="action" value="update"/>
            <input type="hidden" name="id" value="${contact.id}"/>
            <div class="row responsive-label">
                <div class="col-sm-12">
                    <label for="name">Name *</label>
                    <input id="name" name="name" type="text" value="${contact.name}" required/>
                </div>
                <div class="col-sm-12">
                    <label for="email">Email *</label>
                    <input id="email" name="email" type="email" value="${contact.email}" required/>
                </div>
                <div class="col-sm-12">
                    <label for="phone">Phone</label>
                    <input id="phone" name="phone" type="text" value="${contact.phone}"/>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <button type="submit" class="primary">Update Contact</button>
                    <a class="secondary" href="contacts">Cancel</a>
                </div>
            </div>
        </form>
    </section>
</div>
</body>
</html>
