create table game_days
(
    id                   varchar(36)   not null primary key,
    quote                varchar(255)  null,
    author               varchar(45)   null,
    game_day_description varchar(2048) null,
    date_created         datetime(3)   not null default current_timestamp(3)
)