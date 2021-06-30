package za.co.botcoin.model.repository

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IWithdrawalDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.model.service.BotCoinService
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.Resource

class WithdrawalRepository(private val application: Application) {
    private val botCoinService: BotCoinService = BotCoinService()
    private val withdrawalDao: IWithdrawalDao = BotCoinDatabase.getDatabase(application).withdrawalDao()

    private lateinit var mustWithdrawLiveData: MutableLiveData<Boolean>

    public fun withdrawal(mustWithdraw: Boolean, type: String, amount: String, beneficiaryId: String): LiveData<Resource<List<Withdrawal>>> {
        mustWithdrawLiveData.value = mustWithdraw
        return Transformations.switchMap(mustWithdrawLiveData) {
            DataAccessStrategyUtils.synchronizedCache(
                    application,
                    { BotCoinDatabase.getResource { withdrawalDao.getAll() } },
                    { botCoinService.withdrawal(type, amount, beneficiaryId) },
                    { it?.let { withdrawal -> withdrawalDao.upsert(withdrawal, withdrawalDao) } }
            )
        }
    }
}