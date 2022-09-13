package za.co.botcoin.di

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import za.co.botcoin.di.managers.IResourceManager
import za.co.botcoin.di.managers.ResourceManager
import za.co.botcoin.utils.services.alertDialogService.AlertDialogService
import za.co.botcoin.utils.services.ProgressDialogService
import za.co.botcoin.utils.services.alertDialogService.IAlertDialogService
import za.co.botcoin.utils.services.notificationService.INotificationService
import za.co.botcoin.utils.services.notificationService.NotificationService
import za.co.botcoin.utils.services.sharePreferencesService.ISharedPreferencesService
import za.co.botcoin.utils.services.sharePreferencesService.SharedPreferencesService

@Module
class PresentationModule(private val activity: AppCompatActivity) {
    @Provides
    fun progressDialogService(): ProgressDialogService = ProgressDialogService(activity)

    @Provides
    fun alertDialogService(): IAlertDialogService = AlertDialogService(activity)

    @Provides
    fun resourceManager(): IResourceManager = ResourceManager(activity.resources)

    @Provides
    fun sharedPreferencesService(): ISharedPreferencesService = SharedPreferencesService(activity)

    @Provides
    fun notificationService(): INotificationService = NotificationService(activity)
}