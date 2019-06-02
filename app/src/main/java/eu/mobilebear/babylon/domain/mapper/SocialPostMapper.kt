package eu.mobilebear.babylon.domain.mapper

import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.networking.response.responsedata.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialPostMapper @Inject constructor() {

    fun transform(post: Post, user: User?): SocialPost {
        return SocialPost(
            id = post.id,
            body = post.body,
            title = post.title,
            userId = post.userId,
            username = user?.username,
            email = user?.email,
            address = user?.address,
            phone = user?.phone,
            website = user?.website,
            company = user?.company
        )
    }

    fun transform(post: Post): SocialPost {
        return SocialPost(
            id = post.id,
            body = post.body,
            title = post.title,
            userId = post.userId,
            username = null,
            email = null,
            address = null,
            phone = null,
            website = null,
            company = null
        )
    }
}
