<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="View Items" active="items">
    <div class="card">
        <h2 class="card-title">Lost &amp; Found Items</h2>

        <form class="filters" method="get" action="<c:url value='/student/items'/>">
            <input type="search" name="q" placeholder="Search item, category or location" value="<c:out value='${keyword}'/>">
            <select name="type">
                <option value="">All</option>
                <option value="LOST"${typeFilter == 'LOST' ? ' selected' : ''}>Lost</option>
                <option value="FOUND"${typeFilter == 'FOUND' ? ' selected' : ''}>Found</option>
            </select>
            <button type="submit" class="btn">Search</button>
        </form>

        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Item</th><th>Category</th><th>Description</th><th>Location</th><th>Date</th><th>Type</th><th>Availability</th><th></th></tr></thead>
                <tbody>
                <c:forEach var="i" items="${items}">
                    <tr>
                        <td><c:out value="${i.name}"/></td>
                        <td><c:out value="${i.category}"/></td>
                        <td><c:out value="${i.description}"/></td>
                        <td><c:out value="${i.location}"/></td>
                        <td class="nowrap">${i.dateOccurred}</td>
                        <td><span class="badge badge-${i.type.cssClass}">${i.type.label}</span></td>
                        <td><span class="badge badge-${i.claimed ? 'claimed' : 'available'}">${i.availability}</span></td>
                        <td>
                            <c:if test="${not i.claimed}">
                                <a class="btn btn-small" href="<c:url value='/student/claim'><c:param name='item' value='${i.id}'/></c:url>">Claim</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty items}"><div class="empty">No records found.</div></c:if>
        </div>
    </div>

    <div class="card">
        <h2 class="card-title">My Reports</h2>
        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Item</th><th>Type</th><th>Location</th><th>Date</th><th>Approval</th></tr></thead>
                <tbody>
                <c:forEach var="i" items="${myItems}">
                    <tr>
                        <td><c:out value="${i.name}"/></td>
                        <td><span class="badge badge-${i.type.cssClass}">${i.type.label}</span></td>
                        <td><c:out value="${i.location}"/></td>
                        <td class="nowrap">${i.dateOccurred}</td>
                        <td><span class="badge badge-${i.approval.cssClass}">${i.approval.label}</span></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty myItems}"><div class="empty">You have not reported anything yet.</div></c:if>
        </div>
    </div>
</t:layout>
