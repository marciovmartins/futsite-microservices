package com.github.marciovmartins.futsitev3.matches

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Size

@Entity(name = "matches")
class Match(
    @Id
    @Suppress("unused")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:PastOrPresent
    @Column(nullable = false)
    val date: LocalDate,

    @field:Size(max = 255)
    val quote: String?,

    @field:Size(max = 50)
    val author: String?,

    @field:Size(max = 2048)
    val description: String?,
)
