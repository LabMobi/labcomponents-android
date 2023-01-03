package mobi.lab.componentsdemo.di

import dagger.Component
import mobi.lab.componentsdemo.domain.di.UseCaseModule
import mobi.lab.componentsdemo.infrastructure.di.GatewayModule
import mobi.lab.componentsdemo.infrastructure.di.MapperModule
import mobi.lab.componentsdemo.infrastructure.di.PlatformModule
import mobi.lab.componentsdemo.infrastructure.di.ResourceModule
import mobi.lab.componentsdemo.infrastructure.di.StorageModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ResourceModule::class,
        UseCaseModule::class,
        GatewayModule::class,
        MapperModule::class,
        StorageModule::class,
        AppModule::class,
        SchedulerModule::class,
        PlatformModule::class
    ]
)
interface AppComponent : BaseAppComponent
/**
 * DO NOT ADD METHODS HERE. Add methods to [BaseAppComponent].
 */
