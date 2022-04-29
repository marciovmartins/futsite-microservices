package com.github.marciovmartins.futsitev3.ranking.domain

import java.util.UUID

data class PlayerStatistic(
    val playerId: UUID,
    val matches: Int,
    val victories: Int,
    val draws: Int,
    val defeats: Int,
    val goalsInFavor: Int,
    val goalsAgainst: Int,
) {
    val goalsBalance = goalsInFavor - goalsAgainst

    companion object {
        fun add(a: PlayerStatistic, b: PlayerStatistic) = PlayerStatistic(
            playerId = a.playerId,
            matches = a.matches + b.matches,
            victories = a.victories + b.victories,
            draws = a.draws + b.draws,
            defeats = a.defeats + b.defeats,
            goalsInFavor = a.goalsInFavor + b.goalsInFavor,
            goalsAgainst = a.goalsAgainst + b.goalsAgainst,
        )
    }
}
