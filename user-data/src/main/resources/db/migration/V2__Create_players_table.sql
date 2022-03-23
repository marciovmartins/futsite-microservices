create table players
(
    id                      varchar(36) not null primary key,
    amateur_soccer_group_id varchar(36) not null,
    user_id                 varchar(36) not null,
    nickname                varchar(45) null,
    date_created            datetime(3) not null default current_timestamp(3),
    unique index amateur_soccer_group_id_user_id_key (amateur_soccer_group_id, user_id)
)