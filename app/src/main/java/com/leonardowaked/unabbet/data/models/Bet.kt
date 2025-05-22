package com.leonardowaked.unabbet.data.models



data class Bet(
    val matchName: String,
    val homeScorePrediction: Int,
    val awayScorePrediction: Int,
    val amountStaked: Double,
    val potentialPayout: Double,
    val timestamp: Long = System.currentTimeMillis() // Para saber cuándo se hizo
)