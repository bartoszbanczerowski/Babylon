package eu.mobilebear.babylon.presentation.social.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.mobilebear.babylon.domain.interactor.GetPostsUseCase
import eu.mobilebear.babylon.domain.model.PostslValidationAction
import eu.mobilebear.babylon.networking.response.responsedata.Post
import eu.mobilebear.babylon.util.state.NetworkStatus
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class SocialViewModel @Inject constructor(private val getPostsUseCase: GetPostsUseCase) :
    ViewModel() {

    @VisibleForTesting
    internal val mutableScreenState: MutableLiveData<ScreenState> = MutableLiveData()

    val posts: LiveData<ScreenState> by lazy { mutableScreenState }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        super.onCleared()
        getPostsUseCase.dispose()
    }

    fun getPosts() {
        mutableScreenState.value = ScreenState(emptyList(), NetworkStatus.Running)
        getPostsUseCase.execute(PostsObserver())
    }

    @VisibleForTesting
    inner class PostsObserver : DisposableSingleObserver<PostslValidationAction>() {
        override fun onSuccess(action: PostslValidationAction) {
            when (action) {
                is PostslValidationAction.PostsDownloaded -> showPosts(action.postsValidationModel.posts)
                is PostslValidationAction.NoPosts -> showNoPostsInfo()
                is PostslValidationAction.GeneralError -> showError(null)
            }
        }

        override fun onError(error: Throwable) {
            showError(error)
        }
    }

    private fun showPosts(posts: List<Post>) {
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
        val posts: List<Post>?,
        val networkStatus: NetworkStatus
    )
}
