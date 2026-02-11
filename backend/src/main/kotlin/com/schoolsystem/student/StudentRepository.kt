package com.schoolsystem.student

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudentRepository : JpaRepository<Student, UUID>
{
    fun findByParentEmail(parentEmail: String): List<Student>
}
