package eu.mobilebear.babylon.presentation.socialdetail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.isNotNull
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import eu.mobilebear.babylon.domain.interactor.GetSocialPostDetailUseCase
import eu.mobilebear.babylon.domain.model.PostDetailParamsModel
import eu.mobilebear.babylon.domain.model.SocialDetailValidationAction
import eu.mobilebear.babylon.domain.model.SocialDetailValidationModel
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.util.NetworkException
import eu.mobilebear.babylon.util.state.NetworkStatus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

class SocialDetailViewModelTest {

    companion object {
        private const val POST_ID = 1234
        private const val USER_ID = 4321
    }

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @JvmField
    @Rule
    var viewModelRule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockGetSocialPostDetailUseCase: GetSocialPostDetailUseCase

    @Mock
    lateinit var mockNetworkException: NetworkException

    @Mock
    lateinit var mockSocialDetailValidationModel: SocialDetailValidationModel

    @Mock
    lateinit var mockSocialPost: SocialPost

    @Mock
    lateinit var mockComment: Comment

    lateinit var socialDetailViewModel: SocialDetailViewModel

    @Before
    fun setUp() {
        socialDetailViewModel = SocialDetailViewModel(mockGetSocialPostDetailUseCase)
    }

    @Test
    fun `Should clean useCase when viewModel is cleared`() {

        // when
        socialDetailViewModel.onCleared()

        // then
        verify(mockGetSocialPostDetailUseCase).dispose()
    }

    @Test
    fun `Should execute call to get posts when it's called`() {
        // when
        socialDetailViewModel.getPost(POST_ID, USER_ID)

        // then
        verify(mockGetSocialPostDetailUseCase).execute(observer = any(), params = eq(PostDetailParamsModel(POST_ID, USER_ID)))
        assertEquals("Network Status is Running", NetworkStatus.Running, socialDetailViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no post", null, socialDetailViewModel.mutableScreenState.value?.post)
    }

    @Test
    fun `Should be in error state when get Posts return general error`() {
        //given
        socialDetailViewModel.getPost(POST_ID, USER_ID)
        val captorPostsObserver = argumentCaptor<SocialDetailViewModel.PostDetailObserver>()
        verify(mockGetSocialPostDetailUseCase).execute(captorPostsObserver.capture(), eq(PostDetailParamsModel(POST_ID, USER_ID)))
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onSuccess(SocialDetailValidationAction.GeneralError)

        // then
        assertEquals("Network Status is Error", NetworkStatus.Error(null), socialDetailViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no posts", null, socialDetailViewModel.mutableScreenState.value?.post)
    }

    @Test
    fun `Should be in error state when get Posts return network exception`() {
        //given
        socialDetailViewModel.getPost(POST_ID, USER_ID)
        val captorPostsObserver = argumentCaptor<SocialDetailViewModel.PostDetailObserver>()
        verify(mockGetSocialPostDetailUseCase).execute(captorPostsObserver.capture(), eq(PostDetailParamsModel(POST_ID, USER_ID)))
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onError(mockNetworkException)

        // then
        assertEquals("Network Status is Network Error", NetworkStatus.NoConnectionError, socialDetailViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no post", null, socialDetailViewModel.mutableScreenState.value?.post)
    }

    @Test
    fun `Should be in success state when get Posts return no posts`() {
        //given
        socialDetailViewModel.getPost(POST_ID, USER_ID)
        val captorPostsObserver = argumentCaptor<SocialDetailViewModel.PostDetailObserver>()
        verify(mockGetSocialPostDetailUseCase).execute(captorPostsObserver.capture(), eq(PostDetailParamsModel(POST_ID, USER_ID)))
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onSuccess(SocialDetailValidationAction.NoPost)

        // then
        assertEquals("Network Status is Success", NetworkStatus.Success, socialDetailViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no post", null, socialDetailViewModel.mutableScreenState.value?.post)
    }

    @Test
    fun `Should be in success state when get Posts return posts`() {
        //given
        whenever(mockSocialDetailValidationModel.post).thenReturn(mockSocialPost)
        whenever(mockSocialDetailValidationModel.comments).thenReturn(listOf(mockComment))
        socialDetailViewModel.getPost(POST_ID, USER_ID)
        val captorPostsObserver = argumentCaptor<SocialDetailViewModel.PostDetailObserver>()
        verify(mockGetSocialPostDetailUseCase).execute(captorPostsObserver.capture(), isNotNull())
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onSuccess(SocialDetailValidationAction.SocialPostDownloaded(mockSocialDetailValidationModel))

        // then
        assertEquals("Network Status is Success", NetworkStatus.Success, socialDetailViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has post", mockSocialPost, socialDetailViewModel.mutableScreenState.value?.post)
        assertEquals("ViewModel has comments", listOf(mockComment), socialDetailViewModel.mutableScreenState.value?.comments)
    }
}
