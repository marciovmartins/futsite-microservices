package com.github.marciovmartins.futsitev3.user.data.gameDay

import com.github.marciovmartins.futsitev3.user.data.BaseIT
import com.github.marciovmartins.futsitev3.user.data.gameDay.argumentsprovider.ValidGameDayArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.UUID

class GameDayControllerIT : BaseIT() {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidGameDayArgumentsProvider::class)
    fun `create and retrieve a game day`(
        description: String,
        gameDayToCreate: GameDayDTO,
    ) {
        // setup
        val gameDayId = UUID.randomUUID().toString()

        // execution
        val gameDayLocationUrl = webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        val gameDay = webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange().expectStatus().isOk
            .returnResult(GameDayDTO::class.java)
            .responseBody.blockFirst()

        // assertions
        assertThat(gameDay)
            .isNotNull
            .usingRecursiveComparison()
            .isEqualTo(gameDayToCreate)
    }
}