package com.github.marciovmartins.futsitev3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HelloWorldServiceTest {
    @Test
    fun `is called should return hello world string`() {
        // setup
        val expected = "Hello World!"
        val helloWorldService = HelloWorldService()
        // execution
        val result = helloWorldService.call()
        // assertions
        assertThat(result).isEqualTo(expected)
    }
}