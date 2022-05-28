alter table ranking_matches
    add column game_day_id varchar(36) not null after amateur_soccer_group_id,
    add unique index ranking_matches_index1 (amateur_soccer_group_id, game_day_id, game_day_date),
    drop index amateur_soccer_group_id_index;

alter table ranking_players_statistics
    add column game_day_id   varchar(36) not null after amateur_soccer_group_id,
    add column game_day_date date        not null after game_day_id,
    add unique index ranking_players_statistics_index1 (amateur_soccer_group_id, game_day_id, game_day_date, player_id),
    drop index amateur_soccer_group_id_index;
