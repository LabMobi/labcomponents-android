package mobi.lab.componentsdemo.infrastructure.common.http

import mobi.lab.componentsdemo.infrastructure.common.platform.AppEnvironment
import retrofit2.Retrofit
import kotlin.reflect.KClass

internal class RetrofitUnauthorizedResourceFactory(
    env: AppEnvironment,
    httpFactory: HttpClientFactory,
    retrofitFactory: RetrofitFactory,
    converterFactoryProvider: RetrofitConverterFactoryProvider,
) {

    private val retrofit: Retrofit

    init {
        val httpClient = if (env.debug) {
            httpFactory.newInstance(
                interceptors = arrayOf(
                    httpFactory.createLoggingInterceptor(),
                    httpFactory.createContentTypeInterceptor()
                )
            )
        } else {
            httpFactory.newInstance(
                interceptors = arrayOf(
                    httpFactory.createContentTypeInterceptor()
                )
            )
        }
        retrofit = retrofitFactory.newInstance(env.baseUrl, httpClient, converterFactoryProvider)
    }

    fun <T : Any> create(cls: KClass<T>): T {
        return retrofit.create(cls.java)
    }
}
