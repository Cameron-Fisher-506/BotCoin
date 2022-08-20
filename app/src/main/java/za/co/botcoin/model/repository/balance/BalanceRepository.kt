package za.co.botcoin.model.repository.balance

import android.app.Application
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IBalanceDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource
import javax.inject.Inject

class BalanceRepository @Inject constructor(private val application: Application) : BaseRepository() {
    private val balanceDao: IBalanceDao = BotCoinDatabase.getDatabase(application).balanceDao()

    suspend fun fetchBalances(): Resource<List<Balance>> {
        return DataAccessStrategyUtils.synchronizedCache(
            application,
            { BotCoinDatabase.getResource { balanceDao.getAll() } },
            { botCoinService.getBalances("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}") },
            { it.balance?.let { balance -> balanceDao.upsert(balance, balanceDao) } }
        )
    }
}