<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
<img src="${banner}"/>

<div class="navbar" style="width: 900px;">
    <div class="navbar-inner">
        <ul class="nav">
            <li><a href="<spring:url value="/" htmlEscape="true" />"><i class="icon-home"></i>
                Home</a></li>
            <li><a href="<spring:url value="/page1.html" htmlEscape="true" />"><i
                    class="icon-th-list"></i> Page 1</a></li>
            <li><a href="<spring:url value="/page2.html" htmlEscape="true" />"><i
                    class="icon-th-list"></i> Page 2</a></li>
            <li><a href="<spring:url value="/page3.html" htmlEscape="true" />"><i
                    class="icon-th-list"></i> Page 3</a></li>
        </ul>
    </div>
</div>
	
