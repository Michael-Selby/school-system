package com.schoolsystem.student

import org.springframework.stereotype.Service

@Service
class StudentService(private val repository: StudentRepository) {
    fun findByParentEmail(parentEmail: String): List<Student> = repository.findAll().filter { it.parentEmail == parentEmail }
}
