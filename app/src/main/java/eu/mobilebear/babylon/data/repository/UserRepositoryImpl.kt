package eu.mobilebear.babylon.data.repository

import eu.mobilebear.babylon.domain.model.UserValidationModel
import eu.mobilebear.babylon.domain.model.UsersValidationModel
import eu.mobilebear.babylon.domain.repository.UserRepository
import eu.mobilebear.babylon.networking.SocialService
import eu.mobilebear.babylon.networking.response.responsedata.User
import eu.mobilebear.babylon.util.ConnectionChecker
import eu.mobilebear.babylon.util.NetworkException
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl
@Inject
constructor(
    private val socialService: SocialService,
    private val connectionChecker: ConnectionChecker
) : UserRepository {

    override fun requestUsers(): Single<UsersValidationModel> {
        if (!connectionChecker.isConnected) return Single.error(NetworkException())
        return socialService.getUsers()
            .map { response -> mapPostsResponse(response) }
            .onErrorReturn { mapUsersError() }
    }

    override fun requestUser(userd: Int): Single<UserValidationModel> {
        if (!connectionChecker.isConnected) return Single.error(NetworkException())
        return socialService.getUser(userd)
            .map { response -> mapUserResponse(response) }
            .onErrorReturn { mapUserError() }
    }

    private fun mapPostsResponse(response: Response<List<User>>): UsersValidationModel {
        val isResponseSuccessful = response.isSuccessful && response.body() != null

        return if (isResponseSuccessful) {
            if (response.body()!!.isEmpty()) {
                UsersValidationModel(response.body()!!, UsersValidationModel.NO_USERS)
            } else {
                UsersValidationModel(response.body()!!, UsersValidationModel.USERS_DOWNLOADED)
            }
        } else {
            mapUsersError()
        }
    }

    private fun mapUsersError(): UsersValidationModel = UsersValidationModel(emptyList(), UsersValidationModel.GENERAL_ERROR)

    private fun mapUserResponse(response: Response<User>): UserValidationModel {
        val isResponseSuccessful = response.isSuccessful && response.body() != null

        return if (isResponseSuccessful) {
            if (response.body() == null) {
                mapUserError()
            } else {
                UserValidationModel(response.body()!!, UserValidationModel.USER_DOWNLOADED)
            }
        } else {
            mapUserError()
        }
    }

    private fun mapUserError(): UserValidationModel = UserValidationModel(User(), UserValidationModel.GENERAL_ERROR)
}
