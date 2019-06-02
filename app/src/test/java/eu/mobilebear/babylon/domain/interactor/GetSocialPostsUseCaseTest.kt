package eu.mobilebear.babylon.domain.interactor

import com.nhaarman.mockitokotlin2.whenever
import eu.mobilebear.babylon.domain.mapper.SocialPostMapper
import eu.mobilebear.babylon.domain.model.PostsValidationModel
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.domain.model.SocialValidationAction
import eu.mobilebear.babylon.domain.model.UsersValidationModel
import eu.mobilebear.babylon.domain.repository.PostRepository
import eu.mobilebear.babylon.domain.repository.UserRepository
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.networking.response.responsedata.User
import eu.mobilebear.babylon.rx.RxFactory
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

class GetSocialPostsUseCaseTest {

    companion object {
        private const val USER_ID = 1234
    }

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var mockPostRepository: PostRepository

    @Mock
    lateinit var mockUserRepository: UserRepository

    @Mock
    lateinit var mockSocialPostMapper: SocialPostMapper

    @Mock
    lateinit var mockRxFactory: RxFactory

    @Mock
    lateinit var mockPostsValidationModel: PostsValidationModel

    @Mock
    lateinit var mockUsersValidationModel: UsersValidationModel

    @Mock
    lateinit var mockPost: Post

    @Mock
    lateinit var mockUser: User

    @Mock
    lateinit var mockSocialPost: SocialPost


    lateinit var getSocialPostsUseCase: GetSocialPostsUseCase

    @Before
    fun setUp() {
        getSocialPostsUseCase = GetSocialPostsUseCase(mockPostRepository, mockUserRepository, mockSocialPostMapper, mockRxFactory)
        whenever(mockPostRepository.requestPosts()).thenReturn(Single.just(mockPostsValidationModel))
        whenever(mockUserRepository.requestUsers()).thenReturn(Single.just(mockUsersValidationModel))
    }

    @Test
    fun `Should return NO_POSTS action when posts repository return no posts`() {
        // given
        whenever(mockPostsValidationModel.status).thenReturn(PostsValidationModel.NO_POSTS)

        // when
        val testObserver = getSocialPostsUseCase.buildUseCaseSingle().test()

        // then
        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(SocialValidationAction.NoPosts)
    }

    @Test
    fun `Should return GENERAL ERROR action when posts repository return general error`() {
        // given
        whenever(mockPostsValidationModel.status).thenReturn(PostsValidationModel.GENERAL_ERROR)

        // when
        val testObserver = getSocialPostsUseCase.buildUseCaseSingle().test()

        // then
        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertValue(SocialValidationAction.GeneralError)
    }

    @Test
    fun `Should return POSTS_DOWNLOADED action when posts repository return posts and users`() {
        // given
        whenever(mockPostsValidationModel.status).thenReturn(PostsValidationModel.POSTS_DOWNLOADED)
        whenever(mockUsersValidationModel.status).thenReturn(UsersValidationModel.NO_USERS)
        whenever(mockPostsValidationModel.posts).thenReturn(listOf(mockPost))
        whenever(mockUsersValidationModel.users).thenReturn(listOf(mockUser))
        whenever(mockPost.userId).thenReturn(USER_ID)
        whenever(mockUser.id).thenReturn(USER_ID)
        whenever(mockSocialPostMapper.transform(mockPost)).thenReturn(mockSocialPost)

        // when
        val testObserver = getSocialPostsUseCase.buildUseCaseSingle().test()

        // then
        testObserver
            .assertNoErrors()
            .assertComplete()
            .assertOf { SocialValidationAction.SocialPostsDownloaded::class.java }
    }
}
