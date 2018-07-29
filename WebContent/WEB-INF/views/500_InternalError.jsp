<%@ page import="java.time.LocalDateTime" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Error 500: Internal Error</title>
</head>
<spring:url var="homeUrl" value="/" />
<body>
<div align="center">
    <h1>Error 500</h1>
    <h2>Internal server error occurred</h2>
    <h3>
    <%= LocalDateTime.now() %>
    </h3>
    <a href="${homeUrl}">Go back to homepage</a>
</div>
</body>
</html>
