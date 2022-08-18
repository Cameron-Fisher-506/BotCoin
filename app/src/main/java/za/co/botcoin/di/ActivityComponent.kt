package za.co.botcoin.di

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import za.co.botcoin.di.featureModules.HomeViewModule
import za.co.botcoin.utils.ProgressDialog

@Subcomponent(modules = [ViewModelsModule::class, PresentationModule::class, HomeViewModule::class])
interface ActivityComponent {
    fun getViewModelFactory(): ViewModelProvider.Factory
    fun getProgressDialogService(): ProgressDialog
}