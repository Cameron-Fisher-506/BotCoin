package za.co.botcoin.di

import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ManagerModule::class])
interface ApplicationComponent {

    fun getActivityComponent(presentationModule: PresentationModule): ActivityComponent
}