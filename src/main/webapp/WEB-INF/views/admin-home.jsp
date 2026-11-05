<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Dashboard" active="home">
    <div class="card">
        <h2 class="card-title">Welcome, <c:out value="${sessionScope.currentUser.username}"/></h2>
        <p class="subtitle">Administrator dashboard</p>

        <div class="tiles">
            <c:forEach var="stat" items="${stats}" varStatus="s">
                <div class="stat-tile stat-${s.index % 4}">
                    <div class="stat-number">${stat.value}</div>
                    <div class="stat-caption"><c:out value="${stat.key}"/></div>
                </div>
            </c:forEach>
        </div>

        <h3 class="section-title">Latest Activity</h3>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Time</th><th>User</th><th>Role</th><th>Action</th><th>Details</th></tr></thead>
                <tbody>
                <c:forEach var="t" items="${latest}">
                    <tr>
                        <td class="nowrap">${t.createdAtText}</td>
                        <td><c:out value="${t.actorName}"/></td>
                        <td><span class="badge badge-${t.actorType}">${t.actorType}</span></td>
                        <td><c:out value="${t.action}"/></td>
                        <td><c:out value="${t.details}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty latest}"><div class="empty">No records found.</div></c:if>
        </div>
    </div>
</t:layout>
