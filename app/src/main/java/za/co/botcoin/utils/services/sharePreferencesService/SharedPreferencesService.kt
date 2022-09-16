package za.co.botcoin.utils.services.sharePreferencesService

import android.content.Context

class SharedPreferencesService(private val context: Context): ISharedPreferencesService {
    override fun save(key: String, value: String) {
        BaseSharedPreferencesService.save(context, key, value)
    }

    override fun get(sharedPrefName: String): String? = BaseSharedPreferencesService[context, sharedPrefName]
}