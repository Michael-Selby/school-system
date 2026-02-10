package com.schoolsystem.admission

import java.time.LocalDate

data class AdmissionRequest(
    val childFirstName: String,
    val childLastName: String,
    val childDob: LocalDate,
    val childGender: String,
    val preferredGrade: String,
    val previousSchool: String,
    val parentName: String,
    val parentEmail: String,
    val parentPhone: String
)
