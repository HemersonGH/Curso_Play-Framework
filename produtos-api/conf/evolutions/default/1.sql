# --- !Ups

create table produto (
  id                            bigint auto_increment not null,
  titulo                        varchar(255),
  codigo                        varchar(255),
  tipo                          varchar(255),
  descricao                     varchar(255),
  preco                         double,
  constraint pk_produto primary key (id)
);


# --- !Downs

drop table if exists produto;

