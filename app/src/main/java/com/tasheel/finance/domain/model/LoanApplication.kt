package com.tasheel.finance.domain.model

enum class ApplicationStatus(val label: String) {
    DRAFT("Draft"), SUBMITTED("Submitted"), UNDER_REVIEW("Under Review"),
    APPROVED("Approved"), REJECTED("Rejected"), DISBURSED("Disbursed"),
}

data class LoanApplication(
    val id: String,
    val productId: String,
    val productName: String,
    val status: ApplicationStatus,
    val requestedAmount: Double,
    val requestedTenure: Int,
    val fullName: String,
    val nationalId: String,
    val employer: String,
    val monthlySalary: Double,
)
