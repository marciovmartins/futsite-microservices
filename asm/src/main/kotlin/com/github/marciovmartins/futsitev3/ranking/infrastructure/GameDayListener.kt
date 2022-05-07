package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.ranking.usecase.GetPlayerStatistic
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class GameDayListener(
    private val getPlayerStatistic: GetPlayerStatistic,
    private val objectMapper: ObjectMapper,
) {
    @RabbitListener(queues = ["futsitev3.gameday.created"])
    fun receiveGameDayCreatedMessage(messageIn: String) {
        val gameDay = objectMapper.readValue(messageIn, GameDayCreatedDTO::class.java)
        getPlayerStatistic.from(gameDayId = gameDay.gameDayId)
    }
}

data class GameDayCreatedDTO(val gameDayId: UUID)
