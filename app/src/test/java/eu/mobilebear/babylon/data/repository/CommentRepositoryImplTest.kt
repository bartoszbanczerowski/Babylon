package eu.mobilebear.babylon.data.repository

import com.nhaarman.mockitokotlin2.whenever
import eu.mobilebear.babylon.domain.model.CommentsValidationModel
import eu.mobilebear.babylon.networking.SocialService
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.util.ConnectionChecker
import eu.mobilebear.babylon.util.NetworkException
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import retrofit2.Response

class CommentRepositoryImplTest {


    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var mockSocialService: SocialService

    @Mock
    lateinit var mockConnectionChecker: ConnectionChecker

    @Mock
    lateinit var mockCommentsResponse: Response<List<Comment>>

    @Mock
    lateinit var mockComment: Comment

    @Mock
    lateinit var mockThrowable: Throwable

    lateinit var commentRepositoryImpl: CommentRepositoryImpl

    @Before
    fun setUp(){
        commentRepositoryImpl = CommentRepositoryImpl(mockSocialService, mockConnectionChecker)
    }

    @Test
    fun `Should getComments() throw network exception when there is no connection`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(false)

        // when
        val testObserver = commentRepositoryImpl.requestComments().test()

        // then
        testObserver.assertError(NetworkException::class.java)
    }

    @Test
    fun `Should getComments() return CommentsValidationModel COMMENTS_DOWNLOADED state`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getComments()).thenReturn(Single.just(mockCommentsResponse))
        whenever(mockCommentsResponse.isSuccessful).thenReturn(true)
        whenever(mockCommentsResponse.body()).thenReturn(listOf(mockComment))

        // when
        val testObserver = commentRepositoryImpl.requestComments().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(CommentsValidationModel(listOf(mockComment), CommentsValidationModel.COMMENTS_DOWNLOADED))
    }

    @Test
    fun `Should getComments() return PostsValidationModel GENERAL ERROR state when response is not successful`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getComments()).thenReturn(Single.just(mockCommentsResponse))
        whenever(mockCommentsResponse.isSuccessful).thenReturn(false)
        whenever(mockCommentsResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = commentRepositoryImpl.requestComments().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(CommentsValidationModel(emptyList(), CommentsValidationModel.GENERAL_ERROR))
    }

    @Test
    fun `Should getComments() return PostsValidationModel GENERAL ERROR state when error happens`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getComments()).thenReturn(Single.error(mockThrowable))
        whenever(mockCommentsResponse.isSuccessful).thenReturn(false)
        whenever(mockCommentsResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = commentRepositoryImpl.requestComments().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(CommentsValidationModel(emptyList(), CommentsValidationModel.GENERAL_ERROR))
    }
}
