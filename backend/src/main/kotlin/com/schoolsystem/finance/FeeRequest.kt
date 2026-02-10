package com.schoolsystem.finance

import java.math.BigDecimal
import java.util.UUID

data class FeeRequest(
    val parentEmail: String,
    val studentId: UUID,
    val amount: BigDecimal,
    val feeType: String
)
