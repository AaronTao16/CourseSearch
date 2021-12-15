<jsp:useBean id="courseList" scope="request" type="java.util.List"/>
<jsp:useBean id="facetList" scope="request" type="java.util.List"/>

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
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

    <div class="container">
        <form style="text-align: center;" id="search" action="/search" method="get">
            <input class="heading" type="" placeholder=" Enter your search here" name="query">
        </form>
        <div class="row">
            <div class="col-md-3 c-mt-20">
                <br>
                <c:if test="${facetList.size() > 0}">
                    <c:forEach items="${facetList}" var="facet">
                        <c:if test="${facet.labelValues.size() > 1}">
                            <strong>${facet.category}</strong><br>
                            <c:forEach items="${facet.labelValues}" var="labelValue">
                                <span>${labelValue.key} - ${labelValue.value}</span><br>
                            </c:forEach>
                        </c:if>
                    </c:forEach>
                </c:if>
            </div>

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
                                <p>
                                    ${course.description}...
                                    <%--.substring(0, course.description.length()/2)--%>
                                </p>
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

        
    </div>
</body>

</html>