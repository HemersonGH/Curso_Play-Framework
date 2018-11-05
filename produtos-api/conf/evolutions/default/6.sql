# --- !Ups

create table registro_de_acesso (
  id                            bigint auto_increment not null,
  usuario_id                    bigint,
  uri                           varchar(255),
  data                          datetime(6),
  constraint pk_registro_de_acesso primary key (id)
);

alter table registro_de_acesso add constraint fk_registro_de_acesso_usuario_id foreign key (usuario_id) references usuario (id) on delete restrict on update restrict;
create index ix_registro_de_acesso_usuario_id on registro_de_acesso (usuario_id);

# --- !Downs

alter table registro_de_acesso drop foreign key fk_registro_de_acesso_usuario_id;
drop index ix_registro_de_acesso_usuario_id on registro_de_acesso;

drop table if exists registro_de_acesso;
