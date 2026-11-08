<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Approve / Reject Items" active="items">
    <div class="card">
        <h2 class="card-title">Approve / Reject Items</h2>

        <form class="filters" method="get" action="<c:url value='/admin/items'/>">
            <label for="status" style="margin:0;">Show:</label>
            <select id="status" name="status" onchange="this.form.submit()">
                <option value="">All</option>
                <option value="PENDING"${statusFilter == 'PENDING' ? ' selected' : ''}>Pending</option>
                <option value="APPROVED"${statusFilter == 'APPROVED' ? ' selected' : ''}>Approved</option>
                <option value="REJECTED"${statusFilter == 'REJECTED' ? ' selected' : ''}>Rejected</option>
            </select>
            <noscript><button type="submit" class="btn btn-small">Show</button></noscript>
            <a class="btn" href="<c:url value='/admin/items'><c:param name='status' value='${statusFilter}'/><c:param name='export' value='csv'/></c:url>">Export CSV</a>
        </form>

        <div class="table-wrap">
            <table class="data-table">
                <thead><tr><th>Item</th><th>Reported By</th><th>Category</th><th>Picture</th><th>Description</th><th>Location</th><th>Date</th><th>Type</th><th>Approval</th><th>Actions</th></tr></thead>
                <tbody>
                <c:forEach var="i" items="${rows}">
                    <tr>
                        <td><c:out value="${i.name}"/></td>
                        <td><c:out value="${i.reporterName}"/></td>
                        <td><c:out value="${i.category}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${i.hasAttachment}">
                                    <c:url var="pic" value="/admin/attachment"><c:param name="id" value="${i.id}"/></c:url>
                                    <a href="${pic}" target="_blank" rel="noopener" class="thumb-link" data-full="${pic}"
                                       data-title="<c:out value='${i.name}'/>" title="Click to view the full picture">
                                        <img class="thumb" src="${pic}" alt="Picture of <c:out value='${i.name}'/>" loading="lazy">
                                    </a>
                                </c:when>
                                <c:otherwise><span class="muted">No picture</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${i.description}"/></td>
                        <td><c:out value="${i.location}"/></td>
                        <td class="nowrap">${i.dateOccurred}</td>
                        <td><span class="badge badge-${i.type.cssClass}">${i.type.label}</span></td>
                        <td><span class="badge badge-${i.approval.cssClass}">${i.approval.label}</span></td>
                        <td class="actions">
                            <form class="inline-form" method="post" action="<c:url value='/admin/items'/>">
                                <input type="hidden" name="_csrf" value="${csrfToken}">
                                <input type="hidden" name="id" value="${i.id}">
                                <input type="hidden" name="status" value="${statusFilter}">
                                <c:if test="${not i.approval.approved}">
                                    <button type="submit" name="action" value="approve" class="btn btn-small btn-green">Approve</button>
                                </c:if>
                                <c:if test="${not i.approval.rejected}">
                                    <button type="submit" name="action" value="reject" class="btn btn-small btn-red">Reject</button>
                                </c:if>
                                <button type="submit" name="action" value="delete" class="btn btn-small btn-gray"
                                        onclick="return confirm('Delete this item permanently?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <c:if test="${empty rows}"><div class="empty">No records found.</div></c:if>
        </div>
    </div>

    <%-- full-size picture viewer --%>
    <div id="lightbox" class="lightbox" hidden role="dialog" aria-modal="true" aria-label="Attached picture">
        <div class="lightbox-bar">
            <span id="lightbox-title"></span>
            <a id="lightbox-open" class="btn btn-small" href="#" target="_blank" rel="noopener">Open original</a>
            <button type="button" id="lightbox-close" class="btn btn-small btn-red">Close &#10005;</button>
        </div>
        <img id="lightbox-img" alt="Full-size attached picture">
    </div>
    <script>
        (function () {
            var box = document.getElementById('lightbox');
            var img = document.getElementById('lightbox-img');
            function close() { box.hidden = true; img.removeAttribute('src'); document.body.classList.remove('no-scroll'); }
            document.querySelectorAll('.thumb-link').forEach(function (a) {
                a.addEventListener('click', function (e) {
                    e.preventDefault();
                    img.src = a.dataset.full;
                    document.getElementById('lightbox-title').textContent = a.dataset.title || '';
                    document.getElementById('lightbox-open').href = a.dataset.full;
                    box.hidden = false;
                    document.body.classList.add('no-scroll');
                });
            });
            document.getElementById('lightbox-close').addEventListener('click', close);
            box.addEventListener('click', function (e) { if (e.target === box || e.target === img) close(); });
            document.addEventListener('keydown', function (e) { if (e.key === 'Escape' && !box.hidden) close(); });
        })();
    </script>
</t:layout>
