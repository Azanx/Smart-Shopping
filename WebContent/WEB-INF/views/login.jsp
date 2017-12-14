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
	<spring:url var="loginUrl" value="/login" />
	<div class="container">
		<div class="row">
			<div class=col-md-4></div>
			<div class=col>
				<form id="login" action="${loginUrl}" method="post">
					<label for="userName">Username:</label>
					<input type="text" id="userName" name="userName" placeholder="username" />
					<br />
					<label for="password">Password:</label>
					<input type="password" id="password" name="password" placeholder="password" />
					<br />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<input type="submit" value="Log in" />
				</form>
				<c:if test="${param.error!=null}">
					<p class="error"> Invalid username and/or password!</p>
				</c:if>
				<c:if test="${param.logout!=null}">
					<p class="error"> You have been logged out succesfully!</p>
				</c:if>
			</div>
			<div class=col-md-4></div>
		</div>
	</div>
</body>
</html>