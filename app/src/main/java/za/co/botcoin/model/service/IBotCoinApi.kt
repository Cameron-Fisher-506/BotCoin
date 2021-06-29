package za.co.botcoin.model.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import za.co.botcoin.model.models.LunoWithTickers
import za.co.botcoin.model.models.Ticker

interface IBotCoinApi {
    @GET("tickers")
    suspend fun getTickers(@Header("Authorization") auth: String): Response<LunoWithTickers>
}