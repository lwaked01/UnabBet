package com.leonardowaked.unabbet.util


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object BalanceManager {
    private val _userBalance = MutableStateFlow(100000.0) // Saldo inicial
    val userBalance: StateFlow<Double> = _userBalance

    fun addBalance(amount: Double) {
        if (amount > 0) {
            _userBalance.value += amount
        }
    }

    fun deductBalance(amount: Double): Boolean {
        if (amount > 0 && _userBalance.value >= amount) {
            _userBalance.value -= amount
            return true
        }
        return false // Saldo insuficiente o cantidad inválida
    }
}