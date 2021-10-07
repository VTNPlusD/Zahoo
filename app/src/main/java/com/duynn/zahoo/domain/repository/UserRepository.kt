package com.duynn.zahoo.domain.repository

import android.net.Uri
import com.duynn.zahoo.data.model.CountryData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.data.repository.source.remote.body.RegisterBody
import com.duynn.zahoo.domain.DomainResult
import com.duynn.zahoo.utils.extension.Option
import kotlinx.coroutines.flow.Flow

/**
 *Created by duynn100198 on 10/04/21.
 */
interface UserRepository {

    suspend fun login(
        email: String,
        password: String
    ): DomainResult<Unit?>

    suspend fun register(user: RegisterBody): DomainResult<Any>
    suspend fun getAllUser(): DomainResult<List<UserData>>
    suspend fun getCountries(): DomainResult<List<CountryData>>
    suspend fun logout(): DomainResult<Any>
    fun userObservable(): Flow<DomainResult<Option<UserData>>>
    suspend fun checkAuth(): DomainResult<Boolean>
    suspend fun checkAuthInternal()
    suspend fun editUser(
        id: String,
        userName: String,
        phone: String,
        avatarUri: Uri?
    ): DomainResult<Any>

    suspend fun sendCode(email: String): DomainResult<Any>
    suspend fun checkCode(
        code: Int,
        password: String
    ): DomainResult<Any>
}
