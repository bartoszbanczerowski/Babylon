package eu.mobilebear.babylon.domain.repository

import eu.mobilebear.babylon.domain.model.UserValidationModel
import eu.mobilebear.babylon.domain.model.UsersValidationModel
import io.reactivex.Single

interface UserRepository {
    fun requestUsers(): Single<UsersValidationModel>
    fun requestUser(userd: Int): Single<UserValidationModel>
}
