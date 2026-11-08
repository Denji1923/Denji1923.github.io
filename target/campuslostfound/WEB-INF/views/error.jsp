<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Something went wrong">
    <div class="card center">
        <h2 class="card-title">Oops!</h2>
        <c:choose>
            <c:when test="${not empty errorMessage}">
                <%-- message from a LostFoundException (for example: MySQL is not running) --%>
                <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
                <p>If this says it cannot connect to MySQL: start MySQL in XAMPP, check
                   <code>src/main/resources/db.properties</code>, then reload this page.</p>
            </c:when>
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 404}">
                <div class="alert alert-error">That page was not found.</div>
            </c:when>
            <c:when test="${requestScope['javax.servlet.error.status_code'] == 403}">
                <div class="alert alert-error"><c:out value="${requestScope['javax.servlet.error.message']}"/></div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-error">Something went wrong on the server. Please try again.</div>
            </c:otherwise>
        </c:choose>
        <a class="btn" href="<c:url value='/'/>">Back to Home</a>
    </div>
</t:layout>
