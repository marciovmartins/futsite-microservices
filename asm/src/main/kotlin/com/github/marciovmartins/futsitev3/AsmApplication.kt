package com.github.marciovmartins.futsitev3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class AsmApplication

fun main(args: Array<String>) {
    runApplication<AsmApplication>(*args)
}

@RestController
@RequestMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
class HelloWorldController(
    private val helloWorldService: HelloWorldService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun helloWorld(
        @RequestParam name: String?
    ) = ResponseEntity.ok(helloWorldService.call(name))
}

@Service
class HelloWorldService {
    fun call(name: String? = null): HelloWorld {
        val s = name ?: "John Doe"
        return HelloWorld("Hello World, $s!", s)
    }
}

data class HelloWorld(
    val message: String,
    val name: String
)