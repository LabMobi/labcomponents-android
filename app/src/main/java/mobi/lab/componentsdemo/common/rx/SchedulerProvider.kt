package mobi.lab.componentsdemo.common.rx

import io.reactivex.rxjava3.core.CompletableTransformer
import io.reactivex.rxjava3.core.MaybeTransformer
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.SingleTransformer
import mobi.lab.componentsdemo.common.platform.LogoutMonitor
import mobi.lab.componentsdemo.domain.entities.DomainException
import mobi.lab.componentsdemo.domain.entities.ErrorCode

interface SchedulerProvider {
    val main: Scheduler
    val computation: Scheduler
    val io: Scheduler

    fun <T : Any> observable(subscribeOn: Scheduler = io, observeOn: Scheduler = main): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .doOnError(::checkUnauthorizedError)
        }
    }

    fun <T : Any> single(subscribeOn: Scheduler = io, observeOn: Scheduler = main): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .doOnError(::checkUnauthorizedError)
        }
    }

    fun completable(subscribeOn: Scheduler = io, observeOn: Scheduler = main): CompletableTransformer {
        return CompletableTransformer {
            it.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .doOnError(::checkUnauthorizedError)
        }
    }

    fun completableNoAuthCheck(subscribeOn: Scheduler = io, observeOn: Scheduler = main): CompletableTransformer {
        return CompletableTransformer {
            it.subscribeOn(subscribeOn)
                .observeOn(observeOn)
        }
    }

    fun <T : Any> maybe(subscribeOn: Scheduler = io, observeOn: Scheduler = main): MaybeTransformer<T, T> {
        return MaybeTransformer {
            it.subscribeOn(subscribeOn)
                .observeOn(observeOn)
                .doOnError(::checkUnauthorizedError)
        }
    }

    private fun checkUnauthorizedError(error: Throwable) {
        if (error is DomainException && error.isFor(ErrorCode.LOCAL_UNAUTHORIZED)) {
            LogoutMonitor.logout()
        }
    }
}
