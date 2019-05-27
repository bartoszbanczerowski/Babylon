package eu.mobilebear.babylon.rx

import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver

/**
 * This is a default handler for business use cases.
 * It should be used for use cases which don't return any value.
 *
 */
class EmptyObserver<T> : DisposableObserver<T>() {

    override fun onNext(@NonNull t: T) {}

    override fun onError(@NonNull e: Throwable) {}

    override fun onComplete() {}
}
