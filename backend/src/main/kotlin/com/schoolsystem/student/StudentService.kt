package com.schoolsystem.student

import org.springframework.stereotype.Service

@Service
class StudentService(private val repository: StudentRepository) {
    fun findByParentEmail(parentEmail: String): List<Student> = repository.findByParentEmail(parentEmail)

    fun findByIndexNumberForParent(indexNumber: String, parentEmail: String): Student? {
        val student = repository.findByIndexNumber(indexNumber) ?: return null
        return if (student.parentEmail.equals(parentEmail, ignoreCase = true)) student else null
    }
}
