<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Admin Login">
    <div class="container-narrow" style="margin:0 auto;">
        <div class="card">
            <div class="login-icon">&#128737;</div>
            <h2 class="card-title center">Admin Login</h2>

            <c:if test="${not empty error}"><div class="alert alert-error"><c:out value="${error}"/></div></c:if>

            <form method="post" action="<c:url value='/admin/login'/>">
                <input type="hidden" name="_csrf" value="${csrfToken}">

                <label for="username">Username</label>
                <input type="text" id="username" name="username" placeholder="Enter Username"
                       maxlength="50" required autofocus value="<c:out value='${param.username}'/>">

                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter Password" required>

                <button type="submit" class="btn btn-block">LOGIN</button>
            </form>
            <a class="back-link" href="<c:url value='/'/>">&larr; Back</a>
        </div>
    </div>
</t:layout>
