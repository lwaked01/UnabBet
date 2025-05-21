package com.leonardowaked.unabbet.data.models


import com.google.gson.annotations.SerializedName

// Estructura general de la respuesta para partidos (contiene una lista de 'fixtures')
data class FixtureResponse(
    val response: List<MatchResponse>
)

// Representa un partido individual
data class MatchResponse(
    val fixture: Fixture,
    val league: League,
    val teams: Teams,
    val goals: Goals,
    val score: Score // Puede que necesites esta para el score completo si la API lo ofrece
)

data class Fixture(
    val id: Int,
    val referee: String?,
    val timezone: String,
    val date: String,
    val timestamp: Long,
    val periods: Periods,
    val venue: Venue,
    val status: Status
)

data class Periods(
    val first: Int?,
    val second: Int?
)

data class Venue(
    val id: Int,
    val name: String,
    val city: String
)

data class Status(
    val long: String, // "Match Finished", "Half Time", "Live"
    val short: String, // "FT", "HT", "1H", "2H"
    val elapsed: Int? // Minuto actual del partido si está en vivo
)

data class League(
    val id: Int,
    val name: String,
    val country: String,
    val logo: String,
    val flag: String?,
    val season: Int,
    val round: String
)

data class Teams(
    val home: Team,
    val away: Team
)

data class Team(
    val id: Int,
    val name: String,
    val logo: String,
    val winner: Boolean? // Puede ser nulo
)

data class Goals(
    val home: Int?, // Goles del equipo local (puede ser nulo antes del partido)
    val away: Int? // Goles del equipo visitante (puede ser nulo antes del partido)
)

data class Score(
    val halftime: Goals,
    val fulltime: Goals,
    val extratime: Goals,
    val penalty: Goals
)