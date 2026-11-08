<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Transactions" active="transactions">
    <div class="card">
        <h2 class="card-title">All Transactions</h2>

        <form class="filters" method="get" action="<c:url value='/admin/transactions'/>">
            <input type="search" name="q" placeholder="Search name, action or details" value="<c:out value='${keyword}'/>">
            <select name="role">
                <option value="">Everyone</option>
                <option value="student"${roleFilter == 'student' ? ' selected' : ''}>Students</option>
                <option value="admin"${roleFilter == 'admin' ? ' selected' : ''}>Admins</option>
            </select>
            <button type="submit" class="btn">Filter</button>
            <a class="btn" href="<c:url value='/admin/transactions'><c:param name='q' value='${keyword}'/><c:param name='role' value='${roleFilter}'/><c:param name='export' value='csv'/></c:url>">Export CSV</a>
        </form>

        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>#</th><th>Time</th><th>User</th><th>Role</th><th>Action</th><th>Details</th></tr></thead>
                <tbody>
                <c:forEach var="t" items="${rows}">
                    <tr>
                        <td>${t.id}</td>
                        <td class="nowrap">${t.createdAtText}</td>
                        <td><c:out value="${t.actorName}"/></td>
                        <td><span class="badge badge-${t.actorType}">${t.actorType}</span></td>
                        <td><c:out value="${t.action}"/></td>
                        <td><c:out value="${t.details}"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty rows}"><div class="empty">No records found.</div></c:if>
        </div>
    </div>
</t:layout>
