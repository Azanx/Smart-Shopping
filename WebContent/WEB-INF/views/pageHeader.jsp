<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
Logged as:
	<a href="${pageContext.servletContext.contextPath}/<c:out value="${userName}"/>/profile"><c:out value="${userName}"/></a>