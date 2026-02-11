package com.schoolsystem.finance

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/fees")
class FeeTransactionController(private val service: FeeTransactionService) {
    @PostMapping
    fun pay(@RequestBody request: FeeRequest): ResponseEntity<FeeResponse> {
        val saved = service.createTransaction(request)
        val response = FeeResponse(
            id = saved.id,
            studentId = saved.studentId,
            amount = saved.amount,
            feeType = saved.feeType,
            paidAt = saved.paidAt,
            status = saved.status
        )
        return ResponseEntity.created(URI.create("/api/fees/${saved.id}")).body(response)
    }

    @GetMapping
    fun list(@RequestParam parentEmail: String): List<FeeResponse> =
        service.listByParent(parentEmail).map {
            FeeResponse(
                id = it.id,
                studentId = it.studentId,
                amount = it.amount,
                feeType = it.feeType,
                paidAt = it.paidAt,
                status = it.status
            )
        }
}
