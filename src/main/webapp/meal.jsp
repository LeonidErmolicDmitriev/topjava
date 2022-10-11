<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Edit Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>
    <c:out value="${empty meal.id ? 'Add Meal' : 'Edit Meal'}"/></h2>
<form method="post">
    <p>
        <label>DateTime:
            <input type="datetime-local" name="dateTime" value="${meal.dateTime.format(formatter)}"/>
        </label><br>
        <label>Description:
            <input type="text" name="description" value="${meal.description}"/>
        </label><br>
        <label>Calories:
            <input type="number" name="calories" value="${meal.calories}"/>
        </label><br>
        <input type="hidden" name="id" value="${meal.id}"/>
        <button type="submit" name="command" value="save">Save</button>
        <button type="reset" onclick="window.history.go(-1)">Cancel</button>
    </p>
</form>
</body>
</html>