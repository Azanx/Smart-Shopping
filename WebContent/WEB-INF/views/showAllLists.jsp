<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file = "basicStyling.jsp"%>

<title><c:out value = "${userName}"/>:Shopping Lists</title>

</head>
<body>
<spring:url var = "url" value="/list"/>
<c:import var = "pageHeader" url = "pageHeader.jsp"/>
${pageHeader}
<c:out value="${url}"/>
	<br /> Your shopping lists:
	<br />
	<table>
		<c:forEach var="usersList" items="${shoppingLists}">
			<tr>
				<td>
					<form:form action="${url}/delete" modelAttribute="listToDelete">
						<c:out value="${usersList.listNo}" />
						<a href="${url}/${usersList.id}" class="listField">
							<form:input path="listName" value="${usersList.listName}" readonly="true" />
						</a>
						<form:hidden path="id" value="${usersList.id}"/>
						<input type="submit" value="delete list"/>
					</form:form>
				</td>
			</tr>
		</c:forEach>
	</table>
	<br/>
	Add new list:<br/>
	<form:form action="${url}" modelAttribute="newList">
		<table>
			<tr>
				<td><form:label path="listName">List Name: </form:label></td>
				<td><form:input path="listName" placeholder="New list name" /></td>
			</tr>
			<tr><td></td>
				<td><form:errors path="listName" cssClass="error" />
			</tr>
			<tr>
				<td><input type="submit" value="Submit"/></td>
			</tr>
		</table>
	</form:form>
<br/>
</body>
</html>