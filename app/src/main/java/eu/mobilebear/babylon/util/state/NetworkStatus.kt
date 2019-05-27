package eu.mobilebear.babylon.util.state

import eu.mobilebear.babylon.util.NetworkException

sealed class NetworkStatus {

    object Running : NetworkStatus()

    object Success : NetworkStatus()

    object NoConnectionError : NetworkStatus()

    data class Error internal constructor(val message: Throwable? = null) : NetworkStatus()

    companion object {

        fun error(e: Throwable? = null): NetworkStatus = if (isNoConnectionError(e)) NoConnectionError else Error(e)

        private fun isNoConnectionError(e: Throwable?) = e != null && e is NetworkException
    }
}
