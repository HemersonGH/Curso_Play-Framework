# --- !Ups

alter table usuario add column admin bit(1);

# --- !Downs

alter table usuario drop column admin;
