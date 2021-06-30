package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import za.co.botcoin.model.models.Balance
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IBalanceDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class BalancesRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val balanceDao: IBalanceDao = BotCoinDatabase.getDatabase(application).balanceDao()

    private val updateLiveData by lazy { MutableLiveData<Boolean>() }

    public fun fetchBalances(update: Boolean): LiveData<Resource<List<Balance>>> {
        updateLiveData.value = update
        return Transformations.switchMap(updateLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { balanceDao.getAll() } },
                    { botCoinService.getBalances(GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)) },
                    { it.balances.let { balances -> balanceDao.upsert(balances, balanceDao) } }
            )
        }
    }
}