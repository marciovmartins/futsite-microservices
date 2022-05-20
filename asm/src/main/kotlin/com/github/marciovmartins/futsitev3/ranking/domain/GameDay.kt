package com.github.marciovmartins.futsitev3.ranking.domain

import java.time.LocalDate
import java.util.UUID

data class GameDay(
    val gameDayId: UUID,
    val amateurSoccerGroupId: UUID,
    val date: LocalDate,
    val matches: Set<Match>
) {
    constructor(gameDayId: UUID, gameDayDTO: GameDayDTO) : this(
        gameDayId = gameDayId,
        amateurSoccerGroupId = gameDayDTO.amateurSoccerGroupId,
        date = gameDayDTO.date,
        matches = gameDayDTO.matches.map { Match(it) }.toSet()
    )

    fun calculatePlayersStatistics(): PlayersStatistics = matches.flatMap { it.playerStatistics }
        .groupBy { it.playerId }
        .mapValues { it.value.reduce(PlayerStatistic::add) }
        .values.toSet()
        .let { PlayersStatistics(matches.size, it) }
}

data class Match(
    val playerStatistics: Set<PlayerStatistic>
) {
    constructor(matchDTO: MatchDTO) : this(
        playerStatistics = createPlayerStatistics(matchDTO.playerStatistics)
    )

    companion object {
        private fun createPlayerStatistics(playerStatistics: Set<PlayerStatisticDTO>): Set<PlayerStatistic> {
            val statisticsTeams = playerStatistics.map { StatisticsTeam(it.team, it.goalsInFavor, it.goalsAgainst) }
                .groupBy { it.team }
                .mapValues { it.value.reduce(StatisticsTeam::add) }

            val statisticsTeamA = statisticsTeams[PlayerStatisticDTO.Team.A]!!
            val statisticsTeamB = statisticsTeams[PlayerStatisticDTO.Team.B]!!

            if (statisticsTeamA.goalsBalance > statisticsTeamB.goalsBalance) {
                return createWinnerPlayerStatistics(statisticsTeamA, statisticsTeamB, playerStatistics)
            } else if (statisticsTeamA.goalsBalance < statisticsTeamB.goalsBalance) {
                return createWinnerPlayerStatistics(statisticsTeamB, statisticsTeamA, playerStatistics)
            }
            return createDrawPlayerStatistics(statisticsTeamA, playerStatistics)
        }

        private fun createWinnerPlayerStatistics(
            winnerStatisticsTeam: StatisticsTeam,
            loserStatisticTeam: StatisticsTeam,
            playerStatistics: Set<PlayerStatisticDTO>
        ): Set<PlayerStatistic> {
            return playerStatistics.map {
                PlayerStatistic(
                    playerId = it.playerId,
                    matches = 1,
                    victories = if (it.team == winnerStatisticsTeam.team) 1 else 0,
                    draws = 0,
                    defeats = if (it.team == loserStatisticTeam.team) 1 else 0,
                    goalsInFavor = if (it.team == winnerStatisticsTeam.team) {
                        winnerStatisticsTeam.goalsInFavor + loserStatisticTeam.goalsAgainst
                    } else {
                        loserStatisticTeam.goalsInFavor + winnerStatisticsTeam.goalsAgainst
                    },
                    goalsAgainst = if (it.team == winnerStatisticsTeam.team) {
                        winnerStatisticsTeam.goalsAgainst + loserStatisticTeam.goalsInFavor
                    } else {
                        loserStatisticTeam.goalsAgainst + winnerStatisticsTeam.goalsInFavor
                    }
                )
            }.toSet()
        }

        private fun createDrawPlayerStatistics(
            statisticsTeam: StatisticsTeam,
            playerStatistics: Set<PlayerStatisticDTO>
        ): Set<PlayerStatistic> {
            return playerStatistics.map {
                val goals = statisticsTeam.goalsInFavor + statisticsTeam.goalsAgainst
                PlayerStatistic(
                    playerId = it.playerId,
                    matches = 1,
                    victories = 0,
                    draws = 1,
                    defeats = 0,
                    goalsInFavor = goals,
                    goalsAgainst = goals
                )
            }.toSet()

        }

        data class StatisticsTeam(val team: PlayerStatisticDTO.Team, val goalsInFavor: Long, val goalsAgainst: Long) {
            val goalsBalance = goalsInFavor - goalsAgainst

            companion object {
                fun add(a: StatisticsTeam, b: StatisticsTeam) = StatisticsTeam(
                    team = a.team,
                    goalsInFavor = a.goalsInFavor + b.goalsInFavor,
                    goalsAgainst = a.goalsAgainst + b.goalsAgainst
                )
            }
        }
    }
}
