<%@ page contentType="text/html" pageEncoding="UTF-8" import="java.util.*" %>

<!doctype html>
<html lang="en">
  <head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <style type="text/css">
    #input1{
    width: 700px;
    height: 50px;
    border-radius: 15px;
    }
    </style>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="./js/index.js"></script>
    <title>University of Pittsburgh Class Search Engine</title>
  </head>
  <body>
   
    <h1 style="text-align: center;margin-top: 150px;">
      Let's find a class @</h1>
    <br>

    <form style="text-align: center;" id="search" action="/course/searchInstructor" method="get">
      <img src="./img/pitt.png" style="text-align: center;" alt="University of Pittsburgh Class Search" width="280px"><br><br>
      <input id="input1" type="text" placeholder=" Enter your search here" name="instructor">
    </form>
    
    
  </body>
</html>