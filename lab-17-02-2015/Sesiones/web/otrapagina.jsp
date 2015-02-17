<%--
    Document   : otrapagina
    Created on : 17/02/2015, 04:40:24 PM
    Author     : miguel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Otra pagina</title>
  </head>
  <body>
    <h1>Otra pagina <%= session.getAttribute("nomUsuario") %></h1>
  </body>
</html>
