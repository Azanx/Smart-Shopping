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
<title><c:out value="${userName}" /></title>
</head>
<body>
	<c:import var="pageHeader" url="pageHeader.jsp" />
	${pageHeader}
	<br /> Your shopping lists:
	<br />
	<table>
		<form:form action="profile" modelAttribute="user">
			<tr>
				<td><form:label path="userName">Username:</form:label></td>
				<td><form:input path="userName" disabled="true" /></td>
			</tr>
			<tr>
				<td><form:label path="email">Email:</form:label></td>
				<td><form:input path="email" disabled="true" />
			</tr>
		</form:form>
	</table>
	<spring:url var="url" value="/list" />
	<a href="${url}">Back to your lists</a>
</body>
</html>