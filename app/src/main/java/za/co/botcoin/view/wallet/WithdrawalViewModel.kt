package za.co.botcoin.view.wallet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import za.co.botcoin.model.models.*
import za.co.botcoin.model.repository.WithdrawalRepository
import za.co.botcoin.utils.Resource

class WithdrawalViewModel(application: Application) : AndroidViewModel(application) {
    private val withdrawalRepository: WithdrawalRepository = WithdrawalRepository(application)

    lateinit var withdrawalLiveData: LiveData<Resource<List<Withdrawal>>>
    lateinit var sendLiveData: LiveData<Resource<List<Send>>>
    lateinit var receiveLiveData: LiveData<Resource<List<Receive>>>
    lateinit var ordersLiveData: LiveData<Resource<List<Order>>>
    lateinit var stopOrderLiveData: LiveData<Resource<List<StopOrder>>>

    fun withdrawal(mustWithdraw: Boolean, type: String, amount: String, beneficiaryId: String) {
        withdrawalLiveData = withdrawalRepository.withdrawal(mustWithdraw, type, amount, beneficiaryId)
    }

    fun send(mustSend: Boolean, amount: String, currency: String, address: String, destinationTag: String = "") {
        sendLiveData = withdrawalRepository.send(mustSend, amount, currency, address, destinationTag)
    }

    fun receive(mustReceive: Boolean, asset: String) {
        receiveLiveData = withdrawalRepository.receive(mustReceive, asset)
    }

    fun fetchOrders(mustFetchOrders: Boolean) {
        ordersLiveData = withdrawalRepository.fetchOrders(mustFetchOrders)
    }

    fun stopOrder(mustStopOrder: Boolean, orderId: String) {
        stopOrderLiveData = withdrawalRepository.stopOrder(mustStopOrder, orderId)
    }
}