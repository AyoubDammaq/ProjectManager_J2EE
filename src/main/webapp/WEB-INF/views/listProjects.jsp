<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List Of Projets</title>
</head>
<body>
    <h2>List Of Projets</h2>
    <c:if test="${not empty projects}">
        <table border="1">
            <thead>
                <tr>
                    <th>Code</th>
                    <th>Description</th>
                    <th>Start Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="project" items="${projects}">
                    <tr>
                        <td>${project.code}</td>
                        <td>${project.description}</td>
                        <td>${project.startDate}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    <a href="javascript:history.back()">Return to form</a>
</body>
</html>

