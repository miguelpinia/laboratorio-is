-- Crear una base de datos llamada imagenes.

begin;

create schema documentos;

create table documentos.mime_type (
  id serial primary key
  , nombre  text not null
  , extensiones text
  , constraint tipoUnico unique(nombre)
);

insert into documentos.mime_type (nombre, extensiones) values ('image/jpeg', 'jpg jpeg')
                                                              , ('image/bmp', 'bmp')
                                                              , ('image/png', 'png')
                                                              , ('application/pdf', 'pdf')
                                                              , ('application/msword', 'doc');

create table documentos.imagen (
  id serial primary key
  , mime_type_id int not null references documentos.mime_type(id)
  , nombre text not null
  , contenido bytea not null
  , ruta text not null
);


commit;