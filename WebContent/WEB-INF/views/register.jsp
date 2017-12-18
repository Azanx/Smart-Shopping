<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="headElements.jsp"%>
<title>Create new account</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class=col-md-4></div>
			<div class=col-md-4></div>
			<div class=col align="right">
				<c:choose>
					<c:when test="${userName != 'Anonymous'}">
						<c:import var="pageHeader" url="pageHeader.jsp" />
						${pageHeader}
					</c:when>
					<c:otherwise>
						<spring:url var="loginUrl" value="/login"/>
						<a href="${loginUrl}">Sign in!</a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<div class="row">
			<div class=col-md-4></div>
			<div class=col>
				<c:choose>
					<c:when test="${userName != 'Anonymous'}">
						You are already registered.<br/>
						<spring:url var="mainUrl" value="/index"/>
						<a href="${mainUrl}">Go back to the main page</a>
					</c:when>
					<c:otherwise>
						<spring:url var="registerUrl" value="/register"/>
						<form:form action="${registerUrl}" modelAttribute="newUser">
							<form:label path="userName">Username:</form:label>
							<form:input path="userName" placeholder="username"/><br/>
							<form:errors path="userName" cssClass="error" /><br/>
							<form:label path="email">E-mail:</form:label>
							<form:input path="email" placeholder="e-mail"/><br/>
							<form:errors path="email" cssClass="error" /><br/>
							<form:label path="password">Password:</form:label>
							<form:password path="password" placeholder="password"/><br/>
							<form:errors path="password" cssClass="error" /><br/>
							<form:password path="passwordVerification" placeholder="repeat password"/><br/>
							<form:errors path="passwordVerification" cssClass="error" /><br/>
							
							<input type="submit" value="Register"/>
						</form:form>
					</c:otherwise>
				</c:choose>
			</div>
			<div class=col-md-4></div>
		</div>
	</div>
</body>
</html>