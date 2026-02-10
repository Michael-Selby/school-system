package com.schoolsystem.finance

import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class FeeTransactionService(private val repository: FeeTransactionRepository) {
    fun createTransaction(request: FeeRequest): FeeTransaction {
        val transaction = FeeTransaction(
            id = UUID.randomUUID(),
            parentEmail = request.parentEmail,
            studentId = request.studentId,
            amount = request.amount,
            feeType = request.feeType,
            paidAt = Instant.now(),
            status = "PAID"
        )
        return repository.save(transaction)
    }

    fun listByParent(parentEmail: String): List<FeeTransaction> = repository.findByParentEmail(parentEmail)
}
