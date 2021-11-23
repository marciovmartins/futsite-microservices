package com.github.marciovmartins.futsitev3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping

@SpringBootApplication
class AsmApplication

fun main(args: Array<String>) {
    runApplication<AsmApplication>(*args)
}

@Controller
class HelloWorldController(
    private val helloWorldService: HelloWorldService
) {

    @GetMapping("/")
    fun helloWorld(): ResponseEntity<String> = ResponseEntity.ok(helloWorldService.call())
}

@Service
class HelloWorldService {
    fun call() = "Hello World!"
}