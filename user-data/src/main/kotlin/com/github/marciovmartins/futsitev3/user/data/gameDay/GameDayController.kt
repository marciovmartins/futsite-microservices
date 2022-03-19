package com.github.marciovmartins.futsitev3.user.data.gameDay

import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.UUID

@RepositoryRestResource
interface GameDayController : CrudRepository<GameDay, UUID>