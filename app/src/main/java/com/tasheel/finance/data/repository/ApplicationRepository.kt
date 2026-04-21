package com.tasheel.finance.data.repository

import com.tasheel.finance.data.api.TasheelApi
import com.tasheel.finance.data.api.dto.CreateApplicationRequest
import com.tasheel.finance.data.api.dto.RegisterCardRequest
import com.tasheel.finance.data.db.ApplicationDraftDao
import com.tasheel.finance.data.db.ApplicationDraftEntity
import com.tasheel.finance.domain.model.LoanApplication
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRepository @Inject constructor(
    private val api: TasheelApi,
    private val draftDao: ApplicationDraftDao,
) {
    suspend fun createApplication(
        productId: String, amount: Double, tenure: Int,
        fullName: String, nationalId: String, employer: String, salary: Double,
    ): LoanApplication = api.createApplication(
        CreateApplicationRequest(productId, amount, tenure, fullName, nationalId, employer, salary)
    ).toDomain()

    suspend fun submitApplication(id: String): LoanApplication = api.submitApplication(id).toDomain()
    suspend fun getApplication(id: String): LoanApplication = api.getApplication(id).toDomain()

    suspend fun registerCard(applicationId: String, cardNumber: String, expiryMonth: Int, expiryYear: Int, autoDebitDay: Int) =
        api.registerCard(RegisterCardRequest(applicationId, cardNumber, expiryMonth, expiryYear, autoDebitDay))

    // Offline draft support
    suspend fun saveDraft(draft: ApplicationDraftEntity): Long = draftDao.upsert(draft)
    suspend fun getDrafts(): List<ApplicationDraftEntity> = draftDao.getAll()
    suspend fun getDraft(id: Long): ApplicationDraftEntity? = draftDao.getById(id)
    suspend fun deleteDraft(id: Long) = draftDao.delete(id)
}
