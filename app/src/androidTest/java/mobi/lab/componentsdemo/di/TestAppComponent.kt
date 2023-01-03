package mobi.lab.componentsdemo.di

import dagger.Component
import mobi.lab.componentsdemo.domain.di.UseCaseModule
import mobi.lab.componentsdemo.infrastructure.di.GatewayModule
import mobi.lab.componentsdemo.infrastructure.di.MapperModule
import mobi.lab.componentsdemo.infrastructure.di.PlatformModule
import mobi.lab.componentsdemo.infrastructure.di.ResourceModule
import mobi.lab.componentsdemo.infrastructure.di.StorageModule
import javax.inject.Singleton

/**
 * A different implementation of BaseAppCompnent that can be passed into Injector.
 * This component allows us to switch out modules for testing purposes.
 * For example, we provide a different SchedulerProvider via TestSchedulerModule
 */
@Singleton
@Component(
    modules = [
        ResourceModule::class,
        UseCaseModule::class,
        GatewayModule::class,
        MapperModule::class,
        StorageModule::class,
        AppModule::class,
        PlatformModule::class,
        TestSchedulerModule::class
    ]
)
interface TestAppComponent : BaseAppComponent {
    fun inject(item: SchedulerModuleTest)
}
