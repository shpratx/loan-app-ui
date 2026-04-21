package com.tasheel.finance.domain.model

data class Offer(
    val id: String,
    val applicationId: String,
    val approvedAmount: Double,
    val tenure: Int,
    val interestRate: Double,
    val monthlyPayment: Double,
    val totalRepayment: Double,
)

data class Loan(
    val id: String,
    val applicationId: String,
    val principalAmount: Double,
    val outstandingBalance: Double,
    val monthlyPayment: Double,
    val tenure: Int,
    val status: String,
    val nextPaymentDate: String,
    val disbursedDate: String,
)

data class Payment(
    val id: String,
    val loanId: String,
    val amount: Double,
    val date: String,
    val type: String,
    val status: String,
)
