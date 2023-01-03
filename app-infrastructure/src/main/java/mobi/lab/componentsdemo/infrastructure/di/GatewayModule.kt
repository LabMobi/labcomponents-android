package mobi.lab.componentsdemo.infrastructure.di

import dagger.Binds
import dagger.Module
import mobi.lab.componentsdemo.domain.gateway.AuthGateway
import mobi.lab.componentsdemo.infrastructure.auth.AuthProvider

@Module(includes = [GatewayModule.Definitions::class])
object GatewayModule {

    @Module
    internal interface Definitions {
        @Binds fun bindAuthGateway(impl: AuthProvider): AuthGateway
    }
}
