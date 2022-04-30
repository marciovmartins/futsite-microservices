CREATE TABLE ranking_players_statistics
(
    id                      int unsigned not null primary key auto_increment,
    amateur_soccer_group_id varchar(36)  not null,
    player_id               varchar(36)  not null,
    matches                 smallint unsigned not null,
    victories               smallint unsigned not null,
    draws                   smallint unsigned not null,
    defeats                 smallint unsigned not null,
    goals_in_favor          smallint unsigned not null,
    goals_against           smallint unsigned not null,
    index amateur_soccer_group_id_index (amateur_soccer_group_id)
)