package com.schoolsystem.androidapp.data

import kotlinx.serialization.Serializable

@Serializable
data class ParentLoginRequest(
    val parentEmail: String,
    val password: String
)

@Serializable
data class ParentSignupRequest(val fullName: String, val email: String, val phone: String, val password: String)

@Serializable
data class ParentLoginResponse(
    @Serializable(with = UUIDSerializer::class)
    val parentId: java.util.UUID,
    val parentEmail: String,
    val parentName: String
)

@Serializable
data class StudentProfileResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: java.util.UUID,
    val fullName: String,
    val grade: String,
    val status: String,
    val enrollmentDate: String
)
