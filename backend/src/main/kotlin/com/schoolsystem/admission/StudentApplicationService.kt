package com.schoolsystem.admission

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class StudentApplicationService(private val repository: StudentApplicationRepository) {
    @Transactional
    fun submitApplication(request: AdmissionRequest): StudentApplication {
        val application = StudentApplication(
            id = UUID.randomUUID(),
            childFirstName = request.childFirstName,
            childLastName = request.childLastName,
            childDob = request.childDob.atStartOfDay().toInstant(java.time.ZoneOffset.UTC),
            childGender = request.childGender,
            preferredGrade = request.preferredGrade,
            previousSchool = request.previousSchool,
            parentName = request.parentName,
            parentEmail = request.parentEmail,
            parentPhone = request.parentPhone,
            status = "SUBMITTED",
            submittedAt = Instant.now()
        )
        return repository.save(application)
    }

    fun listApplicationsByParent(email: String): List<StudentApplication> =
        repository.findByParentEmail(email)
}
