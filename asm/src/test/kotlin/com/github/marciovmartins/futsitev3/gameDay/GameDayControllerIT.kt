package com.github.marciovmartins.futsitev3.gameDay

import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.gameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.ExpectedException
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.ExpectedResponseBody
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.GameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.InvalidGameDayArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.InvalidMatchArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.InvalidMatchPlayerArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.ValidGameDayArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.ValidMatchPlayerArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class GameDayControllerIT : BaseIT() {
    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidGameDayArgumentsProvider::class)
    @ArgumentsSource(ValidMatchPlayerArgumentsProvider::class)
    fun `create and retrieve a game day`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToCreate: GameDayDTO,
    ) {
        // execution
        val gameDayLocationUrl = webTestClient.post()
            .uri(traverson.follow("gameDays").asLink().href)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        val gameDay = webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange().expectStatus().isOk
            .returnResult(GameDayDTO::class.java)
            .responseBody.blockFirst()

        // assertion
        assertThat(gameDay)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(gameDayToCreate)
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidGameDayArgumentsProvider::class)
    @ArgumentsSource(ValidMatchPlayerArgumentsProvider::class)
    fun `update and retrieve a game day`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToUpdate: GameDayDTO,
    ) {
        // setup
        val gameDayToCreate = gameDayDTO()
        val gameDayLocationUrl = webTestClient.post()
            .uri(traverson.follow("gameDays").asLink().href)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()

        // execution
        webTestClient.put()
            .uri(gameDayLocationUrl)
            .bodyValue(gameDayToUpdate)
            .exchange().expectStatus().isOk
        val gameDay = webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange().expectStatus().isOk
            .returnResult(GameDayDTO::class.java)
            .responseBody.blockFirst()

        // assertion
        assertThat(gameDay)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(gameDayToUpdate)
    }

    @Test
    fun `delete existing game day`() {
        // setup
        val gameDayToCreate = gameDayDTO()
        val gameDayLocationUrl = webTestClient.post()
            .uri(traverson.follow("gameDays").asLink().href)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()

        // execution && assertion
        webTestClient.delete()
            .uri(gameDayLocationUrl)
            .exchange().expectStatus().isNoContent
        webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange().expectStatus().isNotFound
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidGameDayArgumentsProvider::class)
    @ArgumentsSource(InvalidMatchArgumentsProvider::class)
    @ArgumentsSource(InvalidMatchPlayerArgumentsProvider::class)
    fun `create game day with invalid data fails`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToCreate: GameDayDTO,
        expectedExceptions: Set<ExpectedException>,
    ) {
        // execution
        val actualExceptions = webTestClient.post()
            .uri(traverson.follow("gameDays").asLink().href)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isBadRequest
            .expectBody(ExpectedResponseBody::class.java)
            .returnResult().responseBody
        // assertions
        assertThat(actualExceptions)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(
                ExpectedResponseBody(
                    title = "Constraint Violation",
                    status = 400,
                    violations = expectedExceptions
                )
            )
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidGameDayArgumentsProvider::class)
    @ArgumentsSource(InvalidMatchArgumentsProvider::class)
    @ArgumentsSource(InvalidMatchPlayerArgumentsProvider::class)
    fun `update game day with invalid data fails`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToUpdate: GameDayDTO,
        expectedExceptions: Set<ExpectedException>
    ) {
        // setup
        val gameDayToCreate = gameDayDTO()
        val gameDayLocationUrl = webTestClient.post()
            .uri(traverson.follow("gameDays").asLink().href)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        // execution && assertion
        val actualException = webTestClient.put()
            .uri(gameDayLocationUrl)
            .bodyValue(gameDayToUpdate)
            .exchange().expectStatus().isBadRequest
            .expectBody(ExpectedResponseBody::class.java)
            .returnResult().responseBody
        // assertions
        assertThat(actualException)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(
                ExpectedResponseBody(
                    title = "Constraint Violation",
                    status = 400,
                    violations = expectedExceptions
                )
            )
    }
}
