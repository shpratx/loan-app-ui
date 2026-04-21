package com.tasheel.finance.data.api.dto

import com.tasheel.finance.domain.model.*

data class ProductDto(
    val id: String, val name: String, val type: String, val minAmount: Double,
    val maxAmount: Double, val minTenureMonths: Int, val maxTenureMonths: Int,
    val interestRate: Double, val description: String,
) {
    fun toDomain() = Product(id, name, type, minAmount, maxAmount, minTenureMonths, maxTenureMonths, interestRate, description)
}

data class ApplicationDto(
    val id: String, val productId: String, val productName: String, val status: String,
    val requestedAmount: Double, val requestedTenure: Int, val fullName: String,
    val nationalId: String, val employer: String, val monthlySalary: Double,
    val dateOfBirth: String? = null, val address: String? = null, val city: String? = null,
    val region: String? = null, val employmentType: String? = null, val employmentStartDate: String? = null,
    val grossIncome: Double? = null, val otherIncome: Double? = null,
) {
    fun toDomain() = LoanApplication(id, productId, productName,
        ApplicationStatus.entries.find { it.name.equals(status, true) } ?: ApplicationStatus.DRAFT,
        requestedAmount, requestedTenure, fullName, nationalId, employer, monthlySalary)
}

data class CreateApplicationRequest(
    val productId: String, val requestedAmount: Double, val requestedTenure: Int,
    val fullName: String, val nationalId: String, val employer: String, val monthlySalary: Double,
    val dateOfBirth: String? = null, val address: String? = null, val city: String? = null,
    val region: String? = null, val employmentType: String? = null, val employmentStartDate: String? = null,
    val grossIncome: Double? = null, val otherIncome: Double? = null,
)

data class OfferDto(
    val id: String, val applicationId: String, val approvedAmount: Double, val tenure: Int,
    val interestRate: Double, val monthlyPayment: Double, val totalRepayment: Double,
) {
    fun toDomain() = Offer(id, applicationId, approvedAmount, tenure, interestRate, monthlyPayment, totalRepayment)
}

data class LoanDto(
    val id: String, val applicationId: String, val principalAmount: Double,
    val outstandingBalance: Double, val monthlyPayment: Double, val tenure: Int,
    val status: String, val nextPaymentDate: String, val disbursedDate: String,
) {
    fun toDomain() = Loan(id, applicationId, principalAmount, outstandingBalance, monthlyPayment, tenure, status, nextPaymentDate, disbursedDate)
}

data class PaymentDto(
    val id: String, val loanId: String, val amount: Double,
    val date: String, val type: String, val status: String,
) {
    fun toDomain() = Payment(id, loanId, amount, date, type, status)
}

data class RegisterCardRequest(val applicationId: String, val cardNumber: String, val expiryMonth: Int, val expiryYear: Int, val autoDebitDay: Int)
data class SettlementDto(val loanId: String, val settlementAmount: Double, val validUntil: String)
data class LiabilityLetterDto(val loanId: String, val letterUrl: String)
data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val userId: String, val name: String)
