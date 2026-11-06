<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Claims" active="claims">
    <div class="card">
        <h2 class="card-title">Claim Requests</h2>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Item</th><th>Claimed By</th><th>Proof</th><th>Submitted</th><th>Status</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="c" items="${rows}">
                    <tr>
                        <td><c:out value="${c.itemName}"/></td>
                        <td><c:out value="${c.studentName} (${c.studentNumber})"/></td>
                        <td><c:out value="${c.proof}"/></td>
                        <td class="nowrap">${c.createdAtText}</td>
                        <td><span class="badge badge-${c.approval.cssClass}">${c.approval.label}</span></td>
                        <td class="actions">
                            <c:if test="${c.approval.pending}">
                                <form class="inline-form" method="post" action="<c:url value='/admin/claims'/>">
                                    <input type="hidden" name="_csrf" value="${csrfToken}">
                                    <input type="hidden" name="id" value="${c.id}">
                                    <button type="submit" name="action" value="approve" class="btn btn-small btn-green">Approve</button>
                                    <button type="submit" name="action" value="reject" class="btn btn-small btn-red">Reject</button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty rows}"><div class="empty">No records found.</div></c:if>
        </div>
    </div>
</t:layout>
