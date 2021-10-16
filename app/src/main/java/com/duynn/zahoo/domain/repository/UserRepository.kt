package com.duynn.zahoo.domain.repository

import android.app.Activity
import com.duynn.zahoo.data.model.CountryData
import com.duynn.zahoo.data.model.PhoneAuthData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.domain.DomainResult
import com.duynn.zahoo.utils.extension.Option
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow

/**
 *Created by duynn100198 on 10/04/21.
 */
interface UserRepository {
    suspend fun token(): DomainResult<String?>
    suspend fun saveAuthToken(token: String): DomainResult<Any>
    suspend fun clearAuth(): DomainResult<Any>
    fun tokenObservable(): Flow<DomainResult<Option<String>>>

    suspend fun getCountries(): DomainResult<List<CountryData>>
    fun userObservable(): Flow<DomainResult<Option<UserData>>>
    fun getAllUsers(): Flow<DomainResult<List<UserData>>>
    suspend fun saveAllUser(users: List<UserData>): DomainResult<Unit>
    suspend fun checkAuth(): DomainResult<Boolean>
    suspend fun checkAuthInternal()
    suspend fun verifyPhoneNumber(activity: Activity): DomainResult<PhoneAuthData>
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): DomainResult<Unit>
}
