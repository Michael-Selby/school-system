package com.schoolsystem.auth

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "parents")
data class Parent(
    @Id
    var id: UUID = UUID.randomUUID(),

    var fullName: String,

    @Column(unique = true)
    var email: String,

    var phone: String,

    var passwordHash: String,

    var createdAt: Instant = Instant.now()
)
