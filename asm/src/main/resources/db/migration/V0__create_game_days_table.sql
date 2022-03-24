create table game_days
(
    id                      varchar(36) primary key,
    amateur_soccer_group_id varchar(36)  not null,
    date                    date         not null,
    version                 int unsigned not null,
    constraint game_days_amateur_soccer_group_id_date_uindex
        unique (amateur_soccer_group_id, date)
);

create table matches
(
    id          int unsigned auto_increment primary key,
    game_day_id varchar(36)  not null,
    match_order int unsigned not null,
    constraint matches_day_games_id_fk
        foreign key (game_day_id) references game_days (id)
);

create table player_statistics
(
    id             int unsigned auto_increment primary key,
    match_id       int unsigned     not null,
    player_id      varchar(36)      not null,
    team           enum ('A', 'B')  not null,
    goals_in_favor tinyint unsigned not null,
    goals_against  tinyint unsigned not null,
    yellow_cards   tinyint unsigned not null,
    blue_cards     tinyint unsigned not null,
    red_cards      tinyint unsigned not null,
    constraint match_player_statistics_matches_id_fk
        foreign key (match_id) references matches (id)
);
