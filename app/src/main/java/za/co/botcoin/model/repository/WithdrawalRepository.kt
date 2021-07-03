package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import za.co.botcoin.model.models.*
import za.co.botcoin.model.room.*
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class WithdrawalRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val withdrawalDao: IWithdrawalDao = BotCoinDatabase.getDatabase(application).withdrawalDao()
    private val sendDao: ISendDao = BotCoinDatabase.getDatabase(application).sendDao()
    private val receiveDao: IReceiveDao = BotCoinDatabase.getDatabase(application).receiveDao()

    private val stopOrderDao: IStopOrderDao = BotCoinDatabase.getDatabase(application).stopOrderDao()

    private val mustSendLiveData by lazy { MutableLiveData<Boolean>() }
    private val mustWithdrawLiveData by lazy { MutableLiveData<Boolean>() }
    private val mustReceiveLiveData by lazy { MutableLiveData<Boolean>() }
    private val mustStopOrderLiveData by lazy { MutableLiveData<Boolean>() }

    fun withdrawal(mustWithdraw: Boolean, type: String, amount: String, beneficiaryId: String): LiveData<Resource<List<Withdrawal>>> {
        mustWithdrawLiveData.value = mustWithdraw
        return Transformations.switchMap(mustWithdrawLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { withdrawalDao.getAll() } },
                    { botCoinService.withdrawal("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", type, amount, beneficiaryId) },
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
                    { if (destinationTag.isNotBlank()) botCoinService.send("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}",amount, currency, address)
                        else botCoinService.send("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", amount, currency, address, destinationTag) },
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
                    { botCoinService.receive("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", asset) },
                    { receiveDao.upsert(it, receiveDao) }
            )
        }
    }

    fun stopOrder(mustStopOrder: Boolean, orderId: String): LiveData<Resource<List<StopOrder>>> {
        mustStopOrderLiveData.value = mustStopOrder
        return Transformations.switchMap(mustStopOrderLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { stopOrderDao.getAll() } },
                    { botCoinService.stopOrder("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", orderId) },
                    { stopOrderDao.upsert(it, stopOrderDao) }
            )
        }
    }
}