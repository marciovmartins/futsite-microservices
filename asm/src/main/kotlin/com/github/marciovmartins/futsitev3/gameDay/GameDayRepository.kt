package com.github.marciovmartins.futsitev3.gameDay

import org.hibernate.exception.ConstraintViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.web.server.MethodNotAllowedException
import java.util.UUID
import javax.persistence.EntityManager
import javax.persistence.PersistenceException

@Suppress("unused")
interface GameDayRepository : CrudRepository<GameDay, UUID>, CustomizedGameDayRepository {
    @RestResource(exported = false)
    override fun findAll(): MutableIterable<GameDay>

    @RestResource(path = "byAmateurSoccerGroupId", rel = "byAmateurSoccerGroupId", exported = true)
    fun findByAmateurSoccerGroupId(
        @Param("amateurSoccerGroupId") amateurSoccerGroupId: UUID,
        p: Pageable
    ): Page<GameDay>
}

interface CustomizedGameDayRepository {
    fun <S : GameDay?> save(entity: S): S
}

@Repository
class CustomizedGameDayRepositoryImpl(private val em: EntityManager) : CustomizedGameDayRepository {
    override fun <S : GameDay?> save(entity: S): S {
        try {
            em.persist(entity)
        } catch (e: PersistenceException) {
            throwPersistenceException(e)
        }
        return entity
    }

    /**
     * Disabled because of this bug:
     * https://stackoverflow.com/questions/71041196/how-to-update-an-aggregate-with-nested-lists-using-spring-boot-data-rest-jpa-and
     */
    private fun throwPutMethodNotAllowed(): Unit = throw MethodNotAllowedException(
        HttpMethod.PUT,
        listOf(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
    )

    private fun throwPersistenceException(e: PersistenceException) {
        when (val cause = e.cause) {
            is ConstraintViolationException -> if (cause.constraintName == "game_days.game_days_amateur_soccer_group_id_date_uindex") {
                throw GameDayDateNotUniqueConstraintViolation()
            }
        }
        throw e
    }
}