<!DOCTYPE html>

<html lang="en">

<jsp:include page="fragments/staticFiles.jsp"/>

<body>
<div class="miniContainer">

    <jsp:include page="fragments/banner.jsp"/>
    <blockquote>
        <p>Please login:</p>
        <a href="${authorizationUrl}" class="btn btn-primary">Authorizate trow instagram</a>
    </blockquote>
</div>
</body>

</html>
