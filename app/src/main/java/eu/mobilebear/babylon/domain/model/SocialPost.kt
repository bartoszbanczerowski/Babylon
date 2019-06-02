package eu.mobilebear.babylon.domain.model

import eu.mobilebear.babylon.networking.response.responsedata.Address
import eu.mobilebear.babylon.networking.response.responsedata.Company

data class SocialPost constructor(
    var userId: Int,
    var id: Int,
    var title: String,
    var body: String,
    var username: String?,
    var email: String?,
    var address: Address?,
    var phone: String?,
    var website: String?,
    var company: Company?
)
