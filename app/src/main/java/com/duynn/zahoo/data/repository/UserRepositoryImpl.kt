package com.duynn.zahoo.data.repository

import android.app.Activity
import android.app.Application
import com.duynn.zahoo.R
import com.duynn.zahoo.data.error.AppError
import com.duynn.zahoo.data.error.ErrorMapper
import com.duynn.zahoo.data.model.CountryData
import com.duynn.zahoo.data.model.PhoneAuthData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.data.repository.source.UserDataSource
import com.duynn.zahoo.domain.DomainResult
import com.duynn.zahoo.domain.repository.UserRepository
import com.duynn.zahoo.domain.scheduler.AppDispatchers.IO
import com.duynn.zahoo.domain.scheduler.DispatchersProvider
import com.duynn.zahoo.utils.extension.Either
import com.duynn.zahoo.utils.extension.Option
import com.duynn.zahoo.utils.extension.catch
import com.duynn.zahoo.utils.extension.catchError
import com.duynn.zahoo.utils.extension.getOrThrow
import com.duynn.zahoo.utils.extension.mapLeft
import com.duynn.zahoo.utils.extension.rightResult
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

/**
 *Created by duynn100198 on 10/04/21.
 */
@KoinApiExtension
class UserRepositoryImpl(
    private val userRemoteSource: UserDataSource.Remote,
    private val mapper: ErrorMapper,
    private val userLocalSource: UserDataSource.Local,
    application: Application
) : UserRepository, KoinComponent {
    private val dispatchersProvider = get<DispatchersProvider>(named(IO))
    private val checkAuthDeferred = CompletableDeferred<Unit>()
    private val appName = application.getString(R.string.app_name)

    // init {
    //     CoroutineScope(dispatchersProvider.dispatcher()).launch {
    //         while (isActive) {
    //             checkAuthInternal()
    //             delay(CHECK_AUTH_INTERVAL)
    //         }
    //     }
    // }

    private val userObservable: Flow<Either<AppError, Option<UserData>>> =
        combine(
            userLocalSource.userObservable(),
            userLocalSource.tokenObservable()
        ) { userOptional, _ ->
            userOptional.rightResult()
        }.catchError(mapper)
            .distinctUntilChanged()
            .buffer(1)

    override suspend fun token(): DomainResult<String?> {
        return catch { userLocalSource.token() }.mapLeft(mapper::map)
    }

    override suspend fun saveAuthToken(token: String): DomainResult<Any> {
        return catch { userLocalSource.saveAuthToken(token) }.mapLeft(mapper::map)
    }

    override suspend fun clearAuth(): DomainResult<Any> {
        return catch { userLocalSource.removeUserAndToken() }.mapLeft(mapper::map)
    }

    override fun tokenObservable(): Flow<DomainResult<Option<String>>> {
        return userLocalSource
            .tokenObservable()
            .map { it.rightResult() }
            .catchError(mapper)
            .distinctUntilChanged()
            .buffer(1)
    }

    private suspend fun login(): DomainResult<Unit> {
        return catch {
            withContext(dispatchersProvider.dispatcher()) {
                userLocalSource.token()?.let {
                    val dataSnapshot = userRemoteSource.login(it)
                    val newUser = UserData(it, it, appName, "")
                    if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                        val user: UserData? = dataSnapshot.getValue(UserData::class.java)
                        try {
                            if (user?.id != null && user.name != null && user.status != null) {
                                user.nameInPhone = "You"
                                userLocalSource.saveUser(user)
                            } else userRemoteSource.createUser(newUser)
                        } catch (ex: Exception) {
                            userRemoteSource.createUser(newUser)
                        }
                    } else userRemoteSource.createUser(newUser)
                } ?: kotlin.run {
                    userLocalSource.removeUserAndToken()
                }
            }
        }.mapLeft(mapper::map)
    }

    override suspend fun getCountries(): DomainResult<List<CountryData>> {
        return userLocalSource.getCountries().rightResult()
    }

    override fun userObservable() = userObservable

    override suspend fun checkAuth(): DomainResult<Boolean> {
        return catch {
            // checkAuthDeferred.await()
            userLocalSource.token() !== null && userLocalSource.user() !== null
        }.mapLeft(mapper::map)
    }

    override suspend fun checkAuthInternal() {
        try {
            Timber.d("[CHECK AUTH] started")
            userLocalSource.token()
                ?: return userLocalSource.removeUserAndToken()
            // val id = userLocalSource.user()?.id
            //     ?: return userLocalSource.removeUserAndToken()
            Timber.d("[CHECK AUTH] success")
        } catch (e: Exception) {
            Timber.d(e, "[CHECK AUTH] failure: $e")
            if ((e as? HttpException)?.code() in arrayOf(
                    HttpURLConnection.HTTP_UNAUTHORIZED,
                    HttpURLConnection.HTTP_FORBIDDEN
                )
            ) {
                userLocalSource.removeUserAndToken()
                Timber.d(e, "[CHECK AUTH] Login again!")
            }
        } finally {
            checkAuthDeferred.complete(Unit)
        }
    }

    override suspend fun verifyPhoneNumber(activity: Activity): DomainResult<PhoneAuthData> =
        catch {
            withContext((dispatchersProvider.dispatcher())) {
                return@withContext when (val phoneAuth =
                    userRemoteSource.verifyPhoneNumber(userLocalSource.token() ?: "", activity)) {
                    is PhoneAuthData.CodeSent -> phoneAuth
                    is PhoneAuthData.VerificationCompleted -> {
                        userRemoteSource.signInWithPhoneAuthCredential(phoneAuth.credential)
                        phoneAuth
                    }
                }
            }
        }.mapLeft(mapper::map)

    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential
    ): DomainResult<Unit> =
        catch {
            withContext((dispatchersProvider.dispatcher())) {
                userRemoteSource.signInWithPhoneAuthCredential(credential)
                login().getOrThrow()
            }
        }.mapLeft(mapper::map)

    companion object {
        const val CHECK_AUTH_INTERVAL = 180_000L // 3 minutes
    }
}
