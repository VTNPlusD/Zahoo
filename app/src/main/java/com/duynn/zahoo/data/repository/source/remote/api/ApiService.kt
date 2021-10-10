package com.duynn.zahoo.data.repository.source.remote.api

import com.duynn.zahoo.data.repository.source.remote.response.BaseResponse
import com.duynn.zahoo.data.repository.source.remote.response.ImageResponse
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 *Created by duynn100198 on 10/04/21.
 */
interface ApiService {
    @Multipart
    @POST("api/upload")
    suspend fun uploadImage(@Part body: MultipartBody.Part): BaseResponse<ImageResponse>

    companion object Factory {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<ApiService>()
    }
}
