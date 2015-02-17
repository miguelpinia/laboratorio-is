<%--
    Document   : index
    Created on : 17/02/2015, 04:14:57 PM
    Author     : miguel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login</title>
  </head>
  <body>
    <h1>Inicia sesión</h1>
    <p>
    <form method="post" action="Login">
      Usuario: <input type="text" name="usuario"><br/>
      Contraseña: <input type="password" name="contrasena">
      <input type="submit" value="Iniciar sesion"/>
    </form>
  </p>
</body>
</html>
