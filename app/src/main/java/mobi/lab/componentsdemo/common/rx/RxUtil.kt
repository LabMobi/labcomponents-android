package mobi.lab.componentsdemo.common.rx

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun dispose(vararg disposables: Disposable?) {
    for (disposable in disposables) {
        disposable?.dispose()
    }
}

fun clearDisposable(disposable: CompositeDisposable?) {
    disposable?.clear()
}
