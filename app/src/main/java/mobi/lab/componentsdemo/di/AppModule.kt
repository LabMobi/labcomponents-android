package mobi.lab.componentsdemo.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import mobi.lab.componentsdemo.Env
import mobi.lab.componentsdemo.common.util.isDebugBuild
import mobi.lab.componentsdemo.infrastructure.common.platform.AppEnvironment
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, BuildVariantModule::class])
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideAppEnvironment(): AppEnvironment = AppEnvironment(Env.URL_BASE, isDebugBuild())
}
