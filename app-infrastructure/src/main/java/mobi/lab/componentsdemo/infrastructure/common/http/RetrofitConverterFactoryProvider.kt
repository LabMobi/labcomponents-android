package mobi.lab.componentsdemo.infrastructure.common.http

import retrofit2.Converter

/**
 * Provide a Converter.Factory implementation for Retrofit.
 * This let's us abstract away concrete dependencies on whatever data formats we want to use
 * for network requests.
 * Let's us pass around this factory as a dependency and we can switch the underlying
 * implementation without changing how Retrofit instances are created.
 */
interface RetrofitConverterFactoryProvider {
    fun get(): Converter.Factory
}
