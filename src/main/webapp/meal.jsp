<jsp:useBean id="id" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="dateTime" scope="request" type="java.time.LocalDateTime"/>
<jsp:useBean id="description" scope="request" type="java.lang.String"/>
<jsp:useBean id="calories" scope="request" type="java.lang.Integer"/>
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
<h2>Edit Meal</h2>
<FORM method="post">
    <P>
        <label>DateTime:
            <input type="datetime-local" name="dateTime" value="${dateTime.format(formatter)}"/>
        </label><BR>
        <label>Description:
            <input type="text" name="description" value="${description}"/>
        </label><BR>
        <label>Calories:
            <input type="text" name="calories" value="${calories}"/>
        </label><BR>
        <input type="hidden" name="id" value="${id}"/>
        <button type="submit" name="command" value="save">Save</button>
        <button type="submit" name="command" value="cancel">Cancel</button>
    </P>
</FORM>
</body>
</html>