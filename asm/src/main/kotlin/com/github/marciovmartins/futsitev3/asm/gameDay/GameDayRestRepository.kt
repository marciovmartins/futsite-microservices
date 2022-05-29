package com.github.marciovmartins.futsitev3.asm.gameDay

import org.hibernate.exception.ConstraintViolationException
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.stereotype.Repository
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status
import org.zalando.problem.violations.Violation
import java.util.UUID
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@RepositoryRestResource
interface GameDayRestRepository : CrudRepository<GameDay, UUID>, CustomizedGameDayRestRepository {
    @RestResource(exported = false)
    override fun findAll(): MutableIterable<GameDay>

    @RestResource(path = "byAmateurSoccerGroupId", rel = "byAmateurSoccerGroupId", exported = true)
    fun findByAmateurSoccerGroupId(
        @Param("amateurSoccerGroupId") amateurSoccerGroupId: UUID,
        @ParameterObject p: Pageable
    ): Page<GameDay>
}

interface CustomizedGameDayRestRepository {
    fun <S : GameDay?> save(entity: S): S
}

@Repository
class CustomizedGameDayRestRepositoryImpl(private val em: EntityManager) : CustomizedGameDayRestRepository {
    override fun <S : GameDay?> save(entity: S): S {
        try {
            em.persist(entity)
        } catch (e: PersistenceException) {
            throwPersistenceException(e)
        }
        return entity
    }

    private fun throwPersistenceException(e: PersistenceException) {
        when (val cause = e.cause) {
            is ConstraintViolationException -> if (cause.constraintName == "game_days.game_days_amateur_soccer_group_id_date_uindex") {
                throw GameDayDateNotUniqueConstraintViolation()
            }
        }
        throw e
    }

    private class GameDayDateNotUniqueConstraintViolation : AbstractThrowableProblem(
        null,
        "Constraint Violation",
        Status.BAD_REQUEST,
        null
    ) {
        override fun getCause(): Exceptional? = super.cause

        @Suppress("unused")
        val violations = setOf(Violation("date", "A game day for this date already exists"))
    }
}