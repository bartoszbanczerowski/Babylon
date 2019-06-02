package eu.mobilebear.babylon.presentation.social.viewmodel

import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.mobilebear.babylon.domain.interactor.GetSocialPostsUseCase
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.domain.model.SocialValidationAction
import eu.mobilebear.babylon.util.state.NetworkStatus
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class SocialViewModel @Inject constructor(private val getSocialPostsUseCase: GetSocialPostsUseCase) :
    ViewModel() {

    @VisibleForTesting
    internal val mutableScreenState: MutableLiveData<ScreenState> = MutableLiveData()

    val posts: LiveData<ScreenState> by lazy { mutableScreenState }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        super.onCleared()
        getSocialPostsUseCase.dispose()
    }

    @MainThread
    fun getPosts() {
        mutableScreenState.value = ScreenState(emptyList(), NetworkStatus.Running)
        getSocialPostsUseCase.execute(PostsObserver())
    }

    @VisibleForTesting
    inner class PostsObserver : DisposableSingleObserver<SocialValidationAction>() {
        override fun onSuccess(action: SocialValidationAction) {
            when (action) {
                is SocialValidationAction.SocialPostsDownloaded -> showPosts(action.socialValidationModel.posts)
                is SocialValidationAction.NoPosts -> showNoPostsInfo()
                is SocialValidationAction.GeneralError -> showError(null)
            }
        }

        override fun onError(error: Throwable) {
            showError(error)
        }
    }

    private fun showPosts(posts: List<SocialPost>) {
        mutableScreenState.value = ScreenState(
            posts = posts,
            networkStatus = NetworkStatus.Success
        )
    }

    private fun showNoPostsInfo() {
        mutableScreenState.value = ScreenState(
            posts = emptyList(),
            networkStatus = NetworkStatus.Success
        )
    }

    private fun showError(e: Throwable?) {
        val posts = mutableScreenState.value?.posts
        mutableScreenState.value = ScreenState(
            posts,
            NetworkStatus.error(e)
        )
    }

    data class ScreenState(
        val posts: List<SocialPost>?,
        val networkStatus: NetworkStatus
    )
}
