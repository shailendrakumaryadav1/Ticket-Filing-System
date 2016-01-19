# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ticket (
  ticket_id                 varchar(255) not null,
  name                      varchar(255),
  name_id                   varchar(255),
  created_by                varchar(255),
  assigned_to               varchar(255),
  issues                    varchar(255),
  status                    varchar(255),
  comment                   varchar(255),
  constraint pk_ticket primary key (ticket_id))
;

create sequence ticket_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists ticket;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists ticket_seq;

