package eu.mobilebear.babylon.domain.model

sealed class SocialDetailValidationAction {

    class SocialPostDownloaded(val socialValidationModel: SocialDetailValidationModel) : SocialDetailValidationAction()

    object NoPost : SocialDetailValidationAction()

    object GeneralError : SocialDetailValidationAction()
}
