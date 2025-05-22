package com.leonardowaked.unabbet.util

import com.leonardowaked.unabbet.data.models.MatchResponse // ¡Esta importación es CORRECTA para tu MatchResponse!
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Definimos la clase de datos para UNA APUESTA DEL HISTORIAL.
// La llamamos UserBet para que NO COLISIONE con tu Bet.kt en data.models.
data class UserBet( // <-- NOTA: RENOMBRADO a UserBet
    val match: MatchResponse, // Guarda el objeto completo del partido
    val homeScore: Int,
    val awayScore: Int,
    val amount: Double,
    val potentialPayout: Double
)

// Objeto Singleton para gestionar el historial de apuestas a nivel de aplicación.
object BetHistoryManager {
    // MutableStateFlow permite a los Composables observar los cambios en esta lista.
    private val _bets = MutableStateFlow<List<UserBet>>(emptyList()) // <-- Usa UserBet aquí
    val bets: StateFlow<List<UserBet>> = _bets // Expone la lista como StateFlow de solo lectura

    // Función para añadir una nueva apuesta (UserBet) al historial.
    fun addBet(bet: UserBet) { // <-- Acepta un UserBet
        _bets.value = _bets.value + bet // Añade la nueva apuesta de forma inmutable
    }

    // Función opcional para limpiar el historial (útil para pruebas o para un botón de "limpiar").
    fun clearBets() {
        _bets.value = emptyList()
    }
}