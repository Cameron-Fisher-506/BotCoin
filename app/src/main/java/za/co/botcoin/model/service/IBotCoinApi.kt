package za.co.botcoin.model.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import za.co.botcoin.model.models.*

interface IBotCoinApi {
    @GET("tickers")
    suspend fun getTickers(@Header("Authorization") auth: String): Response<AccountWithTickers>

    @GET("balance")
    suspend fun getBalances(@Header("Authorization") auth: String): Response<AccountWithBalances>

    @POST("withdrawals")
    suspend fun withdrawal(@Header("Authorization") auth: String, @Query("type") type: String, @Query("amount") amount: String, @Query("beneficiary_id") beneficiaryId: String): Response<Withdrawal>

    @POST("send")
    suspend fun send(@Header("Authorization") auth: String, @Query("amount") amount: String, @Query("currency") currency: String, @Query("address") address: String,
                     @Query("has_destination_tag") hasDestinationTag: Boolean = true, @Query("destination_tag") destinationTag: String): Response<Send>

    @POST("send")
    suspend fun send(@Header("Authorization") auth: String, @Query("amount") amount: String, @Query("currency") currency: String, @Query("address") address: String): Response<Send>

    @GET("funding_address")
    suspend fun receive(@Header("Authorization") auth: String, @Query("asset") asset: String): Response<Receive>

    @GET("listorders")
    suspend fun getOrders(@Header("Authorization") auth: String): Response<AccountWithOrders>

    @POST("stoporder")
    suspend fun stopOrder(@Header("Authorization") auth: String, @Query("order_id") orderId: String): Response<StopOrder>
}