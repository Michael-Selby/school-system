package com.schoolsystem

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SchoolSystemApplication

fun main(args: Array<String>) {
    runApplication<SchoolSystemApplication>(*args)
}

@RestController
@RequestMapping("/api")
class HealthController {
    @GetMapping("/health")
    fun health(): Map<String, Any> = mapOf(
        "status" to "UP",
        "message" to "School System backend is running"
    )
}
