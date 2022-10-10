<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h4><a href="meals?func=add">Add Meal</a></h4>
<table cols="5">
    <style type="text/css">
        TD, TH {
            border: 1px solid black; /* Параметры рамки */
        }
    </style>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <c:forEach items="${mealsTo}" var="mealTo">
        <tr style=${mealTo.excess?'color:red;':'color:green;'}>
            <td>${mealTo.dateTime.format(formatter)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?func=update&id=${mealTo.id}">Update</a></td>
            <td><a href="meals?func=delete&id=${mealTo.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>