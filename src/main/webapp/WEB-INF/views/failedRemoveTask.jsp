<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Failed - Remove Task</title>
</head>
<body>
    <h2>Failed to Remove Task</h2>
    <p>There was an error while trying to remove the task. Please try again.</p>
    
    <%
        String errorMessage = (String)request.getAttribute("errorMessage");
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
    <p>Error Type: <%= errorMessage %></p>
    <%
        }
    %>
    <a href="javascript:history.back()">Return to form</a>
</body>
</html>