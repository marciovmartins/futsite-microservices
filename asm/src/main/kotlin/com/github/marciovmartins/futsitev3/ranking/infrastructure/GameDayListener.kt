package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.ranking.usecase.GetPlayerStatistic
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.util.UUID

private const val gameDayCreatedRoutingKey = "futsitev3.gameday.created"

@Component
class GameDayListener(
    private val getPlayerStatistic: GetPlayerStatistic,
    private val objectMapper: ObjectMapper,
) {
    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(gameDayCreatedRoutingKey),
            exchange = Exchange("amq.topic", type = "topic"),
            key = [gameDayCreatedRoutingKey]
        )]
    )
    fun receiveGameDayCreatedMessage(messageIn: String) {
        try {
            val gameDay = objectMapper.readValue(messageIn, GameDayCreatedDTO::class.java)
            getPlayerStatistic.from(gameDayId = gameDay.gameDayId)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

data class GameDayCreatedDTO(val gameDayId: UUID)
