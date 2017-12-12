<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="headElements.jsp"%>
<title>Login to Smart-Shopping</title>
</head>
<body>
	<spring:url var="url" value="/" />
	<div class="container">
		<div class="row">
			<div class=col-md-4></div>
			<div class=col>
				<form:form action="${url}login" modelAttribute="user">
					<form:label path="userName">Username:</form:label>
					<form:input path="userName" placeholder="username" />
					<form:errors path="userName" cssClass="error"/>
					<br />
					<form:label path="password">Password:</form:label>
					<form:password path="password" placeholder="password" />
					<form:errors path="password" cssClass="error"/>
					<br />
					<input type="submit" value="Log in" />
				</form:form>
			</div>
			<div class=col-md-4></div>
		</div>
	</div>
</body>
</html>