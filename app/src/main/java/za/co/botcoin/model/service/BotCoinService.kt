package za.co.botcoin.model.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BotCoinService: BaseService() {
    private lateinit var api: IBotCoinApi

    companion object {
        const val BASE_URL = "https://api.mybitx.com/api/1/"
    }

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        this.api = retrofit.create(IBotCoinApi::class.java)
    }

    suspend fun getTickers(auth: String) = getResource { api.getTickers(auth) }
}