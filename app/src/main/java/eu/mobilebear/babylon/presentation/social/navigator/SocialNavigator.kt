package eu.mobilebear.babylon.presentation.social.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import javax.inject.Inject

class SocialNavigator @Inject constructor(private val context: Context) {

    @Suppress("UseExpressionBody")
    fun goToSocialDetail(postId: Int) {
        //TODO implement navigation to SocialDetail
    }

    fun goToMap(lat: String, lng: String) {
        val uriString = "geo:" + lat + "," + lng
        val gmmIntentUri = Uri.parse(uriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        }
    }
}
