package eu.mobilebear.babylon.data.repository

import com.nhaarman.mockitokotlin2.whenever
import eu.mobilebear.babylon.domain.model.PostValidationModel
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import eu.mobilebear.babylon.networking.SocialService
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.util.ConnectionChecker
import eu.mobilebear.babylon.util.NetworkException
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import retrofit2.Response

class PostRepositoryImplTest {

    companion object {
        private const val POST_ID = 1234
    }

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var mockSocialService: SocialService

    @Mock
    lateinit var mockConnectionChecker: ConnectionChecker

    @Mock
    lateinit var mockPostsResponse: Response<List<Post>>

    @Mock
    lateinit var mockPostResponse: Response<Post>

    @Mock
    lateinit var mockPost: Post

    @Mock
    lateinit var mockThrowable: Throwable

    lateinit var postRepositoryImpl: PostRepositoryImpl

    @Before
    fun setUp() {
        postRepositoryImpl = PostRepositoryImpl(mockSocialService, mockConnectionChecker)
    }

    @Test
    fun `Should getPosts() throw network exception when there is no connection`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(false)

        // when
        val testObserver = postRepositoryImpl.requestPosts().test()

        // then
        testObserver.assertError(NetworkException::class.java)
    }

    @Test
    fun `Should getPosts() return PostsValidationModel POSTS_DOWNLOADED state`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getPosts()).thenReturn(Single.just(mockPostsResponse))
        whenever(mockPostsResponse.isSuccessful).thenReturn(true)
        whenever(mockPostsResponse.body()).thenReturn(listOf(mockPost))

        // when
        val testObserver = postRepositoryImpl.requestPosts().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(PostsValidationModel(listOf(mockPost), PostsValidationModel.POSTS_DOWNLOADED))
    }

    @Test
    fun `Should getPosts() return PostsValidationModel NO_POSTS state`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getPosts()).thenReturn(Single.just(mockPostsResponse))
        whenever(mockPostsResponse.isSuccessful).thenReturn(true)
        whenever(mockPostsResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = postRepositoryImpl.requestPosts().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(PostsValidationModel(emptyList(), PostsValidationModel.NO_POSTS))
    }

    @Test
    fun `Should getPosts() return PostsValidationModel GENERAL ERROR state when response is not successful`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getPosts()).thenReturn(Single.just(mockPostsResponse))
        whenever(mockPostsResponse.isSuccessful).thenReturn(false)
        whenever(mockPostsResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = postRepositoryImpl.requestPosts().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(PostsValidationModel(emptyList(), PostsValidationModel.GENERAL_ERROR))
    }

    @Test
    fun `Should getPosts() return PostsValidationModel GENERAL ERROR state when error happens`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getPosts()).thenReturn(Single.error(mockThrowable))
        whenever(mockPostsResponse.isSuccessful).thenReturn(false)
        whenever(mockPostsResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = postRepositoryImpl.requestPosts().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(PostsValidationModel(emptyList(), PostsValidationModel.GENERAL_ERROR))
    }

    @Test
    fun `Should getPost() throw network exception when there is no connection`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(false)

        // when
        val testObserver = postRepositoryImpl.requestPost(POST_ID).test()

        // then
        testObserver.assertError(NetworkException::class.java)
    }

    @Test
    fun `Should getPost() return PostValidationModel POST_DOWNLOADED state`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getPost(POST_ID)).thenReturn(Single.just(mockPostResponse))
        whenever(mockPostResponse.isSuccessful).thenReturn(true)
        whenever(mockPostResponse.body()).thenReturn(mockPost)

        // when
        val testObserver = postRepositoryImpl.requestPost(POST_ID).test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(PostValidationModel(mockPost, PostValidationModel.POST_DOWNLOADED))
    }

    @Test
    fun `Should getPost() return PostValidationModel GENERAL_ERROR state when response is null`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getPost(POST_ID)).thenReturn(Single.just(mockPostResponse))
        whenever(mockPostResponse.isSuccessful).thenReturn(true)
        whenever(mockPostResponse.body()).thenReturn(null)

        // when
        val testObserver = postRepositoryImpl.requestPost(POST_ID).test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(PostValidationModel(Post(), PostValidationModel.GENERAL_ERROR))
    }

    @Test
    fun `Should getPost() return PostValidationModel GENERAL ERROR state when response is not successful`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getPost(POST_ID)).thenReturn(Single.just(mockPostResponse))
        whenever(mockPostResponse.isSuccessful).thenReturn(false)
        whenever(mockPostResponse.body()).thenReturn(mockPost)

        // when
        val testObserver = postRepositoryImpl.requestPost(POST_ID).test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(PostValidationModel(Post(), PostsValidationModel.GENERAL_ERROR))
    }
}

