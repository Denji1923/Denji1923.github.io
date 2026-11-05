<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:layout title="Report Item" active="report">
    <div class="card container-form">
        <h2 class="card-title">Report Lost/Found Item</h2>
        <c:if test="${not empty error}"><div class="alert alert-error"><c:out value="${error}"/></div></c:if>

        <form method="post" enctype="multipart/form-data" action="<c:url value='/student/report'/>">
            <input type="hidden" name="_csrf" value="${csrfToken}">

            <label for="name">Item Name:</label>
            <input type="text" id="name" name="name" maxlength="120" required value="<c:out value='${param.name}'/>">

            <label for="category">Category:</label>
            <select id="category" name="category" required>
                <option value="">-- Select category --</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="<c:out value='${cat}'/>"${param.category == cat ? ' selected' : ''}><c:out value="${cat}"/></option>
                </c:forEach>
            </select>

            <label for="description">Description:</label>
            <textarea id="description" name="description" maxlength="1000"><c:out value="${param.description}"/></textarea>

            <label for="location">Location:</label>
            <input type="text" id="location" name="location" maxlength="150" required value="<c:out value='${param.location}'/>">

            <label for="date">Date:</label>
            <input type="date" id="date" name="date" max="${today}" required value="${empty param.date ? today : param.date}">

            <label for="type">Status:</label>
            <select id="type" name="type">
                <option value="LOST"${param.type != 'FOUND' ? ' selected' : ''}>Lost</option>
                <option value="FOUND"${param.type == 'FOUND' ? ' selected' : ''}>Found</option>
            </select>

            <label for="attachment">Picture (optional):</label>
            <input type="file" id="attachment" name="attachment" accept="image/jpeg,image/png,image/gif,image/webp">
            <div class="hint">JPG, PNG, GIF or WEBP, up to 5 MB. A clear photo helps the admin verify the item.</div>
            <img id="attachment-preview" class="attach-preview" alt="Preview of the chosen picture" hidden>

            <button type="submit" class="btn" style="margin-top:18px;">Submit Report</button>
        </form>
    </div>
    <script>
        (function () {
            var input = document.getElementById('attachment');
            var preview = document.getElementById('attachment-preview');
            input.addEventListener('change', function () {
                preview.hidden = true;
                var file = input.files && input.files[0];
                if (!file) return;
                if (file.size > 5 * 1024 * 1024) {
                    alert('That picture is larger than 5 MB. Please choose a smaller one.');
                    input.value = '';
                    return;
                }
                if (!/^image\//.test(file.type)) {
                    alert('Please choose a picture file (JPG, PNG, GIF or WEBP).');
                    input.value = '';
                    return;
                }
                preview.src = URL.createObjectURL(file);
                preview.hidden = false;
            });
        })();
    </script>
</t:layout>
