<!DOCTYPE html> 

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html lang="en">

<jsp:include page="fragments/staticFiles.jsp"/>

<body>
<div class="container">
    <jsp:include page="fragments/banner.jsp"/>
    <jsp:include page="fragments/mainNavigation.jsp"/>

    <h2><fmt:message key="welcome"/></h2>

    <p>Hello <b>${user.username}</b></p>
    <p><img src="${user.profile_picture}" class="img-thumbnail"/></p>

    <p>This is your info:</p>
    <table class="table">
        <tbody>
        <tr class="success">
            <td>Username:</td>
            <td>${user.username}</td>
        </tr>
        <tr class="danger">
            <td>Full name:</td>
            <td>${user.fullName}</td>
        </tr>
        <tr class="info">
            <td>Website:</td>
            <td>${user.website}</td>
        </tr>
        </tbody>
    </table>

    <jsp:include page="fragments/footer.jsp"/>

</div>
</body>

</html>
