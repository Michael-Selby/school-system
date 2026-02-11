package com.schoolsystem.finance

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class FeeResponse(
    val id: UUID,
    val studentId: UUID,
    val amount: BigDecimal,
    val feeType: String,
    val paidAt: Instant,
    val status: String
)
