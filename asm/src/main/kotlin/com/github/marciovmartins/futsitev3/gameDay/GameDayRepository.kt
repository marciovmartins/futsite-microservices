package com.github.marciovmartins.futsitev3.gameDay

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.web.server.MethodNotAllowedException
import javax.persistence.EntityManager

@Suppress("unused")
interface GameDayRepository : CrudRepository<GameDay, Long>, CustomizedGameDayRepository {
    @RestResource(exported = false)
    override fun findAll(): MutableIterable<GameDay>
}

interface CustomizedGameDayRepository {
    fun <S : GameDay?> save(entity: S): S
}

@Repository
class CustomizedGameDayRepositoryImpl(private val em: EntityManager) : CustomizedGameDayRepository {
    override fun <S : GameDay?> save(entity: S): S {
        if (entity?.id != null) throw MethodNotAllowedException(
            HttpMethod.PUT,
            listOf(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
        )
        em.persist(entity)
        return entity
    }
}