package za.co.botcoin.model.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import za.co.botcoin.model.models.*

interface IBotCoinApi {
    @GET("1/tickers")
    suspend fun getTickers(@Header("Authorization") auth: String): Response<AccountWithTickers>

    @GET("1/balance")
    suspend fun getBalances(@Header("Authorization") auth: String): Response<AccountWithBalances>

    @POST("1/withdrawals")
    suspend fun withdrawal(@Header("Authorization") auth: String, @Query("type") type: String, @Query("amount") amount: String, @Query("beneficiary_id") beneficiaryId: String): Response<Withdrawal>

    @POST("1/send")
    suspend fun send(@Header("Authorization") auth: String, @Query("amount") amount: String, @Query("currency") currency: String, @Query("address") address: String,
                     @Query("has_destination_tag") hasDestinationTag: Boolean = true, @Query("destination_tag") destinationTag: String): Response<Send>

    @POST("1/send")
    suspend fun send(@Header("Authorization") auth: String, @Query("amount") amount: String, @Query("currency") currency: String, @Query("address") address: String): Response<Send>

    @GET("1/funding_address")
    suspend fun receive(@Header("Authorization") auth: String, @Query("asset") asset: String): Response<Receive>

    @GET("1/listorders")
    suspend fun getOrders(@Header("Authorization") auth: String): Response<AccountWithOrders>

    @POST("1/stoporder")
    suspend fun stopOrder(@Header("Authorization") auth: String, @Query("order_id") orderId: String): Response<StopOrder>

    @GET("1/listtrades")
    suspend fun getTrades(@Header("Authorization") auth: String, @Query("pair") pair: String, @Query("sort_desc") sortDescending: Boolean): Response<AccountWithTrades>

    @POST("1/postorder")
    suspend fun postOrder(@Header("Authorization") auth: String, @Query("pair") pair: String, @Query("type") type: String, @Query("volume") volume: String, @Query("price") price: String): Response<PostOrder>

    @GET("exchange/1/candles")
    suspend fun getCandles(@Header("Authorization") auth: String, @Query("pair") pair: String, @Query("since") since: String, @Query("duration") duration: Int): Response<AccountWithCandles>
}