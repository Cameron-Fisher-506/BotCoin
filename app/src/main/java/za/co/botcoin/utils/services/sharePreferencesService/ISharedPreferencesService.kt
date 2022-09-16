package za.co.botcoin.utils.services.sharePreferencesService

import android.content.Context

interface ISharedPreferencesService {
    fun save(key: String, value: String)
    operator fun get(sharedPrefName: String): String?
}