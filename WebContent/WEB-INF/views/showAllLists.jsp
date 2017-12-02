<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><c:out value = "${userName}"/>:Shopping Lists</title>
</head>
<body>
<c:set var="context" value="${pageContext.servletContext.contextPath}" />
<c:import var = "pageHeader" url = "pageHeader.jsp"/>
${pageHeader}

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
	<br/>
	Add new list:<br/>
	<form:form action="list" modelAttribute="newList">
		<table>
			<tr>
				<td><form:label path="listName">List Name: </form:label></td>
				<td><form:input path="listName" placeholder="Enter name for the new list"/></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit"/></td>
			</tr>
		</table>
	</form:form>
<br/>
</body>
</html>