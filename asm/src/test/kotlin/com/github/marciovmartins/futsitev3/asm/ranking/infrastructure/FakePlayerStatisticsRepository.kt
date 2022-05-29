package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import java.time.LocalDate
import java.util.UUID

private val emptyPlayerStatistic = { playerId: UUID -> PlayerStatistic(playerId, 0, 0, 0, 0, 0, 0) }

class FakePlayerStatisticsRepository :
    PlayerStatisticsRepository { //TODO: too complex, what to do to improve that and make it simple?
    private val rows = mutableSetOf<Row>()

    override fun persist(
        amateurSoccerGroupId: UUID,
        gameDayId: UUID,
        gameDayDate: LocalDate,
        playersStatistics: PlayersStatistics
    ) {
        rows.add(Row(amateurSoccerGroupId, gameDayId, gameDayDate, playersStatistics))
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics {
        val collection = rows.filter { it.amateurSoccerGroupId == amateurSoccerGroupId }.toSet()
        if (collection.isEmpty()) return PlayersStatistics(0, emptySet())

        val playersStatistics = collection.map { it.playersStatistics }.reduce(FakePlayerStatisticsRepository::add)
        val matches = playersStatistics.matches
        return playersStatistics.items
            .groupBy { it.playerId }
            .mapValues { it.value.reduce(PlayerStatistic::add) }
            .values.toSet()
            .let { PlayersStatistics(matches, it) }
    }

    override fun delete(gameDayId: UUID) {
        rows.remove(rows.first { it.gameDayId === gameDayId })
    }

    fun persist(gameDay: GameDay) {
        persist(
            amateurSoccerGroupId = gameDay.amateurSoccerGroupId,
            gameDayId = gameDay.gameDayId,
            gameDayDate = gameDay.date,
            playersStatistics = gameDay.calculatePlayersStatistics(),
        )
    }

    fun exists(gameDayId: UUID): Boolean {
        return rows.firstOrNull { it.gameDayId === gameDayId } !== null
    }

    companion object {
        private fun add(a: PlayersStatistics, b: PlayersStatistics): PlayersStatistics {
            val keys = a.items.map { it.playerId }.toSet() + b.items.map { it.playerId }.toSet()
            val playerStatisticsByPlayerIdA = a.items.associateBy { it.playerId }
            val playerStatisticsByPlayerIdB = b.items.associateBy { it.playerId }
            return PlayersStatistics(
                matches = a.matches + b.matches,
                items = keys.map {
                    val playerStatisticsA = playerStatisticsByPlayerIdA[it] ?: emptyPlayerStatistic(it)
                    val playerStatisticsB = playerStatisticsByPlayerIdB[it] ?: emptyPlayerStatistic(it)
                    PlayerStatistic.add(playerStatisticsA, playerStatisticsB)
                }.toSet()
            )
        }
    }
}

private data class Row(
    val amateurSoccerGroupId: UUID,
    val gameDayId: UUID,
    val gameDayDate: LocalDate,
    val playersStatistics: PlayersStatistics,
)