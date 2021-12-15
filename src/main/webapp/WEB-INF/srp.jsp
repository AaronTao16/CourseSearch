<jsp:useBean id="time" scope="request" type="java.lang.Long"/>
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
    <title>University of Pittsburgh Class Search Engine</title>
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

    <div class="container pb-3">
        <form style="text-align: center;" id="search" action="/search" method="get" class="input-group input-group-lg w-75 mt-3 mx-auto">
            <input id="searchInput" class="form-control" type="text" placeholder=" Enter your search here" name="query" aria-describedby="inputGroup-sizing-lg">
            <button class="btn btn-outline-secondary" type="submit" id="button-addon1">Search</button>
        </form>
        <p class="text-secondary" style="text-align: right; margin-right: 200px; margin-bottom: 0"><em>${courseList.size()} search results (${time/1000} seconds)</em></p>
        <div class="row">
            <div class="col-md-3 c-mt-20">
                <br>
                <c:if test="${facetList.size() > 0}">
                    <c:forEach items="${facetList}" var="facet">
                        <strong class="d-block fs-5 mb-1">
                            <c:choose>
                                <c:when test="${facet.category == 'grad'}">
                                    Type
                                </c:when>
                                <c:when test="${facet.category == 'day'}">
                                    Day
                                </c:when>
                                <c:when test="${facet.category == 'elective'}">
                                    Elective in...
                                </c:when>
                                <c:when test="${facet.category == 'required'}">
                                    Required in...
                                </c:when>
                            </c:choose>
                        </strong>
                        <div id="${facet.category}-facet-group" class="facet-group list-group mb-3">
                            <c:forEach items="${facet.labelValues}" var="labelValue">
                                <button data-category="${facet.category}" data-label="${labelValue.key}" type="button" class="list-group-item list-group-item-action d-flex justify-content-between">
                                    <span>${labelValue.key}</span>
                                    <span class="badge bg-primary rounded-pill">${labelValue.value}</span>
                                </button>
                            </c:forEach>
                        </div>
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
                            </div>
                            <div class="col-md-9">
                                <h5 class="family">${course.name}</h5>
                                <p>
                                        ${course.description}
                                </p>
                            </div>

                            <div class="row">


                                <div class="col-md-3">

                                </div>

                                <div class="col-md-9">
                                    <c:if test="${course.sections.size() > 0}">
                                        <div class="row">
                                            <div class="col-md-2">
                                                <strong>Number</strong>
                                            </div>
                                            <div class="col-md-2">
                                                <strong>Type</strong>
                                            </div>
                                            <div class="col-md-5">
                                                <strong>Time</strong>
                                            </div>
                                            <div class="col-md-3">
                                                <strong>Location</strong>
                                            </div>
                                        </div>
                                        <c:forEach items="${course.sections}" var="section" >
                                            <div class="row">
                                                <div class="col-md-2">
                                                        ${section.classNumber}
                                                </div>
                                                <div class="col-md-2">
                                                        ${section.sectionType}
                                                </div>
                                                <div class="col-md-5">
                                                    <c:if test="${section.days.contains('M')}">
                                                        <span>Monday </span>
                                                    </c:if>

                                                    <c:if test="${section.days.contains('T')}">
                                                        <span>Tuesday </span>
                                                    </c:if>

                                                    <c:if test="${section.days.contains('W')}">
                                                        <span>Wednesday </span>
                                                    </c:if>

                                                    <c:if test="${section.days.contains('H')}">
                                                        <span>Thursdays </span>
                                                    </c:if>

                                                    <c:if test="${section.days.contains('F')}">
                                                        <span>Friday </span>
                                                    </c:if>
                                                    <br>
                                                    <span>${section.beginTime.substring(0,5)} - ${section.endTime.substring(0,5)}</span>
                                                </div>
                                                <div class="col-md-3">
                                                    <span>${section.building}</span>
                                                    <span>${section.room}</span>
                                                </div>
                                            </div>
                                        </c:forEach>

                                    </c:if>
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