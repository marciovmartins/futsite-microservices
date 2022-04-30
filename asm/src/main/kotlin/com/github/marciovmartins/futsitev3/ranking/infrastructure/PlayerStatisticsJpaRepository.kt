package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Repository
class PlayerStatisticsJpaRepository(
    private val dao: DAO
) : PlayerStatisticsRepository {
    override fun persist(amateurSoccerGroupId: UUID, playerStatistic: PlayerStatistic) {
        dao.save(PlayerStatisticEntity(amateurSoccerGroupId, playerStatistic))
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics {
        val items = dao.findByAmateurSoccerGroupId(amateurSoccerGroupId)
        return PlayersStatistics(items = items)
    }

    interface DAO : CrudRepository<PlayerStatisticEntity, Long> {
        @Query(
            value = """
            SELECT new com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic(
                    playerId,
                    SUM(matches),
                    SUM(victories),
                    SUM(draws),
                    SUM(defeats),
                    SUM(goalsInFavor),
                    SUM(goalsAgainst)
                )
            FROM ranking_players_statistics
            WHERE amateurSoccerGroupId = :amateurSoccerGroupId
            GROUP BY playerId
        """
        )
        fun findByAmateurSoccerGroupId(@Param("amateurSoccerGroupId") amateurSoccerGroupId: UUID): Set<PlayerStatistic>
    }

    @Entity(name = "ranking_players_statistics")
    data class PlayerStatisticEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Type(type = "uuid-char")
        var amateurSoccerGroupId: UUID,

        @Type(type = "uuid-char")
        var playerId: UUID,

        var matches: Long,
        var victories: Long,
        var draws: Long,
        var defeats: Long,
        var goalsInFavor: Long,
        var goalsAgainst: Long,
    ) {
        constructor(amateurSoccerGroupId: UUID, playerStatistic: PlayerStatistic) : this(
            amateurSoccerGroupId = amateurSoccerGroupId,
            playerId = playerStatistic.playerId,
            matches = playerStatistic.matches,
            victories = playerStatistic.victories,
            draws = playerStatistic.draws,
            defeats = playerStatistic.defeats,
            goalsInFavor = playerStatistic.goalsInFavor,
            goalsAgainst = playerStatistic.goalsAgainst
        )
    }
}
