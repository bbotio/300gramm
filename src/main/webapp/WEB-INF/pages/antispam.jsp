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
                <li><a href="requests">Requests <span class="badge">${requestedCount}</span></a></li>
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

        <form method="post">
            <div class="checkbox">
                <label>
                    <input type="hidden" id="_chbox_1" name="isAntiSpamEnabled"/>
                    <input type="checkbox" id="chbox_1" name="isAntiSpamEnabled" value="true" ${isAntiSpamEnabled}/>
                    Anti-spam enabled
                </label>
            </div>

            <table>
                <tr>
                    <td style="padding-right: 10px;">
                        <div class="form-group" style="margin: auto;">
                            <label for="badWords">Bad words:</label>
                            <input style="margin:auto;" type="text" id="badWords"
                                   value="${badWordsList}"
                                   data-role="tagsinput"
                                   name="badWordsList"
                                   placeholder="Add bad words"/>
                        </div>
                    </td>
                    <td>
                        <span class="label label-danger"
                              style="visibility: ${visibility}; align-content: center">${errorMessage}</span>
                    </td>
                </tr>
            </table>

            <button type="submit" class="btn btn-primary" name="saveAntiSpam">Save</button>
        </form>
        <hr>

        <form method="post">
            <div class="input-group" style="margin-bottom: 20px;">
                <input type="text" class="form-control" name="black_list_input" placeholder="Add user to blacklist...">
            <span class="input-group-btn">
                <button class="btn btn-default" type="submit" name="addToBlackList">Add</button>
            </span>
            </div>
        </form>

        <p>${haveNoUsersInBlackList}</p>
        <div class="list-group">
            <c:forEach items="${blacklist}" var="blackListUser">
                <form method="post">
                    <input type="hidden" name="blackListUserName" value="${blackListUser}"/>

                    <a href="#" class="list-group-item">
                        ${blackListUser}
                     <span class="pull-right">
                        <button class="btn btn-xs btn-warning" type="submit" name="removeFromBlackList">
                            <span class="glyphicon glyphicon-trash"></span>
                        </button>
                    </span>
                    </a>
                </form>
            </c:forEach>
        </div>

    </div>
    <hr>


<jsp:include page="common/footer.jsp"/>