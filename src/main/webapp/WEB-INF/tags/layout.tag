<%@ tag description="Shared page layout: banner, navigation bar, messages" pageEncoding="UTF-8" body-content="scriptless" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="active" required="false" %>
<%-- The navigation shown depends on who is logged in: Student, Administrator, or nobody (polymorphic getRole()). --%>
<c:set var="role" value="${sessionScope.currentUser.role}"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${title}"/> - Campus Lost &amp; Found</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/style.css'/>">
</head>
<body>
<header class="site-header">
    <div class="site-stars">&#9733; &#9733; &#9733;</div>
    <h1 class="site-title">Campus Lost &amp; Found System</h1>
</header>

<c:if test="${role == 'Student' || role == 'Administrator'}">
    <nav class="navbar">
        <span class="brand">Lost&amp;Found</span>
        <c:if test="${role == 'Administrator'}"><span class="brand-tag">ADMIN</span></c:if>
        <span class="nav-spacer"></span>

        <c:if test="${role == 'Student'}">
            <a class="nav-link${active == 'home' ? ' active' : ''}" href="<c:url value='/student/home'/>">Home</a>
            <a class="nav-link${active == 'report' ? ' active' : ''}" href="<c:url value='/student/report'/>">Report Item</a>
            <a class="nav-link${active == 'items' ? ' active' : ''}" href="<c:url value='/student/items'/>">View Items</a>
            <a class="nav-link${active == 'claim' ? ' active' : ''}" href="<c:url value='/student/claim'/>">Claim Item</a>
        </c:if>
        <c:if test="${role == 'Administrator'}">
            <a class="nav-link${active == 'home' ? ' active' : ''}" href="<c:url value='/admin/home'/>">Home</a>
            <a class="nav-link${active == 'transactions' ? ' active' : ''}" href="<c:url value='/admin/transactions'/>">Transactions</a>
            <a class="nav-link${active == 'items' ? ' active' : ''}" href="<c:url value='/admin/items'/>">Approve / Reject Items</a>
            <a class="nav-link${active == 'claims' ? ' active' : ''}" href="<c:url value='/admin/claims'/>">Claims</a>
            <a class="nav-link${active == 'students' ? ' active' : ''}" href="<c:url value='/admin/students'/>">Students</a>
        </c:if>

        <form class="logout-form" method="post" action="<c:url value='/logout'/>">
            <input type="hidden" name="_csrf" value="${csrfToken}">
            <button type="submit" class="nav-link nav-logout">Logout</button>
        </form>
    </nav>
</c:if>

<main class="container">
    <%-- one-time messages saved by a servlet before a redirect --%>
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert alert-success"><c:out value="${sessionScope.flashSuccess}"/></div>
        <c:remove var="flashSuccess" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-error"><c:out value="${sessionScope.flashError}"/></div>
        <c:remove var="flashError" scope="session"/>
    </c:if>

    <jsp:doBody/>
</main>
<footer class="site-footer">Campus Lost &amp; Found System</footer>
</body>
</html>
