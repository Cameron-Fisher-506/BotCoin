package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import za.co.botcoin.model.models.Receive
import za.co.botcoin.model.models.Send
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.room.*
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.Resource

class WithdrawalRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val withdrawalDao: IWithdrawalDao = BotCoinDatabase.getDatabase(application).withdrawalDao()
    private val sendDao: ISendDao = BotCoinDatabase.getDatabase(application).sendDao()
    private val receiveDao: IReceiveDao = BotCoinDatabase.getDatabase(application).receiveDao()

    private val mustSendLiveData by lazy { MutableLiveData<Boolean>() }
    private val mustWithdrawLiveData by lazy { MutableLiveData<Boolean>() }
    private val mustReceiveLiveData by lazy { MutableLiveData<Boolean>() }

    fun withdrawal(mustWithdraw: Boolean, type: String, amount: String, beneficiaryId: String): LiveData<Resource<List<Withdrawal>>> {
        mustWithdrawLiveData.value = mustWithdraw
        return Transformations.switchMap(mustWithdrawLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { withdrawalDao.getAll() } },
                    { botCoinService.withdrawal(type, amount, beneficiaryId) },
                    { withdrawalDao.upsert(it, withdrawalDao) }
            )
        }
    }

    fun send(mustSend: Boolean, amount: String, currency: String, address: String, destinationTag: String = ""): LiveData<Resource<List<Send>>> {
        mustSendLiveData.value = mustSend
        return Transformations.switchMap(mustSendLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { sendDao.getAll() } },
                    { if (destinationTag.isNotBlank()) botCoinService.send(amount, currency, address) else botCoinService.send(amount, currency, address, destinationTag) },
                    { sendDao.upsert(it, sendDao) }
            )
        }
    }

    fun receive(mustReceive: Boolean, asset: String): LiveData<Resource<List<Receive>>> {
        mustReceiveLiveData.value = mustReceive
        return Transformations.switchMap(mustReceiveLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { receiveDao.getAll() } },
                    { botCoinService.receive(asset) },
                    { receiveDao.upsert(it, receiveDao) }
            )
        }
    }
}