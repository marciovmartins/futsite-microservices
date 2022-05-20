create table ranking_matches
(
    ranking_matches_id      int unsigned      not null primary key auto_increment,
    amateur_soccer_group_id varchar(36)       not null,
    game_day_date           date              not null,
    matches                 smallint unsigned not null,
    index amateur_soccer_group_id_index (amateur_soccer_group_id)
)