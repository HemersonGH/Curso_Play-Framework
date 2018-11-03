# --- !Ups

create table token_de_cadastro (
  id                            bigint auto_increment not null,
  usuario_id                    bigint,
  codigo                        varchar(255),
  constraint uq_token_de_cadastro_usuario_id unique (usuario_id),
  constraint pk_token_de_cadastro primary key (id)
);

alter table token_de_cadastro add constraint fk_token_de_cadastro_usuario_id foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;

alter table usuario add column verificado bit(1);

# --- !Downs

drop table if exists token_de_cadastro;

alter table usuario drop column verificado;
