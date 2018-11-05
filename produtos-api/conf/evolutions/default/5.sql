# --- !Ups

create table token_da_api (
  id                            bigint auto_increment not null,
  usuario_id                    bigint,
  codigo                        varchar(255),
  expiracao                     datetime(6),
  constraint uq_token_da_api_usuario_id unique (usuario_id),
  constraint pk_token_da_api primary key (id)
);

alter table token_da_api add constraint fk_token_da_api_usuario_id foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;

# --- !Downs

alter table token_da_api drop foreign key fk_token_da_api_usuario_id;

drop table if exists token_da_api;
