package eu.mobilebear.babylon.domain.model

sealed class PostslValidationAction {

    class PostsDownloaded(val postsValidationModel: PostsValidationModel) : PostslValidationAction()

    object NoPosts : PostslValidationAction()

    object GeneralError : PostslValidationAction()
}
