package za.co.botcoin.di

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import za.co.botcoin.utils.services.ProgressDialogService

@Module
class PresentationModule(private val activity: AppCompatActivity) {
    @Provides
    fun progressDialogService(): ProgressDialogService = ProgressDialogService(activity)
}