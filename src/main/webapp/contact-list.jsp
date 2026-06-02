<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Contact List</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/mini.css/3.0.1/mini-default.min.css"/>
</head>
<body>
<div class="container">
    <header>
        <h1>Contacts</h1>
    </header>

    <section>
        <form action="contacts" method="get" class="row">
            <input type="hidden" name="action" value="search"/>
            <div class="col-sm-8">
                <input type="text" name="keyword" placeholder="Search by name or email" value="${searchQuery}"/>
            </div>
            <div class="col-sm-4">
                <button class="primary" type="submit">Search</button>
                <a class="secondary" href="contacts">Clear</a>
            </div>
        </form>

        <c:if test="${not empty param.success}">
            <div class="card success">${param.success}</div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="card error">${errorMessage}</div>
        </c:if>

        <table>
            <thead>
            <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Created At</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${contacts}" var="contact">
                <tr>
                    <td>${contact.name}</td>
                    <td>${contact.email}</td>
                    <td>${contact.phone}</td>
                    <td>${contact.createdAt}</td>
                    <td>
                        <a class="button small" href="contacts?action=edit&id=${contact.id}">Edit</a>
                        <form action="contacts" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete"/>
                            <input type="hidden" name="id" value="${contact.id}"/>
                            <button class="secondary small" type="submit" onclick="return confirm('Delete this contact?');">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty contacts}">
                <tr>
                    <td colspan="5">No contacts found.</td>
                </tr>
            </c:if>
            </tbody>
        </table>

        <a class="button primary" href="contacts?action=add">Add Contact</a>
    </section>
</div>
</body>
</html>
