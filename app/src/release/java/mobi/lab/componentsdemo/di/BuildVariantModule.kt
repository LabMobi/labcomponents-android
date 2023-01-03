package mobi.lab.componentsdemo.di

import dagger.Module
import dagger.Provides
import mobi.lab.componentsdemo.common.debug.DebugActions
import mobi.lab.componentsdemo.debug.ReleaseDebugActions
import javax.inject.Singleton

@Module
object BuildVariantModule {

    @Provides
    @Singleton
    fun provideDebugActions(): DebugActions {
        return ReleaseDebugActions()
    }
}
