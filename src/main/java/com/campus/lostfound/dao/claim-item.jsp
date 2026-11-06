<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Claim Item" active="claim">
    <div class="card container-form">
        <h2 class="card-title">Claim Item</h2>
        <c:if test="${not empty error}"><div class="alert alert-error"><c:out value="${error}"/></div></c:if>

        <form method="post" action="<c:url value='/student/claim'/>">
            <input type="hidden" name="_csrf" value="${csrfToken}">

            <%-- ${param.item}: the Claim button on View Items opens this page with ?item=ID, which selects the item --%>
            <label for="item">Item:</label>
            <select id="item" name="item" required>
                <option value="">-- Select item --</option>
                <c:forEach var="i" items="${available}">
                    <option value="${i.id}"${param.item == i.id ? ' selected' : ''}><c:out value="${i.name} (${i.type.label} at ${i.location})"/></option>
                </c:forEach>
            </select>

            <label for="proof">Proof of ownership:</label>
            <textarea id="proof" name="proof" maxlength="1000" required
                      placeholder="Describe unique marks, contents, serial number, etc."><c:out value="${param.proof}"/></textarea>

            <button type="submit" class="btn" style="margin-top:18px;">Submit Claim</button>
        </form>
    </div>

    <div class="card">
        <h2 class="card-title">My Claims</h2>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Item</th><th>Proof</th><th>Submitted</th><th>Status</th></tr></thead>
                <tbody>
                <c:forEach var="c" items="${myClaimList}">
                    <tr>
                        <td><c:out value="${c.itemName}"/></td>
                        <td><c:out value="${c.proof}"/></td>
                        <td class="nowrap">${c.createdAtText}</td>
                        <td><span class="badge badge-${c.approval.cssClass}">${c.approval.label}</span></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty myClaimList}"><div class="empty">You have not claimed anything yet.</div></c:if>
        </div>
    </div>
</t:layout>
