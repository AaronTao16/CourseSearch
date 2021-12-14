<jsp:useBean id="courseList" scope="request" type="java.util.List"/>
<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*" %>

<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="../js/srp.js"></script>
    <title>Second</title>
    <style>
        .heading {
            margin-top: 50px;
            margin-left: -13px;
            font-size: 28px;
            border: 2px solid silver;
            padding: 10px;
            width: 100%;
        }

        .section_custom {
            margin-top: 30px;
            border: 1px solid silver;
            padding: 15px;


        }

        .family {
            font-family: Impact, fantasy;
        }
        .c-mt-30{
            margin-top: 30px;
            font-weight: bold;
        }
    </style>
</head>

<body>
    <div class="container">
        <form style="text-align: center;" id="search" action="/search?page=1&" method="get">
            <input id="searchInput" class="heading" type="" placeholder=" Enter your search here" name="query" value="" >
        </form>
        <div class="row">
            <div class="col-md-3 c-mt-20">
                <br>
                <form action="/search" method="get" name="levelForm">
                    <input class="form-check-input" type="radio" name="level" value="All" onclick="handleClick(this)" checked>
                    All Level<BR>
                    <input class="form-check-input" type="radio" name="level" onclick="handleClick(this)" value="Undergraduate">
                    Undergraduate<BR>
                    <input class="form-check-input" type="radio" name="level" onclick="handleClick(this)" value="Graduate">
                    Graduate<BR>
<%--                    <input type="radio" name="id" value="All"> All Level<BR>--%>
<%--                    <input type="radio" name="id" value="Undergraduate"> Undergraduate<BR>--%>
<%--                    <input type="radio" name="id" value="Graduate"> Graduate<BR>--%>
                </form>
                <hr>
<%--                <p>Spring</p>--%>
<%--                <p>Summer</p>--%>
<%--                <p>Fall</p>--%>
<%--                <hr>--%>
<%--                <p>Any Day</p>--%>
<%--                <p>Mondays only</p>--%>
<%--                <p>Tuesdays only</p>--%>
<%--                <p>Wednesdays only</p>--%>
<%--                <p>Thursdays only</p>--%>
<%--                <p>Fridays only</p>--%>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="Any" value="option1" checked="checked">
                    <label class="form-check-label" for="Any">Any Day</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" id="M" value="option2">
                    <label class="form-check-label" for="M">M</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" id="Tu" value="option2">
                    <label class="form-check-label" for="Tu">Tu</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" id="W" value="option2">
                    <label class="form-check-label" for="W">W</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" id="Th" value="option2">
                    <label class="form-check-label" for="Th">Th</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="checkbox" id="F" value="option2">
                    <label class="form-check-label" for="F">F</label>
                </div>

            </div>

            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
            <div class="col-md-8">
<%--                <jsp:useBean id="courseList" scope="request" type="java.util.List"/>--%>
                <c:if test="${courseList.size() == 0}">
                    <div class="row">
                        <h3 class="c-mt-30">Oops! No relevant courses!</h3>
                    </div>
                </c:if>

                <c:if test="${courseList.size() != 0}">
                    <c:forEach items="${courseList}" var="course" varStatus="tagStatus">
                        <div class="row section_custom">
                            <div class="col-md-3 ">
                                <h4 class="family"> <em>${course.dept} ${course.number}</em> </h4>
                                <h6 class="family"> <em>${course.instructor}</em> </h6>
                                <p>
                                    <day1><em>${course.days.contains("M")? "<strong>M</strong>": "M"}</em></day1>
                                    <day1><em>${course.days.contains("T")? "<strong>Tu</strong>": "Tu"}</em></day1>
                                    <day1><em>${course.days.contains("W")? "<strong>W</strong>": "W"}</em></day1>
                                    <day1><em>${course.days.contains("H")? "<strong>Th</strong>": "Th"}</em></day1>
                                    <day1><em>${course.days.contains("F")? "<strong>F</strong>": "F"}</em></day1>
                                </p>
                            </div>
                            <div class="col-md-8">
                                <h5 class="family">${course.name}</h5>
<%--                                <c:if test="${course.highlightFrag != ''}">--%>
<%--                                <p>--%>
<%--                                    ${course.highlightFrag}...--%>
<%--                                    &lt;%&ndash;.substring(0, course.description.length()/2)&ndash;%&gt;--%>
<%--                                </p>--%>
<%--                                </c:if>--%>

<%--                                <c:if test="${course.highlightFrag == ''}">--%>
                                    <p>
                                            ${course.description}
                                    </p>
<%--                                </c:if>--%>
                            </div>

                            <div class="row">
                                <div class="col-md-3">
                                    <strong><em>Terms:Fall,Spring</em></strong>
                                </div>
    <%--                            <div class="col-md-3">--%>
    <%--                                <strong><em>Requirements:2</em></strong>--%>
    <%--                            </div>--%>
                                <div class="col-md-3">
                                    <strong><em>Credits:3</em></strong>
                                </div>
                                <div class="col-md-3">
                                    <strong><em>${course.grad? "Graduate": "Undergraduate"}</em></strong>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
        </div>
        <br>

        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <li class="page-item">
                    <a class="page-link" href="" onclick="prePage(this)">Previous</a>
                </li>
                <li class="page-item"><a id = "l1" class="page-link" href="" onclick="goToPage(this)">1</a></li>
                <li class="page-item"><a id = "l2" class="page-link" href="" onclick="goToPage(this)">2</a></li>
                <li class="page-item"><a id = "l3" class="page-link" href="" onclick="goToPage(this)">3</a></li>
                <jsp:useBean id="hasNext" scope="request" type="java.lang.Boolean"/>
                <c:if test="${hasNext}">
                <li class="page-item">
                        <a id="next" class="page-link" href="" onclick="nextPage(this)">Next</a>
                </li>
                </c:if>

                <c:if test="${!hasNext}">
                    <li class="page-item disabled">
                        <a class="page-link" href="" onclick="nextPage(this)">Next</a>
                    </li>
                </c:if>
            </ul>
        </nav>
    </div>
</body>

</html>