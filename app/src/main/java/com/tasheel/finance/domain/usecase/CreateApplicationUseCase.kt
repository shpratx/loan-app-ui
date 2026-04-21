package com.tasheel.finance.domain.usecase

import com.tasheel.finance.data.repository.ApplicationRepository
import com.tasheel.finance.domain.model.LoanApplication
import javax.inject.Inject

class CreateApplicationUseCase @Inject constructor(private val repo: ApplicationRepository) {
    suspend operator fun invoke(
        productId: String, amount: Double, tenure: Int,
        fullName: String, nationalId: String, employer: String, salary: Double,
    ): LoanApplication = repo.createApplication(productId, amount, tenure, fullName, nationalId, employer, salary)
}
