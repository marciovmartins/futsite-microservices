package com.github.marciovmartins.futsitev3

import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.hateoas.MediaTypes
import org.springframework.hateoas.client.Traverson
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIT {
    @LocalServerPort
    protected var port = 0

    protected lateinit var webClient: WebClient

    protected lateinit var traverson: Traverson

    @BeforeEach
    internal fun setUpBase() {
        webClient = WebClient.create("http://localhost:$port")
        traverson = Traverson(URI.create("http://localhost:$port"), MediaTypes.HAL_JSON)
    }
}