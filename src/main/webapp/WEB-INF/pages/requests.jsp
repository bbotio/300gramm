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
                <li class="active"><a href="requests">Requests</a></li>
                <li><a href="antispam">Antispam</a></li>
                <li><a href="logout">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Approve requests</h1>
        </div>


        <form method="post" action="requests">
            <div class="checkbox">
                <label>
                    <input type="hidden" id="_chbox_1" name="isAutoApproveEnabled"/>
                    <input type="checkbox" id="chbox_1" name="isAutoApproveEnabled" value="true" ${isAutoApproveEnabled}/>
                    Auto-approve enabled
                </label>
            </div>

            <table>
                <tr>
                    <td style="padding-right: 10px">
                        <label>
                            Approve every: <input type="number"
                                                  min="12"
                                                  name="approvePeriod"
                                                  value="${approvePeriod}"/>
                        </label>
                    </td>
                    <td>
                        <span class="label label-danger" style="visibility: ${visibility}; align-content: center">${errorMessage}</span>
                    </td>
                </tr>
            </table>

            <button type="submit" class="btn btn-primary" name="saveAutoApprove">Save</button>
        </form>
        <hr>

        <form method="post">
            <button type="submit" class="btn btn-primary" name="btnApproveAll">Approve all</button>
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
                    <button type="submit" class="btn btn-primary" name="btnApprove">Approve</button>
                </p>
            </form>
            <p></p>
        </c:forEach>
    </div>
    <hr>

<jsp:include page="common/footer.jsp"/>