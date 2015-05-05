<%--
    Document   : listaImagenes.jsp
    Created on : 4/05/2015, 09:50:39 PM
    Author     : miguel
--%>

<%@page import="model.Imagen"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>JSP Page</title>
    <%
        List<Imagen> imagenes = (List<Imagen>) session.getAttribute("imagenes");
        session.removeAttribute("imagenes");
    %>
  </head>
  <body>
    <h1>Hello World!</h1>

    <% for(Imagen imagen : imagenes) { %>
    <h2>Imagen <%=imagen.getNombre()%></h2>
    <img src="Imagen?img=<%=imagen.getNombre()%>"/>
    <% } %>

  </body>
</html>
