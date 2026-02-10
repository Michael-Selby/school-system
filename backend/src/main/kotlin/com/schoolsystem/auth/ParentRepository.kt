package com.schoolsystem.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ParentRepository : JpaRepository<Parent, UUID> {
    fun findByEmail(email: String): Parent?
}
