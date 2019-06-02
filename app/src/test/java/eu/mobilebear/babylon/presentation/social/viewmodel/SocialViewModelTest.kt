package eu.mobilebear.babylon.presentation.social.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.isNull
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import eu.mobilebear.babylon.domain.interactor.GetSocialPostsUseCase
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.domain.model.SocialValidationAction
import eu.mobilebear.babylon.domain.model.SocialValidationModel
import eu.mobilebear.babylon.util.NetworkException
import eu.mobilebear.babylon.util.state.NetworkStatus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

class SocialViewModelTest {

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @JvmField
    @Rule
    var viewModelRule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockGetSocialPostsUseCase: GetSocialPostsUseCase

    @Mock
    lateinit var mockNetworkException: NetworkException

    @Mock
    lateinit var mockSocialValidationModel: SocialValidationModel

    @Mock
    lateinit var mockSocialPost: SocialPost

    lateinit var socialViewModel: SocialViewModel

    @Before
    fun setUp() {
        socialViewModel = SocialViewModel(mockGetSocialPostsUseCase)
    }

    @Test
    fun `Should clean useCase when viewModel is cleared`() {

        // when
        socialViewModel.onCleared()

        // then
        verify(mockGetSocialPostsUseCase).dispose()
    }

    @Test
    fun `Should execute call to get posts when it's called`() {
        // when
        socialViewModel.getPosts()

        // then
        verify(mockGetSocialPostsUseCase).execute(observer = any(), params = isNull())
        assertEquals("Network Status is Running", NetworkStatus.Running, socialViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no posts", emptyList<SocialPost>(), socialViewModel.mutableScreenState.value?.posts)
    }

    @Test
    fun `Should be in error state when get Posts return general error`() {
        //given
        socialViewModel.getPosts()
        val captorPostsObserver = argumentCaptor<SocialViewModel.PostsObserver>()
        verify(mockGetSocialPostsUseCase).execute(captorPostsObserver.capture(), isNull())
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onSuccess(SocialValidationAction.GeneralError)

        // then
        assertEquals("Network Status is Error", NetworkStatus.Error(null), socialViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no posts", emptyList<SocialPost>(), socialViewModel.mutableScreenState.value?.posts)
    }

    @Test
    fun `Should be in error state when get Posts return network exception`() {
        //given
        socialViewModel.getPosts()
        val captorPostsObserver = argumentCaptor<SocialViewModel.PostsObserver>()
        verify(mockGetSocialPostsUseCase).execute(captorPostsObserver.capture(), isNull())
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onError(mockNetworkException)

        // then
        assertEquals("Network Status is Network Error", NetworkStatus.NoConnectionError, socialViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no posts", emptyList<SocialPost>(), socialViewModel.mutableScreenState.value?.posts)
    }

    @Test
    fun `Should be in success state when get Posts return no posts`() {
        //given
        socialViewModel.getPosts()
        val captorPostsObserver = argumentCaptor<SocialViewModel.PostsObserver>()
        verify(mockGetSocialPostsUseCase).execute(captorPostsObserver.capture(), isNull())
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onSuccess(SocialValidationAction.NoPosts)

        // then
        assertEquals("Network Status is Success", NetworkStatus.Success, socialViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has no posts", emptyList<SocialPost>(), socialViewModel.mutableScreenState.value?.posts)
    }

    @Test
    fun `Should be in success state when get Posts return posts`() {
        //given
        whenever(mockSocialValidationModel.posts).thenReturn(listOf(mockSocialPost))
        socialViewModel.getPosts()
        val captorPostsObserver = argumentCaptor<SocialViewModel.PostsObserver>()
        verify(mockGetSocialPostsUseCase).execute(captorPostsObserver.capture(), isNull())
        val socialValidationModelObserver = captorPostsObserver.firstValue

        // when
        socialValidationModelObserver.onSuccess(SocialValidationAction.SocialPostsDownloaded(mockSocialValidationModel))

        // then
        assertEquals("Network Status is Success", NetworkStatus.Success, socialViewModel.mutableScreenState.value?.networkStatus)
        assertEquals("ViewModel has exactly same amount of posts", listOf(mockSocialPost), socialViewModel.mutableScreenState.value?.posts)
    }
}
