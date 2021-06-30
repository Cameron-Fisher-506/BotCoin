package za.co.botcoin.model.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import za.co.botcoin.model.models.AccountWithBalances
import za.co.botcoin.model.models.AccountWithTickers
import za.co.botcoin.model.models.Withdrawal

interface IBotCoinApi {
    @GET("tickers")
    suspend fun getTickers(@Header("Authorization") auth: String): Response<AccountWithTickers>

    @GET("balance")
    suspend fun getBalances(@Header("Authorization") auth: String): Response<AccountWithBalances>

    @POST("withdrawals")
    suspend fun withdrawal(@Query("type") type: String, @Query("amount") amount: String, @Query("beneficiary_id") beneficiaryId: String): Response<Withdrawal>
}