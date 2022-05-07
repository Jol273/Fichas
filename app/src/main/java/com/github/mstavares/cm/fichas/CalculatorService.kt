package com.github.mstavares.cm.fichas

import retrofit2.http.*

data class PostOperationRequest(
    val expression: String, val result: Double, val timestamp: Long)
data class PostOperationResponse(val message: String)
data class GetOperationsResponse(val uuid: String, val expression: String, val result: Double, val timestamp: Long)
data class DeleteOperationByIdResponse(val message: String)
data class DeleteAllOperationsResponse(val message: String)

interface CalculatorService {

    @Headers("apikey: 8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17")
    @POST("operations")
    suspend fun insert(@Body body: PostOperationRequest): PostOperationResponse

    @Headers("apikey: 8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17")
    @GET("operations")
    suspend fun getAll(): List<GetOperationsResponse>

    @Headers("apikey: 8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17")
    @DELETE("operations/{uuid}")
    suspend fun deleteByID(@Path("uuid") uuid: String): DeleteOperationByIdResponse

    @Headers("apikey: 8270435acfead39ccb03e8aafbf37c49359dfbbcac4ef4769ae82c9531da0e17")
    @DELETE("operations")
    suspend fun deleteAll(): DeleteAllOperationsResponse
}