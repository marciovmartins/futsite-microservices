package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.gameDay.TestGameDayCreated
import com.github.marciovmartins.futsitev3.ranking.usecase.GetPlayerStatistic
import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

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
        val gameDay = TestGameDayCreated(gameDayId = gameDayId.toString())
        val gameDayJson = objectMapper.writeValueAsString(gameDay)

        // when
        rabbitTemplate.convertAndSend("futsitev3.gameday.created", gameDayJson)

        // then
        verify { getPlayerStatistic.from(gameDayId) }
    }
}