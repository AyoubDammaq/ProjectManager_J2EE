<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>
</head>
<style>
.full-width-button {
	width: 100%;
}
</style>
<body>
	<h1>Project Form</h1>
	<form id="form-create-project"
		action="<%=request.getContextPath()%>/project" method="post">
		<table border="1">
			<tr>
				<td>Code de projet</td>
				<td><input type="text" id="code" name="code"></td>
			</tr>
			<tr>
				<td>Description de projet</td>
				<td><input type="text" id="description" name="description"></td>
			</tr>
			<tr>
				<td>Date de début</td>
				<td><input type="text" id="startDate" name="startDate"></td>
			</tr>
			<tr>
				<td colspan="1"><input type="submit" name="action"
					value="Create Project" form="form-create-project"
					class="full-width-button"></td>
				<td colspan="2"><input type="submit" name="action"
					value="Update Project" form="form-create-project"
					class="full-width-button"></td>
			</tr>
			<tr>
				<td colspan="1"><input type="submit" name="action"
					value="Remove Project" form="form-create-project"
					class="full-width-button"></td>
				<td colspan="2"><input type="submit" name="action"
					value="List Of Projects" form="form-create-project"
					class="full-width-button"></td>
			</tr>
		</table>
	</form>
	<a href="javascript:history.back()">Return to welcome page</a>

</body>
</html>
