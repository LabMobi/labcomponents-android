package mobi.lab.componentsdemo.common.rx

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

object TestSchedulerProvider : SchedulerProvider {
    override val main: Scheduler = Schedulers.trampoline()
    override val computation: Scheduler = Schedulers.trampoline()
    override val io: Scheduler = Schedulers.trampoline()
}
