package com.github.marciovmartins.futsitev3.user.data.player

import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import java.util.UUID

@RepositoryRestResource
interface PlayerRepository : CrudRepository<Player, UUID> {
    @RestResource(path = "byAmateurSoccerGroupId", rel = "byAmateurSoccerGroupId", exported = true)
    fun findByAmateurSoccerGroupId(
        @Param("amateurSoccerGroupId") amateurSoccerGroupId: UUID,
        @ParameterObject p: Pageable
    ): Page<Player>
}