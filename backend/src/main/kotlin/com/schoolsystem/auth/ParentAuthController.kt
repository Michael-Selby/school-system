package com.schoolsystem.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/auth")
class ParentAuthController(
    private val parentService: ParentService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody request: ParentSignupRequest): ResponseEntity<ParentSignupResponse> {
        val saved = parentService.register(request)
        val response = ParentSignupResponse(parentId = saved.id, email = saved.email)
        return ResponseEntity.created(URI.create("/api/auth/signup/${'$'}{saved.id}"))
            .body(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: ParentAuthLoginRequest): ResponseEntity<ParentLoginResponseDTO> {
        val parent = parentService.authenticate(request) ?: return ResponseEntity.status(401).build()
        val response = ParentLoginResponseDTO(
            parentId = parent.id,
            parentEmail = parent.email,
            parentName = parent.fullName
        )
        return ResponseEntity.ok(response)
    }
}