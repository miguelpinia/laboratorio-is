begin;
set client_encoding = 'utf-8';

-- alumno
-- Nombre, apellido1, apellido2
-- Número de cuenta, correo, promedio
-- créditos, semestre, historial, saldo, video, fecha nacimiento
create table alumno(
  id serial primary key,
  nombre text not null,
  apellido1 text not null,
  apellido2 text not null,
  correo text not null,
  promedio numeric not null,
  numero_cuenta int not null,
  creditos int not null,
  -- historial blob not null,
  fecha_nacimiento date not null,
  constraint NumeroCuenta unique(numero_cuenta)
);

-- videojuego
-- costo, género, tamaño, nombre, año, empresa, descripción, clasificación
-- descargas,

create table videojuego(
  id int primary key,
  nombre text not null,
  genero int not null,
  tamano numeric not null,
  fecha_liberacion date not null,
  empresa int not null,
  clasificación int not null,
  descargas int not null,
  link text not null
);


create table empresa(
  -- Nombre
  -- Catálogo de empresas
);
-- administrador
-- correo, nombre, apellido1, apellido2
-- contraseña

-- cuenta
-- nombre de usuario, tipo, contraseña, saldo total, historial de compra,
-- fecha de registro, historial


commit;
