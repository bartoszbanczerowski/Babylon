package eu.mobilebear.babylon.presentation.social.view

interface SocialPostOnClickListener {

    fun onSocialPostAddressClicked(lat: String, lng: String)
    fun onSocialPostClicked(postId: Int, userId: Int)
}
