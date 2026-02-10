package com.schoolsystem.admission

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/applications")
class StudentApplicationController(private val service: StudentApplicationService) {
    @PostMapping
    fun submit(@RequestBody request: AdmissionRequest): ResponseEntity<ApplicationResponse> {
        val saved = service.submitApplication(request)
        val response = ApplicationResponse(
            id = saved.id,
            status = saved.status,
            submittedAt = saved.submittedAt
        )
        return ResponseEntity.created(URI.create("/api/applications/${saved.id}")).body(response)
    }

    @GetMapping
    fun list(@RequestParam parentEmail: String): List<ApplicationResponse> =
        service.listApplicationsByParent(parentEmail).map {
            ApplicationResponse(
                id = it.id,
                status = it.status,
                submittedAt = it.submittedAt
            )
        }
}

data class ApplicationResponse(
    val id: java.util.UUID,
    val status: String,
    val submittedAt: java.time.Instant
)
