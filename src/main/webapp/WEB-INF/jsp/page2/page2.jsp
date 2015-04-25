<!DOCTYPE html>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html lang="en">

<jsp:include page="../fragments/staticFiles.jsp"/>

<body>
<div class="container">
    <jsp:include page="../fragments/banner.jsp"/>
    <jsp:include page="../fragments/mainNavigation.jsp"/>
    <h2><fmt:message key="welcome"/></h2>
    <p>This is page 2</p>
    <jsp:include page="../fragments/footer.jsp"/>

</div>
</body>

</html>
