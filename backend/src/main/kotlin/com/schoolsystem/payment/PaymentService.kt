package com.schoolsystem.payment

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.util.UUID

@Service
class PaymentService(
    @Value("\${PAYSTACK_SECRET_KEY:}") private val paystackSecretKey: String
) {
    private val client: RestClient by lazy {
        require(paystackSecretKey.isNotBlank()) { "PAYSTACK_SECRET_KEY is not configured" }
        RestClient.builder()
            .baseUrl("https://api.paystack.co")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $paystackSecretKey")
            .build()
    }

    fun initiateFormPurchase(email: String, phone: String, childIndex: String?): PaymentInitResponse {
        val reference = UUID.randomUUID().toString()
        val payload = PaystackInitRequest(
            email = email,
            amount = 100, // 1 GHS = 100 pesewas (lowest unit required by Paystack)
            currency = "GHS",
            reference = reference,
            metadata = mapOf("phone" to phone, "childIndex" to childIndex)
        )
        val resp = client.post()
            .uri("/transaction/initialize")
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)
            .retrieve()
            .body(PaystackInitResponse::class.java)
            ?: throw IllegalStateException("No response from Paystack initialize")
        if (resp.status && resp.data != null) {
            return PaymentInitResponse(
                authorizationUrl = resp.data.authorization_url,
                reference = resp.data.reference
            )
        }
        throw IllegalStateException("Failed to initialize payment: ${resp.message}")
    }

    fun verify(reference: String): PaymentVerifyResponse {
        val resp = client.get()
            .uri("/transaction/verify/{reference}", reference)
            .retrieve()
            .body(PaystackVerifyResponse::class.java)
            ?: return PaymentVerifyResponse(success = false, status = "unknown")
        val status = resp.data?.status ?: "failed"
        val ok = status.equals("success", ignoreCase = true)
        return PaymentVerifyResponse(success = ok, status = status)
    }
}
