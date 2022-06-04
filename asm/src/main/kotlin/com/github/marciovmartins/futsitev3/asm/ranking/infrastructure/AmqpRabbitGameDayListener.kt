package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.DeletePlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.GetPlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.UpdatePlayerStatic
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.util.UUID

private const val gameDayCreatedRoutingKey = "futsitev3.gameday.created"
private const val gameDayDeletedRoutingKey = "futsitev3.gameday.deleted"
private const val gameDayUpdatedRoutingKey = "futsitev3.gameday.updated"

@Component
class AmqpRabbitGameDayListener(
    private val getPlayerStatistic: GetPlayerStatistic,
    private val deletePlayerStatistic: DeletePlayerStatistic,
    private val updatePlayerStatic: UpdatePlayerStatic,
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
            val gameDay = objectMapper.readValue<GameDayEventDTO>(messageIn)
            getPlayerStatistic.from(gameDayId = gameDay.gameDayId)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(gameDayDeletedRoutingKey),
            exchange = Exchange("amq.topic", type = "topic"),
            key = [gameDayDeletedRoutingKey]
        )]
    )
    fun receiveGameDayDeletedMessage(messageIn: String) {
        try {
            val gameDay = objectMapper.readValue<GameDayEventDTO>(messageIn)
            deletePlayerStatistic.with(gameDayId = gameDay.gameDayId)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @RabbitListener(
        bindings = [QueueBinding(
            value = Queue(gameDayUpdatedRoutingKey),
            exchange = Exchange("amq.topic", type = "topic"),
            key = [gameDayUpdatedRoutingKey]
        )]
    )
    fun receiveGameDayUpdatedMessage(messageIn: String) {
        try {
            val gameDayUpdatedDTO = objectMapper.readValue<GameDayEventDTO>(messageIn)
            updatePlayerStatic.with(gameDayUpdatedDTO.gameDayId)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

data class GameDayEventDTO(val gameDayId: UUID)
