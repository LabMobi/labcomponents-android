package mobi.lab.componentsdemo.common.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class AndroidSchedulerProvider : SchedulerProvider {
    override val main: Scheduler = AndroidSchedulers.mainThread()
    override val computation: Scheduler = Schedulers.computation()
    override val io: Scheduler = Schedulers.io()
}
