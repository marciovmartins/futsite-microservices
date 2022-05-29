package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.gameDay.TestGameDayEvent
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.DeletePlayerStatistic
import com.github.marciovmartins.futsitev3.asm.ranking.usecase.GetPlayerStatistic
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit

private const val gameDayCreatedRoutingKey = "futsitev3.test.ranking.gameday.created.GameDayCreatedListenerIT"
private const val gameDayDeletedRoutingKey = "futsitev3.test.ranking.gameday.deleted.GameDayCreatedListenerIT"

class GameDayCreatedListenerIT : BaseIT() {
    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var getPlayerStatistic: GetPlayerStatistic

    @MockkBean
    lateinit var deletePlayerStatistic: DeletePlayerStatistic

    @Test
    fun `calculate players statistics from game day created event`() {
        // given
        val gameDayId = UUID.randomUUID()
        val gameDay = TestGameDayEvent(gameDayId = gameDayId.toString())
        val gameDayJson = objectMapper.writeValueAsString(gameDay)

        every { getPlayerStatistic.from(any()) } just Runs

        // when
        rabbitTemplate.convertAndSend("amq.topic", gameDayCreatedRoutingKey, gameDayJson)

        // then
        await().atMost(1, TimeUnit.SECONDS).pollInterval(Duration.ofMillis(100)).untilAsserted {
            verify { getPlayerStatistic.from(gameDayId) }
        }
    }

    @Test
    fun `delete players statistics from game day deleted event`() {
        // given
        val gameDayId = UUID.randomUUID()
        val gameDay = TestGameDayEvent(gameDayId = gameDayId.toString())
        val gameDayJson = objectMapper.writeValueAsString(gameDay)

        every { deletePlayerStatistic.with(any()) } just Runs

        // when
        rabbitTemplate.convertAndSend("amq.topic", gameDayDeletedRoutingKey, gameDayJson)

        // then
        await().atMost(1, TimeUnit.SECONDS).pollInterval(Duration.ofMillis(100)).untilAsserted {
            verify { deletePlayerStatistic.with(gameDayId) }
        }
    }

    class TestGameDayListener(
        getPlayerStatistic: GetPlayerStatistic,
        deletePlayerStatistic: DeletePlayerStatistic,
        objectMapper: ObjectMapper
    ) : GameDayListener(getPlayerStatistic, deletePlayerStatistic, objectMapper) {
        @RabbitListener(
            bindings = [QueueBinding(
                value = Queue(gameDayCreatedRoutingKey),
                exchange = Exchange("amq.topic", type = "topic"),
                key = [gameDayCreatedRoutingKey]
            )]
        )
        override fun receiveGameDayCreatedMessage(messageIn: String) {
            super.receiveGameDayCreatedMessage(messageIn)
        }

        @RabbitListener(
            bindings = [QueueBinding(
                value = Queue(gameDayDeletedRoutingKey),
                exchange = Exchange("amq.topic", type = "topic"),
                key = [gameDayDeletedRoutingKey]
            )]
        )
        override fun receiveGameDayDeletedMessage(messageIn: String) {
            super.receiveGameDayDeletedMessage(messageIn)
        }
    }

    @TestConfiguration
    class MyConfiguration {
        @Bean
        fun testGameDayListenerBean(
            getPlayerStatistic: GetPlayerStatistic,
            deletePlayerStatistic: DeletePlayerStatistic,
            objectMapper: ObjectMapper
        ): GameDayListener = TestGameDayListener(getPlayerStatistic, deletePlayerStatistic, objectMapper)
    }
}