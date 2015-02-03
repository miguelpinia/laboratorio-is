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

    <form action="hola.jsp">
      Dame tu nombre: <input type="text" name="nombre"/><br/>
      Dame tu contraseña: <input type="password" nombre="password"/><br/>
      <input type="submit"/>
    </form>

    <%
        String nombre2 = request.getParameter("nombre");
        String pass = request.getParameter("password");
        if(nombre2 != null && !nombre2.equals("")){
            out.println("Qué aburrido eres "+nombre2);
            out.println("Tu password es: "+pass);
        }
    %>

  </body>
</html>
