<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
<jsp:useBean id="newMeal" scope="request" type="java.lang.Boolean"/>
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
    <c:out value="${newMeal ? 'Add Meal' : 'Edit Meal'}"/></h2>
<FORM method="post">
    <P>
        <label>DateTime:
            <input type="datetime-local" name="dateTime" value="${meal.dateTime.format(formatter)}"/>
        </label><BR>
        <label>Description:
            <input type="text" name="description" value="${meal.description}"/>
        </label><BR>
        <label>Calories:
            <input type="number" name="calories" value="${meal.calories}"/>
        </label><BR>
        <input type="hidden" name="id" value="${meal.id}"/>
        <input type="hidden" name="newMeal" value="${newMeal}"/>
        <button type="submit" name="command" value="save">Save</button>
        <button type="reset" onclick="window.history.go(-1)">Cancel</button>
    </P>
</FORM>
</body>
</html>