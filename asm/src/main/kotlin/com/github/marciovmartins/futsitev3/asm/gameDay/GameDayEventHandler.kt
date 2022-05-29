package com.github.marciovmartins.futsitev3.asm.gameDay

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.rest.core.annotation.HandleAfterCreate
import org.springframework.data.rest.core.annotation.HandleAfterDelete
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
        val gameDayCreated = GameDayEvent(gameDayId = gameDay.id!!)
        val gameDayCreatedJson = objectMapper.writeValueAsString(gameDayCreated)
        rabbitTemplate.convertAndSend("amq.topic", "futsitev3.gameday.created", gameDayCreatedJson)
    }

    @HandleAfterDelete
    fun handleGameDayAfterDelete(gameDay: GameDay) {
        val gameDayDeleted = GameDayEvent(gameDayId = gameDay.id!!)
        val gameDayDeletedJson = objectMapper.writeValueAsString(gameDayDeleted)
        rabbitTemplate.convertAndSend("amq.topic", "futsitev3.gameday.deleted", gameDayDeletedJson)
    }
}

data class GameDayEvent(val gameDayId: UUID)