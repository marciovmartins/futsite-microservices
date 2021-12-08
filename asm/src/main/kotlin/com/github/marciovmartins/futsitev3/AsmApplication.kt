package com.github.marciovmartins.futsitev3

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.validation.Validator


@SpringBootApplication
class AsmApplication

fun main(args: Array<String>) {
    runApplication<AsmApplication>(*args)
}

@Configuration
class RestValidationConfiguration : RepositoryRestConfigurer {
    @Autowired
    private lateinit var validator: Validator

    override fun configureValidatingRepositoryEventListener(validatingListener: ValidatingRepositoryEventListener) {
        validatingListener.addValidator("beforeCreate", validator)
        validatingListener.addValidator("beforeSave", validator)
    }
}
