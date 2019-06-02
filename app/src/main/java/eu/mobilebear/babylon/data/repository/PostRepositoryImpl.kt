package eu.mobilebear.babylon.data.repository

import eu.mobilebear.babylon.domain.model.PostValidationModel
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.networking.SocialService
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.util.ConnectionChecker
import eu.mobilebear.babylon.util.NetworkException
import io.reactivex.Single
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl
@Inject
constructor(
    private val socialService: SocialService,
    private val connectionChecker: ConnectionChecker
) : PostRepository {


    override fun requestPosts(): Single<PostsValidationModel> {
        if (!connectionChecker.isConnected) return Single.error(NetworkException())
        return socialService.getPosts()
            .map { response -> mapPostsResponse(response) }
            .onErrorReturn { mapPostsError() }
    }

    override fun requestPost(postId: Int): Single<PostValidationModel> {
        if (!connectionChecker.isConnected) return Single.error(NetworkException())
        return socialService.getPost(postId)
            .map { response -> mapPostResponse(response) }
            .onErrorReturn { mapPostError() }
    }

    private fun mapPostsResponse(response: Response<List<Post>>): PostsValidationModel {
        val isResponseSuccessful = response.isSuccessful && response.body() != null

        return if (isResponseSuccessful) {
            if (response.body()!!.isEmpty()) {
                PostsValidationModel(response.body()!!, PostsValidationModel.NO_POSTS)
            } else {
                PostsValidationModel(response.body()!!, PostsValidationModel.POSTS_DOWNLOADED)
            }
        } else {
            mapPostsError()
        }
    }

    private fun mapPostsError(): PostsValidationModel = PostsValidationModel(emptyList(), PostsValidationModel.GENERAL_ERROR)

    private fun mapPostResponse(response: Response<Post>): PostValidationModel {
        val isResponseSuccessful = response.isSuccessful && response.body() != null

        return if (isResponseSuccessful) {
            if (response.body() == null) {
                mapPostError()
            } else {
                PostValidationModel(response.body()!!, PostValidationModel.POST_DOWNLOADED)
            }
        } else {
            mapPostError()
        }
    }

    private fun mapPostError(): PostValidationModel = PostValidationModel(Post(-1,-1, "", ""), PostsValidationModel.GENERAL_ERROR)
}
