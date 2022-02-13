create table game_days
(
    id          int unsigned auto_increment primary key,
    date        date          not null,
    quote       varchar(255)  null,
    author      varchar(50)   null,
    description varchar(2048) null
);

create table matches
(
    id          int unsigned auto_increment primary key,
    game_day_id int unsigned not null,
    match_order int unsigned not null,
    constraint matches_day_games_id_fk
        foreign key (game_day_id) references game_days (id)
);

create table players
(
    id             int unsigned auto_increment primary key,
    match_id       int unsigned     not null,
    user_id        varchar(36)      not null,
    team           enum ('A', 'B')  not null,
    goals_in_favor tinyint unsigned not null,
    goals_against  tinyint unsigned not null,
    yellow_cards   tinyint unsigned not null,
    blue_cards     tinyint unsigned not null,
    red_cards      tinyint unsigned not null,
    constraint match_players_matches_id_fk
        foreign key (match_id) references matches (id)
);

