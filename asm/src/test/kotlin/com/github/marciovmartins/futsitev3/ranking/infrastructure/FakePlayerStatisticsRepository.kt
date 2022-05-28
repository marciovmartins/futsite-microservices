package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.GameDay
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import java.time.LocalDate
import java.util.UUID

private val emptyPlayerStatistic = { playerId: UUID -> PlayerStatistic(playerId, 0, 0, 0, 0, 0, 0) }

class FakePlayerStatisticsRepository :
    PlayerStatisticsRepository { //TODO: too complex, what to do to improve that and make it simple?
    private val rows = mutableMapOf<UUID, MutableMap<LocalDate, PlayersStatistics>>()

    override fun persist(amateurSoccerGroupId: UUID, gameDayDate: LocalDate, playersStatistics: PlayersStatistics) {
        val map = rows.computeIfAbsent(amateurSoccerGroupId) { mutableMapOf() }
        map[gameDayDate] = playersStatistics
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics {
        val collection = rows[amateurSoccerGroupId]
        if (collection == null || collection.isEmpty()) return PlayersStatistics(0, emptySet())

        val playersStatistics = collection.values.reduce(FakePlayerStatisticsRepository::add)
        val matches = playersStatistics.matches
        return playersStatistics.items
            .groupBy { it.playerId }
            .mapValues { it.value.reduce(PlayerStatistic::add) }
            .values.toSet()
            .let { PlayersStatistics(matches, it) }
    }

    override fun delete(amateurSoccerGroupId: UUID, gameDayDate: LocalDate) {
        rows[amateurSoccerGroupId]?.remove(gameDayDate)
    }

    fun persist(gameDay: GameDay) {
        persist(
            amateurSoccerGroupId = gameDay.amateurSoccerGroupId,
            gameDayDate = gameDay.date,
            playersStatistics = gameDay.calculatePlayersStatistics(),
        )
    }

    fun exists(amateurSoccerGroupId: UUID, gameDayDate: LocalDate): Boolean {
        val amateurSoccerGroupResult = rows[amateurSoccerGroupId] ?: return false
        return amateurSoccerGroupResult[gameDayDate] !== null
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
