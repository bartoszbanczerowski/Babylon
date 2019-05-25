package eu.mobilebear.babylon.networking

import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.networking.response.responsedata.User
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SocialService {

    @GET("posts")
    fun getPosts(): Single<Response<List<Post>>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: Int): Single<Response<Post>>

    @GET("users")
    fun getUsers(): Single<Response<List<User>>>

    @GET("users/{id}")
    fun getUser(@Path("id") id: Int): Single<Response<User>>

    @GET("comments")
    fun getComments(): Single<Response<List<Comment>>>

    @GET("comments/{id}")
    fun getComment(@Path("id") id: Int): Single<Response<Comment>>
}
