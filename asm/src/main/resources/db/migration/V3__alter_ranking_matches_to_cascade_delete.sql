alter table ranking_players_statistics
    add column game_day_date date null after amateur_soccer_group_id;

update ranking_players_statistics rps
    inner join ranking_matches rm on rps.matches = rm.matches
set rps.game_day_date = rm.game_day_date
where rps.game_day_date is null;

alter table ranking_players_statistics
    change column game_day_date game_day_date date not null,
    add index ranking_players_statistics_amateur_soccer_group_id_game_day_id (amateur_soccer_group_id, game_day_date);
