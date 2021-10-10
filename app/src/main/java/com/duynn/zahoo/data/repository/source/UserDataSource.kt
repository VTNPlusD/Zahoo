package com.duynn.zahoo.data.repository.source

import android.app.Activity
import com.duynn.zahoo.data.model.CountryData
import com.duynn.zahoo.data.model.PhoneAuthData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.utils.extension.Option
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

/**
 *Created by duynn100198 on 10/04/21.
 */
interface UserDataSource {

    interface Local {
        suspend fun token(): String?
        suspend fun saveAuthToken(token: String)
        fun tokenObservable(): Flow<Option<String>>
        suspend fun user(): UserData?
        suspend fun saveUser(user: UserData)
        fun userObservable(): Flow<Option<UserData>>
        suspend fun removeUserAndToken()
        suspend fun getCountries(): List<CountryData>
    }

    interface Remote {
        suspend fun login(phoneNumber: String): DataSnapshot
        suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Unit?
        suspend fun verifyPhoneNumber(token: String, activity: Activity): PhoneAuthData
        suspend fun createUser(userData: UserData): Unit?
    }
}
