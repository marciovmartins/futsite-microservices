package com.github.marciovmartins.futsitev3.ranking.domain

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsByGameDay.Match
import java.util.UUID

data class PlayerStatistic(
    val playerId: UUID,
    val classification: String?,
    val points: Short,
    val matches: Short,
    val victories: Short,
    val draws: Short,
    val defeats: Short,
    val goalsInFavor: Short,
    val goalsAgainst: Short,
    val goalsBalance: Short
) {
    constructor(playerStatisticsByGameDay: PlayerStatisticsByGameDay) : this(
        playerId = playerStatisticsByGameDay.playerId,
        classification = null,
        points = calculatePoints(playerStatisticsByGameDay.match),
        matches = 1,
        victories = if (playerStatisticsByGameDay.match == Match.WIN) 1 else 0,
        draws = if (playerStatisticsByGameDay.match == Match.DRAW) 1 else 0,
        defeats = if (playerStatisticsByGameDay.match == Match.LOST) 1 else 0,
        goalsInFavor = playerStatisticsByGameDay.goalsInFavor,
        goalsAgainst = playerStatisticsByGameDay.goalAgainst,
        goalsBalance = (playerStatisticsByGameDay.goalsInFavor - playerStatisticsByGameDay.goalAgainst).toShort()
    )

    companion object {
        fun calculatePoints(match: Match): Short = when (match) {
            Match.WIN -> 3
            Match.DRAW -> 1
            Match.LOST -> 0
        }

        fun add(a: PlayerStatistic, b: PlayerStatistic) = PlayerStatistic(
            playerId = a.playerId,
            classification = null,
            points = (a.points + b.points).toShort(),
            matches = (a.matches + b.matches).toShort(),
            victories = (a.victories + b.victories).toShort(),
            draws = (a.draws + b.draws).toShort(),
            defeats = (a.defeats + b.defeats).toShort(),
            goalsInFavor = (a.goalsInFavor + b.goalsInFavor).toShort(),
            goalsAgainst = (a.goalsAgainst + b.goalsAgainst).toShort(),
            goalsBalance = (a.goalsBalance + b.goalsBalance).toShort()
        )
    }
}
