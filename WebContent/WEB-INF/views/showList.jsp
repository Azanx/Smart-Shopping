<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="basicStyling.jsp"%>
<title><c:out value="${userName}" /> - List: <c:out
		value="${listItems.listName}" /></title>
</head>
<body>
	<c:import var="pageHeader" url="pageHeader.jsp" />
	<spring:url var="urlToLists" value="/list" />
	<spring:url var="urlToCurrent" value="/list/${listId}" />
	${pageHeader}

	<br /> Items in list:
	<b><c:out value="${listItems.listName}" /></b>
	<br />

	<c:forEach var="listItem" items="${listItems.listItems}">
		<c:choose>
			<c:when test="${listItem.bought}">
				<c:set var="boughtStyle" value="bought" />
				<c:set var="boughtButtonText" value="unmark" />
			</c:when>
			<c:otherwise>
				<c:set var="boughtStyle" value="unbought" />
				<c:set var="boughtButtonText" value="mark as done" />
			</c:otherwise>
		</c:choose>
		<form:form action="${urlToCurrent}/setBought"
			modelAttribute="listItemToModify">

			<form:label path="itemName">${listItem.itemNo}</form:label>
			<form:input path="itemName" value="${listItem.itemName}"
				cssClass="${boughtStyle}" disabled="true" />
			<form:hidden path="id" value="${listItem.id}" />
			<form:hidden path="bought" value="${not listItem.bought}" />
			<input type="submit" value="${boughtButtonText}" />
		</form:form>
	</c:forEach>
	<br />
	<form:form action="${urlToCurrent}" modelAttribute="shoppingList">
		Add new items:<br />
		<c:forEach items="${shoppingList.listItems}" varStatus="vs">
			<form:input path="listItems[${vs.index}].itemName"
				placeholder="New item" />
			<br />
		</c:forEach>
		<input type="submit" value="Submit" />
	</form:form>
	<br />
	<a href="${urlToLists}">Back to all lists</a>
</body>
</html>