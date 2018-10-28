# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table produto (
  titulo                        varchar(255),
  codigo                        varchar(255),
  tipo                          varchar(255),
  descricao                     varchar(255),
  preco                         double
);


# --- !Downs

drop table if exists produto;

