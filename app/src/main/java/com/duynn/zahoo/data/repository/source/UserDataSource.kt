package com.duynn.zahoo.data.repository.source

import android.net.Uri
import com.duynn.zahoo.data.model.CountryData
import com.duynn.zahoo.data.model.UserData
import com.duynn.zahoo.data.repository.source.remote.body.RegisterBody
import com.duynn.zahoo.data.repository.source.remote.response.BaseResponse
import com.duynn.zahoo.data.repository.source.remote.response.LoginResponse
import com.duynn.zahoo.utils.extension.Option
import kotlinx.coroutines.flow.Flow

/**
 *Created by duynn100198 on 10/04/21.
 */
interface UserDataSource {

    interface Local {
        /*
        token
        */
        suspend fun token(): String?
        suspend fun saveAuthToken(token: String)
        fun tokenObservable(): Flow<Option<String>>

        /*
        user local
         */
        suspend fun user(): UserData?
        suspend fun saveUser(user: UserData)
        fun userObservable(): Flow<Option<UserData>>
        suspend fun removeUserAndToken()
        suspend fun getCountries(): List<CountryData>
    }

    interface Remote {
        suspend fun login(
            email: String,
            password: String,
            deviceToken: String
        ): BaseResponse<LoginResponse>

        suspend fun register(user: RegisterBody): BaseResponse<Any>
        suspend fun getAllUser(): BaseResponse<List<UserData>>
        suspend fun logout(deviceToken: String): BaseResponse<Any>
        suspend fun getUser(id: String): BaseResponse<UserData>
        suspend fun editUser(
            id: String,
            userName: String,
            phone: String,
            avatarUri: Uri?
        ): BaseResponse<Any>

        suspend fun sendCode(email: String): BaseResponse<Any>
        suspend fun checkCode(
            code: Int,
            password: String
        ): BaseResponse<Any>
    }
}
