package eu.mobilebear.babylon.domain.model

sealed class SocialValidationAction {

    class SocialPostsDownloaded(val socialValidationModel: SocialValidationModel) : SocialValidationAction()

    object NoPosts : SocialValidationAction()

    object GeneralError : SocialValidationAction()
}
