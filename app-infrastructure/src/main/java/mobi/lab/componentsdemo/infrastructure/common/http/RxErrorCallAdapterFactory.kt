package mobi.lab.componentsdemo.infrastructure.common.http

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.reflect.Type

/**
 * CallAdapterFactory wraps RxJava2CallAdapterFactory. The internal RxJavaCallAdapters are also wrapped
 * to return our own internal error object when needed.
 *
 * So when consuming the stream, onError() callback's Throwable type can be checked against our
 * internal error type and used when available
 */
internal class RxErrorCallAdapterFactory private constructor(val errorTransformer: ErrorTransformer) : CallAdapter.Factory() {

    private val originalFactory by lazy { RxJava3CallAdapterFactory.create() }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        val originalAdapter = originalFactory.get(returnType, annotations, retrofit)
        if (originalAdapter == null) {
            // RxJava adapter not possible for the returnType. Ignore
            return null
        }
        return RxCallAdapterWrapper(errorTransformer, originalAdapter)
    }

    companion object {
        fun create(errorTransformer: ErrorTransformer): CallAdapter.Factory = RxErrorCallAdapterFactory(errorTransformer)
    }

    private class RxCallAdapterWrapper<R>(
        val errorTransformer: ErrorTransformer,
        val wrappedCallAdapter: CallAdapter<R, *>
    ) : CallAdapter<R, Any> {

        override fun responseType(): Type = wrappedCallAdapter.responseType()

        override fun adapt(call: Call<R>): Any {
            return when (val result = wrappedCallAdapter.adapt(call)) {
                is Observable<*> -> result.onErrorResumeNext { throwable: Throwable -> Observable.error(transformException(throwable)) }
                is Single<*> -> result.onErrorResumeNext { throwable: Throwable -> Single.error(transformException(throwable)) }
                is Flowable<*> -> result.onErrorResumeNext { throwable: Throwable -> Flowable.error(transformException(throwable)) }
                is Maybe<*> -> result.onErrorResumeNext { throwable: Throwable -> Maybe.error(transformException(throwable)) }
                is Completable -> result.onErrorResumeNext { throwable: Throwable -> Completable.error(transformException(throwable)) }
                else -> result
            }
        }

        private fun transformException(error: Throwable): Throwable {
            return errorTransformer.transform(error)
        }
    }
}
