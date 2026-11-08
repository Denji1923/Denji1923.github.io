<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Students" active="students">
    <div class="card">
        <h2 class="card-title">Registered Students</h2>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Student No.</th><th>Full Name</th><th>Reports</th><th>Claims</th></tr></thead>
                <tbody>
                <c:forEach var="r" items="${rows}">
                    <tr>
                        <td><c:out value="${r.studentNumber}"/></td>
                        <td><c:out value="${r.fullName}"/></td>
                        <td>${r.reports}</td>
                        <td>${r.claims}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty rows}"><div class="empty">No students have registered yet.</div></c:if>
        </div>
    </div>
</t:layout>
