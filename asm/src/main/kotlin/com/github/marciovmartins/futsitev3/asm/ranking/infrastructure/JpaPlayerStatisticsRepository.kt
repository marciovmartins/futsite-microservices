package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatisticsRepository
import com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayersStatistics
import com.github.marciovmartins.futsitev3.asm.ranking.domain.ProcessedGameDay
import com.github.marciovmartins.futsitev3.asm.shared.domain.LocalDateInterval
import org.hibernate.annotations.SQLInsert
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Repository
class JpaPlayerStatisticsRepository(
    private val matchesDAO: MatchesDAO,
    private val playerStatisticDAO: PlayerStatisticDAO,
) : PlayerStatisticsRepository {
    override fun persist(processedGameDay: ProcessedGameDay) {
        val matchesEntity = MatchesEntity(
            processedGameDay.amateurSoccerGroupId,
            processedGameDay.gameDayId,
            processedGameDay.date,
            processedGameDay.playersStatistics.matches,
        )
        matchesDAO.save(matchesEntity)

        val playerStatisticEntities = processedGameDay.playersStatistics.items
            .map {
                PlayerStatisticEntity(
                    processedGameDay.amateurSoccerGroupId,
                    processedGameDay.gameDayId,
                    processedGameDay.date,
                    it
                )
            }
        playerStatisticDAO.saveAll(playerStatisticEntities)
    }

    override fun findBy(amateurSoccerGroupId: UUID, interval: LocalDateInterval): PlayersStatistics {
        val matches = matchesDAO.findNumberOfMatchesByAmateurSoccerGroupIdAndGameDayDateBetween(
            amateurSoccerGroupId,
            interval.beginInclusive,
            interval.endInclusive
        ) ?: 0
        val items = playerStatisticDAO.findByAmateurSoccerGroupIdAndGameDayBetween(
            amateurSoccerGroupId,
            interval.beginInclusive,
            interval.endInclusive
        )
        return PlayersStatistics(matches = matches.toInt(), items = items)
    }

    @Transactional
    override fun delete(gameDayId: UUID) {
        matchesDAO.deleteByGameDayId(gameDayId)
        playerStatisticDAO.deleteByGameDayId(gameDayId)
    }

    interface MatchesDAO : CrudRepository<MatchesEntity, Long> {
        @Query(
            value = """
            SELECT SUM(matches)
            FROM ranking_matches
            WHERE amateurSoccerGroupId = :amateurSoccerGroupId
                AND (gameDayDate BETWEEN :dateStart AND :dateEnd)
            GROUP BY amateurSoccerGroupId
        """
        )
        fun findNumberOfMatchesByAmateurSoccerGroupIdAndGameDayDateBetween(
            @Param("amateurSoccerGroupId") amateurSoccerGroupId: UUID,
            @Param("dateStart") dateStart: LocalDate,
            @Param("dateEnd") dateEnd: LocalDate
        ): Long?

        fun deleteByGameDayId(gameDayId: UUID)
    }

    @Entity(name = "ranking_matches")
    @SQLInsert( // https://stackoverflow.com/a/28586116/7069261
        sql = """
                INSERT INTO ranking_matches
                    (amateur_soccer_group_id, game_day_date, game_day_id, matches)
                VALUES
                    (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    matches = VALUES(matches),
                    ranking_matches_id = LAST_INSERT_ID(ranking_matches_id)
            """
    )
    data class MatchesEntity(
        @Id
        @Column(name = "ranking_matches_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Type(type = "uuid-char")
        var amateurSoccerGroupId: UUID,

        @Type(type = "uuid-char")
        var gameDayId: UUID,

        var gameDayDate: LocalDate,

        var matches: Long,
    ) {
        constructor(amateurSoccerGroupId: UUID, gameDayId: UUID, gameDayDate: LocalDate, matches: Int) : this(
            amateurSoccerGroupId = amateurSoccerGroupId,
            gameDayId = gameDayId,
            gameDayDate = gameDayDate,
            matches = matches.toLong(),
        )
    }

    interface PlayerStatisticDAO : CrudRepository<PlayerStatisticEntity, Long> {
        @Query(
            value = """
            SELECT new com.github.marciovmartins.futsitev3.asm.ranking.domain.PlayerStatistic(
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
                AND (gameDayDate BETWEEN :dateStart AND :dateEnd)
            GROUP BY playerId
        """
        )
        fun findByAmateurSoccerGroupIdAndGameDayBetween(
            @Param("amateurSoccerGroupId") amateurSoccerGroupId: UUID,
            @Param("dateStart") dateStart: LocalDate,
            @Param("dateEnd") dateEnd: LocalDate
        ): Set<PlayerStatistic>

        fun deleteByGameDayId(gameDayId: UUID)
    }

    @Entity(name = "ranking_players_statistics")
    @SQLInsert(
        sql = """
            INSERT INTO ranking_players_statistics 
                (amateur_soccer_group_id, defeats, draws, game_day_date, game_day_id, goals_against, goals_in_favor, matches, player_id, victories)
            VALUES
                (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                defeats = VALUES(defeats),
                draws = VALUES(draws),
                goals_against = VALUES(goals_against),
                goals_in_favor = VALUES(goals_in_favor),
                matches = VALUES(matches),
                victories = VALUES(victories),
                id = LAST_INSERT_ID(id)
            
        """
    )
    data class PlayerStatisticEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Type(type = "uuid-char")
        var gameDayId: UUID,

        @Type(type = "uuid-char")
        var amateurSoccerGroupId: UUID,

        var gameDayDate: LocalDate,

        @Type(type = "uuid-char")
        var playerId: UUID,

        var matches: Long,
        var victories: Long,
        var draws: Long,
        var defeats: Long,
        var goalsInFavor: Long,
        var goalsAgainst: Long,
    ) {
        constructor(
            amateurSoccerGroupId: UUID,
            gameDayId: UUID,
            gameDayDate: LocalDate,
            playerStatistic: PlayerStatistic
        ) : this(
            amateurSoccerGroupId = amateurSoccerGroupId,
            gameDayId = gameDayId,
            gameDayDate = gameDayDate,
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
