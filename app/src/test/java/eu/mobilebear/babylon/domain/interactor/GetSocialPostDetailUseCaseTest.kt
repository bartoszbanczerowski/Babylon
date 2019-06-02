package eu.mobilebear.babylon.domain.interactor

import com.nhaarman.mockitokotlin2.whenever
import eu.mobilebear.babylon.domain.mapper.SocialPostMapper
import eu.mobilebear.babylon.domain.model.CommentsValidationModel
import eu.mobilebear.babylon.domain.model.PostDetailParamsModel
import eu.mobilebear.babylon.domain.model.PostValidationModel
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import eu.mobilebear.babylon.domain.model.SocialDetailValidationAction
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.domain.model.SocialValidationAction
import eu.mobilebear.babylon.domain.model.UserValidationModel
import eu.mobilebear.babylon.domain.model.UsersValidationModel
import eu.mobilebear.babylon.domain.repository.CommentRepository
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.domain.repository.UserRepository
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.networking.response.responsedata.User
import eu.mobilebear.babylon.rx.RxFactory
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import java.lang.IllegalArgumentException

class GetSocialPostDetailUseCaseTest {

    companion object {
        private const val POST_ID = 4321
        private const val USER_ID = 1234
        private val PARAMS = PostDetailParamsModel(POST_ID, USER_ID)
    }

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var mockPostRepository: PostRepository

    @Mock
    lateinit var mockUserRepository: UserRepository

    @Mock
    lateinit var mockCommentRepository: CommentRepository

    @Mock
    lateinit var mockSocialPostMapper: SocialPostMapper

    @Mock
    lateinit var mockRxFactory: RxFactory

    @Mock
    lateinit var mockPostValidationModel: PostValidationModel

    @Mock
    lateinit var mockUserValidationModel: UserValidationModel

    @Mock
    lateinit var mockCommentsValidationModel: CommentsValidationModel

    @Mock
    lateinit var mockPost: Post

    @Mock
    lateinit var mockUser: User

    @Mock
    lateinit var mockComment: Comment

    @Mock
    lateinit var mockSocialPost: SocialPost


    lateinit var getSocialPostDetailUseCase: GetSocialPostDetailUseCase

    @Before
    fun setUp() {
        getSocialPostDetailUseCase = GetSocialPostDetailUseCase(mockPostRepository, mockUserRepository, mockCommentRepository, mockSocialPostMapper,mockRxFactory)
        whenever(mockPostRepository.requestPost(POST_ID)).thenReturn(Single.just(mockPostValidationModel))
        whenever(mockUserRepository.requestUser(USER_ID)).thenReturn(Single.just(mockUserValidationModel))
        whenever(mockCommentRepository.requestComments()).thenReturn(Single.just(mockCommentsValidationModel))
    }

    @Test
    fun `Should throw IllegalArgumentException when params are null`() {
        // when
        val testObserver = getSocialPostDetailUseCase.buildUseCaseSingle(null).test()

        // then
        testObserver
            .assertNotComplete()
            .assertError(IllegalArgumentException::class.java)
    }

    @Test
    fun `Should return GENERAL_ERROR action when posts repository return no post`() {
        // given
        whenever(mockPostValidationModel.status).thenReturn(PostValidationModel.GENERAL_ERROR)

        // when
        val testObserver = getSocialPostDetailUseCase.buildUseCaseSingle(PARAMS).test()

        // then
        testObserver
            .assertComplete()
            .assertValue(SocialDetailValidationAction.GeneralError)
    }

    @Test
    fun `Should return POST_DOWNLOADED action when posts repository return post`() {
        // given
        whenever(mockPostValidationModel.status).thenReturn(PostValidationModel.POST_DOWNLOADED)
        whenever(mockPostValidationModel.post).thenReturn(mockPost)
        whenever(mockUserValidationModel.status).thenReturn(UserValidationModel.USER_DOWNLOADED)
        whenever(mockUserValidationModel.user).thenReturn(mockUser)
        whenever(mockCommentsValidationModel.status).thenReturn(CommentsValidationModel.COMMENTS_DOWNLOADED)
        whenever(mockCommentsValidationModel.comments).thenReturn(listOf(mockComment))
        whenever(mockPost.id).thenReturn(POST_ID)
        whenever(mockPost.userId).thenReturn(USER_ID)
        whenever(mockComment.postId).thenReturn(POST_ID)
        whenever(mockUser.id).thenReturn(USER_ID)
        whenever(mockSocialPostMapper.transform(mockPost, mockUser)).thenReturn(mockSocialPost)
        whenever(mockSocialPost.id).thenReturn(POST_ID)

        // when
        val testObserver = getSocialPostDetailUseCase.buildUseCaseSingle(PARAMS).test()

        // then
        testObserver
            .assertComplete()
            .assertOf { SocialDetailValidationAction.SocialPostDownloaded::class.java }
    }
}
