package com.tasheel.finance.data.repository

import com.tasheel.finance.data.api.TasheelApi
import com.tasheel.finance.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoanRepository @Inject constructor(private val api: TasheelApi) {
    suspend fun getLoans(): List<Loan> = api.getLoans().map { it.toDomain() }
    suspend fun getLoan(id: String): Loan = api.getLoan(id).toDomain()
    suspend fun getPayments(loanId: String): List<Payment> = api.getPayments(loanId).map { it.toDomain() }
    suspend fun getSettlement(loanId: String) = api.getSettlement(loanId)
    suspend fun requestLiabilityLetter(loanId: String) = api.requestLiabilityLetter(loanId)
    suspend fun getOffer(applicationId: String): Offer = api.getOffer(applicationId).toDomain()
    suspend fun acceptOffer(offerId: String): Offer = api.acceptOffer(offerId).toDomain()
    suspend fun declineOffer(offerId: String): Offer = api.declineOffer(offerId).toDomain()
}
