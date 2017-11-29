<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${userName}:Shopping Lists</title>
</head>
<body>

	Logged as:
	<a href="profile">${userName}</a>
	<br /> Your shopping lists:
	<br />
	<table>
		<tr>
			<td>List Number</td>
			<td>List Name</td>
		</tr>
		<c:forEach var="listItem" items="${shoppingLists}">
			<tr>
				<td><c:out value="${listItem.listNo}" /></td>
				<td><c:out value="${listItem.listName}" /></td>
			</tr>
		</c:forEach>
	</table>
	<form:form action="list" modelAttribute="newList">
	
	</form:form>
	Create new list
</body>
</html>