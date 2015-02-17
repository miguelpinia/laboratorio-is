<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Inicio</title>
  </head>
  <body>
    <h1>Bienvenido <%=session.getAttribute("nomUsuario")%></h1>
    <p>
      Hola :D
      <a href="otrapagina.jsp">Pícame</a> &nbsp; <a href="Logout">Salir</a>
    </p>

    <h2>Mensajes del servidor</h2>
    <ul>
      <%
          List<String> list = (List<String>) session.getAttribute("mensajes");
          for (String mensajes : list) {
      %>

      <li><%= mensajes %></li>

        <%
            }
        %>
    </ul>
  </body>
</html>
