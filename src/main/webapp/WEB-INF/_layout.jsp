<%@ page contentType="text/html;charset=UTF-8" %>
<%  // вставка JAVA коду
    String pageBody = (String) request.getAttribute("page-body");
    // якщо сторінка не заявлена, перенаправлення на сторінку "home"
    if( pageBody == null) {
        pageBody = "home";
    }
    String bodyFile = "/WEB-INF/" + pageBody + ".jsp" ;
    // доступ до контексту
    String context = request.getContextPath();
%>
<html>
<head>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="<%=context%>/css/site.css"/>
    <title>Title</title>
</head>
<body>
<header>
<nav>
    <div class="nav-wrapper brown">
        <a href="<%=context%>/" class="brand-logo">Logo</a>
        <ul id="nav-mobile" class="right hide-on-med-and-down">
            <li><a href="<%=context%>/cart"><i class="material-icons prefix">shopping_cart</i></a></li>
            <li><a href="<%=context%>/promotion">Promotions</a></li>
            <li><a href="collapsible.html">JavaScript</a></li>
        </ul>
    </div>
</nav>
</header>

<main class="container">
   <%--  <jsp:include page="home.jsp"/> --%>
         <jsp:include page="<%= bodyFile %>"/>
</main>

<footer class="page-footer brown">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Footer Content</h5>
                <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
            </div>
            <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Links</h5>
                <ul>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2024 Copyright Text
            <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
        </div>
    </div>
</footer>

     <!-- Compiled and minified JavaScript -->
     <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
     <script src="<%=context%>/js/site.js"></script>
</body>
</html>