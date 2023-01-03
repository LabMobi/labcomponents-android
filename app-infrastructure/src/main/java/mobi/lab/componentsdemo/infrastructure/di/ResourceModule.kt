package mobi.lab.componentsdemo.infrastructure.di

import dagger.Module
import dagger.Provides
import mobi.lab.componentsdemo.domain.storage.SessionStorage
import mobi.lab.componentsdemo.infrastructure.auth.remote.AuthResource
import mobi.lab.componentsdemo.infrastructure.common.http.ErrorTransformer
import mobi.lab.componentsdemo.infrastructure.common.http.HttpClientFactory
import mobi.lab.componentsdemo.infrastructure.common.http.RetrofitConverterFactoryProvider
import mobi.lab.componentsdemo.infrastructure.common.http.RetrofitFactory
import mobi.lab.componentsdemo.infrastructure.common.http.RetrofitResourceFactory
import mobi.lab.componentsdemo.infrastructure.common.http.RetrofitUnauthorizedResourceFactory
import mobi.lab.componentsdemo.infrastructure.common.json.MoshiFactory
import mobi.lab.componentsdemo.infrastructure.common.platform.AppEnvironment
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
object ResourceModule {

    @Provides
    @Singleton
    internal fun provideRetrofitConverterFactoryProvider(): RetrofitConverterFactoryProvider {
        return object : RetrofitConverterFactoryProvider {
            override fun get(): Converter.Factory {
                return MoshiConverterFactory.create(MoshiFactory.get())
            }
        }
    }

    @Provides
    @Singleton
    internal fun provideRetrofitFactory(errorTransformer: ErrorTransformer): RetrofitFactory = RetrofitFactory(errorTransformer)

    /**
     * A Retrofit API resource factory with a built in request authentication interceptor
     */
    @Provides
    @Singleton
    internal fun provideResourceFactory(
        env: AppEnvironment,
        sessionStorage: SessionStorage,
        retrofitFactory: RetrofitFactory,
        converterFactoryProvider: RetrofitConverterFactoryProvider,
    ): RetrofitResourceFactory = RetrofitResourceFactory(
        env,
        HttpClientFactory,
        retrofitFactory,
        sessionStorage,
        converterFactoryProvider,
    )

    /**
     * A Retrofit API resource factory without an authentication interceptor.
     * Any requests that don't need authentication should be provided included in a Retrofit Resource interface and provided via
     * this ResourceFactory to avoid unnecessary authentication tokens added to requests.
     */
    @Provides
    @Singleton
    internal fun provideUnauthorizedResourceFactory(
        env: AppEnvironment,
        retrofitFactory: RetrofitFactory,
        converterFactoryProvider: RetrofitConverterFactoryProvider,
    ): RetrofitUnauthorizedResourceFactory = RetrofitUnauthorizedResourceFactory(env, HttpClientFactory, retrofitFactory, converterFactoryProvider)

    @Provides
    @Singleton
    internal fun provideAuthResource(resFactory: RetrofitResourceFactory): AuthResource = resFactory.create(AuthResource::class)
}
