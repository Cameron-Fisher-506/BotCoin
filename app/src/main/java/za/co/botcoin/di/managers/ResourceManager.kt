package za.co.botcoin.di.managers

import android.content.res.Resources

class ResourceManager(private val resources: Resources): IResourceManager {
    override fun getString(stringId: Int): String = resources.getString(stringId)
}