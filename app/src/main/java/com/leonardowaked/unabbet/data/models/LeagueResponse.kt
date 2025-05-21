package com.leonardowaked.unabbet.data.models


import com.google.gson.annotations.SerializedName

// Estructura general de la respuesta para ligas
data class LeaguesResponse(
    val response: List<LeagueData>
)

// Datos de una liga/competición
data class LeagueData(
    val league: LeagueInfo,
    val country: CountryInfo,
    val seasons: List<SeasonInfo>
)

data class LeagueInfo(
    val id: Int,
    val name: String,
    val type: String, // Ej: "League", "Cup"
    val logo: String
)

data class CountryInfo(
    val name: String,
    val code: String?,
    val flag: String?
)

data class SeasonInfo(
    val year: Int,
    val start: String,
    val end: String,
    val current: Boolean, // Si es la temporada actual
    val coverage: CoverageInfo
)

data class CoverageInfo(
    val fixtures: FixturesCoverage,
    val standings: Boolean,
    val players: Boolean,
    val top_scorers: Boolean,
    val top_assists: Boolean,
    val top_cards: Boolean,
    val injuries: Boolean,
    val predictions: Boolean,
    val odds: Boolean
)

data class FixturesCoverage(
    val events: Boolean,
    val lineups: Boolean,
    val statistics_fixtures: Boolean,
    val statistics_players: Boolean
)