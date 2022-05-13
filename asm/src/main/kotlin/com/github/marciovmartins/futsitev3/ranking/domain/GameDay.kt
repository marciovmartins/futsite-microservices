package com.github.marciovmartins.futsitev3.ranking.domain

import java.time.LocalDate
import java.util.UUID

data class GameDay(
    val gameDayId: UUID,
    val amateurSoccerGroupId: UUID,
    val date: LocalDate,
    val matches: Set<Match>
) {
    fun calculatePlayersStatistics(): PlayersStatistics = matches.flatMap { it.playerStatistics }
        .groupBy { it.playerId }
        .mapValues { it.value.reduce(PlayerStatistic::add) }
        .values.toSet()
        .let(::PlayersStatistics)
}

data class Match(
    val playerStatistics: Set<PlayerStatistic>
)
