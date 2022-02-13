package com.github.marciovmartins.futsitev3.gameDay

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RestResource

@Suppress("unused")
interface GameDayRepository : CrudRepository<GameDay, Long> {
    @RestResource(exported = false)
    override fun findAll(): MutableIterable<GameDay>
}