package com.schoolsystem.androidapp.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ParentLoginRequest(
    val parentEmail: String,
    val password: String,
    @Serializable(with = UUIDSerializer::class)
    val studentId: UUID
)

@Serializable
data class ParentSignupRequest(val fullName: String, val email: String, val phone: String, val password: String)

@Serializable
data class ParentLoginResponse(
    val parentEmail: String,
    @Serializable(with = UUIDSerializer::class)
    val studentId: UUID,
    val studentName: String
)
