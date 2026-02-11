package com.schoolsystem.finance

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "fee_transactions")
data class FeeTransaction(
    @Id
    var id: UUID = UUID.randomUUID(),
    var parentEmail: String,
    var studentId: UUID,
    var amount: BigDecimal,
    var feeType: String,
    var paidAt: Instant = Instant.now(),
    var status: String = "PENDING"
)
