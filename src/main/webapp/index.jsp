<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Welcome">
    <div class="card center">
        <h2 class="welcome-title">Welcome</h2>
        <p class="subtitle">Select your portal</p>

        <div class="portals">
            <div class="portal-card">
                <div class="portal-icon">&#127891;</div>
                <div class="portal-name">Student</div>
                <a class="btn" href="<c:url value='/student/login'/>">Enter</a>
            </div>
            <div class="portal-card portal-admin">
                <div class="portal-icon">&#128737;</div>
                <div class="portal-name">Admin</div>
                <a class="btn" href="<c:url value='/admin/login'/>">Enter</a>
            </div>
        </div>
    </div>
</t:layout>
