<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
  <ul class="nav navbar-nav">
    <li><a href="<spring:url value="/" htmlEscape="true" />">Home</a></li>
    <li><a href="<spring:url value="/page1" htmlEscape="true" />">Page 1</a></li>
    <li><a href="<spring:url value="/page2" htmlEscape="true" />">Page 1</a></li>
    <li><a href="<spring:url value="/logout" htmlEscape="true" />">Logout</a></li>
  </ul>
</div>