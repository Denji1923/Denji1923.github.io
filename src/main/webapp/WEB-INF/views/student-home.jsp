<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Home" active="home">
    <div class="card">
        <h2 class="card-title">Welcome, <c:out value="${sessionScope.currentUser.fullName}"/></h2>
        <p class="subtitle">Student No. <c:out value="${sessionScope.currentUser.studentNumber}"/></p>

        <div class="tiles">
            <div class="stat-tile stat-0"><div class="stat-number">${myReports}</div><div class="stat-caption">My Reports</div></div>
            <div class="stat-tile stat-1"><div class="stat-number">${pendingReports}</div><div class="stat-caption">Awaiting Approval</div></div>
            <div class="stat-tile stat-2"><div class="stat-number">${myClaims}</div><div class="stat-caption">My Claims</div></div>
        </div>

        <h3 class="section-title">Recently Posted Items</h3>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Item</th><th>Category</th><th>Location</th><th>Date</th><th>Type</th></tr></thead>
                <tbody>
                <c:forEach var="i" items="${recentItems}">
                    <tr>
                        <td><c:out value="${i.name}"/></td>
                        <td><c:out value="${i.category}"/></td>
                        <td><c:out value="${i.location}"/></td>
                        <td class="nowrap">${i.dateOccurred}</td>
                        <td><span class="badge badge-${i.type.cssClass}">${i.type.label}</span></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty recentItems}"><div class="empty">No records found.</div></c:if>
        </div>
    </div>
</t:layout>
