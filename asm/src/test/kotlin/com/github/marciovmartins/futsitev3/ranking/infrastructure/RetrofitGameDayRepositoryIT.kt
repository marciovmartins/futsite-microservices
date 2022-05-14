package com.github.marciovmartins.futsitev3.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.ranking.domain.defaultGameDay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.integration.ClientAndServer
import org.mockserver.integration.ClientAndServer.startClientAndServer
import org.mockserver.matchers.Times.exactly
import org.mockserver.model.Header
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID


class RetrofitGameDayRepositoryIT : BaseIT() {
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var asmGameDayProperties: AsmGameDayProperties

    private lateinit var mockServer: ClientAndServer

    @BeforeEach
    fun startServer() {
        mockServer = startClientAndServer(1080)
    }

    @AfterEach
    fun stopServer() {
        mockServer.stop()
    }

    @Test
    fun `retrieve game day from http service`() {
        // given
        val gameDayId = UUID.randomUUID()
        val amateurSoccerGroupId = UUID.randomUUID()
        val player1 = UUID.randomUUID()
        val player2 = UUID.randomUUID()
        val player3 = UUID.randomUUID()
        val player4 = UUID.randomUUID()

        val expectedGameDay = defaultGameDay(player1, player2, player3, player4, gameDayId, amateurSoccerGroupId)
        val gameDayResponse = defaultGameDayResponse(player1, player2, player3, player4, gameDayId, amateurSoccerGroupId)

        mockServer.`when`(
            request()
                .withMethod("GET")
                .withPath("/gameDays/" + expectedGameDay.gameDayId),
            exactly(1)
        ).respond(
            response()
                .withStatusCode(200)
                .withHeaders(
                    Header("Content-Type", "application/json; charset=utf-8"),
                )
                .withBody(objectMapper.writeValueAsString(gameDayResponse))
        )

        // when
        val gameDayRepository = RetrofitGameDayRepository(asmGameDayProperties, objectMapper)
        val gameDay = gameDayRepository.findBy(expectedGameDay.gameDayId)

        // then
        assertThat(gameDay)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(expectedGameDay)
    }
}