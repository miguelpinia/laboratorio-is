<%--
    Document   : hola
    Created on : 3/02/2015, 04:21:32 PM
    Author     : miguel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% String nombre = "Miguel"; %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
  </head>
  <body>
    <h1>
          Bienvenido <%=nombre%>
    </h1>

    <form action="hola.jsp" method="post">
      Dame tu nombre: <input type="text" name="nombre"/><br/>
      Dame tu contraseña: <input type="password" name="password"/><br/>
      <input type="submit"/>
    </form>

    <%
        String nombre2 = request.getParameter("nombre");
        String pass = request.getParameter("password");
        if(nombre2 != null && !nombre2.equals("")
            && pass != null && !pass.equals("")){
            request.setAttribute("nombre", nombre2);
            request.setAttribute("password", pass);
            response.sendRedirect("aburridoooo");
        }

    %>

  </body>
</html>
