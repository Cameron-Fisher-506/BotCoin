package za.co.botcoin.model.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BotCoinService : BaseService() {
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

    suspend fun getBalances(auth: String) = getResource { api.getBalances(auth) }

    suspend fun withdrawal(auth: String, type: String, amount: String, beneficiaryId: String) = getResource { api.withdrawal(auth, type, amount, beneficiaryId) }

    suspend fun send(auth: String, amount: String, currency: String, address: String, destinationTag: String) = getResource { api.send(auth, amount, currency, address, destinationTag = destinationTag) }

    suspend fun send(auth: String, amount: String, currency: String, address: String) = getResource { api.send(auth, amount, currency, address) }

    suspend fun receive(auth: String, asset: String) = getResource { api.receive(auth, asset) }

    suspend fun getOrders(auth: String) = getResource { api.getOrders(auth) }

    suspend fun stopOrder(auth: String, orderId: String) = getResource { api.stopOrder(auth, orderId) }

    suspend fun getTrades(auth: String, pair: String, sortDescending: Boolean) = getResource { api.getTrades(auth, pair, sortDescending) }

    suspend fun postOrder(auth: String, pair: String, type: String, volume: String, price: String) = getResource { api.postOrder(auth, pair, type, volume, price) }
}