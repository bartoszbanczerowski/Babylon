package eu.mobilebear.babylon.presentation.socialdetail.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.mobilebear.babylon.domain.interactor.GetSocialPostDetailUseCase
import eu.mobilebear.babylon.domain.model.PostDetailParamsModel
import eu.mobilebear.babylon.domain.model.SocialDetailValidationAction
import eu.mobilebear.babylon.domain.model.SocialPost
import eu.mobilebear.babylon.networking.response.responsedata.Comment
import eu.mobilebear.babylon.util.state.NetworkStatus
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

class SocialDetailViewModel @Inject constructor(private val getSocialPostDetailUseCase: GetSocialPostDetailUseCase) :
    ViewModel() {

    @VisibleForTesting
    internal val mutableScreenState: MutableLiveData<ScreenState> = MutableLiveData()

    val post: LiveData<ScreenState> by lazy { mutableScreenState }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        super.onCleared()
        getSocialPostDetailUseCase.dispose()
    }

    fun getPost(postId: Int, userId: Int) {
        mutableScreenState.value = ScreenState(null, NetworkStatus.Running)
        getSocialPostDetailUseCase.execute(PostDetailObserver(), PostDetailParamsModel(postId, userId))
    }

    @VisibleForTesting
    inner class PostDetailObserver : DisposableSingleObserver<SocialDetailValidationAction>() {
        override fun onSuccess(action: SocialDetailValidationAction) {
            when (action) {
                is SocialDetailValidationAction.SocialPostDownloaded -> showPostAndComments(action.socialValidationModel.post, action.socialValidationModel.comments)
                is SocialDetailValidationAction.NoPost -> showNoPostsInfo()
                is SocialDetailValidationAction.GeneralError -> showError(null)
            }
        }

        override fun onError(error: Throwable) {
            showError(error)
        }
    }

    private fun showPostAndComments(post: SocialPost, comments: List<Comment>) {
        mutableScreenState.value = ScreenState(
            post = post,
            networkStatus = NetworkStatus.Success,
            comments = comments
        )
    }

    private fun showNoPostsInfo() {
        mutableScreenState.value = ScreenState(
            post = null,
            networkStatus = NetworkStatus.Success
        )
    }

    private fun showError(e: Throwable?) {
        val posts = mutableScreenState.value?.post
        mutableScreenState.value = ScreenState(
            posts,
            NetworkStatus.error(e)
        )
    }

    data class ScreenState(
        val post: SocialPost?,
        val networkStatus: NetworkStatus,
        val comments: List<Comment> = emptyList()
    )
}
