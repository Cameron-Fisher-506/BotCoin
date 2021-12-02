package za.co.botcoin.model.repository.account

import android.app.Application
import za.co.botcoin.model.repository.BaseViewModel

class AccountViewModel(application: Application) : BaseViewModel(application) {
    var repository: AccountRepository = AccountRepository(application)
}