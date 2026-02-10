package com.schoolsystem.auth

import com.schoolsystem.student.StudentRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/auth")
class ParentAuthController(
    private val parentService: ParentService,
    private val studentRepository: StudentRepository
) {

    @PostMapping("/signup")
    fun signup(@RequestBody request: ParentSignupRequest): ResponseEntity<ParentSignupResponse> {
        val saved = parentService.register(request)
        val response = ParentSignupResponse(parentId = saved.id, email = saved.email)
        return ResponseEntity.created(URI.create("/api/auth/signup/${saved.id}")).body(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: ParentAuthLoginRequest): ResponseEntity<ParentLoginResponseDTO> {
        val parent = parentService.authenticate(request) ?: return ResponseEntity.status(401).build()
        val student = studentRepository.findByIdOrNull(request.studentId) ?: return ResponseEntity.badRequest().build()
        if (!student.parentEmail.equals(parent.email, ignoreCase = true)) {
            return ResponseEntity.status(401).build()
        }
        val response = ParentLoginResponseDTO(
            parentEmail = parent.email,
            studentId = student.id,
            studentName = "${student.firstName} ${student.lastName}"
        )
        return ResponseEntity.ok(response)
    }
}

fun StudentRepository.findByIdOrNull(id: java.util.UUID) = findById(id).orElse(null)
