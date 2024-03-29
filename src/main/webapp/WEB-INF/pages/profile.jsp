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
                <li class="active"><a href="profile">Profile</a></li>
                <li><a href="requests">Requests <span class="badge">${requestedCount}</span></a></li>
                <li><a href="antispam">Antispam</a></li>
                <li><a href="logout">Logout</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container">

    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">User Profile</h1>
        </div>

        <p class="lead"><img src="${picture}"/> <br/>
        <p><b>Username:</b> ${username}</p>
        <p><b>Full Name:</b> ${fullname}</p>
        <p><b>Follows:</b> <span class="badge">${follows}</span></p>
        <p><b>Followed by:</b> <span class="badge">${followedBy}</span></p>
    </div>
    <hr>

<jsp:include page="common/footer.jsp"/>