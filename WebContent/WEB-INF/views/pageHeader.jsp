<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
Logged as:
<spring:url var="url" value="/profile"/>
	<a href="${url}"><c:out value="${userName}"/></a>
<spring:url var="logoutUrl" value="/logout"/>
<form:form action="${logoutUrl}">
	<input type="submit" value="Logout" />
</form:form>