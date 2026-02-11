package com.schoolsystem.student

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/parents")
class StudentController(private val service: StudentService) {
    @GetMapping("/students")
    fun students(@RequestParam email: String): List<StudentProfileResponse> =
        service.findByParentEmail(email).map {
            StudentProfileResponse(
                id = it.id,
                fullName = "${it.firstName} ${it.lastName}",
                grade = it.grade,
                status = it.status,
                enrollmentDate = it.enrollmentDate
            )
        }
}

data class StudentProfileResponse(
    val id: java.util.UUID,
    val fullName: String,
    val grade: String,
    val status: String,
    val enrollmentDate: java.time.Instant
)
