package eu.mobilebear.babylon.data.repository

import com.nhaarman.mockitokotlin2.whenever
import eu.mobilebear.babylon.domain.model.UserValidationModel
import eu.mobilebear.babylon.domain.model.UsersValidationModel
import eu.mobilebear.babylon.networking.SocialService
import eu.mobilebear.babylon.networking.response.responsedata.User
import eu.mobilebear.babylon.util.ConnectionChecker
import eu.mobilebear.babylon.util.NetworkException
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import retrofit2.Response

class UserRepositoryImplTest {

    companion object {
        private const val USER_ID = 1234
    }

    @JvmField
    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    lateinit var mockSocialService: SocialService

    @Mock
    lateinit var mockConnectionChecker: ConnectionChecker

    @Mock
    lateinit var mockUsersResponse: Response<List<User>>

    @Mock
    lateinit var mockUserResponse: Response<User>

    @Mock
    lateinit var mockUser: User

    @Mock
    lateinit var mockThrowable: Throwable

    lateinit var userRepositoryImpl: UserRepositoryImpl

    @Before
    fun setUp(){
        userRepositoryImpl = UserRepositoryImpl(mockSocialService, mockConnectionChecker)
    }

    @Test
    fun `Should getUsers() throw network exception when there is no connection`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(false)

        // when
        val testObserver = userRepositoryImpl.requestUsers().test()

        // then
        testObserver.assertError(NetworkException::class.java)
    }

    @Test
    fun `Should getUsers() return UsersValidationModel POSTS_DOWNLOADED state`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getUsers()).thenReturn(Single.just(mockUsersResponse))
        whenever(mockUsersResponse.isSuccessful).thenReturn(true)
        whenever(mockUsersResponse.body()).thenReturn(listOf(mockUser))

        // when
        val testObserver = userRepositoryImpl.requestUsers().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(UsersValidationModel(listOf(mockUser), UsersValidationModel.USERS_DOWNLOADED))
    }

    @Test
    fun `Should getUsers() return UsersValidationModel NO_POSTS state`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getUsers()).thenReturn(Single.just(mockUsersResponse))
        whenever(mockUsersResponse.isSuccessful).thenReturn(true)
        whenever(mockUsersResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = userRepositoryImpl.requestUsers().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(UsersValidationModel(emptyList(), UsersValidationModel.NO_USERS))
    }

    @Test
    fun `Should getUsers() return UsersValidationModel GENERAL ERROR state when response is not successful`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getUsers()).thenReturn(Single.just(mockUsersResponse))
        whenever(mockUsersResponse.isSuccessful).thenReturn(false)
        whenever(mockUsersResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = userRepositoryImpl.requestUsers().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(UsersValidationModel(emptyList(), UsersValidationModel.GENERAL_ERROR))
    }

    @Test
    fun `Should getUsers() return UsersValidationModel GENERAL ERROR state when error happens`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getUsers()).thenReturn(Single.error(mockThrowable))
        whenever(mockUsersResponse.isSuccessful).thenReturn(false)
        whenever(mockUsersResponse.body()).thenReturn(emptyList())

        // when
        val testObserver = userRepositoryImpl.requestUsers().test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(UsersValidationModel(emptyList(), UsersValidationModel.GENERAL_ERROR))
    }

    @Test
    fun `Should getUser() throw network exception when there is no connection`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(false)

        // when
        val testObserver = userRepositoryImpl.requestUser(USER_ID).test()

        // then
        testObserver.assertError(NetworkException::class.java)
    }

    @Test
    fun `Should getUser() return UserValidationModel USER_DOWNLOADED state`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getUser(USER_ID)).thenReturn(Single.just(mockUserResponse))
        whenever(mockUserResponse.isSuccessful).thenReturn(true)
        whenever(mockUserResponse.body()).thenReturn(mockUser)

        // when
        val testObserver = userRepositoryImpl.requestUser(USER_ID).test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(UserValidationModel(mockUser, UserValidationModel.USER_DOWNLOADED))
    }

    @Test
    fun `Should getUser() return UserValidationModel GENERAL_ERROR state when response is null`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getUser(USER_ID)).thenReturn(Single.just(mockUserResponse))
        whenever(mockUserResponse.isSuccessful).thenReturn(false)
        whenever(mockUserResponse.body()).thenReturn(mockUser)

        // when
        val testObserver = userRepositoryImpl.requestUser(USER_ID).test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(UserValidationModel(User(), UserValidationModel.GENERAL_ERROR))
    }

    @Test
    fun `Should getUser() return UserValidationModel GENERAL ERROR state when response is not successful`() {
        // given
        whenever(mockConnectionChecker.isConnected).thenReturn(true)
        whenever(mockSocialService.getUser(USER_ID)).thenReturn(Single.just(mockUserResponse))
        whenever(mockUserResponse.isSuccessful).thenReturn(false)
        whenever(mockUserResponse.body()).thenReturn(mockUser)

        // when
        val testObserver = userRepositoryImpl.requestUser(USER_ID).test()

        // then
        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(UserValidationModel(User(), UserValidationModel.GENERAL_ERROR))
    }
}
