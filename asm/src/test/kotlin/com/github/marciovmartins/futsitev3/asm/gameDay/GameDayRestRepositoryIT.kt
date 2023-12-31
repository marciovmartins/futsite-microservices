package com.github.marciovmartins.futsitev3.asm.gameDay

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.asm.BaseIT
import com.github.marciovmartins.futsitev3.asm.gameDay.GameDayFixture.testGameDayDTO
import com.github.marciovmartins.futsitev3.asm.gameDay.argumentsprovider.InvalidGameDayArgumentsProvider
import com.github.marciovmartins.futsitev3.asm.gameDay.argumentsprovider.InvalidMatchArgumentsProvider
import com.github.marciovmartins.futsitev3.asm.gameDay.argumentsprovider.InvalidPlayerStatisticsArgumentsProvider
import com.github.marciovmartins.futsitev3.asm.gameDay.argumentsprovider.ValidGameDayArgumentsProvider
import com.github.marciovmartins.futsitev3.asm.gameDay.argumentsprovider.ValidMatchPlayerArgumentsProvider
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.http.HttpStatus
import java.time.Duration
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.TimeUnit

class GameDayRestRepositoryIT : BaseIT() {
    companion object {
        val gameDayCreatedMessages = mutableListOf<TestGameDayEvent>()
        val gameDayDeletedMessages = mutableListOf<TestGameDayEvent>()
        val gameDayUpdatedMessages = mutableListOf<TestGameDayEvent>()
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidGameDayArgumentsProvider::class)
    @ArgumentsSource(ValidMatchPlayerArgumentsProvider::class)
    fun `create and retrieve a game day`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToCreate: TestGameDayDTO,
    ) {
        // setup
        val gameDayId = UUID.randomUUID().toString()
        val expectedMessage = TestGameDayEvent(gameDayId = gameDayId)

        // execution
        val gameDayLocationUrl = webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        val responseGet = webTestClient.get()
            .uri(gameDayLocationUrl)
            .exchange()

        // assertion
        responseGet.expectStatus().isOk
        val gameDay = responseGet.returnResult(TestGameDayDTO::class.java).responseBody.blockFirst()
        assertThat(gameDay)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(gameDayToCreate)

        await().atMost(1, TimeUnit.SECONDS).pollInterval(Duration.ofMillis(100)).untilAsserted {
            assertThat(gameDayCreatedMessages).contains(expectedMessage)
        }
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(ValidGameDayArgumentsProvider::class)
    @ArgumentsSource(ValidMatchPlayerArgumentsProvider::class)
    fun `update and retrieve a game day`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToUpdate: TestGameDayDTO,
    ) {
        // setup
        val gameDayId = UUID.randomUUID().toString()
        val gameDayToCreate = testGameDayDTO().copy(amateurSoccerGroupId = gameDayToUpdate.amateurSoccerGroupId)
        val gameDayLocationUrl = webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()
        val expectedMessage = TestGameDayEvent(gameDayId = gameDayId)

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
        val gameDay = responseGet.returnResult(TestGameDayDTO::class.java).responseBody.blockFirst()
        assertThat(gameDay)
            .isNotNull
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .isEqualTo(gameDayToUpdate)

        await().atMost(1, TimeUnit.SECONDS).pollInterval(Duration.ofMillis(100)).untilAsserted {
            assertThat(gameDayUpdatedMessages).contains(expectedMessage)
        }
    }

    @Test
    fun `delete existing game day`() {
        // setup
        val gameDayId = UUID.randomUUID().toString()
        val gameDayToCreate = testGameDayDTO()
        val gameDayLocationUrl = webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
            .bodyValue(gameDayToCreate)
            .exchange().expectStatus().isCreated
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()

        val expectedDeletedMessage = TestGameDayEvent(gameDayId)

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

        await().atMost(1, TimeUnit.SECONDS).pollInterval(Duration.ofMillis(100)).untilAsserted {
            assertThat(gameDayDeletedMessages).contains(expectedDeletedMessage)
        }
    }

    @ParameterizedTest(name = "{0}")
    @ArgumentsSource(InvalidGameDayArgumentsProvider::class)
    @ArgumentsSource(InvalidMatchArgumentsProvider::class)
    @ArgumentsSource(InvalidPlayerStatisticsArgumentsProvider::class)
    fun `create game day with invalid data fails`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToCreate: TestGameDayDTO,
        expectedExceptions: Set<ExpectedException>,
    ) {
        // setup
        val gameDayId = UUID.randomUUID().toString()

        // execution
        val response = webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
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
    @ArgumentsSource(InvalidPlayerStatisticsArgumentsProvider::class)
    fun `update game day with invalid data fails`(
        @Suppress("UNUSED_PARAMETER") description: String,
        gameDayToUpdate: TestGameDayDTO,
        expectedExceptions: Set<ExpectedException>
    ) {
        // setup
        val gameDayId = UUID.randomUUID().toString()
        val gameDayToCreate = testGameDayDTO().copy(amateurSoccerGroupId = gameDayToUpdate.amateurSoccerGroupId)
        val gameDayLocationUrl = webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
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

    @Test
    fun `cannot add two game days with the same day`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID().toString()
        val date = LocalDate.now().toString()
        val gameDayToCreate = testGameDayDTO(amateurSoccerGroupId = amateurSoccerGroupId, date = date)
        val secondGameDayToCreate = testGameDayDTO(amateurSoccerGroupId = amateurSoccerGroupId, date = date)
        webTestClient.put()
            .uri("gameDays/{id}", UUID.randomUUID().toString())
            .bodyValue(gameDayToCreate)
            .exchange()
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()

        // execution
        val response = webTestClient.put()
            .uri("gameDays/{id}", UUID.randomUUID().toString())
            .bodyValue(secondGameDayToCreate)
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
                    violations = setOf(
                        ExpectedException(
                            message = "A game day for this date already exists",
                            field = "date"
                        )
                    )
                )
            )
    }

    @Test
    fun `do not allow update the amateur soccer group id`() {
        // setup
        val gameDayId = UUID.randomUUID().toString()
        val gameDayToCreate = testGameDayDTO()
        val gameDayToUpdate = gameDayToCreate.copy(amateurSoccerGroupId = UUID.randomUUID().toString())
        webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
            .bodyValue(gameDayToCreate)
            .exchange()
            .returnResult(Unit::class.java)
            .responseHeaders.location.toString()

        // execution
        val response = webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
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
                    violations = setOf(
                        ExpectedException(message = "Cannot update amateur soccer group id")
                    )
                )
            )
    }

    @Test
    fun `validate finding 0 game days by amateurSoccerGroupId`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID().toString()

        // execution
        val response = webTestClient.get()
            .uri("gameDays/search/byAmateurSoccerGroupId?amateurSoccerGroupId={id}", amateurSoccerGroupId)
            .exchange()

        // assertions
        val actualException = response.expectStatus().isOk
            .expectBody(GameDayCollection::class.java)
            .returnResult().responseBody
        assertThat(actualException)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .ignoringFields("_links.self.href")
            .isEqualTo(
                GameDayCollection(
                    _embedded = GameDayCollection.EmbeddedGameDays(emptyList()),
                    _links = Links(self = Links.Link("something")),
                    page = Page(size = 20, totalElements = 0, totalPages = 0, number = 0)
                )
            )
    }

    @Test
    fun `validate finding 1 game day by amateurSoccerGroupId`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID().toString()
        val gameDay = createGameDay(amateurSoccerGroupId)

        // execution
        val response = webTestClient.get()
            .uri("gameDays/search/byAmateurSoccerGroupId?amateurSoccerGroupId={id}", amateurSoccerGroupId)
            .exchange()

        // assertions
        val actualException = response.expectStatus().isOk
            .expectBody(GameDayCollection::class.java)
            .returnResult().responseBody
        assertThat(actualException)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .ignoringFields("_links.self.href")
            .isEqualTo(
                GameDayCollection(
                    _embedded = GameDayCollection.EmbeddedGameDays(listOf(gameDay)),
                    _links = Links(self = Links.Link("something")),
                    page = Page(size = 20, totalElements = 1, totalPages = 1, number = 0)
                )
            )
    }

    @Test
    fun `validate finding multiple game days by amateurSoccerGroupId`() {
        // setup
        val amateurSoccerGroupId = UUID.randomUUID().toString()
        val gameDay1 = createGameDay(amateurSoccerGroupId, LocalDate.now())
        val gameDay2 = createGameDay(amateurSoccerGroupId, LocalDate.now().minusDays(1))
        val gameDay3 = createGameDay(amateurSoccerGroupId, LocalDate.now().minusDays(2))
        val gameDay4 = createGameDay(amateurSoccerGroupId, LocalDate.now().minusDays(3))
        val gameDay5 = createGameDay(amateurSoccerGroupId, LocalDate.now().minusDays(4))

        // execution
        val response = webTestClient.get()
            .uri("gameDays/search/byAmateurSoccerGroupId?amateurSoccerGroupId={id}", amateurSoccerGroupId)
            .exchange()

        // assertions
        val actualException = response.expectStatus().isOk
            .expectBody(GameDayCollection::class.java)
            .returnResult().responseBody
        assertThat(actualException)
            .usingRecursiveComparison()
            .ignoringCollectionOrder()
            .ignoringFields("_links.self.href")
            .isEqualTo(
                GameDayCollection(
                    _embedded = GameDayCollection.EmbeddedGameDays(
                        listOf(
                            gameDay1, gameDay2, gameDay3, gameDay4, gameDay5,
                        )
                    ),
                    _links = Links(self = Links.Link("something")),
                    page = Page(size = 20, totalElements = 5, totalPages = 1, number = 0)
                )
            )
    }

    private fun createGameDay(amateurSoccerGroupId: Any? = null, date: Any? = LocalDate.now()): TestGameDayDTO {
        val gameDayToCreate = testGameDayDTO(amateurSoccerGroupId = amateurSoccerGroupId, date = date)
        val gameDayId = UUID.randomUUID().toString()
        webTestClient.put()
            .uri("gameDays/{id}", gameDayId)
            .bodyValue(gameDayToCreate)
            .exchange()
        return webTestClient.get()
            .uri("gameDays/{id}", gameDayId)
            .exchange()
            .expectBody(TestGameDayDTO::class.java)
            .returnResult().responseBody!!
    }

    @EnableRabbit
    @TestConfiguration
    class MyConfiguration {
        @Autowired
        lateinit var objectMapper: ObjectMapper

        @RabbitListener(
            bindings = [QueueBinding(
                value = Queue("futsitev3.test.gameday.created.GameDayRestRepositoryIT"),
                exchange = Exchange("amq.topic", type = "topic"),
                key = ["futsitev3.gameday.created"]
            )]
        )
        fun receiveGameDayCreatedEvent(messageIn: String) {
            try {
                gameDayCreatedMessages += objectMapper.readValue(messageIn, TestGameDayEvent::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @RabbitListener(
            bindings = [QueueBinding(
                value = Queue("futsitev3.test.gameday.deleted.GameDayRestRepositoryIT"),
                exchange = Exchange("amq.topic", type = "topic"),
                key = ["futsitev3.gameday.deleted"]
            )]
        )
        fun receiveGameDayDeletedEvent(messageIn: String) {
            try {
                gameDayDeletedMessages += objectMapper.readValue(messageIn, TestGameDayEvent::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @RabbitListener(
            bindings = [QueueBinding(
                value = Queue("futsitev3.test.gameday.updated.GameDayRestRepositoryIT"),
                exchange = Exchange("amq.topic", type = "topic"),
                key = ["futsitev3.gameday.updated"]
            )]
        )
        fun receiveGameDayUpdatedEvent(messageIn: String) {
            try {
                gameDayUpdatedMessages += objectMapper.readValue(messageIn, TestGameDayEvent::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
