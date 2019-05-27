package eu.mobilebear.babylon.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxFactory @Inject constructor() {

    val ioScheduler: Scheduler
        get() = Schedulers.io()

    val computationScheduler: Scheduler
        get() = Schedulers.computation()

    val singleThreadScheduler: Scheduler
        get() = Schedulers.single()

    val mainThreadScheduler: Scheduler
        get() = AndroidSchedulers.mainThread()
}
