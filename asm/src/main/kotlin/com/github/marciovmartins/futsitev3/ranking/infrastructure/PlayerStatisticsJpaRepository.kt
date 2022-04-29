package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class PlayerStatisticsJpaRepository() : PlayerStatisticsRepository {
    override fun persist(amateurSoccerGroupId: UUID, playerStatistic: PlayerStatistic) {
        TODO("Not yet implemented")
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics {
        return PlayersStatistics(items = emptySet())
    }
}