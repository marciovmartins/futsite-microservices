package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.GameDayRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsByGameDay
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersRanking
import java.util.UUID

class TestDoubleGameDayRepository : GameDayRepository {
    private val items = mutableSetOf<PlayerStatisticsByGameDay>()

    override fun persist(playerStatisticsByGameDay: PlayerStatisticsByGameDay) {
        items += playerStatisticsByGameDay
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersRanking = items
        .filter { it.amateurSoccerGroupId == amateurSoccerGroupId }
        .toSet()
        .let { PlayersRanking.from(it) }
}
