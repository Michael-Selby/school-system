package com.schoolsystem.student

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "students")
data class Student(
    @Id
    var id: UUID = UUID.randomUUID(),

    var firstName: String,

    var lastName: String,

    @Column(unique = true)
    var email: String,

    @Column(unique = true)
    var indexNumber: String? = null,

    var grade: String,

    var parentName: String,

    var parentEmail: String,

    var enrollmentDate: Instant = Instant.now(),

    var status: String = "ACTIVE",

    var enrolledAt: Instant = Instant.now()
)
