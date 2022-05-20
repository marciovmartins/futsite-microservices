package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.ranking.domain.PlayersStatistics
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Repository
class JpaPlayerStatisticsRepository(
    private val matchesDAO: MatchesDAO,
    private val playerStatisticDAO: PlayerStatisticDAO,
) : PlayerStatisticsRepository {
    override fun persist(amateurSoccerGroupId: UUID, gameDayDate: LocalDate, playersStatistics: PlayersStatistics) {
        val matchesEntity = MatchesEntity(amateurSoccerGroupId, gameDayDate, playersStatistics.matches)
        matchesDAO.save(matchesEntity)

        val playerStatisticEntities = playersStatistics.items.map { PlayerStatisticEntity(amateurSoccerGroupId, it) }
        playerStatisticDAO.saveAll(playerStatisticEntities)
    }

    override fun findBy(amateurSoccerGroupId: UUID): PlayersStatistics {
        val matches = matchesDAO.findByAmateurSoccerGroupId(amateurSoccerGroupId) ?: 0
        val items = playerStatisticDAO.findByAmateurSoccerGroupId(amateurSoccerGroupId)
        return PlayersStatistics(matches = matches.toInt(), items = items)
    }

    interface PlayerStatisticDAO : CrudRepository<PlayerStatisticEntity, Long> {
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

    interface MatchesDAO : CrudRepository<MatchesEntity, Long> {
        @Query(
            value = """
            SELECT SUM(matches)
            FROM ranking_matches
            WHERE amateurSoccerGroupId = :amateurSoccerGroupId
            GROUP BY amateurSoccerGroupId
        """
        )
        fun findByAmateurSoccerGroupId(@Param("amateurSoccerGroupId") amateurSoccerGroupId: UUID): Long?
    }

    @Entity(name = "ranking_matches")
    data class MatchesEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Type(type = "uuid-char")
        var amateurSoccerGroupId: UUID,

        var gameDayDate: LocalDate,

        var matches: Long,
    ) {
        constructor(amateurSoccerGroupId: UUID, gameDayDate: LocalDate, matches: Int) : this(
            amateurSoccerGroupId = amateurSoccerGroupId,
            gameDayDate = gameDayDate,
            matches = matches.toLong(),
        )
    }
}
