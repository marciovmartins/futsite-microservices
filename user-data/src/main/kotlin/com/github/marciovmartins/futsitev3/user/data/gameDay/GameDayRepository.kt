package com.github.marciovmartins.futsitev3.user.data.gameDay

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import java.util.UUID

@RepositoryRestResource
interface GameDayRepository : CrudRepository<GameDay, UUID> {
    @RestResource(exported = false)
    override fun findAll(): MutableIterable<GameDay>
}