<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List Of Tasks</title>
</head>
<body>
    <h2>List Of Tasks</h2>
    <c:if test="${not empty tasks}">
        <table border="1">
            <thead>
                <tr>
                    <th>Code</th>
                    <th>Description</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Associed Project</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="task" items="${tasks}">
                    <tr>
                        <td>${task.code}</td>
                        <td>${task.description}</td>
                        <td>${task.startDate}</td>
                        <td>${task.endDate}</td>
                        <td>${task.project.description}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <a href="javascript:history.back()">Return to form</a>
</body>
</html>

