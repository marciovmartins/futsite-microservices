package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.gameDay.TestGameDayEvent
import com.github.marciovmartins.futsitev3.ranking.usecase.GetPlayerStatistic
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit

class GameDayCreatedListenerIT : BaseIT() {
    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var getPlayerStatistic: GetPlayerStatistic

    @Test
    fun `calculate players statistics from game day created`() {
        // given
        val gameDayId = UUID.randomUUID()
        val gameDay = TestGameDayEvent(gameDayId = gameDayId.toString())
        val gameDayJson = objectMapper.writeValueAsString(gameDay)

        every { getPlayerStatistic.from(any()) } just Runs

        // when
        rabbitTemplate.convertAndSend("amq.topic", "futsitev3.test.ranking.gameday.created.listener", gameDayJson)

        // then
        await().atMost(1, TimeUnit.SECONDS).pollInterval(Duration.ofMillis(100)).untilAsserted {
            verify { getPlayerStatistic.from(gameDayId) }
        }
    }

    class TestGameDayListener(
        getPlayerStatistic: GetPlayerStatistic,
        objectMapper: ObjectMapper
    ) : GameDayListener(getPlayerStatistic, objectMapper) {
        @RabbitListener(queues = ["futsitev3.test.ranking.gameday.created.listener"])
        override fun receiveGameDayCreatedMessage(messageIn: String) {
            super.receiveGameDayCreatedMessage(messageIn)
        }
    }

    @TestConfiguration
    class MyConfiguration {

        @Bean
        fun testGameDayListenerBean(
            getPlayerStatistic: GetPlayerStatistic,
            objectMapper: ObjectMapper
        ): GameDayListener = TestGameDayListener(getPlayerStatistic, objectMapper)

        @Bean
        fun testGameDayCreatedQueue() = Queue("futsitev3.test.ranking.gameday.created.listener")

        @Bean
        fun binding(@Qualifier("testGameDayCreatedQueue") queue: Queue): Binding = BindingBuilder.bind(queue)
            .to(TopicExchange("amq.topic"))
            .with("futsitev3.test.ranking.gameday.created.listener")
    }
}