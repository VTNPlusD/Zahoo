package com.duynn.zahoo.data.repository.source.remote.api

import retrofit2.Retrofit
import retrofit2.create

/**
 *Created by duynn100198 on 10/04/21.
 */
interface ApiService {

    companion object Factory {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<ApiService>()
    }
}
