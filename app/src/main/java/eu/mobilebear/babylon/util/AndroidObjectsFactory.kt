package eu.mobilebear.babylon.util

import android.content.IntentFilter
import javax.inject.Singleton

@Singleton
open class AndroidObjectsFactory {

    fun createIntentFilter(action: String): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(action)
        return intentFilter
    }

}
