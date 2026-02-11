package com.schoolsystem.auth

data class ParentSignupRequest(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String
)
