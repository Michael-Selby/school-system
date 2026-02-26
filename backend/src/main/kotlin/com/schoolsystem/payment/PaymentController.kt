package com.schoolsystem.payment

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    @PostMapping("/forms/initiate")
    fun initiate(@RequestBody req: InitiateFormPurchaseRequest): ResponseEntity<PaymentInitResponse> {
        val resp = paymentService.initiateFormPurchase(req.email, req.phone, req.childIndex)
        return ResponseEntity.ok(resp)
    }

    @GetMapping("/verify")
    fun verify(@RequestParam reference: String): ResponseEntity<PaymentVerifyResponse> {
        val resp = paymentService.verify(reference)
        return ResponseEntity.ok(resp)
    }
}
