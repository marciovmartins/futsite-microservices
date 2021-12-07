package com.github.marciovmartins.futsitev3

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIT {
    @LocalServerPort
    protected var port = 0

    protected lateinit var retrofit: Retrofit

    private val objectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(ParameterNamesModule())
        .registerModule(Jdk8Module())
        .registerModule(JavaTimeModule());

    @BeforeEach
    internal fun setUpBase() {
        retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:$port")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }
}