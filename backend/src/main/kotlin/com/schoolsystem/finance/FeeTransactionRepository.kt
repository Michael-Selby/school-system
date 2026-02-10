package com.schoolsystem.finance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FeeTransactionRepository : JpaRepository<FeeTransaction, UUID> {
    fun findByParentEmail(email: String): List<FeeTransaction>
}
