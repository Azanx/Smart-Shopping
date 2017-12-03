<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:import var="pageHeader" url="pageHeader.jsp" />
	${pageHeader}

	<br /> Your shopping lists <c:out value="ąśćżźó"/>:
	<br />
	<table>
		<tr>
			<td>Item Number</td>
			<td>Item Name</td>
		</tr>
		<tr>
			<td></td>
			<td></td>
		</tr>
		<c:forEach var="listItem" items="${listItems.listItems}">
			<tr>
				<td><c:out value="${listItem.itemNo}" /></td>
				<td><c:out value="${listItem.itemName}" /></td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<form:form action="${listId}" modelAttribute="shoppingList">
	Add new items:
	<table>
			<c:forEach items="${shoppingList.listItems}" varStatus="vs">
				<tr>
					<td><form:input path="listItems[${vs.index}].itemName"
							placeholder="Name for the new item" /></td>
				</tr>
			</c:forEach>
			<tr>
				<td><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form:form>
	<br />
</body>
</html>