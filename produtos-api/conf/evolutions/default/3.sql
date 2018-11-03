# --- !Ups

alter table usuario add column email varchar(255);
alter table usuario add column senha varchar(255);

# --- !Downs

alter table usuario drop column email;
alter table usuario drop column senha;

