package com.schoolsystem.admission

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "parent_applications")
data class StudentApplication(
    @Id
    var id: UUID = UUID.randomUUID(),
    var childFirstName: String,
    var childLastName: String,
    var childDob: Instant,
    var childGender: String,
    var preferredGrade: String,
    var previousSchool: String,
    var parentName: String,
    var parentEmail: String,
    var parentPhone: String,
    var status: String = "SUBMITTED",
    var submittedAt: Instant = Instant.now()
)
