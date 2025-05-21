package com.leonardowaked.unabbet.network


import com.leonardowaked.unabbet.data.models.FixtureResponse
import com.leonardowaked.unabbet.data.models.LeaguesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FootballApiService {

    // --- Configuración de la API Key y Host (¡REEMPLAZA ESTO!) ---
    companion object {
        const val API_KEY = "e45bd775045378abc7b946b1fa0cf0b4" //
        const val API_HOST = "v3.football.api-sports.io" // El host de API-Football
    }

    @Headers(
        "x-rapidapi-key: $API_KEY",
        "x-rapidapi-host: $API_HOST"
    )
    @GET("fixtures")
    suspend fun getFixtures(
        @Query("date") date: String? = null, // Para partidos por fecha (próximos)
        @Query("live") live: String? = null // Para partidos en vivo (ej: "all")
    ): Response<FixtureResponse> // Cambiado a FixtureResponse para la estructura correcta

    @Headers(
        "x-rapidapi-key: $API_KEY",
        "x-rapidapi-host: $API_HOST"
    )
    @GET("leagues")
    suspend fun getLeagues(): Response<LeaguesResponse>
}