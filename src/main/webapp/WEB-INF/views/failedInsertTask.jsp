<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Failed - Task Insertion</title>
</head>
<body>
    <h2>Task Insertion Failed</h2>
    <p>An error occurred while trying to add the task. Please try again.</p>
    
    <% if (request.getAttribute("errorType") != null) { %>
        <p>Error Type : ${errorType}</p>
    <% } %>
    
    <% if (request.getAttribute("errorMessage") != null) { %>
        <p>${errorMessage}</p>
    <% } %>
    
    <a href="javascript:history.back()">Return to form</a>
</body>
</html>
