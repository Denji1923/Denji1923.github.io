<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Student Login">
    <div class="container-narrow" style="margin:0 auto;">
        <div class="card">
            <div class="login-icon">&#127891;</div>
            <h2 class="card-title center">Student Login</h2>

            <c:if test="${not empty error}"><div class="alert alert-error"><c:out value="${error}"/></div></c:if>

            <form method="post" action="<c:url value='/student/login'/>">
                <input type="hidden" name="_csrf" value="${csrfToken}">

                <label for="studentNumber">Student Number</label>
                <input type="text" id="studentNumber" name="studentNumber" placeholder="25-22-081"
                       maxlength="9" required autofocus value="<c:out value='${param.studentNumber}'/>">

                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" placeholder="Enter Full Name"
                       maxlength="120" required value="<c:out value='${param.fullName}'/>">

                <button type="submit" class="btn btn-block">LOGIN</button>
            </form>
            <a class="back-link" href="<c:url value='/'/>">&larr; Back</a>
        </div>
    </div>
</t:layout>
