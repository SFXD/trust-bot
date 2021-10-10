-- apply changes
create table instance (
  id                            bigint generated by default as identity not null,
  key                           varchar(255) not null,
  location                      varchar(255),
  release_version               varchar(255),
  release_number                varchar(255),
  status                        varchar(255),
  environment                   varchar(10),
  constraint ck_instance_environment check ( environment in ('SANDBOX','PRODUCTION')),
  constraint uq_instance_key unique (key),
  constraint pk_instance primary key (id)
);

create table instance_user (
  id                            bigint generated by default as identity not null,
  instance_id                   bigint,
  user_id                       bigint,
  constraint pk_instance_user primary key (id)
);

create table user (
  id                            bigint generated by default as identity not null,
  username                      varchar(255) not null,
  constraint uq_user_username unique (username),
  constraint pk_user primary key (id)
);

create index ix_instance_user_instance_id on instance_user (instance_id);
alter table instance_user add constraint fk_instance_user_instance_id foreign key (instance_id) references instance (id) on delete restrict on update restrict;

create index ix_instance_user_user_id on instance_user (user_id);
alter table instance_user add constraint fk_instance_user_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

