package za.co.botcoin.model.repository

import android.app.Application
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

    suspend fun withdrawal(type: String, amount: String, beneficiaryId: String): Resource<List<Withdrawal>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { withdrawalDao.getAll() } },
                { botCoinService.withdrawal("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", type, amount, beneficiaryId) },
                { withdrawalDao.upsert(it, withdrawalDao) }
        )
    }

    suspend fun send(amount: String, currency: String, address: String, destinationTag: String = ""): Resource<List<Send>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { sendDao.getAll() } },
                {
                    if (destinationTag.isNotBlank()) botCoinService.send("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", amount, currency, address)
                    else botCoinService.send("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", amount, currency, address, destinationTag)
                },
                { sendDao.upsert(it, sendDao) }
        )
    }

    suspend fun receive(asset: String, keyId: String, secretKey: String): Resource<List<Receive>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { receiveDao.getAll() } },
                { botCoinService.receive("Basic ${GeneralUtils.getAuth(keyId, secretKey)}", asset) },
                { receiveDao.upsert(it, receiveDao) }
        )
    }

    suspend fun stopOrder(orderId: String): Resource<List<StopOrder>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { stopOrderDao.getAll() } },
                { botCoinService.stopOrder("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", orderId) },
                { stopOrderDao.upsert(it, stopOrderDao) }
        )
    }
}