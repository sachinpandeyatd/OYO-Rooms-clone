<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Check Out</title>
</head>
<body>
	<form action="">
		<input type="text" name="roomId" placeholder="Room ID">
		<input type="submit" value="Check Out">
	</form>
	
	<c:if test="${param.roomId != null }">
		<jsp:useBean id="beanDAO"  class="com.oyo_rooms_with_hib.OyoDAO" />
		<c:out value="${beanDAO.checkoutDAO(param.roomId) }" />
	</c:if>
</body>
</html>