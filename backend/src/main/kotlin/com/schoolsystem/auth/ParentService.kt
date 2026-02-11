package com.schoolsystem.auth

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ParentService(
    private val repository: ParentRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun register(request: ParentSignupRequest): Parent {
        val existing = repository.findByEmail(request.email)
        require(existing == null) { "Email is already registered" }
        val parent = Parent(
            id = UUID.randomUUID(),
            fullName = request.fullName,
            email = request.email,
            phone = request.phone,
            passwordHash = passwordEncoder.encode(request.password),
            createdAt = Instant.now()
        )
        return repository.save(parent)
    }

    fun authenticate(request: ParentAuthLoginRequest): Parent? {
        val parent = repository.findByEmail(request.parentEmail) ?: return null
        return if (passwordEncoder.matches(request.password, parent.passwordHash)) parent else null
    }
}
