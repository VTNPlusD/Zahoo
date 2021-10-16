package com.duynn.zahoo.data.repository.source.local

import android.app.Application
import com.duynn.zahoo.data.model.CountryData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.data.repository.source.UserDataSource
import com.duynn.zahoo.data.repository.source.local.api.DatabaseApi
import com.duynn.zahoo.data.repository.source.local.api.SharedPrefApi
import com.duynn.zahoo.data.repository.source.local.api.pref.SharedPrefKey.KEY_COUNTRIES
import com.duynn.zahoo.data.repository.source.local.api.pref.SharedPrefKey.KEY_TOKEN
import com.duynn.zahoo.data.repository.source.local.api.pref.SharedPrefKey.KEY_USER
import com.duynn.zahoo.domain.scheduler.AppDispatchers.IO
import com.duynn.zahoo.domain.scheduler.DispatchersProvider
import com.duynn.zahoo.utils.extension.getCountriesFromAssets
import com.duynn.zahoo.utils.extension.mapNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import timber.log.Timber

/**
 *Created by duynn100198 on 10/04/21.
 */
@KoinApiExtension
@ExperimentalCoroutinesApi
class UserLocalDataSourceImpl(
    private val sharedPrefApi: SharedPrefApi,
    private val databaseApi: DatabaseApi,
    private val application: Application
) : UserDataSource.Local, KoinComponent {
    private val dispatchersProvider = get<DispatchersProvider>(named(IO))
    private var token by sharedPrefApi.delegate(null as String?, KEY_TOKEN, commit = true)
    private var userLocal by sharedPrefApi.delegate(null as String?, KEY_USER, commit = true)

    private var userObservable = sharedPrefApi.observeString(KEY_USER)
        .flowOn(dispatchersProvider.dispatcher())
        .map { json -> json.mapNotNull { sharedPrefApi.objectFromJson(it, UserData::class.java) } }
        .buffer(1)
        .also { Timber.i("User $it") }

    private var tokenObservable = sharedPrefApi.observeString(KEY_TOKEN)
        .flowOn(dispatchersProvider.dispatcher())
        .buffer(1)
        .also { Timber.i("Token $it") }

    override suspend fun saveAuthToken(token: String) =
        withContext(Dispatchers.IO) { this@UserLocalDataSourceImpl.token = token }

    override fun tokenObservable() = tokenObservable

    override suspend fun token() = withContext(dispatchersProvider.dispatcher()) { token }

    override suspend fun user() =
        withContext(dispatchersProvider.dispatcher()) {
            sharedPrefApi.objectFromJson(userLocal,
                UserData::class.java)
        }

    override suspend fun saveUser(user: UserData) =
        withContext(dispatchersProvider.dispatcher()) {
            this@UserLocalDataSourceImpl.userLocal =
                sharedPrefApi.jsonFormObject(user, UserData::class.java)
        }

    override fun userObservable() = userObservable

    override suspend fun removeUserAndToken() =
        withContext(dispatchersProvider.dispatcher()) {
            userLocal = null
            token = null
            Timber.i("remove User And Token")
        }

    override suspend fun getCountries() =
        withContext(dispatchersProvider.dispatcher()) {
            val exist = sharedPrefApi.getList(KEY_COUNTRIES, CountryData::class.java)
            return@withContext if (exist.isNullOrEmpty()) {
                val newData = sharedPrefApi.listFromJson(application.getCountriesFromAssets,
                    CountryData::class.java)
                sharedPrefApi.putList(KEY_COUNTRIES,
                    CountryData::class.java,
                    newData ?: emptyList())
                newData ?: emptyList()
            } else exist
        }

    override fun getAllUser(): Flow<List<UserData>> {
        return databaseApi.getAllUsers()
            .flowOn(dispatchersProvider.dispatcher())
            .distinctUntilChanged()
            .also { Timber.i("getAllUser $it") }
    }

    override suspend fun saveAllUser(users: List<UserData>) {
        return withContext(dispatchersProvider.dispatcher()) {
            databaseApi.saveAllUser(users)
        }
    }
}
