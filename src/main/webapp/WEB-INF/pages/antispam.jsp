<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="common/header.jsp"/>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">

    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="profile">300gramm</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="profile">Profile</a></li>
                <li><a href="requests">Requests</a></li>
                <li class="active"><a href="antispam">Antispam</a></li>
                <li><a href="logout">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Anti-Spam settings</h1>
        </div>


        <form method="post" action="requests">
            <div class="checkbox">
                <label>
                    <input type="hidden" id="_chbox_1" name="isAntiSpamEnabled"/>
                    <input type="checkbox" id="chbox_1" name="isAntiSpamEnabled" value="true" ${isAntiSpamEnabled}/>
                    Anti-spam enabled
                </label>
            </div>


            <div class="form-group">
                <label for="badWords">Bad words:</label>
                <textarea class="form-control" rows="3" id="badWords"></textarea>
            </div>

            <button type="button" class="btn btn-primary" name="saveAntiSpam">Save</button>
        </form>
        <hr>
    </div>
    <hr>

<jsp:include page="common/footer.jsp"/>