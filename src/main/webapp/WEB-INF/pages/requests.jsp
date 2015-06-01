<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
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
            <a class="navbar-brand" href="#">300gramm</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="profile">Profile</a></li>
                <li class="active"><a href="requests">Requests</a></li>
                <li><a href="logout">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Requests</h1>
        </div>

        <form method="post">
            <label>
                Approve every: <input type="text"
                                      required="required"
                                      pattern="^(?!00:00)([0-2][0-3]:[0-5][0-9])"
                                      title="00:01 - 23:59"
                                      name="approvePeriod"
                                      value="${approvePeriod}"/>
            </label>
            <button type="submit" name="saveNewPeriod">Save</button>
        </form>
        <hr>

        <form method="post">
            <button type="submit" name="btnApproveAll">Approve all</button>
        </form>
        <hr>

        <p>${haveNoUsersRequestedBy}</p>
        <c:forEach items="${users}" var="user">
            <p class="lead"><img src="${user.profilePictureUrl}"/> <br/>

            <p><b>Username:</b> ${user.userName}</p>
            <p><b>Full Name:</b> ${user.fullName}</p>

            <form method="post">
                <input type="hidden" name="userId" value="${user.id}"/>
                <p>
                    <button type="submit" name="btnApprove">Approve</button>
                </p>
            </form>
            <p></p>
        </c:forEach>

    </div>
    <hr>

<jsp:include page="common/footer.jsp"/>