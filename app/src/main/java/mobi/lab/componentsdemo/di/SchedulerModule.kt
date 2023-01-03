package mobi.lab.componentsdemo.di

import dagger.Module
import dagger.Provides
import mobi.lab.componentsdemo.common.rx.AndroidSchedulerProvider
import mobi.lab.componentsdemo.common.rx.SchedulerProvider
import javax.inject.Singleton

@Module
object SchedulerModule {
    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider = AndroidSchedulerProvider()
}
