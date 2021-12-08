package com.github.marciovmartins.futsitev3.matches

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "matches")
class Match(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val date: LocalDate,

    val quote: String?,

    val author: String?,

    val description: String?,
)
