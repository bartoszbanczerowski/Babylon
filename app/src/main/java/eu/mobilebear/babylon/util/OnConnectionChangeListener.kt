package eu.mobilebear.babylon.util

interface OnConnectionChangeListener {

    fun onConnectionLost()

    fun onConnectionReestablished()

    fun onConnectionStatusDetected(connected: Boolean)
}
