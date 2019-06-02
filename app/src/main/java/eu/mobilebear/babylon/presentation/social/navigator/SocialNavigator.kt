package eu.mobilebear.babylon.presentation.social.navigator

import android.content.Context
import android.content.Intent
import android.net.Uri
import eu.mobilebear.babylon.presentation.socialdetail.SocialDetailActivity
import javax.inject.Inject

class SocialNavigator @Inject constructor(private val context: Context) {

    companion object {
        private const val GOOGLE_MAPS_APP_PACKAGE = "com.google.android.apps.maps"
        const val POST_ID = "postId"
        const val USER_ID = "userId"
    }

    fun goToSocialDetail(postId: Int, userId: Int) {
        val intent = Intent(context, SocialDetailActivity::class.java)
        intent.putExtra(POST_ID, postId)
        intent.putExtra(USER_ID, userId)
        context.startActivity(intent)
    }

    fun goToMap(lat: String, lng: String) {
        val uriString = "geo:$lat,$lng"
        val gmmIntentUri = Uri.parse(uriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage(GOOGLE_MAPS_APP_PACKAGE)
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        }
    }
}
