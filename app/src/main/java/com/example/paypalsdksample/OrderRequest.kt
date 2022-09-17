package com.example.paypalsdksample

data class OrderRequest(
    val intent: String,
    val purchaseUnits: Array<PurchaseUnit>,
    val applicationContext: ApplicationContext
)

data class PurchaseUnit(
    val amount: Amount
)

data class Amount(
    val currencyCode: String,
    val value: String
)

data class ApplicationContext(
    val returnUrl: String,
    val cancelUrl: String
)
