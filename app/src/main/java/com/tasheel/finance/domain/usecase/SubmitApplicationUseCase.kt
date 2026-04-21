package com.tasheel.finance.domain.usecase

import com.tasheel.finance.data.repository.ApplicationRepository
import com.tasheel.finance.domain.model.LoanApplication
import javax.inject.Inject

class SubmitApplicationUseCase @Inject constructor(private val repo: ApplicationRepository) {
    suspend operator fun invoke(applicationId: String): LoanApplication = repo.submitApplication(applicationId)
}
