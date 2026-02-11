package com.schoolsystem.auth

import java.util.UUID

data class ParentAuthLoginRequest(
    val parentEmail: String,
    val password: String,
    val studentId: UUID
)

data class ParentSignupResponse(val parentId: UUID, val email: String)

data class ParentLoginResponseDTO(val parentEmail: String, val studentId: UUID, val studentName: String)
