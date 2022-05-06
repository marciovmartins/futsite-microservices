package com.github.marciovmartins.futsitev3.gameDay

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.RepositoryEventHandler
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@RepositoryEventHandler(GameDay::class)
class GameDayEventHandler(
    private val rabbitTemplate: RabbitTemplate,
    private val objectMapper: ObjectMapper,
) {
    @HandleAfterCreate
    fun handleGameDayAfterSave(gameDay: GameDay) {
        val gameDayCreated = GameDayCreated(gameDayId = gameDay.id!!)
        val gameDayCreatedJson = objectMapper.writeValueAsString(gameDayCreated)
        rabbitTemplate.convertAndSend("futsitev3.gameday.created", gameDayCreatedJson)
    }
}

data class GameDayCreated(val gameDayId: UUID)