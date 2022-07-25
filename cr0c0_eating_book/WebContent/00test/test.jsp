<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="test.JdbcConnection" %>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	Connection conn = new JdbcConnection().getConnection();
	String result="ok jdbc";
	if(conn==null) result="error db";
%>
<h1><%=result %></h1>

<%
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("select * from testTB");

while(rs.next()) {
	String id = rs.getString(1);
	String content = rs.getString(2);
	
	out.println("ID : " + id + " / CONTENT : " + content);
}
%>
</body>
</html>