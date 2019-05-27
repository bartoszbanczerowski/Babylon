package eu.mobilebear.babylon.domain.interactor.base

import androidx.annotation.IntDef
import eu.mobilebear.babylon.rx.RxFactory
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseReactiveUseCase(
    @ThreadExecutor val scheduler: Int,
    val rxFactory: RxFactory
) {

    protected val threadExecutorScheduler: Scheduler = getThreadExecutorScheduler(scheduler)

    protected val postExecutionThreadScheduler: Scheduler = rxFactory.mainThreadScheduler

    private val disposables = CompositeDisposable()

    /**
     * Dispose from current [CompositeDisposable].
     */
    open fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    protected fun addDisposable(disposable: Disposable) {
        disposables.add(checkNotNull(disposable, { "disposable cannot be null!" }))
    }

    private fun getThreadExecutorScheduler(@ThreadExecutor scheduler: Int): Scheduler {
        return when (scheduler) {
            IO_THREAD -> rxFactory.ioScheduler
            COMPUTATION_THREAD -> rxFactory.computationScheduler
            SINGLE_THREAD -> rxFactory.singleThreadScheduler
            else -> throw IllegalArgumentException("Unknown thread executor!")
        }
    }

    companion object {
        const val IO_THREAD = 0
        const val COMPUTATION_THREAD = 1
        const val SINGLE_THREAD = 2

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(IO_THREAD, COMPUTATION_THREAD, SINGLE_THREAD)
        annotation class ThreadExecutor
    }
}
