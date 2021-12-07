package com.github.marciovmartins.futsitev3.matches

import com.github.marciovmartins.futsitev3.BaseIT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import retrofit2.Call
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

class MatchControllerIT : BaseIT() {
    @Test
    fun `should succeed to create and retrieve a match`() {
        // setup
        val matchToCreate = MatchFixture.match()
        val matchClient = retrofit.create<MatchClient>()
        // execution
        val createdMatch = matchClient.create(matchToCreate).execute().body()!!
        val match = matchClient.findOne(createdMatch.id).execute().body()!!
        // assertion
        assertThat(matchToCreate)
            .usingRecursiveComparison()
            .ignoringFields("internalId")
            .isEqualTo(createdMatch)
            .isEqualTo(match)
    }
}

interface MatchClient {
    @POST("matches")
    fun create(@Body match: Match): Call<Match>

    @GET("matches/{matchId}")
    fun findOne(@Path("matchId") matchId: UUID): Call<Match>
}
