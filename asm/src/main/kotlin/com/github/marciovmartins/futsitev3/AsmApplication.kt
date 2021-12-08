package com.github.marciovmartins.futsitev3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AsmApplication

fun main(args: Array<String>) {
    runApplication<AsmApplication>(*args)
}
