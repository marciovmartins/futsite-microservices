package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.shared.domain.LocalDateInterval
import java.util.UUID

private val emptyPlayerStatistic = { playerId: UUID -> PlayerStatistic(playerId, 0, 0, 0, 0, 0, 0) }

//TODO: too complex, what to do to improve that and make it simple?
class FakePlayerStatisticsRepository : PlayerStatisticsRepository {
    private val rows = mutableMapOf<UUID, ProcessedGameDay>()

    override fun persist(processedGameDay: ProcessedGameDay) {
        rows[processedGameDay.gameDayId] = (processedGameDay)
    }

    override fun findBy(amateurSoccerGroupId: UUID, interval: LocalDateInterval): PlayersStatistics {
        val collection = rows.filterValues {
            it.amateurSoccerGroupId == amateurSoccerGroupId && interval.contains(it.date)
        }.values.toSet()
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
        rows.remove(gameDayId)
    }

    fun exists(gameDayId: UUID): Boolean {
        return rows[gameDayId] !== null
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

