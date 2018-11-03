# --- !Ups

create table usuario (
  id                            bigint auto_increment not null,
  nome                          varchar(255),
  constraint pk_usuario primary key (id)
);


# --- !Downs

drop table if exists usuario;

