package com.stepstone.base.util.rx

import io.reactivex.observers.DisposableSingleObserver

/**
 * This is a default handler for business use cases.
 * It should be used for use cases which don't return any value.
 *
 */

open class EmptySingleObserver<T> : DisposableSingleObserver<T>() {

    override fun onSuccess(result: T) {
    }

    override fun onError(throwable: Throwable) {
    }
}
