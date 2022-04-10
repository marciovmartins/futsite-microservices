package com.github.marciovmartins.futsitev3.ranking.domain

data class PlayersRanking(val items: Set<PlayerRanking>) {
    companion object {
        fun from(playersStatisticsByGameDay: Set<PlayerStatisticsByGameDay>): PlayersRanking {
            return PlayersRanking(emptySet())
        }
    }
}