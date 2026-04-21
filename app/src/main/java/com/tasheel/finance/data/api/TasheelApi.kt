package com.tasheel.finance.data.api

import com.tasheel.finance.data.api.dto.*
import retrofit2.http.*

interface TasheelApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: String): ProductDto

    @POST("applications")
    suspend fun createApplication(@Body request: CreateApplicationRequest): ApplicationDto

    @POST("applications/{id}/submit")
    suspend fun submitApplication(@Path("id") id: String): ApplicationDto

    @GET("applications/{id}")
    suspend fun getApplication(@Path("id") id: String): ApplicationDto

    @GET("offers/application/{applicationId}")
    suspend fun getOffer(@Path("applicationId") applicationId: String): OfferDto

    @POST("offers/{id}/accept")
    suspend fun acceptOffer(@Path("id") id: String): OfferDto

    @POST("offers/{id}/decline")
    suspend fun declineOffer(@Path("id") id: String): OfferDto

    @GET("loans")
    suspend fun getLoans(): List<LoanDto>

    @GET("loans/{id}")
    suspend fun getLoan(@Path("id") id: String): LoanDto

    @GET("loans/{id}/settlement")
    suspend fun getSettlement(@Path("id") id: String): SettlementDto

    @POST("loans/{id}/liability-letter")
    suspend fun requestLiabilityLetter(@Path("id") id: String): LiabilityLetterDto

    @GET("payments/loan/{loanId}")
    suspend fun getPayments(@Path("loanId") loanId: String): List<PaymentDto>

    @POST("cards/register")
    suspend fun registerCard(@Body request: RegisterCardRequest): Unit

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
