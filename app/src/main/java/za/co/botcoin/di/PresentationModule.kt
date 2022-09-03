package za.co.botcoin.di

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.di.managers.ResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogService
import za.co.botcoin.utils.services.ProgressDialogService

@Module
class PresentationModule(private val activity: AppCompatActivity) {
    @Provides
    fun progressDialogService(): ProgressDialogService = ProgressDialogService(activity)

    @Provides
    fun alertDialogService(): AlertDialogService = AlertDialogService(activity)

    @Provides
    fun resourceManager(): IResourceManager = ResourceManager(activity.resources)
}