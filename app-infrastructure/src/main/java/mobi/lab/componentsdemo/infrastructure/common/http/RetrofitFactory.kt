package mobi.lab.componentsdemo.infrastructure.common.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit

internal class RetrofitFactory(private val errorTransformer: ErrorTransformer) {

    fun newInstance(
        baseUrl: String,
        httpClient: OkHttpClient,
        converterFactoryProvider: RetrofitConverterFactoryProvider
    ): Retrofit {
        return Retrofit.Builder()
            .validateEagerly(true)
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactoryProvider.get())
            .addCallAdapterFactory(RxErrorCallAdapterFactory.create(errorTransformer))
            .client(httpClient)
            .build()
    }
}
