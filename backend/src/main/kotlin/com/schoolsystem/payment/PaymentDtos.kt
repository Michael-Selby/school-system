package com.schoolsystem.payment

data class InitiateFormPurchaseRequest(
    val email: String,
    val phone: String,
    val childIndex: String?
)

data class PaymentInitResponse(
    val authorizationUrl: String,
    val reference: String
)

data class PaymentVerifyResponse(
    val success: Boolean,
    val status: String
)

// Paystack API DTOs
internal data class PaystackInitRequest(
    val email: String,
    val amount: Int,
    val currency: String = "GHS",
    val reference: String,
    val metadata: Map<String, Any?>? = null
)

internal data class PaystackInitResponse(
    val status: Boolean,
    val message: String,
    val data: PaystackInitData?
)

internal data class PaystackInitData(
    val authorization_url: String,
    val access_code: String?,
    val reference: String
)

internal data class PaystackVerifyResponse(
    val status: Boolean,
    val message: String,
    val data: PaystackVerifyData?
)

internal data class PaystackVerifyData(
    val status: String,
    val reference: String,
    val amount: Int,
    val currency: String
)
