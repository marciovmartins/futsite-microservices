package com.github.marciovmartins.futsitev3.asm.ranking.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDay
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayDTO
import com.github.marciovmartins.futsitev3.asm.ranking.domain.GameDayRepository
import org.springframework.stereotype.Repository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.UUID

@Repository
class RetrofitGameDayRepository(
    asmGameDayProperties: AsmGameDayProperties,
    objectMapper: ObjectMapper,
) : GameDayRepository {
    private val service = Retrofit.Builder()
        .baseUrl(asmGameDayProperties.baseUrl)
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .build()
        .create(AsmGameDayService::class.java)

    override fun findBy(gameDayId: UUID): GameDay? = service.getGameDay(gameDayId).execute().body()
        ?.let { GameDay(gameDayId, it) }
}

private interface AsmGameDayService {
    @GET("gameDays/{gameDayId}")
    fun getGameDay(@Path("gameDayId") gameDayId: UUID): Call<GameDayDTO>
}
