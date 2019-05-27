package eu.mobilebear.babylon.util

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionChecker
@Inject constructor(
    private val application: Application,
    private val androidObjectsFactory: AndroidObjectsFactory
) : BroadcastReceiver() {

    private val connectivityManager: ConnectivityManager by lazy { application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    private val listeners = mutableSetOf<OnConnectionChangeListener>()

    private var lastNetworkInfo: NetworkInfo? = null

    private var registered: Boolean = false

    private var firstReceive = true

    val isConnected: Boolean
        get() {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    override fun onReceive(context: Context, intent: Intent) {
        val networkInfo = connectivityManager.activeNetworkInfo

        // ignore doubled messages
        if (equals(lastNetworkInfo, networkInfo)) {
            return
        }
        setLastNetworkInfo(networkInfo)
        if (networkInfo == null) {
            notifyConnectionLost()
            return
        }
        val connected = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        val failover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false)
        if (failover) {
            // it's only network switching
            return
        }

        when {
            firstReceive -> notifyConnectionStatusDetected(connected)
            connected -> notifyConnectionReestablished()
            else -> notifyConnectionLost()
        }

        setFirstReceive(false)
    }

    @Synchronized
    @Suppress("DEPRECATION")
    // TODO: refactor checking for connection state according to: https://developer.android.com/reference/android/net/ConnectivityManager#CONNECTIVITY_ACTION
    fun registerOnConnectionChangeListener(listener: OnConnectionChangeListener) {
        listeners.add(listener)

        /*
         * Register receiver when first external listener is added
         */
        if (listeners.size == 1) {
            val intentFilter = androidObjectsFactory.createIntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            application.registerReceiver(this, intentFilter)
            registered = true
        }
    }

    @Synchronized
    fun unregisterOnConnectionChangeListener(listener: OnConnectionChangeListener) {
        listeners.remove(listener)

        if (listeners.isEmpty() && registered) {
            application.unregisterReceiver(this)
            registered = false
        }
    }

    fun setFirstReceive(firstReceive: Boolean) {
        this.firstReceive = firstReceive
    }

    fun setLastNetworkInfo(lastNetworkInfo: NetworkInfo?) {
        this.lastNetworkInfo = lastNetworkInfo
    }

    @Synchronized
    private fun notifyConnectionReestablished() {
        listeners.forEach { it.onConnectionReestablished() }
    }

    @Synchronized
    private fun notifyConnectionLost() {
        listeners.forEach { it.onConnectionLost() }
    }

    @Synchronized
    private fun notifyConnectionStatusDetected(connected: Boolean) {
        listeners.forEach { it.onConnectionStatusDetected(connected) }
    }

    @Suppress("DEPRECATION")
    private fun equals(first: NetworkInfo?, second: NetworkInfo?): Boolean {
        if (first == null) {
            return second == null
        }
        if (second == null) {
            return false
        }
        var equals = true
        equals = equals and (first.type == second.type)
        equals = equals and (first.subtype == second.subtype)
        equals = equals and (first.state == second.state)
        equals = equals and (first.detailedState == second.detailedState)
        return equals
    }
}
