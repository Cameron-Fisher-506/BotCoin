package za.co.botcoin.model.repository.withdrawal

import android.app.Application
import za.co.botcoin.model.models.Withdrawal
import za.co.botcoin.model.repository.BaseRepository
import za.co.botcoin.model.room.BotCoinDatabase
import za.co.botcoin.model.room.IWithdrawalDao
import za.co.botcoin.model.room.upsert
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.DataAccessStrategyUtils
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.Resource

class WithdrawalRepository(private val application: Application) : BaseRepository() {
    private val withdrawalDao: IWithdrawalDao = BotCoinDatabase.getDatabase(application).withdrawalDao()

    suspend fun withdrawal(type: String, amount: String, beneficiaryId: String): Resource<List<Withdrawal>> {
        return DataAccessStrategyUtils.synchronizedCache(
                application,
                { BotCoinDatabase.getResource { withdrawalDao.getAll() } },
                { botCoinService.withdrawal("Basic ${GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)}", type, amount, beneficiaryId) },
                { withdrawalDao.upsert(it, withdrawalDao) }
        )
    }
}