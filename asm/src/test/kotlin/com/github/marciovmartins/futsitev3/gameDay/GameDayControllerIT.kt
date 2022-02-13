package com.github.marciovmartins.futsitev3.gameDay

import com.github.marciovmartins.futsitev3.BaseIT
import com.github.marciovmartins.futsitev3.gameDay.GameDayFixture.gameDayDTO
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.InvalidGameDayArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.InvalidMatchArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.InvalidPlayerArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.ValidGameDayArgumentsProvider
import com.github.marciovmartins.futsitev3.gameDay.argumentsprovider.ValidMatchPlayerArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.http.HttpStatus

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
        val responseGet = webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange()
        // assertion
        responseGet.expectStatus().isOk
        val gameDay = responseGet.returnResult(GameDayDTO::class.java).responseBody.blockFirst()
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
        val responsePut = webTestClient.put()
            .uri(gameDayLocationUrl)
            .bodyValue(gameDayToUpdate)
            .exchange()
        val responseGet = webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange()
        // assertion
        responsePut.expectStatus().isOk
        responseGet.expectStatus().isOk
        val gameDay = responseGet.returnResult(GameDayDTO::class.java).responseBody.blockFirst()
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
        // execution
        val responseDelete = webTestClient.delete()
            .uri(gameDayLocationUrl)
            .exchange()
        val responseGet = webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange()
        // assertions
        responseDelete.expectStatus().isNoContent
        responseGet.expectStatus().isNotFound
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidGameDayArgumentsProvider::class)
    @ArgumentsSource(InvalidMatchArgumentsProvider::class)
    @ArgumentsSource(InvalidPlayerArgumentsProvider::class)
    fun `create game day with invalid data fails`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToCreate: GameDayDTO,
        expectedExceptions: Set<ExpectedException>,
    ) {
        // execution
        val response = webTestClient.post()
            .uri(traverson.follow("gameDays").asLink().href)
            .bodyValue(gameDayToCreate)
            .exchange()
        // assertions
        val actualExceptions = response.expectStatus().isBadRequest
            .expectBody(ExpectedResponseBody::class.java)
            .returnResult().responseBody
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
    @ArgumentsSource(InvalidPlayerArgumentsProvider::class)
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
            .exchange()
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        // execution
        val response = webTestClient.put()
            .uri(gameDayLocationUrl)
            .bodyValue(gameDayToUpdate)
            .exchange()
        // assertions
        val actualException = response.expectStatus().isBadRequest
            .expectBody(ExpectedResponseBody::class.java)
            .returnResult().responseBody
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

    @Test
    fun `do not allow retrieve multiple game days`() {
        // execution
        val response = webTestClient.get()
            .uri(traverson.follow("gameDays").asLink().href)
            .exchange()
        // assertions
        response.expectStatus().isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
    }
}
