<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*" %>

<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">

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
        <form style="text-align: center;" id="search" action="/course/searchInstructor" method="get">
            <input class="heading" type="" placeholder=" Enter your search here" name="instructor">
        </form>
        <div class="row">
            <div class="col-md-2 c-mt-20">
                <br>
                <p>Any Requirements</p>
                <p>No requirements</p>
                <p>Requirements A</p>
                <p>Requirements B</p>
                <p>Requirements c</p>
                <hr>
                <p>Spring</p>
                <p>Summer</p>
                <p>Fall</p>
                <hr>
                <p>Any Day</p>
                <p>Mondays only</p>
                <p>Tuesdays only</p>
                <p>Wednesdays only</p>
                <p>Thursdays only</p>
                <p>Fridays only</p>

            </div>

            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <div class="col-md-8">
                <jsp:useBean id="courseList" scope="request" type="java.util.List"/>
                <c:forEach items="${courseList}" var="course" varStatus="tagStatus">
                    <div class="row section_custom">
                        <div class="col-md-3 ">
                            <h4 class="family"> <em>${course.dept} ${course.number}</em> </h4>
                            <h6 class="family"> <em>${course.instructor}</em> </h6>
                            <p>
                                <day1><strong><em>M</em></strong></day1>
                                <day2><em>Tu</em></day2>
                                <day3><em>W</em></day3>
                                <day4><em>Th</em></day4>
                                <day5><em>F</em></day5>
                                <day6><em>Sa</em></day6>
                                <day7><em>Su</em></day7>
                            </p>
                        </div>
                        <div class="col-md-8">
                            <h5 class="family">${course.name}</h5>
                            <p>
                                ${course.description}
                            </p>
                        </div>

                        <div class="row">
                            <div class="col-md-3">
                                <strong><em>Terms:Fall,Spring</em></strong>
                            </div>
                            <div class="col-md-3">
                                <strong><em>Requirements:2</em></strong>
                            </div>
                            <div class="col-md-3">
                                <strong><em>Credits:2</em></strong>
                            </div>
                            <div class="col-md-3">
                                <strong><em>Graduate</em></strong>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>
        </div>

        
    </div>
</body>

</html>