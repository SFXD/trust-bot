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

create table instance_subscriber (
  id                            bigint generated by default as identity not null,
  instanceid                    bigint not null,
  subscriberid                  bigint not null,
  constraint pk_instance_subscriber primary key (id)
);

create table subscriber (
  id                            bigint generated by default as identity not null,
  username                      varchar(255) not null,
  constraint uq_subscriber_username unique (username),
  constraint pk_subscriber primary key (id)
);

create index ix_instance_subscriber_instanceid on instance_subscriber (instanceid);
alter table instance_subscriber add constraint fk_instance_subscriber_instanceid foreign key (instanceid) references instance (id) on delete restrict on update restrict;

create index ix_instance_subscriber_subscriberid on instance_subscriber (subscriberid);
alter table instance_subscriber add constraint fk_instance_subscriber_subscriberid foreign key (subscriberid) references subscriber (id) on delete restrict on update restrict;

