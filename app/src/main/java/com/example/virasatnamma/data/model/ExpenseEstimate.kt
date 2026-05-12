package com.example.virasatnamma.data.model

data class ExpenseEstimate(
    val travel: Double = 0.0,
    val food: Double = 0.0,
    val stay: Double = 0.0,
    val misc: Double = 0.0
) {
    val total: Double
        get() = travel + food + stay + misc
}