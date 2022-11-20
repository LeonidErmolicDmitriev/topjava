<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<section>
    <hr/>
    <h2><spring:message code="meals.title"/></h2>
    <form method="get" action="meals">
        <input type="hidden">
        <dl>
            <dt><spring:message code="meals.start_date"/></dt>
            <dd><label>
                <input type="date" name="startDate" value="${param.startDate}">
            </label></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.end_date"/></dt>
            <dd><label>
                <input type="date" name="endDate" value="${param.endDate}">
            </label></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.start_time"/></dt>
            <dd><label>
                <input type="time" name="startTime" value="${param.startTime}">
            </label></dd>
        </dl>
        <dl>
            <dt><spring:message code="meals.end_time"/></dt>
            <dd><label>
                <input type="time" name="endTime" value="${param.endTime}">
            </label></dd>
        </dl>
        <button type="submit"><spring:message code="meals.filter"/></button>
    </form>
    <hr/>
    <a href="meals/create"><spring:message code="meals.add"/></a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th><spring:message code="meals.date"/></th>
            <th><spring:message code="meals.description"/></th>
            <th><spring:message code="meals.calories"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr data-meal-excess="${meal.excess}">
                <td>
                        ${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        <%--                        ${fn:formatDateTime(meal.dateTime)}--%>
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals/update?id=${meal.id}">Update</a></td>
                <td><a href="meals/delete?id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>